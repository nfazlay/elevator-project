package com.sysc3303.properties;
import java.io.Serializable;

/**
 * Message - A message class that contains all four parameters input by the message file.
 *
 */
public class OkMessage extends Data implements Serializable {
	   private static final long serialVersionUID = 2L;
       
	   private String message = "";
	   private MessageType type = null;
	   private Systems sender;
	   private Systems receiver;

   /**
    * Constructor of the message class
    */
       
   public OkMessage(String message) {
	   this.type = MessageType.OK;
	   this.message = message;
   }
   /**
    * Get the message.
    * @return message
    */
   public String getMessage() { 
	   return message; }
   
   @Override
	public MessageType getType() {
		return type;
	}
		
	/**
	 * Get the Sender of the request.
	 * @return sender
	 */
   @Override
	public Systems getSender() { 
	   return sender; }
	
	/**
	 * Get the receiver of the request.
	 * @return receiver
	 */
   @Override
	public Systems getReceiver() { 
	   return receiver; }
	/**
	 * Get the Sender of the request.
	 * @return sender
	 */
	
   @Override
	public void setSender(Systems sender) { 
	    this.sender = sender ; }
	
	/**
	 * Get the receiver of the request.
	 * @return receiver
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
