package hu.bme.cr.strategies;

import java.util.Collections;

/**
 * 
 * @author Zoltán Koleszár
 *
 * Represents a strategy which decides about channel access randomly.
 * 
 */
public class RandomStrategy implements IStrategy {

	@Override
	public StrategyParameters decideInSetPhase(StrategyParameters params) {
		return new StrategyParameters(Collections.emptyList(), decideInInitPhase(params.getSize()));
	}

	@Override
	public StrategyParameters decide(StrategyParameters params) {
		return new StrategyParameters(Collections.emptyList(), decideInInitPhase(params.getSize()));
	}

	@Override
	public String toString() {
		return "Random strategy";
	}
}
