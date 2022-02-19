package com.sysc3303;

import java.io.*;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

import com.sysc3303.properties.Data;
import com.sysc3303.properties.Helper;
import com.sysc3303.properties.StateMessage;
import com.sysc3303.properties.Message;
import com.sysc3303.properties.OkMessage;
import com.sysc3303.properties.Systems;
 
/**
 * Java Scheduler
 *
 *
 * 
 */
public class Scheduler implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket receivePacket;
    protected Data message, done;
    private Queue<DatagramPacket> floorPackets = new LinkedList<DatagramPacket>();
    //private Queue<DatagramPacket> elevPackets = new LinkedList<DatagramPacket>();
    
    public Scheduler(int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(10*1000);//10 seconds
    }
 
    public static void main(String[] args) {
		Scheduler s;
		try {
			s = new Scheduler(8080);
			s.start();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    @SuppressWarnings("incomplete-switch")
	private void start() throws IOException, ClassNotFoundException{
    	try {
    		while (true) {
    			
    			System.out.println("SCHEDULER: Waiting for packet...\n");
    			byte[] bufReceived = new byte[1024];
    			receivePacket = new DatagramPacket(bufReceived, bufReceived.length);
    			socket.receive(receivePacket);
    			
    			try {
    				message = (Data) Data.fromByteArray(receivePacket.getData());
    			} catch (ClassNotFoundException ce) {
    				ce.printStackTrace();
    			}
    			
    			System.out.println("SCHEDULER: Received packet from: " + message.getSender());
    			System.out.println("SCHEDULER: Packet data: " + message);
    			
    			switch(message.getSender()) {
					case ELEVATOR:
						switch(((StateMessage) message).getState()){
							case STATIONARY:
								//give task
								if(!floorPackets.isEmpty()) {
									//add more logic when more elevators
									Message req = (Message) Data.fromByteArray(floorPackets.poll().getData());
									sendToSystem(req, Systems.ELEVATOR, receivePacket);
									
									//send to floor to switch lamp off
//									done  = new OkMessage("Turn Off Lamp");
//									sendToSystem(done, Systems.FLOOR, floorPackets.poll());
								}
								else {// no packets
									done  = new OkMessage("Cool");
									sendToSystem(done, Systems.ELEVATOR, receivePacket);
								}
								break;
							case MOVING:
								//add logic to see if going the same direction and pick up passenger
								//if going the same direction and above/below the pickup floor
								//pickup the passenger respectedly
								done  = new OkMessage("Cool");
								sendToSystem(done, Systems.ELEVATOR, receivePacket);
								break;
							case OPENDOOR:
								done  = new OkMessage("Cool");
								sendToSystem(done, Systems.ELEVATOR, receivePacket);
								break;
							case CLOSEDOOR:
								// in future: if request
									//remove from floorreq
									//send to floor
								done  = new OkMessage("Cool");
								sendToSystem(done, Systems.ELEVATOR, receivePacket);
								break;
						}						
						break;
					case FLOOR:
						floorPackets.add(receivePacket);
						break;
				}
    		}
    	}
    	catch (SocketTimeoutException ex) {
            System.out.println("SCHEDULER: Closing Scheduler socket due to inactivity");
            socket.close();
    	}
        	
    }
    
    public void sendToSystem(Data message, Systems system, DatagramPacket packet) throws IOException {
    	DatagramPacket sendPacket = Helper.sendPacket(message, Systems.SCHEDULER, system, packet.getAddress(), packet.getPort());
    	socket.send(sendPacket);
    }
    
    @Override
    public void run() {
        try {
			start();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
