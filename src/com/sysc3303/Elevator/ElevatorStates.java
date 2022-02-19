package com.sysc3303.Elevator;

public enum ElevatorStates {
    STATIONARY("Stationary", (byte)1),
    MOVING("Moving", (byte)2),
    OPENDOOR("Opening door", (byte)3),
    CLOSEDOOR("Closing door", (byte)4);


    private final String state;
    private final byte val;

    private ElevatorStates(String state, byte val)
    {
        this.state = state;
        this.val = val;
    }
    
    public byte getValue() {
    	return this.val;
    }
    
    public String getState() {
    	return this.state;
    }

}
