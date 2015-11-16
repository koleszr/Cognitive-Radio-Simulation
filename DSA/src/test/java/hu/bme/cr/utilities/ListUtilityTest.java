package hu.bme.cr.utilities;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListUtilityTest {
	
	@Test
	public void testGetIndexesDescending() {
		List<Double> doubles = Arrays.asList(0.345, 1.0, 0.346, 1.0, 0.344);
		
		System.out.println(ListUtility.getIndexesDescending(doubles));
	}

}
