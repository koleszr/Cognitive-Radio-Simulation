package hu.bme.cr.strategies;

import java.util.Collections;
import java.util.Random;

/**
 * 
 * @author Zoltán Koleszár
 *
 * Represents a strategy which decides about channel access randomly.
 * 
 */
public class RandomStrategy implements IStrategy {

	@Override
	public int decideInInitPhase(int max) {
		return new Random().nextInt(max);
	}

	@Override
	public StrategyParameters decideInSetPhase(StrategyParameters params) {
		return new StrategyParameters(Collections.emptyList(), Collections.emptyList(), decideInInitPhase(params.getSize()), 0);
	}

	@Override
	public StrategyParameters decide(StrategyParameters params) {
		return new StrategyParameters(Collections.emptyList(), Collections.emptyList(), decideInInitPhase(params.getSize()), 0);
	}

	@Override
	public String toString() {
		return "Random strategy";
	}
}
