package hu.bme.cr.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import hu.bme.cr.utilities.ListUtility;

/**
 * 
 * @author Zoltán Koleszár
 *
 * Represents a strategy which implements the regret tracking algorithm.
 * 
 */
public class RegretTrackingStrategy implements IStrategy {
	
	private double stepSize;
	
	public RegretTrackingStrategy(double stepSize) {
		this.stepSize = stepSize;
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
	public StrategyParameters decideInSetPhase(StrategyParameters params) {
		// 1. caclculate the instantaneous regret list
		List<Double> instRegret = calcInstantaneousRegrets(params);	
		List<Integer> indexesDescending = ListUtility.getIndexesDescending(instRegret);
		
		ListUtility.swapToMax(instRegret, indexesDescending, params.getStrategyIndex());
		
		return new StrategyParameters(instRegret, indexesDescending);
	}

	/**
	 * Updates mean regret and defines which strategy to use in the next period.
	 * 
	 * @return 
	 */
	@Override
	public StrategyParameters decide(StrategyParameters params) {
		// 1. calculate channel access probabilities
		double utilityMax = Collections.max(params.getUtilities());
		double utilityMin = Collections.min(params.getUtilities());
		double m = (params.getSize() - 1) * (utilityMax - utilityMin);
		
		List<Double> updatedRegrets = updateRegret(params);
		List<Double> probabilities = new ArrayList<>(updatedRegrets.size());
		
		for (int i = 0; i < updatedRegrets.size(); i++) {
			if (i == params.getStrategyIndex()) {
				probabilities.add(1 - (calcSameStPr(updatedRegrets, params.getStrategyIndex()) / m));
			}
			else {
				probabilities.add(calcDiffStPr(updatedRegrets, i) / m);
			}
		}
		
		List<Integer> indexesDescending = ListUtility.getIndexesDescending(probabilities);
		ListUtility.swapToMax(probabilities, indexesDescending, params.getStrategyIndex());
		
		return new StrategyParameters(updatedRegrets, indexesDescending);
	}
	
	/**
	 * Calculates the instantaneous regret list.
	 * 
	 * @return instantaneous regret list
	 */
	private List<Double> calcInstantaneousRegrets(StrategyParameters params) {
		int size = params.getUtilities().size();
		double utility = params.getUtilities().get(params.getStrategyIndex());
		
		List<Double> instRegret = new ArrayList<>(size);
		
		for (int i = 0; i < size; i++) {
			instRegret.add(params.getUtilities().get(i) - utility);
		}
		return instRegret;
	}
	
	/**
	 * Calculate the probability that the Cognitive Radio
	 * will play the same strategy in the next period.
	 * 
	 * @return
	 */
	private double calcSameStPr(List<Double> oldRegrets, int strategyIndex) {
		List<Double> regrets = new ArrayList<>(oldRegrets);
		regrets.remove(strategyIndex);
		regrets = regrets
					.stream()
					.filter(p -> p > 0.0)
					.collect(Collectors.toCollection(ArrayList::new));
		
		double result = 0.0;
		
		for (int i = 0; i < regrets.size(); i++) {
			result += regrets.get(i);
		}
		
		return result;
	}
	
	/**
	 * Calculate the probability that the Cognitive Radio
	 * will play the the ith strategy in the next period.
	 * 
	 * @return
	 */
	private double calcDiffStPr(List<Double> regrets, int i) {
		return Math.max(0, regrets.get(i));
	}
	
	/**
	 * Updates the mean regret list.
	 * 
	 * @return updated mean regret list
	 */
	private List<Double> updateRegret(StrategyParameters params) {
		List<Double> updatedRegrets = new ArrayList<>(params.getRegrets().size());
		
		List<Double> instRegrets = calcInstantaneousRegrets(params);
		
		if (params.getRegrets().size() != instRegrets.size()) {
			throw new IllegalArgumentException("Not equal list sizes!");
		}
		
		for (int i = 0; i < instRegrets.size(); i++) {
			double oldRegret = params.getRegrets().get(i);
			double regret = oldRegret + stepSize * (instRegrets.get(i) - oldRegret); 
			updatedRegrets.add(regret);
		}
		
		return updatedRegrets;
	}

	@Override
	public String toString() {
		return "RegretTracking";
	}
}
