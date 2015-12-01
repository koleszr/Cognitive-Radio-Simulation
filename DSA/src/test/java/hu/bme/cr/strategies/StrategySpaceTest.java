package hu.bme.cr.strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import hu.bme.cr.entity.CognitiveRadio;

import static org.junit.Assert.assertTrue;

public class StrategySpaceTest {
	
	@Test
	public void testPermutation() {
		assertTrue(StrategySpace.getStrategySpace(Arrays.asList(true, true, false, false)).size() == 6);
	}

	@Test
	public void printStrategySpace() {
		for (int i = 1; i <= 2; i++) {
			List<Boolean> strategies = new ArrayList<>(Collections.nCopies(i, true));
			strategies.addAll(new ArrayList<>(Collections.nCopies(5 - i, false)));
			CognitiveRadio.getStrategySpace().addAll(StrategySpace.getStrategySpace(strategies));
		}	
		
		CognitiveRadio.getStrategySpace().stream().forEach(System.out::println);
	}
}
