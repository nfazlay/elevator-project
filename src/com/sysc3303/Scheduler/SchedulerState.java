package com.sysc3303.Scheduler;

/**
 * Enum for Scheduler State
 * 
 *
 */
public enum SchedulerState {
	/**
	 * Add Elevator too list of elevators
	 */
	ADD_ELEVATOR,
	/**
	 * Set Probability to elevator
	 */
	SET_PROB,
	/**
	 * Send OK Response to Elevator
	 */
	SEND_OK,
	/**
	 * Send Broken
	 */
	SEND_BROKEN;
}
