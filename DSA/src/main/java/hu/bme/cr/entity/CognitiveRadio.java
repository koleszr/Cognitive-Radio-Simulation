package hu.bme.cr.entity;

import java.util.List;

import hu.bme.cr.strategies.IStrategy;

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
	 * The amount of time to switch between channel sensing
	 * and transmission modes.
	 */
	public static final double MODE_SWITCH_TIME = 0.05;

	/**
	 * Transmission rate demand of the ith player (CR).
	 */
	private double demand;
	
	/**
	 * The number of channels that the ith player can access in a time slot.
	 */
	private int maxChannels;
	
	/**
	 * the strategy played by the ith player, which can be:
	 * <ul>
	 * 	<li>competitive</li>
	 * 	<li>cooperative</li>
	 * 	<li>mix of the latter two</li>
	 * </ul>
	 */
	private IStrategy strategy;
	
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
	
	
	private CognitiveRadio(CognitiveRadioBuilder builder) {
		this.demand = builder.demand;
		this.maxChannels = builder.maxChannels;
		this.strategy = builder.strategy;
		this.accessDecisions = builder.accessDecisions;
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
		private List<Boolean> accessDecisions;
		
		public CognitiveRadioBuilder setDemand(double demand) {
			this.demand = demand;
			return this;
		}
		
		public CognitiveRadioBuilder setMaxChannels(int maxChannels) {
			this.maxChannels = maxChannels;
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
		
		public CognitiveRadio build() {
			return new CognitiveRadio(this);
		}
	}
}
