package hu.bme.cr.entity;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * Models a wireless channel that is used by cognitive radio (CR) devices.
 * 
 *
 */
public class Channel {
	
	// quality of the channel, i.e. transmission rate
	private double transmissionRate;
	
	// starting frequency of the channel
	private double frequency;
	
	// defines if the channel is accessed by a primary user (PU)
	private boolean occupied;
	
	public Channel(double transmissionRate, double frequency, boolean occupied) {
		this.transmissionRate = transmissionRate;
		this.frequency = frequency;
		this.occupied = occupied;
	}
	
	/*
	 * Getters and setters
	 */

	public double getTransmissionRate() {
		return transmissionRate;
	}

	public void setTransmissionRate(double transmissionRate) {
		this.transmissionRate = transmissionRate;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	
}
