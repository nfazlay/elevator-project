package com.sysc3303.properties;

import java.time.LocalTime;
import java.io.Serializable;

/**
 * Message Class extends Data class
 * 
 *
 */
public class Message extends Data implements Serializable {
	   private static final long serialVersionUID = 1L;
       
	   private LocalTime date;     //Time stamp of when the button is pressed.
       private int floor;     //Floor on which the passenger is making a request
       private String direction;     // A String representing up or down, null is accepted when at lowest/highest floor.
       private int carButton;   // Floor button within the elevator which is providing service to the passenger. 
       
	   private MessageType type = null;
	   private Systems sender;
	   private Systems receiver;
   

	/**
	 * Constructor of the Message class
	 * @param date
	 * @param floor
	 * @param direction
	 * @param carButton
	 * @param sender
	 * @param receiver
	 */
	public Message(LocalTime date, int floor, String direction, int carButton, Systems sender, Systems receiver) {
		  this.type = MessageType.REQUEST;
	      this.date = date;
	      this.floor = floor;
	      //System.out.println("Message: " + floorButton);
	      this.direction = direction;
	      this.carButton = carButton;
	      this.sender = sender;
	      this.receiver = receiver;
	   }

   /**
    * Get the Date of the message.
    * 
    * @return Date
    */
   public LocalTime getDate() { 
	   return date; }
   
   /**
    * Get the floor number of the request.
    * 
    * @return integer floor
    */
   public int getfloor() { 
	   return floor; }
   /**
    * Get the Direction of the request.
    * 
    * @return String Direction
    */
   public String getDirection() { 
	   return direction; }
   
   /**
    * Get the Car Button of the request.
    * 
    * @return intCar button 
    */
   public int getCarButton() { 
	   return carButton; }
       
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
    * 
    * @return String message information
    */
   @Override
   public String toString() { 
	   return date.toString() + " " + floor + " " + direction + " " + carButton; 
   }
       
}
