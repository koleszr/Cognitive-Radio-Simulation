package hu.bme.cr.entity;

/**
 * 
 * @author Zoltán Koleszár
 *
 * <p>A wrapper class to pass parameters to IStrategy implementations.</p>
 * 
 */
public class StrategyParameters {
	
	private double transMissionRate;
	private double demand;
	private double modeSwitchType;
	private double contentionLevel;
	
	
	public StrategyParameters(StrategyParametersBuilder builder) {
		this.transMissionRate = builder.transMissionRate;
		this.demand = builder.demand;
		this.modeSwitchType = builder.modeSwitchType;
		this.contentionLevel = builder.contentionLevel;
	}
	
	
	
	public double getTransMissionRate() {
		return transMissionRate;
	}

	public void setTransMissionRate(double transMissionRate) {
		this.transMissionRate = transMissionRate;
	}

	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	public double getModeSwitchType() {
		return modeSwitchType;
	}

	public void setModeSwitchType(double modeSwitchType) {
		this.modeSwitchType = modeSwitchType;
	}

	public double getContentionLevel() {
		return contentionLevel;
	}

	public void setContentionLevel(double contentionLevel) {
		this.contentionLevel = contentionLevel;
	}


	/**
	 * 
	 * @author Zoltán Koleszár
	 * 
	 * Builder class for StrategyParameter.
	 * 
	 */
	public static class StrategyParametersBuilder {
		private double transMissionRate;
		private double demand;
		private double modeSwitchType;
		private double contentionLevel;
		
		
		public StrategyParametersBuilder setTransMissionRate(double transMissionRate) {
			this.transMissionRate = transMissionRate;
			return this;
		}
		public StrategyParametersBuilder setDemand(double demand) {
			this.demand = demand;
			return this;
		}
		public StrategyParametersBuilder setModeSwitchType(double modeSwitchType) {
			this.modeSwitchType = modeSwitchType;
			return this;
		}
		public StrategyParametersBuilder setContentionLevel(double contentionLevel) {
			this.contentionLevel = contentionLevel;
			return this;
		}

		public StrategyParameters build() {
			return new StrategyParameters(this);
		}
	}

}
