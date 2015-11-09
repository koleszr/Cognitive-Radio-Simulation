package hu.bme.cr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import hu.bme.cr.entity.Channel;
import hu.bme.cr.entity.CognitiveRadio;
import hu.bme.cr.entity.CognitiveRadio.CognitiveRadioBuilder;
import hu.bme.cr.strategies.MaxUtilityStrategy;
import hu.bme.cr.strategies.RandomStrategy;
import hu.bme.cr.strategies.RegretTrackingStrategy;
import hu.bme.cr.strategies.StrategySpace;

public class CRSystem {
	
	private List<CognitiveRadio> radios;
	
	private List<Channel> channels;
	
	public CRSystem() {

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
			// read the number of channels and set params
			System.out.print("Number of channels: ");
			int channelNumber = scanner.nextInt();
			
			initChannels(scanner, channelNumber);
			
			// read the number of channels that the users can access
			System.out.print("Maximum number of channels that users can use: ");
			int maxChannels = scanner.nextInt();
			
			CognitiveRadio.setMaxChannels(maxChannels);
			
			// calculate the strategy space
			List<Boolean> strategies = new ArrayList<>(Collections.nCopies(maxChannels, true));
			strategies.addAll(new ArrayList<>(Collections.nCopies(channelNumber - maxChannels, false)));
			
			CognitiveRadio.setStrategySpace(StrategySpace.getStrategySpace(strategies));
			
			// read the number of cognitive radio devices and set params
			System.out.print("Number of cognitive radio devices: ");
			int radioNumber = scanner.nextInt();
			
			initRadios(scanner, radioNumber, channelNumber);			
		}
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
			
			
			// regret tracking algorithm
			switch (strategyNumber) {
			case 1:
				System.out.print("Stepsize (1 - fix, 2 - decreasing): ");
				crb.setStrategy(new RegretTrackingStrategy(scanner.nextInt()));
				break;
			case 2:
				crb.setStrategy(new MaxUtilityStrategy());
				break;
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
			System.out.print("Transmission rate of the channel: ");
			double transmissionRate = scanner.nextDouble();
			
			// get frequency 
			System.out.print("Frequency of the channel: ");
			double frequency = scanner.nextDouble();

			// set Channel parameters and add it to channels list 
			channels.add(new Channel(transmissionRate, frequency, false));
		}
	}
	
	public void play() {
		// play init phase
		for (CognitiveRadio radio : radios) {
			radio.playInitPhase();
		}
	}
	
	
	
	public void printAll() {
		CognitiveRadio.getStrategySpace().stream().forEach(System.out::println);
		
		getRadios().stream().forEach(System.out::println);
		
		getChannels().stream().forEach(System.out::println);
	}
}

