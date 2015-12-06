package hu.bme.cr.strategies;

import java.util.List;

/**
 * 
 * @author Zoltán Koleszár
 *
 * <p>A wrapper class to pass parameters to IStrategy implementations.</p>
 */
public class StrategyParameters {
	
	private List<Double> utilities;
	
	private List<Double> regrets;
	
	private List<Integer> strategyIndexes;
	
	// size of the strategy space
	private int size;
	
	private int strategyIndex;
	
	private int round;
	
	public StrategyParameters() {
		
	}
	
	/**
	 * Used as input: 
	 * <ul>
	 * <li>utilities: payoff of each strategy at period t - 1</li>
	 * <li>regrets: mean regret of each strategy at period t - 1</li>
	 * <li>strategyIndex: the index of the strategy that was used at t - 1</li>
	 * <li>size: size of the strategy space</li>
	 * </ul>
	 * 
	 * @param utilities
	 * @param regrets
	 * @param strategyIndex
	 * @param size
	 */
	public StrategyParameters(List<Double> utilities, List<Double> regrets, int size, int strategyIndex, int round) {
		this.utilities = utilities;
		this.regrets = regrets;
		this.strategyIndex = strategyIndex;
		this.size = size;
		this.round = round;
	}
	
	/**
	 * Used as output:
	 * <ul>
	 * <li>regrets: mean regret of each strategy at period t</li>
	 * <li>strategyIndexes: order of strategies to use</li>
	 * </ul>
	 * 
	 * @param regrets
	 * @param strategyIndexes
	 */
	public StrategyParameters(List<Double> regrets, List<Integer> strategyIndexes) {
		this.regrets = regrets;
		this.strategyIndexes = strategyIndexes;
	}
	/*
	 * Getters and setters
	 */
	
	public List<Double> getUtilities() {
		return utilities;
	}

	public void setUtilities(List<Double> utilities) {
		this.utilities = utilities;
	}

	public List<Double> getRegrets() {
		return regrets;
	}

	public void setRegrets(List<Double> regrets) {
		this.regrets = regrets;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<Integer> getStrategyIndexes() {
		return strategyIndexes;
	}

	public void setStrategyIndexes(List<Integer> strategyIndexes) {
		this.strategyIndexes = strategyIndexes;
	}
	
	public int getStrategyIndex() {
		return strategyIndex;
	}

	public void setStrategyIndex(int strategyIndex) {
		this.strategyIndex = strategyIndex;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}
}
