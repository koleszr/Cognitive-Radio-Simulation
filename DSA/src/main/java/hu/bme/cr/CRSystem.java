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
import java.util.stream.Collectors;

import hu.bme.cr.entity.Channel;
import hu.bme.cr.entity.CognitiveRadio;
import hu.bme.cr.entity.CognitiveRadio.CognitiveRadioBuilder;
import hu.bme.cr.strategies.MaxUtilityStrategy;
import hu.bme.cr.strategies.RandomStrategy;
import hu.bme.cr.strategies.RegretTrackingStrategy;
import hu.bme.cr.strategies.StrategySpace;
import hu.bme.cr.uf.MixedUtilityFunction;
import hu.bme.cr.uf.UtilityFunctionParameters.UtilityFunctionParametersBuilder;
import hu.bme.cr.utilities.ChannelUtility;
import hu.bme.cr.utilities.CognitiveRadioUtility;
import hu.bme.cr.utilities.ListUtility;
import hu.bme.cr.utilities.UtilityConstants;

public class CRSystem {
	
	private static final String INIT_PHASE = "INIT_PHASE";

	private List<CognitiveRadio> radios;
	
	private List<Channel> channels;
	
	private Map<String, Integer> collisions;
	
	private PrintStream out;
	
	private Scanner scanner = new Scanner(System.in);
	
	private int strategySpaceSize;
	
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
		System.out.print("Where should I log? (1 - console, 2 - in text file): ");
		initLog(scanner.nextInt());
		
		System.out.println();
		
		// read the number of channels and set params
		System.out.print("Number of channels: ");
		int channelNumber = scanner.nextInt();
		
		initChannels(channelNumber);
		
		// read the number of channels that the users can access
		System.out.print("\nMaximum number of channels that users can use: ");
		int maxChannels = scanner.nextInt();
		System.out.println();
		
		CognitiveRadio.setMaxChannels(maxChannels);
		
		// calculate the strategy space
		setStrategySpace(maxChannels, channelNumber);
		strategySpaceSize = CognitiveRadio.getStrategySpace().size();
		
		// read the number of cognitive radio devices and set params
		System.out.print("Number of cognitive radio devices: ");
		int radioNumber = scanner.nextInt();
		
