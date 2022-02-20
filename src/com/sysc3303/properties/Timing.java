package com.sysc3303.properties;

/**
 * Enum containing elevator timing
 *
 */
public enum Timing {
	/**
	 * Open door in 4s
	 */
	OPENDOOR(4),
	/**
	 * Close door in 4s
	 */
	CLOSEDOOR(4),
	/**
	 * Move one floor in 5s
	 */
	MOVE(5),
	/**
	 * Default delay
	 */
	DEFAULT(1);
	
	private int value ;

	/**
	 * Constructor for Timing Class
	 * @param i Time
	 */
	Timing(int i) {
		this.value = i;
	}
	
	/**
	 * Get time for each State
	 * @return int Time
	 */
	public int getTime() {
		return this.value;
	}

}
