package com.sysc3303.properties;

import java.io.Serializable;

import com.sysc3303.Elevator.ElevatorStates;

/**
 * StateMessage Class extends Data
 *
 */
public class StateMessage extends Data implements Serializable {
	   private static final long serialVersionUID = 1L;
       
	   private ElevatorStates state;
	   private int currFloor;
	   private MessageType type = null;
	   private Systems sender;
	   private Systems receiver;

   /**
    * Constructor of the StateMessage class
    */
       
   public StateMessage(ElevatorStates state, int currFloor) {
	   this.type = MessageType.STATE;
	   this.state = state;
	   this.currFloor = currFloor;
   }
   /**
    * Get the state.
    * 
    * @return ElevatorStates state
    */
   public ElevatorStates getState() { 
	   return state; }
   /**
    * Get current floor
    * @return int currfloor
    */
   public int getFloor() {
	   return currFloor;
   }
   
  	/**
  	 * Get Type of Message
  	 * 
  	 * @return MessageType type
  	 */
  	public MessageType getType() {
		return type;
	}
	
   /**
    * Get the Sender of the request.
    * 
    * @return sender
    */
   public Systems getSender() { 
	   return sender; }

   /**
    * Get the receiver of the request.
    * 
    * @return receiver
    */
   public Systems getReceiver() { 
	   return receiver; }
   /**
    * Set the Sender of the request.
    * 
    * @param sender
    */
   @Override
   public void setSender(Systems sender) { 
	    this.sender = sender ; }

   /**
    * Set the receiver of the request.
    * 
    * @param receiver
    */
   @Override
   public void setReceiver(Systems receiver) { 
	    this.receiver = receiver; }
   
   /**
    * override the toString method so it's can returned properly
    * @return String message information
    */
   @Override
   public String toString() { 
	   return "Current State: " + state.getState() + " || Current Floor: " + currFloor; 
   }
   
}
