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

import org.bson.Document;

import hu.bme.cr.dsl.DataStore;
import hu.bme.cr.entity.Channel;
import hu.bme.cr.entity.CognitiveRadio;
import hu.bme.cr.entity.CognitiveRadio.CognitiveRadioBuilder;
import hu.bme.cr.strategies.MaxUtilityStrategy;
import hu.bme.cr.strategies.RandomStrategy;
import hu.bme.cr.strategies.RegretTrackingStrategy;
import hu.bme.cr.strategies.StrategySpace;
import hu.bme.cr.uf.CompetitiveUtilityFunction;
import hu.bme.cr.uf.MixedUtilityFunction;
import hu.bme.cr.uf.UtilityFunctionParameters.UtilityFunctionParametersBuilder;
import hu.bme.cr.utilities.ChannelUtility;
import hu.bme.cr.utilities.CognitiveRadioUtility;
import hu.bme.cr.utilities.ListUtility;
import hu.bme.cr.utilities.UtilityConstants;

public class CRSystem {
	
	private static final String INIT_PHASE = "INIT_PHASE";
	private static final String SET_PHASE = "SET_PHASE";
	private static final String NORMAL_PHASE = "NORMAL_PHASE";
	
	private DataStore ds;
	
	private Document doc;
	
	private List<Document> phases;

	private List<CognitiveRadio> radios;
	
	private List<Channel> channels;
	
	private Map<String, Integer> collisions;
	
	private PrintStream out;
	
	private Scanner scanner = new Scanner(System.in);
	
	private int strategySpaceSize;
	
	public CRSystem() {
		collisions = new HashMap<>(27);
		ds = new DataStore();
		doc = new Document();
		phases = new ArrayList<>();
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
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.print("Where should I log? (1 - console, 2 - in text file): ");
		String fileName = initLog(scanner.nextInt());
	
		// read the number of channels and set params
		System.out.print("Number of channels: ");
		int channelNumber = scanner.nextInt();
		
		initChannels(channelNumber);
		
		// read the number of channels that the users can access
		System.out.print("Maximum number of channels that users can use: ");
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
		
		doc.append("name", fileName)
			.append("subslots", UtilityConstants.NUMBER_OF_SUBSLOTS)
			.append("radios", radios.size())
			.append("channels", channels.size())
			.append("strategySpaceSize", strategySpaceSize);
	}
	
	
	
	/**
	 * Initial game phase. Steps:
	 * <ol>
	 * <li>Every radio choose a channel access (primary strategy) and a secondary strategy order.</li>
	 * <li>Channel access (steps described in play method).</li>
	 * <ol>
	 */
	public void playInitPhase() {
		out.println("****************");
		out.println("** INIT PHASE **");
		out.println("****************");
		
		// 1. Every radio choose a channel access (primary strategy) and a secondary strategy order
		radios.stream().forEach(CognitiveRadio::playInitPhase);	

		play(INIT_PHASE);
	}
	
	/**
	 * Set game phase. Steps:
	 * <ol>
	 * <li>Play set phase of the CognitiveRadios strategy.</li>
	 * <li>Channel access (steps described in play method).</li>
	 * <ol>
	 */
	public void playSetPhase() {
		radios.stream().forEach(CognitiveRadio::playSetPhase);
		radios.stream().forEach(radio -> out.println("Regrets: " + ListUtility.formatDoubleList(radio.getRegrets(), reg -> String.format("%.3f", reg))));
		persist(INIT_PHASE);
		radios.stream().forEach(radio -> radio.getContentions().stream().forEach(List::clear));
		
		out.println();
		out.println();
		out.println("****************");
		out.println("** SET PHASE  **");
		out.println("****************");
		
		play(SET_PHASE);
	}
	
	/**
	 * Normal game phase. Steps:
	 * <ol>
	 * <li>Play normal/decide phase of the CognitiveRadios strategy.</li>
	 * <li>Channel access (steps described in play method).</li>
	 * <ol>
	 * 
	 * @param number of the round
	 */
	public void playDecidePhase(int r) {
		radios.stream().forEach(CognitiveRadio::playDecidePhase);
		radios.stream().forEach(radio -> out.println("Regrets: " + ListUtility.formatDoubleList(radio.getRegrets(), reg -> String.format("%.3f", reg))));
		
		if (r == 0) {
			persist(SET_PHASE);
		}
		else {
			persist(NORMAL_PHASE + "_" + r);
		}
		
		radios.stream().forEach(radio -> radio.getContentions().stream().forEach(List::clear));
		
		out.println();
		out.println();
		out.println("***************************");
		out.println("** NORMAL PHASE, ROUND " + r + "**");
		out.println("***************************");
		
		play(NORMAL_PHASE + "_" + r);
	}
	
