package hu.bme.cr.utilities;

import java.util.Random;
import static hu.bme.cr.utilities.UtilityConstants.*;

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
	
	/**
	 * Calculates the probability that the ith user
	 * captues the cth channel.
	 * 
	 * @param contention - the number of competing user estimate on the same channel
	 * @return channel capture probability
	 */
	public double calculateCaptureProbability(double contention) {
		return (1 / (1 + contention)) * Math.pow(1 - MODE_SWITCH_TIME / MAX_BACKOFF, 1 + contention);
	}
	
	/**
	 * Calculates the probability that the ith user
	 * will cause collision on the cth channel.
	 * 
	 * @param contention - the number of competing user estimate on the same channel
	 * @return channel collision probability
	 */
	public double calculateCollisionProbability(double contention) {
		double element1 = MODE_SWITCH_TIME / MAX_BACKOFF;
		double element2 = 1 / (1 + contention);
		double element3 = Math.pow(element1, 1 + contention);
		double element4 = Math.pow(1 - MODE_SWITCH_TIME / MAX_BACKOFF, 1 + contention);
		
		return  element1 + element2 * (1 - element3 - element4);
	}
}
