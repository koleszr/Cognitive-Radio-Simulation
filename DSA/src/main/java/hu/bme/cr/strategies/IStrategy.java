package hu.bme.cr.strategies;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 
 * @author Zoltán Koleszár
 *
 * Represents a game theoretical strategy, that a cognitive radio device (CR) can play.
 * 
 * Because the regret tracking algorithm consists of three phases, 
 * therefore is three different methods which are used to decide
 * which channels can be utilized by the given CognitiveRadio entity.
 * 
 */
public interface IStrategy {
	
	/**
	 * Generates a uniformly distributed random number
	 * between 0 (inclusive) and max (exclusive).
	 * 
	 * @param max top boundary
	 * @return index of the strategy to play in the next decision period
	 */
	default public List<Integer> decideInInitPhase(int max) {
		List<Integer> result = IntStream.range(0, max).boxed().collect(Collectors.toList());
		Collections.shuffle(result);
		return result;
	}
	
	StrategyParameters decideInSetPhase(StrategyParameters params);

	StrategyParameters decide(StrategyParameters params);
}
