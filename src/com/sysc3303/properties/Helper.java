package com.sysc3303.properties;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public interface Helper {
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
