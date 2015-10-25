package hu.bme.cr.utilities;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ChannelUtilityTest {
	
	/**
	 * Test case for competing user number estimate.
	 * 
	 * In this test case the number of CSMA subslots is 5,
	 * therefore both the probabilities and channelCaptures
	 * list should have 5 elements.
	 * 
	 * The probabilites of channel captures in each subslot:
	 * [0.9, 0.3, 0.3, 0.2, 0.8]
	 * 
	 * The success of channel captures in each subslot:
	 * [true, false, false, false, true]
	 * 
	 */
	@Test
	public void testUserNumberEstimate() {
		List<Double> probabilities = new ArrayList<Double>(5);
		probabilities.add(0.9);
		probabilities.add(0.3);
		probabilities.add(0.3);
		probabilities.add(0.2);
		probabilities.add(0.8);
		
		List<Boolean> channelCaptures = new ArrayList<Boolean>(5);
		channelCaptures.add(true);
		channelCaptures.add(false);
		channelCaptures.add(false);
		channelCaptures.add(false);
		channelCaptures.add(true);
		
		assertEquals(2.9432, ChannelUtility.calculateUserEstimate(channelCaptures, probabilities), 0.1);
	}
	
	/**
	 * Test case for different sized arguments.
	 * 
	 * IllegalArgumentException is expected.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testThrowsIllegalArgumentExceptioninSuccessProbability() {
		List<Double> probabilities = new ArrayList<Double>(5);
		probabilities.add(0.9);
		probabilities.add(0.3);
		
		List<Boolean> channelCaptures = new ArrayList<Boolean>(5);
		channelCaptures.add(true);
		
		ChannelUtility.calculateUserEstimate(channelCaptures, probabilities);
	}
	
	/**
	 * Test case for channel capture probability calculations.
	 */
	@Test
	public void testChannelCaptureProbability() {
		assertEquals(0.45, ChannelUtility.calculateChannelCaptureProbability(0.5), 0.01);
		assertEquals(0.22, ChannelUtility.calculateChannelCaptureProbability(0.73), 0.01);
		assertEquals(0.81, ChannelUtility.calculateChannelCaptureProbability(0.14), 0.01);
	}
	
	/**
	 * Test case to check whether IllegalArgumentException
	 * is thrown in case of a negative back off.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testThrowsIllegalArgumentExceptioninChannelCaptureProbability() {
		ChannelUtility.calculateChannelCaptureProbability(-0.5);
	}

}
