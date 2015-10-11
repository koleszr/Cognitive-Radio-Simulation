package hu.bme.cr.strategies;

import java.util.List;
import hu.bme.cr.entity.Channel;
import hu.bme.cr.entity.CognitiveRadio;

/**
 * 
 * @author Zoltán Koleszár
 *
 * Represents a game theoretical strategy, that a cognitive radio device (CR) can play.
 * 
 */
public interface IStrategy {
	
	/**
	 * Maximal back off time for CSMA.
	 */
	static final double MAX_BACKOFF = 1.0;
	
	/**
	 * This method is used to decide which channels can be utilized 
	 * by the given {@link CognitiveRadio} entity.
	 * 
	 * @param cognitiveRadio - represents the device which wants to get access to certain number of channels
	 * @param channel - the radio channels to be used to transmit on
	 * @return true if the device can access the channel, otherwise false
	 */
	List<Boolean> decide(CognitiveRadio cognitiveRadio, List<Channel> channels);

}