		initRadios(radioNumber, channelNumber);			
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
	 * Initial game phase. Steps:
	 * <ol>
	 * <li>Every radio choose a channel access (strategy)</li>
	 * <li>Channel access in each subslot by generating a random backoff time.</li>
	 * <li>Calculate channel access probability by backoff time and add it to
	 * CognitiveRadios captureProbabilities list.</li>
	 * <li>Check if there was a collision on the channel at the given subslot 
	 * according to the backoff times.</li>
	 * <li>Calculate user estimate for each CognitiveRadio.</li>
	 * <li>Calculate channel capture probability for each CognitiveRadio using user estimate.</li>
	 * <li>Calculate channel collision probability for each CognitiveRadio using user estimate.</li>
	 * <li>Calculate utility for each CognitiveRadio.</li>
	 * </ol>
	 */
	private void playInitPhase() {
		out.println("****************");
		out.println("** INIT PHASE **");
		out.println("****************");

		collisions.put(INIT_PHASE, 0);
		
		// 1. Every radio choose a channel access (primary strategy)
		radios.stream().forEach(CognitiveRadio::playInitPhase);	
		
		// play every strategy
		// 2. Channel access in each subslot by generating a random backoff time.
		// 3. Calculate channel access probability by backoff time and add it to CognitiveRadios captureProbabilities list.
		// 4. Check if there was a collision on the channel at the given subslot according to the backoff times.
		for (int s = 0; s < strategySpaceSize; s++) {
			
			for (int i = 0; i < channels.size(); i++) {
				out.println();
				out.println("Channel " + (i + 1) + ":");
				
				for (int j = 0; j < UtilityConstants.NUMBER_OF_SUBSLOTS; j++) {
					List<Double> backoffTimes = new ArrayList<>(radios.size());
					
					for (CognitiveRadio r : radios) {
						
						// if r can access given channel than we generate a backoff time 
						if (CognitiveRadio.getStrategySpace().get(r.getAccessDecisions().get(s)).get(i)) {
							double backOff = CognitiveRadioUtility.generateBackOff(UtilityConstants.MAX_BACKOFF);
							backoffTimes.add(backOff);
							
							r.getCaptureProbabilities().get(i).add(ChannelUtility.calculateChannelCaptureProbability(backOff));
						}
						// if not than we add NaN to the list
						else {
							backoffTimes.add(Double.NaN);
							r.getCaptureProbabilities().get(i).add(0.0);
						}
					}
					
					// check if there was a collision on the channel
					int minIndex = findMinIndex(backoffTimes);
					
					if (minIndex == -1) {
						collisions.replace(INIT_PHASE, Math.incrementExact(collisions.get(INIT_PHASE)));
					}
					
					for (int k = 0; k < radios.size(); k++) {
						radios.get(k).getCaptured().get(i).add(k == minIndex);
					}
				}
			}
		
			// 5. Calculate user estimate (contentions) for each CognitiveRadio.
			// 6. Calculate channel capture probability for each CognitiveRadio using user estimate.
			// 7. Calculate channel collision probability for each CognitiveRadio using user estimate.
			// 8. Calculate utility for each CognitiveRadio.
			for (CognitiveRadio r : radios) {
				List<Double> contentions = r.getContentions().get(r.getAccessDecisions().get(s));
				List<Double> captures = new ArrayList<>(channels.size());
				List<Double> collisions = new ArrayList<>(channels.size());
				
				for (int i = 0; i < r.getCaptured().size(); i++) {
					double contention = ChannelUtility.calculateUserEstimate(r.getCaptured().get(i), r.getCaptureProbabilities().get(i));
					contentions.add(contention);
					captures.add(CognitiveRadioUtility.calculateCaptureProbability(contention));
					Collections.replaceAll(captures, Double.NaN, 0.0);
					collisions.add(CognitiveRadioUtility.calculateCollisionProbability(contention));
					Collections.replaceAll(collisions, Double.NaN, 0.0);
				}
				
				Collections.replaceAll(contentions, Double.NaN, 0.0);
				
				UtilityFunctionParametersBuilder builder = new UtilityFunctionParametersBuilder();
				builder.setTransMissionRates(channels.stream().map(Channel::getTransmissionRate).collect(Collectors.toList()));
				builder.setAccessDecisions(CognitiveRadio.getStrategySpace().get(r.getAccessDecisions().get(s)));
				builder.setContentionLevel(contentions);
				builder.setCaptureProbabilities(captures);
				builder.setCollisionProbabilities(collisions);
				builder.setDemand(r.getDemand());
				
				// TODO calculate utility on different rate1, rate2 and rate3 values
				builder.setRate1(1);
				builder.setRate2(1);
				builder.setRate3(1);
				builder.setKappa(0);
				
				MixedUtilityFunction uf = new MixedUtilityFunction(builder.build());
				r.getUtilities().set(r.getAccessDecisions().get(s), uf.calculateUtility());
			}
			
			// log
			printCognitiveRadioData(s);
			out.println("Number of collisions in init phase: " + collisions.get(INIT_PHASE));
			
			radios.stream().forEach(CRSystem::clearRadio);
		}
		
		for (CognitiveRadio r : radios) {
			out.println(r.getUtilities());
		}
	}
	
	// TODO
	private void playSetPhase() {
		
	}
	
	// TODO
	private void playDecidePhase() {
		
	}
	
	/**
	 * Prints CognitiveRadio data to output.
	 */
	private void printCognitiveRadioData(int strategyIndex) {
		for (CognitiveRadio r : radios) {
			out.println();
			out.println("Radio " + radios.indexOf(r) + ": ");
			
			out.println("Channel access decision: ");
			out.println(CognitiveRadio.getStrategySpace().get(r.getAccessDecisions().get(strategyIndex)));
			out.println();
			
			out.println("Channel captures (Subslot, Channel)");
			r.getCaptured().stream().forEach(out::println);
			out.println();
			
			out.println("Channel capture probabilities (Subslot, Channel)");
			r.getCaptureProbabilities().stream().forEach(cps -> out.println(ListUtility.formatDoubleList(cps, cp -> String.format("%.3f", cp))));
			out.println();
			
			out.println("User estimate on strategy " + r.getAccessDecisions().get(strategyIndex));
			out.println(ListUtility.formatDoubleList(r.getContentions().get(r.getAccessDecisions().get(strategyIndex)), c -> String.format("%.3f", c)));
			out.println();
			
			out.println("Utility on strategy " + r.getAccessDecisions().get(strategyIndex) + ": " + r.getUtilities().get(r.getAccessDecisions().get(strategyIndex)));
		}
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
			out.println("NO COLLISION - No radios attempt to access the channel!");
			return -2;
		}
		else if (sortedBackOffs.get(1).equals(Double.NaN)) {
			out.println("NO COLLISION - Only one radio attempts to access the channel!");
			return backoffTimes.indexOf(sortedBackOffs.get(0));
		}
		
