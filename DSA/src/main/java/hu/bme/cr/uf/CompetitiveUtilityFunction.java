package hu.bme.cr.uf;

import java.util.List;

import hu.bme.cr.entity.UtilityFunctionParameters;
import static hu.bme.cr.utilities.UtilityConstants.*;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * Represents a competitive utility function
 * where the users aim is to maximize its own 
 * utility independently from others. 
 */
public class CompetitiveUtilityFunction implements IUtilityFunction {
	
	private UtilityFunctionParameters params;
	
	public CompetitiveUtilityFunction(UtilityFunctionParameters params) {
		this.params = params;
	}

	/**
	 * Calculates the payoff of a selfish user.
	 * 
	 * @throws IllegalArgumentException - sizes of the lists from params are not equal
	 * @return payoff of the ith user
	 */
	public double calculateUtility() throws IllegalArgumentException {
		List<Boolean> accessDecisions = params.getAccessDecisions();
		List<Double> contentions = params.getContentions();
		List<Double> transMissionRates = params.getTransMissionRates();
		
		if (accessDecisions.size() != contentions.size() || accessDecisions.size() != transMissionRates.size()) {
			throw new IllegalArgumentException("Size of access decisions and contentions is not equal!");
		}
		
		double competitiveElement = 0.0;
		double constantElement = (1 - MODE_SWITCH_TIME / MAX_BACKOFF);
		
		for (int i = 0; i < contentions.size(); i++) {
			if (accessDecisions.get(i)) {
				double element1 = transMissionRates.get(i) / ( params.getDemand() * (1 + contentions.get(i)) );
				competitiveElement += element1 * Math.pow(constantElement, contentions.get(i));
			}
		}
		
		return Math.min(competitiveElement, 1.0);
	}

}
