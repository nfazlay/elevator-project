package com.sysc3303.properties;
import java.io.Serializable;

/**
 * OkMessage Class extends Data
 *
 */
public class BrokenMessage extends Data implements Serializable {
	   private static final long serialVersionUID = 2L;
       
	   private String message = "";
	   private MessageType type = null;
	   private Systems sender;
	   private Systems receiver;

   /**
    * Constructor of the message class
    */
   public BrokenMessage(String message) {
	   this.type = MessageType.BROKEN;
	   this.message = message;
   }
   /**
	* Get the message.
	* @return message
	*/
   public String getMessage() { 
	   return message; }
   
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
	   return message; 
   }
   
}
