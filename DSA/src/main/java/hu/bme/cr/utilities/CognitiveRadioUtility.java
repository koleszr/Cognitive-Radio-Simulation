package hu.bme.cr.utilities;

import java.util.Random;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * <p>A collection of static methods that are related to 
 * the operation of a CognitiveRadio instance.</p>
 */
public class CognitiveRadioUtility {
	
	private CognitiveRadioUtility() {
		
	}

	/**
	 * Generates a random back off time on the interval of [0, maxBackOff].
	 * 
	 * @param maxBackOff - top boundary of the back off time
	 * @return back off time used to simulate a CSMA channel attempt
	 */
	public static double generateBackOff(double maxBackOff) {
		return new Random().nextDouble() * maxBackOff;
	}
}
