package hu.bme.cr.strategies;

import java.util.List;

/**
 * 
 * @author Zolt�n Kolesz�r
 *
 * Represents a game theoretical strategy, that a cognitive radio device (CR) can play.
 * 
 */
public interface IStrategy {
	
	/**
	 * This method is used to decide which channels can be utilized 
	 * by the given CognitiveRadio entity.
	 * 
	 * @return true if the device can access the channel, otherwise false
	 */
	List<Boolean> decide();

}
