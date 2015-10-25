package hu.bme.cr.uf;

import static hu.bme.cr.utilities.UtilityConstants.MAX_BACKOFF;
import static hu.bme.cr.utilities.UtilityConstants.MODE_SWITCH_TIME;

import java.util.List;

import hu.bme.cr.entity.UtilityFunctionParameters;

/**
 * 
 * @author Zoltán Koleszár
 *
 * Represents a utility function of a user,
 * which plays competitive and cooperative
 * strategies simultaneously.
 * 
 * With the rate1, rate2 and rate3 parameters can the client 
 * set the behavior of the player, i.e. a more cooperative or
 * a more competitive behavior.
 * In case of rate2 = 0 and rate3 = 0, the client can use the
 * CompetitiveUtilityFunction implementation.
 */
public class MixedUtilityFunction implements IUtilityFunction {
	
	private UtilityFunctionParameters params;
	
	public MixedUtilityFunction(UtilityFunctionParameters params) {
		this.params = params; 
	}

	/**
	 * Calculates the payoff of a user
	 * that uses mixed strategies, i.e. 
	 * it is competitive and cooperative
	 * at the same time.
	 * 
	 * @return payoff of the ith user
	 */
	public double calculateUtility() {
		double rate1 = params.getRate1();
		double rate2 = params.getRate2();
		double rate3 = params.getRate3();
		
		return rate1 * calculateSelfishInterest()
				+ rate2 * calculateTransmissionPenalty()
				+ rate3 * calculateCollisionPenalty();
	}
	
	/**
	 * Calculates the selfish payoff of the utility function.
	 * 
	 * @throws IllegalArgumentException - sizes of the lists from params are not equal
	 * @return payoff of the ith user
	 */
	private double calculateSelfishInterest() {
		List<Boolean> accessDecisions = params.getAccessDecisions();
		List<Double> contentions = params.getContentions();
		List<Double> transmissionRates = params.getTransMissionRates();
		
		if (accessDecisions.size() != contentions.size() || accessDecisions.size() != transmissionRates.size()) {
			throw new IllegalArgumentException("List sizes are not equal in selfish payoff calculation!");
		}
		
		int size = transmissionRates.size();
		double competitiveElement = 0.0;
		double constantElement = (1 - MODE_SWITCH_TIME / MAX_BACKOFF);
		
		for (int i = 0; i < size; i++) {
			if (accessDecisions.get(i)) {
				double element1 = transmissionRates.get(i) / ( params.getDemand() * (1 + contentions.get(i)) );
				competitiveElement += element1 * Math.pow(constantElement, contentions.get(i));
			}
		}
		
		return Math.min(competitiveElement, 1);
	}
	
	/**
	 * Calculates the cooperative payoff of the utility function,
	 * which is a penalty if the users transmission rate exceeds
	 * its demand.
	 *  
	 * @throws IllegalArgumentException - sizes of the lists from params are not equal
	 * @return payoff
	 */
	private double calculateTransmissionPenalty() throws IllegalArgumentException {
		double demand = params.getDemand();
		
		List<Double> transmissionRates = params.getTransMissionRates();
		List<Double> captureProbabilities = params.getCaptureProbabilities();
		
		if (transmissionRates.size() != captureProbabilities.size()) {
			throw new IllegalArgumentException("List sizes are not equal in transmission penalty calculation!");
		}
		
		int size = transmissionRates.size();
		double penalty = 0.0;
		
		for (int i = 0; i < size; i++) {
			penalty += transmissionRates.get(i) * captureProbabilities.get(i);
		}
		
		return (-1 / demand) * Math.max(0, penalty - demand + params.getKappa());
	}
	
	/**
	 * Calculates the cooperative payoff of the utility function
	 * which is a penalty if the user causes collision on the 
	 * given channel.
	 * 
	 * @throws IllegalArgumentException - sizes of the lists from params are not equal
	 * @return payoff
	 */
	private double calculateCollisionPenalty() {
		List<Double> contentions = params.getContentions();
		List<Double> transmissionRates = params.getTransMissionRates();
		List<Double> collisionProbabilities = params.getCollisionProbabilities();
		
		if (contentions.size() != transmissionRates.size() || contentions.size() != collisionProbabilities.size()) {
			throw new IllegalArgumentException("List sizes are not equal in collision penalty calculation!");
		}
		
		int size = transmissionRates.size();
		double penalty = 0.0;
		double denominator = 0.0;
		
		for (int i = 0; i < size; i++) {
			denominator += transmissionRates.get(i);
			
			if (contentions.get(i) > 0.001) {
				penalty += (transmissionRates.get(i) * collisionProbabilities.get(i)) / contentions.get(i);
			}
		}
		
		return (-1 / denominator) * penalty;
	}

}
