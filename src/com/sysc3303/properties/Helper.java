package com.sysc3303.properties;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Helper interface to implement helper methods
 *
 */
public interface Helper {
    /**
     * Packs data to be sent over UDP
     * 
     * @param message Data to send
     * @param sender Sending System
     * @param receiver Receiving System
     * @param address InetAddress to send
     * @param port Port to send
     * @return DatagramPacket
     * @throws IOException
     */
    public static DatagramPacket sendPacket(Data message, Systems sender, Systems receiver, InetAddress address, int port) throws IOException {
		message.setReceiver(receiver);
		message.setSender(sender);
		System.out.println(sender + ": Sending packet to: " + receiver);
		System.out.println(sender + ": Packet data: " + message);
		byte[] bufSend;
		DatagramPacket sendPacket;
		bufSend = Data.toByteArray(message);
		sendPacket = new DatagramPacket(bufSend, bufSend.length, 
				address, port);
		return sendPacket;
    }

}
