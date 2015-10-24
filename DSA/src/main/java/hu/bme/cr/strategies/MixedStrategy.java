package hu.bme.cr.strategies;

import java.util.List;

import hu.bme.cr.entity.UtilityFunctionParameters;

public class MixedStrategy implements IStrategy {
	
	private UtilityFunctionParameters parameters;
	
	public MixedStrategy(UtilityFunctionParameters parameters) {
		this.parameters = parameters;
	}

	public List<Boolean> decide() {
		// TODO Auto-generated method stub
		return null;
	}

}
