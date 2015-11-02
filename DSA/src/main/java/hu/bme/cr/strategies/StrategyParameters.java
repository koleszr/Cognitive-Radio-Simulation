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
	
	private int strategyIndex;
	
	// size of the strategy space
	private int size;
	
	/**
	 * Constructor for IStrategy implementation input and output.
	 * 
	 * Used as input: 
	 * <ul>
	 * <li>utilities: payoff of each strategy at period t - 1</li>
	 * <li>regrets: mean regret of each strategy at period t - 1</li>
	 * <li>strategyIndex: the index of the strategy that was used at period t - 1</li>
	 * <li>size: size of the strategy space</li>
	 * </ul>
	 * 
	 * Used as output:
	 * <ul>
	 * <li>utilities: empty list</li>
	 * <li>regrets: mean regret of each strategy at period t</li>
	 * <li>strategyIndex: the index of the strategy that was used at period t</li>
	 * <li>size: 0</li>
	 * </ul>
	 * In case of using as output the regrets and decisions must be set.
	 * 
	 * @param utilities
	 * @param regrets
	 * @param strategyIndex
	 * @param size
	 */
	public StrategyParameters(List<Double> utilities, List<Double> regrets, int strategyIndex, int size) {
		this.utilities = utilities;
		this.regrets = regrets;
		this.strategyIndex = strategyIndex;
		this.size = size;
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

	public int getStrategyIndex() {
		return strategyIndex;
	}

	public void setStrategyIndex(int strategyIndex) {
		this.strategyIndex = strategyIndex;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
