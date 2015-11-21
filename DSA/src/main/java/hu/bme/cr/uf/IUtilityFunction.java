package hu.bme.cr.uf;

import java.util.List;

/**
 * 
 * @author Zolt�n Kolesz�r
 *
 * Represents the local utility function
 * used to calculate the payoff of each user.
 */
public interface IUtilityFunction {
	
	double calculateUtility(UtilityFunctionParameters params);
	
	String getType();
	
	List<Double> getRates();
}
