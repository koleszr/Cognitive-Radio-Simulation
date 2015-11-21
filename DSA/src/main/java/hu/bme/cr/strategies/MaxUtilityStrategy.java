package hu.bme.cr.strategies;

import java.util.Collections;

import hu.bme.cr.utilities.ListUtility;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * Represents a strategy which chooses channels
 * according to the max utility.
 * 
 */
public class MaxUtilityStrategy implements IStrategy {

	/**
	 * Gets the index of the stratergy with the maximal
	 * utility in this decision period. In the next decision
	 * period that strategy will be played.
	 * 
	 * @param params
	 * @return StrategyParams object that contains the strategy index to play in next decision period
	 */
	@Override
	public StrategyParameters decideInSetPhase(StrategyParameters params) {
		return new StrategyParameters(Collections.emptyList(), ListUtility.getIndexesDescending(params.getUtilities()));
	}

	@Override
	public StrategyParameters decide(StrategyParameters params) {
		return decideInSetPhase(params);
	}
	
	@Override
	public String toString() {
		return "MaxUtility";
	}

}