	/**
	 * Ends the game by persisting important 
	 * data to MongoDB database ("thesis" database, 
	 * "simulations" collection) and by closing output.
	 */
	public void endGame() {
		persist(NORMAL_PHASE + "_" + (UtilityConstants.ROUNDS - 1));
		doc.append("phases", phases);
		ds.getDocuments().insertOne(doc);
		
		out.println();
		out.println("***********************");
		out.println("** End of Simulation **");
		out.println("***********************");
		out.close();		
	}
	
	/**
	 * Steps: 
	 * <ol>
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
	 * 
	 * @param key - key to store the number of collisions
	 */
	private void play(String key) {
		// play every strategy
		// 1. Channel access in each subslot by generating a random backoff time.
		// 2. Calculate channel access probability by backoff time and add it to CognitiveRadios captureProbabilities list.
		// 3. Check if there was a collision on the channel at the given subslot according to the backoff times.
		for (int s = 0; s < strategySpaceSize; s++) {
			collisions.put(key + "_" + s, 0);
			
			for (int i = 0; i < channels.size(); i++) {
				System.out.println();
				System.out.println("Channel " + (i + 1) + ":");
				
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
						collisions.replace(key + "_" + s, Math.incrementExact(collisions.get(key + "_" + s)));
					}
					
					for (int k = 0; k < radios.size(); k++) {
						radios.get(k).getCaptured().get(i).add(k == minIndex);
					}
				}
			}
		
			// 4. Calculate user estimate (contentions) for each CognitiveRadio.
			// 5. Calculate channel capture probability for each CognitiveRadio using user estimate.
			// 6. Calculate channel collision probability for each CognitiveRadio using user estimate.
			// 7. Calculate utility for each CognitiveRadio.
			for (CognitiveRadio r : radios) {
				List<Double> contentions = r.getContentions().get(r.getAccessDecisions().get(s));
				List<Double> captures = new ArrayList<>(channels.size());
				List<Double> collisionsProbabilities = new ArrayList<>(channels.size());
				
				for (int i = 0; i < r.getCaptured().size(); i++) {
					double contention = ChannelUtility.calculateUserEstimate(r.getCaptured().get(i), r.getCaptureProbabilities().get(i));
					contentions.add(contention);
					captures.add(CognitiveRadioUtility.calculateCaptureProbability(contention));
					Collections.replaceAll(captures, Double.NaN, 0.0);
					collisionsProbabilities.add(CognitiveRadioUtility.calculateCollisionProbability(contention));
					Collections.replaceAll(collisionsProbabilities, Double.NaN, 0.0);
				}
				
				Collections.replaceAll(contentions, Double.NaN, 0.0);
				
				UtilityFunctionParametersBuilder builder = new UtilityFunctionParametersBuilder();
				builder.setTransMissionRates(channels.stream().map(Channel::getTransmissionRate).collect(Collectors.toList()));
				builder.setAccessDecisions(CognitiveRadio.getStrategySpace().get(r.getAccessDecisions().get(s)));
				builder.setContentionLevel(contentions);
				builder.setCaptureProbabilities(captures);
				builder.setCollisionProbabilities(collisionsProbabilities);
				builder.setDemand(r.getDemand());
				
				// calculate utility							
				r.getUtilities().set(r.getAccessDecisions().get(s), r.calculateUtility(builder.build()));
			}
			
			
			// log
			printCognitiveRadioData(s);
			out.println("Number of collisions in " + key + "_" + s + " phase: " + collisions.get(key + "_" + s));

