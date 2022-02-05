package com.sysc3303.properties;

import java.time.LocalTime;
import java.io.Serializable;

/**
 * Message - A message class that contains all four parameters input by the message file.
 * @author linpuliu
 *
 */
public class Message implements Data, Serializable {
	   private static final long serialVersionUID = 1L;
       
	   private LocalTime date;     //Time stamp of when the button is pressed.
       private int floor;     //Floor on which the passenger is making a request
       private String floorButton;     // A String representing up or down, null is accepted when at lowest/highest floor.
       private int carButton;   // Floor button within the elevator which is providing service to the passenger. 
       private Systems sender;
       private Systems receiver;

   /**
    * Constructor of the message class
    * @param date
    * @param floor
    * @param floorButton
    * @param carButton
    * @param Sender
    */
   public Message(LocalTime date, int floor, String floorButton, int carButton, Systems sender, Systems receiver) {
      this.date = date;
      this.floor = floor;
      if(floorButton == "Up" || floorButton == "Down" || floorButton == null) {
    		  this.floorButton = floorButton;           //Only accepting Up, down and null.
      }
      this.carButton = carButton;
      this.sender = sender;
      this.receiver = receiver;
   }

       /**
        * Get the Date of the message.
        * @return Date
        */
       public LocalTime getDate() { 
    	   return date; }
       
       /**
        * Get the floor number of the request.
        * @return integer floor
        */
       public int getfloor() { 
    	   return floor; }
       /**
        * Get the floor button of the request.
        * @return String floor button
        */
       public String getfB() { 
    	   return floorButton; }
       
       /**
        * Get the Car Button of the request.
        * @return intCar button 
        */
       public int getcB() { 
    	   return carButton; }
       
       /**
        * Get the Sender of the request.
        * @return sender
        */
       public Systems getSender() { 
    	   return sender; }
 
       /**
        * Get the receiver of the request.
        * @return receiver
        */
       public Systems getReceiver() { 
    	   return receiver; }
       /**
        * Get the Sender of the request.
        * @return sender
        */
       
       public Systems setSender(Systems sender) { 
    	   return this.sender= sender ; }
 
       /**
        * Get the receiver of the request.
        * @return receiver
        */
       public Systems setReceiver(Systems receiver) { 
    	   return this.receiver = receiver; }
       
       /**
        * override the toString method so it's can returned properly
        * @return String message information
        */
       @Override
       public String toString() { 
    	   return date.toString() + " " + floor + " " + floorButton + " " + carButton; 
       }
       
    }