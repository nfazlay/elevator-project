package com.sysc3303.properties;

/**
 * 
 * ENUM for Direction Values
 *
 */

public enum Direction {
	UP(0), DOWN(1);
	
	private int direction;
	
    /**
     * Private constructor
     * 
     * @param direction: The value mapping to the chosen direction
     */
	private Direction(int direction) {
		this.direction = direction;
	}
	
	/**
	 * Getter for direction
	 * 
	 * @return Value of direction as int UP(0) DOWN(1)
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * Changes to opposite direction
	 */
	public void changeDirection() {
		this.direction = (this.direction == 0) ? 1 :0 ; 
	}
	
	/**
	 * Prints direction as UP or Down
	 */
	public void print() {
		String s = (this.direction == 0) ? "UP" : "DOWN" ;
		System.out.println(s);
	}

}
