package hu.bme.cr.entity;

import java.util.List;

/**
 * 
 * @author Zoltán Koleszár
 *
 * <p>A wrapper class to pass parameters to IUtilityFunction implementations.</p>
 * 
 */
public class UtilityFunctionParameters {
	
	private List<Double> transMissionRates;
	private double demand;
	private List<Boolean> accessDecisions;
	private List<Double> contentions;
	
	
	public UtilityFunctionParameters(UtilityFunctionParametersBuilder builder) {
		this.transMissionRates = builder.transMissionRates;
		this.demand = builder.demand;
		this.contentions = builder.contentions;
		this.accessDecisions = builder.accessDecisions;
	}
	
	
	
	public List<Double> getTransMissionRates() {
		return transMissionRates;
	}

	public void setTransMissionRates(List<Double> transMissionRates) {
		this.transMissionRates = transMissionRates;
	}

	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		this.demand = demand;
	}

	public List<Boolean> getAccessDecisions() {
		return accessDecisions;
	}

	public void setAccessDecisions(List<Boolean> accessDecisions) {
		this.accessDecisions = accessDecisions;
	}

	public List<Double> getContentions() {
		return contentions;
	}

	public void setContentions(List<Double> contentions) {
		this.contentions = contentions;
	}


	/**
	 * 
	 * @author Zoltán Koleszár
	 * 
	 * Builder class for StrategyParameter.
	 * 
	 */
	public static class UtilityFunctionParametersBuilder {
		private List<Double> transMissionRates;
		private double demand;
		private List<Double> contentions;
		private List<Boolean> accessDecisions;
		
		public UtilityFunctionParametersBuilder setTransMissionRates(List<Double> transMissionRates) {
			this.transMissionRates = transMissionRates;
			return this;
		}
		
		public UtilityFunctionParametersBuilder setDemand(double demand) {
			this.demand = demand;
			return this;
		}
		
		public UtilityFunctionParametersBuilder setContentionLevel(List<Double> contentions) {
			this.contentions = contentions;
			return this;
		}

		public UtilityFunctionParametersBuilder setAccessDecisions(List<Boolean> accessDecisions) {
			this.accessDecisions = accessDecisions;
			return this;
		}

		public UtilityFunctionParameters build() {
			return new UtilityFunctionParameters(this);
		}

	}

}