			radios.stream().forEach(CRSystem::clearRadio);
		}
		
		for (CognitiveRadio r : radios) {
			out.println("Utilities: " + ListUtility.formatDoubleList(r.getUtilities(), u -> String.format("%.3f", u)));
		}
	}
	
	private void persist(String phase) {
		Document phaseDoc = new Document();
		
		List<Document> radioDocs = new ArrayList<>();
		for (CognitiveRadio r : radios) {
			Document radioDoc = new Document();
			radioDoc
				.append("radio", radios.indexOf(r))
				.append("accessDecisions", r.getAccessDecisions())
				.append("utilities", r.getUtilities())
				.append("regrets", r.getRegrets())
				.append("demand", r.getDemand())
				.append("contentions", r.getContentions())
				.append("utilityFunction", r.getUtilityFunction().getType())
				.append("rates", r.getUtilityFunction().getRates())
				.append("strategy", r.getStrategy().toString());
			
			radioDocs.add(radioDoc);
		}

		// .append("collisions", collisions.entrySet().stream().filter(c -> c.getKey().contains(phase)).collect(Collectors.toList()))
		phaseDoc
			.append("radios", radioDocs);
		
		phases.add(phaseDoc);
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
			System.out.println("NO COLLISION - 1 user");
			return 0;
		}
		
		List<Double> sortedBackOffs = new ArrayList<>(backoffTimes);
		Collections.sort(sortedBackOffs);
		
		if (sortedBackOffs.get(0).equals(Double.NaN)) {
			System.out.println("NO COLLISION - No radios attempt to access the channel!");
			return -2;
		}
		else if (sortedBackOffs.get(1).equals(Double.NaN)) {
			System.out.println("NO COLLISION - Only one radio attempts to access the channel!");
			return backoffTimes.indexOf(sortedBackOffs.get(0));
		}
		
		int minIndex = backoffTimes.indexOf(sortedBackOffs.get(0));
		int min2Index = backoffTimes.indexOf(sortedBackOffs.get(1));
		
		if (backoffTimes.get(minIndex) + UtilityConstants.MODE_SWITCH_TIME < backoffTimes.get(min2Index)) {
			System.out.println("NO COLLISION - " + ListUtility.formatDoubleList(backoffTimes, b -> String.format("%.3f", b)));
			return minIndex;
		}
		
		System.out.println("COLLISION - 1st backoff: " + String.format("%.3f", backoffTimes.get(minIndex)) + 
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
			
			// get and set utility function
			System.out.print("Utility function of user " + (i + 1) + " (1 - competitive, 2 - mixed): ");
			setUtilityFunction(crb, scanner.nextInt());
			
			System.out.println();
			
			// captureProbabilities
			crb.setCaptureProbabilities(ListUtility.getInitial2DList(Double.class, channelNumber, UtilityConstants.NUMBER_OF_SUBSLOTS));
			
			// captured
			crb.setCaptured(ListUtility.getInitial2DList(Boolean.class, channelNumber, UtilityConstants.NUMBER_OF_SUBSLOTS));
			
			// contentions
			crb.setContentions(ListUtility.getInitial2DList(Double.class, strategySpaceSize, channelNumber));
			
			// utilities
			crb.setUtilities(ListUtility.fillListWithNValues(0.0, strategySpaceSize));
			
			// regrets
			crb.setRegrets(new ArrayList<>(strategySpaceSize));
			
			radios.add(crb.build());
		}	
	}

	/**
	 * Sets initial parameters of Channel entities.
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
	private String initLog(int n) {
		String fileName = "simulation_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());

		if (n == 1) {
			out = System.out;
		}
		else if (n == 2) {
			try {
				out = new PrintStream(new File("C:\\Users\\Zoltán Koleszár\\Documents\\Diplomaterv\\log\\" + fileName + ".txt"));	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			throw new IllegalArgumentException("Wrong logging output!");
		}

		return fileName;
	}
	
	/**
	 * Sets the utiliti function of the given user.
	 * 
	 * @param crb
	 * @param n
	 */
	private void setUtilityFunction(CognitiveRadioBuilder crb, int n) {
		if (n == 1) {
			crb.setUtilityFunction(new CompetitiveUtilityFunction());
		}
		else if (n == 2) {
			System.out.print("Rate for the competitive part of the utility function: ");
			double rate1 = scanner.nextDouble();
			System.out.print("Rate for the overtransmission penalty: ");
			double rate2 = scanner.nextDouble();
			System.out.print("Rate for the collision penalty: ");
			double rate3 = scanner.nextDouble();
			
			crb.setUtilityFunction(new MixedUtilityFunction(rate1, rate2, rate3));
		}
		else {
			throw new IllegalArgumentException("Choose 1 - competitive, or 2 - mixed to set utility function!");
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
	
	/**
	 * Clears the 2-dimensional lists of the given radio.
	 * @param radio
	 */
	private static void clearRadio(CognitiveRadio radio) {
		radio.getCaptured().stream().forEach(List::clear);
		radio.getCaptureProbabilities().stream().forEach(List::clear);
	}
}

