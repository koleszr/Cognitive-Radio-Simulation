package hu.bme.cr.entity;

/**
 * 
 * @author Zoltán Koleszár
 * 
 * Models a wireless channel that is can be allocated by cognitive radio (CR) devices.
 * 
 *
 */
public class Channel {
	
	/**
	 * Quality of the channel, i.e. transmission rate.
	 */
	private double transmissionRate;
	
	/**
	 * Starting frequency of the channel.
	 */
	private double frequency;
	
	/**
	 * Defines if the channel is accessed by a primary user (PU).
	 */
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
