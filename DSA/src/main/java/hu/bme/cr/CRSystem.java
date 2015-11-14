package hu.bme.cr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import hu.bme.cr.entity.Channel;
import hu.bme.cr.entity.CognitiveRadio;
import hu.bme.cr.entity.CognitiveRadio.CognitiveRadioBuilder;
import hu.bme.cr.strategies.MaxUtilityStrategy;
import hu.bme.cr.strategies.RandomStrategy;
import hu.bme.cr.strategies.RegretTrackingStrategy;
import hu.bme.cr.strategies.StrategySpace;
import hu.bme.cr.utilities.CognitiveRadioUtility;
import hu.bme.cr.utilities.UtilityConstants;

public class CRSystem {
	
	private List<CognitiveRadio> radios;
	
	private List<Channel> channels;
	
	private Map<String, Integer> collisions;
	
	private PrintStream out;
	
	public CRSystem() {
		collisions = new HashMap<>(102);
	}
	
	public CRSystem(List<CognitiveRadio> radios, List<Channel> channels) {
		this.radios = radios;
		this.channels = channels;
	}

	/*
	 * Getter and setter
	 */

	public List<CognitiveRadio> getRadios() {
		return radios;
	}

	public void setRadios(List<CognitiveRadio> radios) {
		this.radios = radios;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
	
	/**
	 * Initialize the cognitive radio system.
	 */
	public void init() {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Where should I log? (1 - console, 2 - in text file): ");
			initLog(scanner.nextInt());
			
			System.out.println();
			
			// read the number of channels and set params
			System.out.print("\nNumber of channels: ");
			int channelNumber = scanner.nextInt();
			
			initChannels(scanner, channelNumber);
			
			// read the number of channels that the users can access
			System.out.print("\nMaximum number of channels that users can use: ");
			int maxChannels = scanner.nextInt();
			System.out.println();
			
			CognitiveRadio.setMaxChannels(maxChannels);
			
			// calculate the strategy space
			setStrategySpace(maxChannels, channelNumber);
			
			// read the number of cognitive radio devices and set params
			System.out.print("Number of cognitive radio devices: ");
			int radioNumber = scanner.nextInt();
			
			initRadios(scanner, radioNumber, channelNumber);			
		}
	}
	
	public void play() {
		// play init phase
		playInitPhase();
		
		// play set phase
		playSetPhase();
		
		// play decide phase (100 rounds)
		for (int i = 0; i < 100; i++) {
			playDecidePhase();
		}
		
		out.println("Number of collisions: " + collisions);
		out.close();
	}
	
	/**
	 * TODO
	 */
	private void playInitPhase() {
		// every CognitiveRadio object chooses a random strategy
		radios.stream().forEach(CognitiveRadio::playInitPhase);	
		
		collisions.put("INIT_PHASE", 0);
		
		for (int i = 0; i < channels.size(); i++) {
			out.println("\nChannel " + (i + 1) + ":");
			
			for (int j = 0; j < UtilityConstants.NUMBER_OF_SUBSLOTS; j++) {
				List<Double> backoffTimes = new ArrayList<>(radios.size());

				for (CognitiveRadio r : radios) {
					
					// if r can access given channel than we generate a backoff time 
					if (r.getAccessDecisions().get(i)) {
						backoffTimes.add(CognitiveRadioUtility.generateBackOff(UtilityConstants.MAX_BACKOFF));
					}
					// if not than we add NaN to the list
					else {
						backoffTimes.add(Double.NaN);
					}
				}
				
				// check if there was a collision on the channel
				int minIndex = findMinIndex(backoffTimes);
				
				if (minIndex == -1) {
					collisions.replace("INIT_PHASE", Math.incrementExact(collisions.get("INIT_PHASE")));
				}
			}
		}
	}
	
	// TODO
	private void playSetPhase() {
		
	}
	
	// TODO
	private void playDecidePhase() {
		
	}
	
