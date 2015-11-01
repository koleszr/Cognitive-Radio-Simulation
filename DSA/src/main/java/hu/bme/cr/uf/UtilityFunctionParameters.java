package hu.bme.cr.uf;

import java.util.List;

/**
 * 
 * @author Zoltán Koleszár
 *
 * <p>A wrapper class to pass parameters to IUtilityFunction implementations.</p>
 * 
 */
public class UtilityFunctionParameters {
	private double demand;
	private double rate1;
	private double rate2;
	private double rate3;
	private double kappa;

	private List<Double> transMissionRates;
	private List<Boolean> accessDecisions;
	private List<Double> contentions;
	private List<Double> captureProbabilities;
	private List<Double> collisionProbabilities;
	
	
	public UtilityFunctionParameters(UtilityFunctionParametersBuilder builder) {
		this.transMissionRates = builder.transMissionRates;
		this.demand = builder.demand;
		this.contentions = builder.contentions;
		this.accessDecisions = builder.accessDecisions;
		this.rate1 = builder.rate1;
		this.rate2 = builder.rate2;
		this.rate3 = builder.rate3;
		this.kappa = builder.kappa;
		this.captureProbabilities = builder.captureProbabilities;
		this.collisionProbabilities = builder.collisionProbabilities;
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

	public double getRate1() {
		return rate1;
	}

	public void setRate1(double rate1) {
		this.rate1 = rate1;
	}

	public double getRate2() {
		return rate2;
	}

	public void setRate2(double rate2) {
		this.rate2 = rate2;
	}

	public double getRate3() {
		return rate3;
	}

	public void setRate3(double rate3) {
		this.rate3 = rate3;
	}

	public double getKappa() {
		return kappa;
	}

	public void setKappa(double kappa) {
		this.kappa = kappa;
	}

	public List<Double> getCaptureProbabilities() {
		return captureProbabilities;
	}

	public void setCaptureProbabilities(List<Double> captureProbabilities) {
		this.captureProbabilities = captureProbabilities;
	}

	public List<Double> getCollisionProbabilities() {
		return collisionProbabilities;
	}

	public void setCollisionProbabilities(List<Double> collisionProbabilities) {
		this.collisionProbabilities = collisionProbabilities;
	}


	/**
	 * 
	 * @author Zoltán Koleszár
	 * 
	 * Builder class for StrategyParameter.
	 * 
	 */
	public static class UtilityFunctionParametersBuilder {
		private double demand;
		private double rate1;
		private double rate2;
		private double rate3;
		private double kappa;
		
		private List<Double> transMissionRates;
		private List<Double> contentions;
		private List<Boolean> accessDecisions;
		private List<Double> captureProbabilities;
		private List<Double> collisionProbabilities;
		
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
		
		public UtilityFunctionParametersBuilder setRate1(double rate1) {
			this.rate1 = rate1;
			return this;
		}

		public UtilityFunctionParametersBuilder setRate2(double rate2) {
			this.rate2 = rate2;
			return this;
		}

		public UtilityFunctionParametersBuilder setRate3(double rate3) {
			this.rate3 = rate3;
			return this;
		}

		public UtilityFunctionParametersBuilder setKappa(double kappa) {
			this.kappa = kappa;
			return this;
		}

		public UtilityFunctionParametersBuilder setCaptureProbabilities(List<Double> captureProbabilities) {
			this.captureProbabilities = captureProbabilities;
			return this;
		}

		public UtilityFunctionParametersBuilder setCollisionProbabilities(List<Double> collisionProbabilities) {
			this.collisionProbabilities = collisionProbabilities;
			return this;
		}

		public UtilityFunctionParameters build() {
			return new UtilityFunctionParameters(this);
		}

	}

}
