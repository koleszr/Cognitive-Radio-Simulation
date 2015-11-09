package hu.bme.cr.strategies;

import java.util.Collections;
import java.util.Random;

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
	 * In the init phase the strategy generates
	 * a random number which will be the index
	 * of the strategy from strategy space played
	 * in the next decision period.
	 * 
	 * @param max top boundary - exclusive
	 * @return strategy space index to play
	 */
	@Override
	public int decideInInitPhase(int max) {
		return new Random().nextInt(max);
	}

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
		int strategyIndex = params.getUtilities().indexOf(Collections.max(params.getUtilities()));
		return new StrategyParameters(Collections.emptyList(), Collections.emptyList(), strategyIndex, 0);
	}

	@Override
	public StrategyParameters decide(StrategyParameters params) {
		return decideInSetPhase(params);
	}
	
	@Override
	public String toString() {
		return "Max utility strategy";
	}

}
