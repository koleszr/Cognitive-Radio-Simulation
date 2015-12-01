package hu.bme.cr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.cr.entity.CognitiveRadio;
import hu.bme.cr.strategies.StrategySpace;

/**
 * 
 * @author Zoltán Koleszár
 *
 * In case of testing findMinIndex the method 
 * modifier in CRSystem class must be changed
 * to public.
 */
public class CRSystemTest {
	
	@Test
	public void testFindMinIndexSuccess() {
		List<Double> backoffTimes = Arrays.asList(Double.NaN, 0.35, Double.NaN, 0.401, 0.670);
		CRSystem system = new CRSystem();
		
//		 Assert.assertEquals(1, system.findMinIndex(backoffTimes));
	}
	
	@Test
	public void testFindMinIndexFailure() {
		List<Double> backoffTimes = Arrays.asList(Double.NaN, 0.35, Double.NaN, 0.398, 0.670);
		
		CRSystem system = new CRSystem();
		
//		Assert.assertEquals(-1, system.findMinIndex(backoffTimes));
		
		backoffTimes = Arrays.asList(Double.NaN, 0.35, Double.NaN, 0.35, 0.670);
		
//		Assert.assertEquals(-1, system.findMinIndex(backoffTimes));
		
		backoffTimes = Arrays.asList(Double.NaN, 0.35, Double.NaN, Double.NaN, Double.NaN);
		
//		Assert.assertEquals(-2, system.findMinIndex(backoffTimes));
		
		backoffTimes = Arrays.asList(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
		
//		Assert.assertEquals(-2, system.findMinIndex(backoffTimes));
	}
	
	@Test
	public void testSetCompetingUserNumber() {
		CRContentionSimulation system = new CRContentionSimulation();
		
		for (int i = 1; i <= 2; i++) {
			List<Boolean> strategies = new ArrayList<>(Collections.nCopies(i, true));
			strategies.addAll(new ArrayList<>(Collections.nCopies(5 - i, false)));
			CognitiveRadio.getStrategySpace().addAll(StrategySpace.getStrategySpace(strategies));
		}
		
		system.setCompetingUserNumber();
	}
 
}
