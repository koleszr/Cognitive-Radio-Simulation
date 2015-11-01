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
	
	/**
	 * Constructor for IStrategy implementation input and output.
	 * 
	 * Used as input: 
	 * <ul>
	 * <li>utilities: payoff of each strategy at period t - 1</li>
	 * <li>regrets: mean regret of each strategy at period t - 1</li>
	 * <li>strategyIndex: the index of the strategy that was used at period t - 1</li>
	 * </ul>
	 * 
	 * Used as output:
	 * <ul>
	 * <li>utilities: empty list</li>
	 * <li>regrets: mean regret of each strategy at period t</li>
	 * <li>strategyIndex: the index of the strategy that was used at period t</li>
	 * </ul>
	 * In case of using as output the regrets and decisions must be set.
	 * 
	 * @param utilities
	 * @param regrets
	 */
	public StrategyParameters(List<Double> utilities, List<Double> regrets, int strategyIndex) {
		this.utilities = utilities;
		this.regrets = regrets;
		this.strategyIndex = strategyIndex;
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
}
