package hu.bme.cr.strategies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.iterators.PermutationIterator;

public class StrategySpace {

	private StrategySpace() {
		
	}
	
	/**
	 * Defines the strategies of a strategy space.
	 * 
	 * @param strategy
	 * @return every strategy of the strategy space
	 */
	public static List<List<Boolean>> getStrategySpace(List<Boolean> strategy) {
		
		List<List<Boolean>> result = new ArrayList<List<Boolean>>();
		
		Iterator<List<Boolean>> it = new PermutationIterator<Boolean>(strategy);
		
		while(it.hasNext()) {
			List<Boolean> str = it.next();
			
			if (!result.contains(str)) {
				result.add(str);				
			}
		}
		
		return result;
	}
}