		int minIndex = backoffTimes.indexOf(sortedBackOffs.get(0));
		int min2Index = backoffTimes.indexOf(sortedBackOffs.get(1));
		
		if (backoffTimes.get(minIndex) + UtilityConstants.MODE_SWITCH_TIME < backoffTimes.get(min2Index)) {
			out.println("NO COLLISION - " + ListUtility.formatDoubleList(backoffTimes, b -> String.format("%.3f", b)));
			return minIndex;
		}
		
		out.println("COLLISION - 1st backoff: " + String.format("%.3f", backoffTimes.get(minIndex)) + 
				", 2nd backoff: " + String.format("%.3f", backoffTimes.get(min2Index)));
		return -1;
	}
	
	/**
	 * Setting initial parameters of n Cognitive Radios.
	 * 
	 * @param n - number of CognitiveRadio entities to initialize
	 * @param channelNumber - number of Channels in the system
	 */
	private void initRadios(int n, int channelNumber) {
		radios = new ArrayList<>(n);
		
		for (int i = 0; i < n; i++) {
			CognitiveRadioBuilder crb = new CognitiveRadioBuilder();
			
			// get and set demand
			System.out.print("Demand of user " + (i + 1) + " in bit/slot: ");
			crb.setDemand(scanner.nextDouble());
			
			// get and set strategy
			System.out.print("Strategy of user " + (i + 1) + " (1 - regret tracking, 2 - max utility, 3 - random): ");
			setStrategy(crb, scanner.nextInt());
			
			System.out.println();
			
			// captureProbabilities
			List<List<Double>> captureProbabilities = new ArrayList<>(channelNumber);
			for (int j = 0; j < channelNumber; j++) {
				captureProbabilities.add(new ArrayList<>(UtilityConstants.NUMBER_OF_SUBSLOTS));
			}
			crb.setCaptureProbabilities(captureProbabilities);
			
			// captured
			List<List<Boolean>> captured = new ArrayList<>(channelNumber);
			for (int j = 0; j < channelNumber; j++) {
				captured.add(new ArrayList<>(UtilityConstants.NUMBER_OF_SUBSLOTS));
			}
			crb.setCaptured(captured);
			
			// contentions
			List<List<Double>> contentions = new ArrayList<>(strategySpaceSize);
			for (int j = 0; j < CognitiveRadio.getStrategySpace().size(); j++) {
				contentions.add(new ArrayList<>(channelNumber));
			}
			crb.setContentions(contentions);
			
			// utilities
			List<Double> utilities = new ArrayList<>(strategySpaceSize);
			for (int j = 0; j < CognitiveRadio.getStrategySpace().size(); j++) {
				utilities.add(0.0);
			}
			crb.setUtilities(utilities);
			
			// regrets
			crb.setRegrets(new ArrayList<>(strategySpaceSize));
			
			radios.add(crb.build());
		}	
	}
	
	/**
	 * Setting initial parameters of Channel entities.
	 * 
	 * @param n - number of channels to initialize
	 */
	private void initChannels(int n) {
		channels = new ArrayList<>(n);
		
		for (int i = 0; i < n; i++) {
			// get transmission rate
			System.out.print("Transmission rate of channel " + (i + 1) + ": ");
			double transmissionRate = scanner.nextDouble();
			
			// get frequency 
			System.out.print("Frequency of channel " + (i + 1) + ": ");
			double frequency = scanner.nextDouble();
			
			System.out.println();

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
	
	/**
	 * Set strategy for given CognitiveRadioBuilder.
	 * @param crb
	 * @param strategyNumber
	 */
	private void setStrategy(CognitiveRadioBuilder crb, int strategyNumber) {
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
	}
	
	private static void clearRadio(CognitiveRadio radio) {
		radio.getCaptured().stream().forEach(List::clear);
		radio.getCaptureProbabilities().stream().forEach(List::clear);
		radio.getContentions().stream().forEach(List::clear);
		//radio.getUtilities().clear();
	}
}

