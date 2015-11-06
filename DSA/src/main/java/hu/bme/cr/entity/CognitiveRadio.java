package hu.bme.cr.entity;

import java.util.List;

import hu.bme.cr.strategies.IStrategy;
import hu.bme.cr.strategies.StrategyParameters;
import hu.bme.cr.uf.IUtilityFunction;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * <br>
 * Models a secondary users (SU) cognitive radio (CR) device, that is capable of:
 * <ul>
 * 	<li>spectrum sensing, e.g. sensing allocated channels</li>
 * 	<li>spectrum management, e.g. during the sensing collected data 
 * 	can be used to decide whether to access the channel or not</li>
 * 	<li>spectrum mobility, e.g. changing operating frequency band 
 * 	according to used/unused radio channels</li>
 * </ul>
 * 
 * Secondary user, cognitive radio and player in this context have the same meaning.
 */
public class CognitiveRadio {

	/**
	 * Transmission rate demand of the ith player (CR).
	 */
	private double demand;
	
	/**
	 * The number of channels that the ith player can access in a time slot.
	 */
	private int maxChannels;
	
	/**
	 * The strategy played by the ith player, which can be:
	 * <ul>
	 * 	<li>competitive</li>
	 * 	<li>cooperative</li>
	 * 	<li>mix of the latter two</li>
	 * </ul>
	 */
	private IStrategy strategy;
	
	/**
	 * Utility function to calculate the payoff of the given channel.
	 */
	private IUtilityFunction utilityFunction;
	
	/**
	 * Channel access decision vector of the ith player.
	 * <ul>
	 * 	<li>true: the secondary user can access the given {@link Channel}</li>
	 * 	<li>false: the given {@link Channel} can not be accessed, because it 
	 * 	is either occupied by a primary user (PU) or the decision made by the
	 * 	strategy decided not to access it</li>
	 * </ul>
	 */
	private List<Boolean> accessDecisions;
	
	/**
	 * List of probabilites that the CognitiveRadio
	 * captures the channel at subslot w, where
	 * w = 1, 2, ... W.
	 */
	private List<List<Double>> captureProbabilities;
	
	/**
	 * Contention estimate vector for the ith player.
	 * It stores the user number estimates for each channel. 
	 */
	private List<Double> contentions;
	
	/**
	 * We have to know the utilities of every strategies
	 * from the strategy space (those which the Cognitive Radio
	 * didn't play as well) to implement the regret tracking
	 * algorithm.
	 */
	private List<Double> utilities;
	
	/**
	 * The mean regret of each channel.
	 */
	private List<Double> regrets;
	
	/**
	 * Strategy space of the user, which is calculated
	 * by the StrategySpace class. It is given by the
	 * maximal number of channels that the user can access,
	 * i.e. maxChannels.
	 */
	private List<List<Boolean>> strategySpace;
	
	/**
	 * Constructor that uses the services of its Builder class.
	 * 
	 * @param builder
	 */
	private CognitiveRadio(CognitiveRadioBuilder builder) {
		this.demand = builder.demand;
		this.maxChannels = builder.maxChannels;
		this.strategy = builder.strategy;
		this.utilityFunction = builder.utilityFunction;
		this.accessDecisions = builder.accessDecisions;
		this.captureProbabilities = builder.captureProbabilities;
		this.contentions = builder.contentions;
		this.utilities = builder.utilities;
		this.regrets = builder.regrets;
		this.strategySpace = builder.strategySpace;
	}
	
	// TODO
	public void playInitPhase(int max) {
		strategy.decideInInitPhase(max);
	}
	
	// TODO
	public void playSetPhase(StrategyParameters params) {
		strategy.decideInSetPhase(params);
	}
	
	// TODO
	public void play(StrategyParameters params) {
		strategy.decide(params);
	}
	
	/*
	 * Getters and setters
	 */
	
	public double getDemand() {
		return demand;
	}

	public int getMaxChannels() {
		return maxChannels;
	}

	public IStrategy getStrategy() {
		return strategy;
	}

	public List<Boolean> getAccessDecisions() {
		return accessDecisions;
	}
	
	public void setDemand(double demand) {
		this.demand = demand;
	}

	public void setMaxChannels(int maxChannels) {
		this.maxChannels = maxChannels;
	}

	public void setStrategy(IStrategy strategy) {
		this.strategy = strategy;
	}

	public void setAccessDecisions(List<Boolean> accessDecisions) {
		this.accessDecisions = accessDecisions;
	}

	public List<List<Double>> getCaptureProbabilities() {
		return captureProbabilities;
	}

	public void setCaptureProbabilities(List<List<Double>> captureProbabilities) {
		this.captureProbabilities = captureProbabilities;
	}

	public List<Double> getContentions() {
		return contentions;
	}

	public void setContentions(List<Double> contentions) {
		this.contentions = contentions;
	}

	public List<Double> getUtilities() {
		return utilities;
	}

	public void setUtilities(List<Double> utilities) {
		this.utilities = utilities;
	}

	public List<List<Boolean>> getStrategySpace() {
		return strategySpace;
	}

	public void setStrategySpace(List<List<Boolean>> strategySpace) {
		this.strategySpace = strategySpace;
	}

	public List<Double> getRegrets() {
		return regrets;
	}

	public void setRegrets(List<Double> regrets) {
		this.regrets = regrets;
	}

	public IUtilityFunction getUtilityFunction() {
		return utilityFunction;
	}

	public void setUtilityFunction(IUtilityFunction utilityFunction) {
		this.utilityFunction = utilityFunction;
	}


	/**
	 * 
	 * @author Zoltán Koleszár
	 *
	 * Builder class for CognitiveRadio.
	 * 
	 */
	public static class CognitiveRadioBuilder {

		private double demand;
		private int maxChannels;
		private IStrategy strategy;
		private IUtilityFunction utilityFunction;
		private List<Boolean> accessDecisions;
		private List<List<Double>> captureProbabilities;
		private List<Double> contentions;
		private List<Double> utilities;
		private List<Double> regrets;
		private List<List<Boolean>> strategySpace;
		
		public CognitiveRadioBuilder setDemand(double demand) {
			this.demand = demand;
			return this;
		}
		
		public CognitiveRadioBuilder setMaxChannels(int maxChannels) {
			this.maxChannels = maxChannels;
			return this;
		}
		
		public CognitiveRadioBuilder setUtilityFunction(IUtilityFunction utilityFunction) {
			this.utilityFunction = utilityFunction;
			return this;
		}
		
		public CognitiveRadioBuilder setStrategy(IStrategy strategy) {
			this.strategy = strategy;
			return this;
		}
		
		public CognitiveRadioBuilder setAccessDecisions(List<Boolean> accessDecisions) {
			this.accessDecisions = accessDecisions;
			return this;
		}

		public CognitiveRadioBuilder setCaptureProbabilities(List<List<Double>> captureProbabilities) {
			this.captureProbabilities = captureProbabilities;
			return this;
		}

		public CognitiveRadioBuilder setContentions(List<Double> contentions) {
			this.contentions = contentions;
			return this;
		}
		
		public CognitiveRadioBuilder setUtilities(List<Double> utilities) {
			this.utilities = utilities;
			return this;
		}
		
		public CognitiveRadioBuilder setStrategySpace(List<List<Boolean>> strategySpace) {
			this.strategySpace = strategySpace;
			return this;
		}
		
		public CognitiveRadioBuilder setRegrets(List<Double> regrets) {
			this.regrets = regrets;
			return this;
		}
		
		public CognitiveRadio build() {
			return new CognitiveRadio(this);
		}

	
	}
}