	/**
	 * Returns the index of the min element or
	 * -1 if there was a collision on the channel or
	 * -2 if the channel is unused.
	 * 
	 * @param backoffTimes
	 * @return index of the min element
	 */
	private int findMinIndex(List<Double> backoffTimes) {
		if (backoffTimes.size() == 1) {
			out.println("NO COLLISION - 1 user");
			return 0;
		}
		
		List<Double> sortedBackOffs = new ArrayList<>(backoffTimes);
		Collections.sort(sortedBackOffs);
		
		if (sortedBackOffs.get(0).equals(Double.NaN)) {
			out.println("NO COLLISION - NaN");
			return -2;
		}
		else if (sortedBackOffs.get(1).equals(Double.NaN)) {
			out.println("NO COLLISION - NaN");
			return 0;
		}
		
		int minIndex = backoffTimes.indexOf(sortedBackOffs.get(0));
		int min2Index = backoffTimes.indexOf(sortedBackOffs.get(1));
		
		if (backoffTimes.get(minIndex) + UtilityConstants.MODE_SWITCH_TIME < backoffTimes.get(min2Index)) {
			out.println("NO COLLISION - " + backoffTimes);
			return minIndex;
		}
		
		out.println("COLLISION - 1st backoff: " + backoffTimes.get(minIndex) + ", 2nd backoff: " + backoffTimes.get(min2Index));
		return -1;
	}
	
	public void printAll() {
		CognitiveRadio.getStrategySpace().stream().forEach(out::println);
		
		getRadios().stream().forEach(out::println);
		
		getChannels().stream().forEach(out::println);
	}
	
	/**
	 * Setting initial parameters of n Cognitive Radios.
	 * 
	 * @param n - number of CognitiveRadio entities to initialize
	 * @param channelNumber - number of Channels in the system
	 */
	private void initRadios(Scanner scanner, int n, int channelNumber) {
		radios = new ArrayList<>(n);
		
		for (int i = 0; i < n; i++) {
			CognitiveRadioBuilder crb = new CognitiveRadioBuilder();
			
			// get and set demand
			System.out.print("Demand of user " + (i + 1) + " in bit/slot: ");
			crb.setDemand(scanner.nextDouble());
			
			// get and set strategy
			System.out.print("Strategy of user " + (i + 1) + " (1 - regret tracking, 2 - max utility): ");
			int strategyNumber = scanner.nextInt();
			
			
			switch (strategyNumber) {
			// regret tracking algorithm
			case 1:
				System.out.print("Stepsize (1 - fix, 2 - decreasing): ");
				crb.setStrategy(new RegretTrackingStrategy(scanner.nextInt()));
				break;
			// max utility strategy
			case 2:
				crb.setStrategy(new MaxUtilityStrategy());
				break;
			// random strategy
			case 3:
				crb.setStrategy(new RandomStrategy());
				break;
			default:
				throw new IllegalArgumentException("Wrong strategy!");		
			}
			
			radios.add(crb.build());
		}	
	}
	
	/**
	 * Setting initial parameters of Channel entities.
	 * 
	 * @param n - number of channels to initialize
	 */
	private void initChannels(Scanner scanner, int n) {
		channels = new ArrayList<>(n);
		
		for (int i = 0; i < n; i++) {
			// get transmission rate
			System.out.print("Transmission rate of channel " + (i + 1) + ": ");
			double transmissionRate = scanner.nextDouble();
			
			// get frequency 
			System.out.print("Frequency of channel " + (i + 1) + ": ");
			double frequency = scanner.nextDouble();

			// set Channel parameters and add it to channels list 
			channels.add(new Channel(transmissionRate, frequency, false));
		}
	}
	
	/**
	 * Sets where to print results.
	 * 
	 * @param n
	 */
	private void initLog(int n) {
		if (n == 1) {
			out = System.out;
		}
		else if (n == 2) {
			try {
				String date = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
				out = new PrintStream(new File("C:\\Users\\Zoltán Koleszár\\Documents\\Diplomaterv\\log\\simulation_" + date + ".txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			throw new IllegalArgumentException("Wrong logging output!");
		}
	}
	
	/**
	 * Sets the CognitiveRadio strategySpace.
	 * 
	 * @param maxChannels
	 * @param channelNumber
	 */
	private void setStrategySpace(int maxChannels, int channelNumber) {
		for (int i = 1; i <= maxChannels; i++) {
			List<Boolean> strategies = new ArrayList<>(Collections.nCopies(i, true));
			strategies.addAll(new ArrayList<>(Collections.nCopies(channelNumber - i, false)));
			CognitiveRadio.getStrategySpace().addAll(StrategySpace.getStrategySpace(strategies));
		}	
	}
}

