package com.sysc3303;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

import com.sysc3303.properties.Data;
import com.sysc3303.properties.Message;
import com.sysc3303.properties.Systems;
 
/**
 * Java Scheduler
 *
 *
 * 
 */
public class Scheduler implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket sendPacket, receivePacket;
    protected Message message;
    private Queue<DatagramPacket> floorPackets = new LinkedList<DatagramPacket>();
    private Queue<DatagramPacket> elevPackets = new LinkedList<DatagramPacket>();
    
    public Scheduler(int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(10*1000);//10 seconds
    }
 
    public static void main(String[] args) {
		Scheduler s;
		try {
			s = new Scheduler(8080);
			s.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void start() throws IOException{
    	try {
    		while (true) {
    			
    			System.out.println("Scheduler: Waiting for packets...\n");
    			byte[] bufReceived = new byte[1024];
    			receivePacket = new DatagramPacket(bufReceived, bufReceived.length);
    			socket.receive(receivePacket);
    			
    			// For iteration 1, received data is a Passenger event that was sent initially.
    			// This will be changed in future iterations.
    			try {
    				message = (Message) Data.fromByteArray(receivePacket.getData());
    			} catch (ClassNotFoundException ce) {
    				ce.printStackTrace();
    			}
    			
    			if(message.getMessage() != "") {//Elevator sent first message
    				//System.out.println(message);
    				elevPackets.add(receivePacket);
    				System.out.println("Scheduler: Elevator connected\n");
    			}
    			else {
    				if (message.getSender() == Systems.FLOOR) {
    					floorPackets.add(receivePacket);
    					System.out.println("Scheduler: Packet received from Floor");
    					System.out.println("Scheduler packet data: " + message);
    					//pack message to send to Elevator
    					message.setReceiver(Systems.ELEVATOR);
    					message.setSender(Systems.SCHEDULER);
    					System.out.println("Scheduler: Sending packet to Elevator");
    					byte[] bufSend = Data.toByteArray(message);
    					sendPacket = new DatagramPacket(bufSend, bufSend.length, 
    							elevPackets.peek().getAddress(), elevPackets.peek().getPort());
    					
    					socket.send(sendPacket);
    					System.out.println("Scheduler: Packet sent to Elevator.\n");
    					
    					
    				}
    				else{//Elevator
    					System.out.println("Scheduler: Packet received from Elevator");
    					System.out.println("Scheduler packet data: " + message);
    					//pack message to send to Elevator
    					message.setReceiver(Systems.FLOOR);
    					message.setSender(Systems.SCHEDULER);
    					
    					System.out.println("Scheduler: Sending packet to Floor");
    					byte[] bufSend = Data.toByteArray(message);
    					sendPacket = new DatagramPacket(bufSend, bufSend.length, 
    							floorPackets.peek().getAddress(), floorPackets.peek().getPort());
    					
    					socket.send(sendPacket);
    					
    					System.out.println("Scheduler: Packet sent to Floor.\n");
    				}
    			}
    			
    		}
    	}
    	catch (SocketTimeoutException ex) {
            System.out.println("Closing Scheduler socket due to inactivity");
            socket.close();
    	}
        	
    }
    
    @Override
    public void run() {
        try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
