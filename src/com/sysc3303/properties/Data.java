package com.sysc3303.properties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * The Data Class is responsible for converting the information to and from a bytearray.
 *
 */
public abstract class Data {
	
	public abstract MessageType getType();
	public abstract void setSender(Systems sender);
	public abstract void setReceiver(Systems receiver);
	public abstract Systems getSender();
	public abstract Systems getReceiver();
	
	/**
	 * Converts the event object to a byte array.
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (ObjectOutputStream os = new ObjectOutputStream(bos)) {
			os.writeObject(obj);
		}
		return bos.toByteArray();
	}

	/**
	 * Converts the byte array to an event object.
	 * 
	 * @param byteArr
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object fromByteArray(byte[] byteArr) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArr);
		ObjectInput in = new ObjectInputStream(bis);
		return in.readObject();
	}
}

