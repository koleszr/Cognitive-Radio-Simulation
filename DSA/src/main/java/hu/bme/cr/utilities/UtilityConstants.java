package hu.bme.cr.utilities;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * <p>Utility class to store commonly used constants,
 * such as maximal back off time or mode switch time.</p>
 * 
 */
public class UtilityConstants {
	
	private UtilityConstants() {

	}
	
	/**
	 * Maximal back off time for CSMA.
	 */
	public static final double MAX_BACKOFF = 1.0;
	
	/**
	 * The amount of time to switch between channel sensing
	 * and transmission modes for a Cognitive Radio device.
	 */
	public static final double MODE_SWITCH_TIME = 0.05;
	
	/**
	 * The number of CSMA subslots.
	 */
	public static final double NUMBER_OF_SUBSLOTS = 10;

}
