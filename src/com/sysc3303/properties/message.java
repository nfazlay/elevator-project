import java.util.*;

/**
 * Message - A message class that contains all four parameters input by the message file.
 * @author linpuliu
 *
 */
class Message {
       
	   private Date date;     //Time stamp of when the button is pressed.
       private int floor;     //Floor on which the passenger is making a request
       private String floorButton;     // A String representing up or down, null is accepted when at lowest/highest floor.
       private int carButton;   // Floor button within the elevator which is providing service to the passenger. 

   /**
    * Constructor of the message class
    * @param date
    * @param floor
    * @param floorButton
    * @param carButton
    */
   public Message(Date date, int floor, String floorButton, int carButton) {
      this.date = date;
      this.floor = floor;
      if(floorButton == "Up" || floorButton == "Down" || floorButton == null) {
    		  this.floorButton = floorButton;           //Only accepting Up, down and null.
      }
      this.carButton = carButton;
   }

       /**
        * Get the Date of the message.
        * @return Date
        */
       public Date getDate() { 
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
        * override the toString method so it's can returned properly
        * @return String message information
        */
       @Override
       public String toString() { 
    	   return date.toString() + " " + floor + " " + floorButton + " " + carButton; 
       }
       
    }