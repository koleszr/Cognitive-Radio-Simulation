package hu.bme.cr.strategies;

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
	
	
	int decideInInitPhase(int max);
	
	StrategyParameters decideInSetPhase(StrategyParameters params);

	StrategyParameters decide(StrategyParameters params);
}
