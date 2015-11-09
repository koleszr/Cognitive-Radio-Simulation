package hu.bme.cr.utilities;

import static hu.bme.cr.utilities.UtilityConstants.MAX_BACKOFF;
import static hu.bme.cr.utilities.UtilityConstants.MODE_SWITCH_TIME;

import java.util.List;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * <p>A collection of static methods that are related to 
 * calculate the characteristics of a channel, e.g.
 * estimated number of competing users.</p>
 *
 */
public class ChannelUtility {
	
	private ChannelUtility() {
		
	}

	/**
	 * Calculates the probability of a channel capture
	 * depending of the generated back off time of a CognitiveRadio.
	 * 
	 * @param backOff
	 * @throws IllegalArgumentException - if backOff < 0.0
	 * @return channel capture probability
	 */
	public static double calculateChannelCaptureProbability(double backOff) throws IllegalArgumentException {
		if (backOff < 0.0) {
			throw new IllegalArgumentException("Back off time is less than 0!");
		}
		
		return 1 - ((backOff + MODE_SWITCH_TIME) / MAX_BACKOFF);
	}
	
	/**
	 * Calculates the unsuccessful channel captures in time slot t.
	 * 
	 * @param channelCaptures - indicates whether the Cognitive Radio captured the channel in
	 * 			subslot w, where w = 1, 2, ... W, W is the size of the list.
	 * @return the number of unsuccessful channel captures
	 */
	private static int calculateUnsuccessfulChannelCaptures(final List<Boolean> channelCaptures) {
		int result = 0;
		
		for (Boolean channelCaptured : channelCaptures) {
			if (!channelCaptured) {
				result++;
			}
		}
		
		return result;
	}
	
	/**
	 * Calculates the mean probability of unsuccessful channel capture attempts.
	 * 
	 * @param channelCaptures - indicates whether the Cognitive Radio captured the channel in
	 * 			subslot w, where w = 1, 2, ... W, W is the size of the list.
	 * @param probabilities - tells the channel capture probability for each subslot
	 * @param unsuccessfulChannelCaptures - number of unsuccessful channel captures in the decision period
	 * @return mean probability of channel capture failure
	 * @throws IllegalArgumentException - the size of the two parameters is not equal
	 */
	private static double calculateFailureProbability(final List<Boolean> channelCaptures, 
													  final List<Double> probabilities,
													  int unsuccessfulChannelCaptures) throws IllegalArgumentException {
		
		if (channelCaptures.size() != probabilities.size()) {
			throw new IllegalArgumentException("The size of the two parameters is not equal!");
		}
		
		int size = channelCaptures.size();
		double numenator = 0.0;
		
		for (int i = 0; i < size; i++) {
			if (!channelCaptures.get(i)) {
				numenator += probabilities.get(i);
			}
		}
		
		return numenator / unsuccessfulChannelCaptures;
	}

	/**
	 * Returns the sum of successful channel capture attempt probabilities
	 * in natural logarithm.
	 * 
	 * @param channelCaptures - indicates whether the Cognitive Radio captured the channel in
	 * 			subslot w, where w = 1, 2, ... W, W is the size of the list.
	 * @param probabilities - tells the channel capture probability for each subslot
	 * @return sum of successful channel capture attempt probabilities in natural logarithm.
	 * @throws IllegalArgumentException - the size of the two parameters is not equal
	 */
	private static double calculateSuccessProbability(final List<Boolean> channelCaptures, 
													  final List<Double> probabilities) throws IllegalArgumentException {
		if (channelCaptures.size() != probabilities.size()) {
			throw new IllegalArgumentException("The size of the two parameters is not equal!");
		}
		
		int size = channelCaptures.size();
		double result = 0.0;
		
		for (int i = 0; i < size; i++) {
			if (channelCaptures.get(i)) {
				result += Math.log(probabilities.get(i));
			}
		}
		
		return result;
	}
	
	/**
	 * Calculates the maximum likelihood estimate of competing users.
	 * 
	 * @param channelCaptures - indicates whether the Cognitive Radio captured the channel in
	 * 			subslot w, where w = 1, 2, ... W, W is the size of the list.
	 * @param probabilities - the channel capture probability for each subslot
	 * @return maximum likelihood estimate of competing users
	 */
	public static double calculateUserEstimate(final List<Boolean> channelCaptures, final List<Double> probabilities) {
		// |I0|
		int uCC = calculateUnsuccessfulChannelCaptures(channelCaptures);

		// ln(ac0)
		double lnFP = Math.log(calculateFailureProbability(channelCaptures, probabilities, uCC));
		
		// successful ln(ac[w])
		double sCC = calculateSuccessProbability(channelCaptures, probabilities);
		
		double numenator = Math.log(1 + (uCC * lnFP) / sCC);
		
		return (1 - numenator / lnFP);
	}
	
}
