package hu.bme.cr.utilities;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ListUtilityTest {
	
	@Test
	public void testGetIndexesDescending() {
		List<Double> doubles = Arrays.asList(0.345, 1.0, 0.346, 1.0, 0.344);
		
		System.out.println("getIndexesDescending test: " + ListUtility.getIndexesDescending(doubles));
	}
	
	@Test
	public void testSwapToMax() {
		List<Double> utilities = Arrays.asList(0.5, 0.5, 0.1, 0.2, 0.1);
		List<Integer> order = Arrays.asList(0, 1, 3, 5, 4);
				
		ListUtility.swapToMax(utilities, order, 1);	
		
		System.out.println("swapToMax test: " + order);
		
		Assert.assertEquals(Arrays.asList(1, 0, 3, 5, 4), order);
	}

}
