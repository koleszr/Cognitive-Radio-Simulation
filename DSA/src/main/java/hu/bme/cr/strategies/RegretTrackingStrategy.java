package hu.bme.cr.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RegretTrackingStrategy implements IStrategy {
	
	private StrategyParameters parameters;
	
	private double stepSize;
	
	public RegretTrackingStrategy(StrategyParameters parameters, double stepSize) {
		this.parameters = parameters;
		this.stepSize = stepSize;
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
		List<Double> instRegret = calcInstantaneousRegrets();	
		
		return new StrategyParameters(Collections.emptyList(), instRegret, instRegret.indexOf(Collections.max(instRegret)), 0);
	}

	/**
	 * Updates mean regret and defines which strategy to use in the next period.
	 * 
	 * @return 
	 */
	@Override
	public StrategyParameters decide() {
		// 1. calculate channel access probabilities
		double utilityMax = Collections.max(parameters.getUtilities());
		double utilityMin = Collections.min(parameters.getUtilities());
		double m = (parameters.getSize() - 1) * (utilityMax - utilityMin);
		
		List<Double> probabilities = new ArrayList<>(parameters.getRegrets().size());
		
		for (int i = 0; i < parameters.getRegrets().size(); i++) {
			if (i == parameters.getStrategyIndex()) {
				probabilities.add(1 - (calcSameStPr() / m));
			}
			else {
				probabilities.add(calcDiffStPr(i) / m);
			}
		}
		
		return new StrategyParameters(Collections.emptyList(), updateRegret(), probabilities.indexOf(Collections.max(probabilities)), 0);
	}
	
	/**
	 * Calculates the instantaneous regret list.
	 * 
	 * @return instantaneous regret list
	 */
	private List<Double> calcInstantaneousRegrets() {
		int size = parameters.getUtilities().size();
		double utility = parameters.getUtilities().get(parameters.getStrategyIndex());
		
		List<Double> instRegret = new ArrayList<>(size);
		
		for (int i = 0; i < size; i++) {
			instRegret.add(parameters.getUtilities().get(i) - utility);
		}
		return instRegret;
	}
	
	/**
	 * Calculate the probability that the Cognitive Radio
	 * will play the same strategy in the next period.
	 * 
	 * @return
	 */
	private double calcSameStPr() {
		List<Double> regrets = new ArrayList<>(parameters.getRegrets());
		regrets.remove(parameters.getStrategyIndex());
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
	private double calcDiffStPr(int i) {
		return Math.max(0, parameters.getRegrets().get(i));
	}
	
	/**
	 * Updates the mean regret list.
	 * 
	 * @return updated mean regret list
	 */
	private List<Double> updateRegret() {
		List<Double> updatedRegrets = new ArrayList<>(parameters.getRegrets().size());
		
		List<Double> instRegrets = calcInstantaneousRegrets();
		
		if (parameters.getRegrets().size() != instRegrets.size()) {
			throw new IllegalArgumentException("Not equal list sizes!");
		}
		
		for (int i = 0; i < instRegrets.size(); i++) {
			double oldRegret = parameters.getRegrets().get(i);
			double regret = oldRegret + stepSize * (instRegrets.get(i) - oldRegret); 
			updatedRegrets.add(regret);
		}
		
		return updatedRegrets;
	}

}
