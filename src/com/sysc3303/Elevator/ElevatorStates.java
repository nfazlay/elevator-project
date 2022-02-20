package com.sysc3303.Elevator;

/**
 * Enum class for Elevator States
 *
 */
public enum ElevatorStates {
    /**
     * Elevator Stationary Position
     */
    STATIONARY("Stationary", (byte)1),
    /**
     * Elevator Moving
     */
    MOVING("Moving", (byte)2),
    /**
     * Elevator Open Door
     */
    OPENDOOR("Opening door", (byte)3),
    /**
     * Elevator Close Door
     */
    CLOSEDOOR("Closing door", (byte)4);


    private final String state;
    private final byte val;

    /**
     * Constructor for ElevatorStates
     * @param state 
     * @param val
     */
    private ElevatorStates(String state, byte val)
    {
        this.state = state;
        this.val = val;
    }
    
    /**
     * Get value of state in byte
     * @return byte 
     */
    public byte getValue() {
    	return this.val;
    }
    
    /**
     * Get vaue of state in string
     * @return String State
     */
    public String getState() {
    	return this.state;
    }

}
