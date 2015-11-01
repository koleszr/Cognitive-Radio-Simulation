package hu.bme.cr.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MixedStrategy implements IStrategy {
	
	private StrategyParameters parameters;
	
	public MixedStrategy(StrategyParameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * Generates a uniformly distributed random number
	 * between 0 (inclusive) and max (exclusive).
	 * 
	 * @param max top boundary
	 * @return index of the strategy to play in the next decision period
	 */
	@Override
	public int decideInInitPhase(int max) {
		return new Random().nextInt(max);
	}

	/**
	 * Creates an instantaneous regret list and then returns
	 * the index of the highest valued element and the mean
	 * regret list which equals to the instantaneous regret 
	 * list in this phase.
	 * 
	 * @return index of the strategy to play in the next decision period
	 */
	@Override
	public StrategyParameters decideInSetPhase() {
		// 1. caclculate the instantaneous regret list
		int size = parameters.getUtilities().size();
		double utility = parameters.getUtilities().get(parameters.getStrategyIndex());
		
		List<Double> instRegret = new ArrayList<>(size);
		
		for (int i = 0; i < size; i++) {
			instRegret.add(parameters.getUtilities().get(i) - utility);
		}
		
		int strategyIndex = instRegret.indexOf(Collections.max(instRegret));	
		
		return new StrategyParameters(null, instRegret, strategyIndex);
	}



	@Override
	public StrategyParameters decide() {
		// TODO Auto-generated method stub
		return null;
	}

}
