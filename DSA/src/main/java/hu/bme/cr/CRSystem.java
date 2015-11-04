package hu.bme.cr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import hu.bme.cr.entity.Channel;
import hu.bme.cr.entity.CognitiveRadio;
import hu.bme.cr.entity.CognitiveRadio.CognitiveRadioBuilder;
import hu.bme.cr.strategies.RegretTrackingStrategy;
import hu.bme.cr.strategies.StrategySpace;

public class CRSystem {
	
	private int subslots;
	
	private List<CognitiveRadio> radios;
	
	private List<Channel> channels;
	
	public CRSystem() {

	}
	
	public CRSystem(List<CognitiveRadio> radios, List<Channel> channels, int subslots) {
		this.radios = radios;
		this.channels = channels;
		this.subslots = subslots;
	}

	/*
	 * Getter and setter
	 */
	
	public int getSubslots() {
		return subslots;
	}

	public void setSubslots(int subslots) {
		this.subslots = subslots;
	}

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
			System.out.println("Number of channels: ");
			int channelNumber = scanner.nextInt();
			
			initChannels(channelNumber);
			
			// read the number of cognitive radio devices and set params
			System.out.println("Number of cognitive radio devices: ");
			int radioNumber = scanner.nextInt();
			
			initRadios(radioNumber, channelNumber);
			
			// read the number of subslots
			System.out.println("Number of subslots: ");
			subslots = scanner.nextInt();			
		}
	}
	
	/**
	 * Setting initial parameters of n Cognitive Radios.
	 * 
	 * @param n - number of CognitiveRadio entities to initialize
	 * @param channelNumber - number of Channels in the system
	 */
	private void initRadios(int n, int channelNumber) {
		
		try (Scanner scanner = new Scanner(System.in)) {
			radios = new ArrayList<>(n);
			
			for (int i = 0; i < n; i++) {
				CognitiveRadioBuilder crb = new CognitiveRadioBuilder();
				
				// get and set demand
				System.out.println("Demand of user " + i + " in bit/slot: ");
				crb.setDemand(scanner.nextDouble());
				
				// get and set max number of channels
				System.out.println("Demand of max channel that user " + i + " can use: ");
				int maxChannels = scanner.nextInt();
				crb.setMaxChannels(maxChannels);
				
				// get and set strategy
				System.out.println("Strategy of user " + i + "(1 - regret tracking, 2 - utility): ");
				int strategyNumber = scanner.nextInt();
				
				// get and set step size
				System.out.println("Stepsize (1 - fix, 2 - decreasing): ");
				int stepSize = scanner.nextInt();
				
				// TODO
				if (strategyNumber == 1) {
					crb.setStrategy(new RegretTrackingStrategy(stepSize));
				}
				else {
					throw new IllegalArgumentException("Wrong strategy!");
				}
				
				// set strategy space
				List<Boolean> strategies = Collections.nCopies(maxChannels, true);
				strategies.addAll(Collections.nCopies(channelNumber - maxChannels, false));
				crb.setStrategySpace(StrategySpace.getStrategySpace(strategies));
				
				radios.add(crb.build());
			}
			
		}
	}
	
	/**
	 * Setting initial parameters of Channel entities.
	 * 
	 * @param n - number of channels to initialize
	 */
	private void initChannels(int n) {
		
		try (Scanner scanner = new Scanner(System.in)) {
			channels = new ArrayList<>(n);
			
			for (int i = 0; i < n; i++) {
				// get transmission rate
				System.out.println("Transmission rate of the channel: ");
				double transmissionRate = scanner.nextDouble();
				
				// get frequency 
				System.out.println("Frequency of the channel: ");
				double frequency = scanner.nextDouble();

				// set Channel parameters and add it to channels list 
				channels.add(new Channel(transmissionRate, frequency, false));
			}
		}
	}
}

