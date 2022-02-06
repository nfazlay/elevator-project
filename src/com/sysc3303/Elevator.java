package com.sysc3303;

import com.sysc3303.properties.Message;
import com.sysc3303.properties.Systems;
import com.sysc3303.properties.Data;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;

/**
 * 
 *
 */
public class Elevator implements Runnable {
    private InetAddress ip;
    private DatagramSocket socket;
    private int port;
    private DatagramPacket sendPacket, receivePacket;
    protected Message receivedMessage, messageToSend;

    public Elevator() {
        try {
            // Construct a datagram socket and bind it to any available port on the local host machine. 
        	//This socket will be used to send and receive UDP Datagram packets.
            socket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }
    
    public void start() throws IOException {

        try {
            //Bind to IP and Port
            this.port = 8080;
            this.ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        Message initMessage = new Message("Hello Scheduler");
        byte[] initBuf = Data.toByteArray(initMessage);
        sendPacket = new DatagramPacket(initBuf, initBuf.length, ip, port);
        socket.send(sendPacket);
                
        while(true) {
        	
            byte[] bufReceived = new byte[1024];
            receivePacket = new DatagramPacket(bufReceived, bufReceived.length);
            System.out.println("Elevator: Waiting for packets\n");
            socket.receive(receivePacket);
            
            // For iteration 1, received data is a Passenger event that was sent initially.
            // This will be changed in future iterations.
            try {
                receivedMessage = (Message) Data.fromByteArray(receivePacket.getData());
            } catch (ClassNotFoundException ce) {
                ce.printStackTrace();
            }
            
            if(receivedMessage.getReceiver() == Systems.ELEVATOR) {
            	System.out.println("Elevator: Packet received:");
            	System.out.println("Elevator From host: " + receivePacket.getAddress());
            	System.out.println("Elevator Host port: " + receivePacket.getPort());
            	System.out.println("Elevator packet data: " + receivedMessage + "\n");
            	break;
            }
        }
        // change message and send
        receivedMessage.setReceiver(Systems.SCHEDULER);
        receivedMessage.setSender(Systems.ELEVATOR);
        byte[] bufSend = Data.toByteArray(receivedMessage);
        
        sendPacket = new DatagramPacket(bufSend, bufSend.length, ip, port);
        
        System.out.println("Elevator: Sending packet to Scheduler");
        System.out.println("Elevator Host Address: " + sendPacket.getAddress());
        System.out.println("Elevator Destination host port: " + sendPacket.getPort());
        System.out.println("Elevator packet data: " + receivedMessage);
        socket.send(sendPacket);
        
        System.out.println("Elevator: Packet sent.\n");
        
        System.out.println("Closing Elevator socket\n");
        socket.close();
        	
    }

    public static void main(String args[]) throws IOException {
        Elevator c = new Elevator();
        c.start();
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