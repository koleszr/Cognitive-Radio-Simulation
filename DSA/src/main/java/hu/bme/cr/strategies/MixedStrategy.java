package hu.bme.cr.strategies;

import java.util.List;

import hu.bme.cr.entity.StrategyParameters;

public class MixedStrategy implements IStrategy {
	
	private StrategyParameters parameters;
	
	public MixedStrategy(StrategyParameters parameters) {
		this.parameters = parameters;
	}

	public List<Boolean> decide() {
		// TODO Auto-generated method stub
		return null;
	}

}
