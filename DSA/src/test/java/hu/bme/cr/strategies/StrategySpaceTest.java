package hu.bme.cr.strategies;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class StrategySpaceTest {
	
	@Test
	public void testPermutation() {
		assertTrue(StrategySpace.getStrategySpace(Arrays.asList(true, true, false, false)).size() == 6);
	}

}
