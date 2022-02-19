package com.sysc3303.Elevator;

import com.sysc3303.properties.Message;
import com.sysc3303.properties.MessageType;
import com.sysc3303.properties.OkMessage;
import com.sysc3303.properties.StateMessage;
import com.sysc3303.properties.Systems;
import com.sysc3303.properties.Data;
import com.sysc3303.properties.Helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.net.SocketException;

/**
 * 
 *
 */
public class Elevator implements Runnable {
    private InetAddress ip;
    private DatagramSocket socket;
    private int port;
    private DatagramPacket receivePacket;
    protected Data receivedMessage, messageToSend;
    
    private Queue<Message> requests = new LinkedList<Message>();
    private ElevatorStates currState;
    private int lamp;
    private int currFloor;
    private int floor;
    private String dir;
    private boolean passenger;
    

    public Elevator() {
    	passenger = false;
    	currState = ElevatorStates.STATIONARY;
    	lamp = -1;
    	currFloor = 0;
        try {
            // Construct a datagram socket and bind it to any available port on the local host machine. 
        	//This socket will be used to send and receive UDP Datagram packets.
            socket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }
    
    public void start() throws IOException, ClassNotFoundException, InterruptedException {

        try {
            //Bind to IP and Port
            this.port = 8080;
            this.ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
//        Message initMessage = new Message("Hello Scheduler");
//        byte[] initBuf = Data.toByteArray(initMessage);
//        sendPacket = new DatagramPacket(initBuf, initBuf.length, ip, port);
//        socket.send(sendPacket);
                
        while(true) {
        	Thread.sleep(1*1000);
	    	
	    	if(!requests.isEmpty() && currState == ElevatorStates.STATIONARY) {//requests available
	    		lamp = requests.peek().getCarButton();
	    		floor = requests.peek().getfloor();  
	    		dir = requests.peek().getDirection();
	    	}
	    	
			switch(currState) {
	    		case STATIONARY:
	    			if(requests.isEmpty()) {
	    				currState = ElevatorStates.STATIONARY;
	    			}
	    			else {//has requests
	    				if(currFloor != floor) {
	    					currState = ElevatorStates.MOVING;
	    				}
	    				else {
	    					//Open door
	    					currState = ElevatorStates.OPENDOOR;            					
	    					passenger = true;
	    				}
	    			}
	    			//send to scheduler
	    			break;
	    		case CLOSEDOOR:
	    			//MOVE or STATIONARY
	    			if(lamp == -1) {//passenger served
	    				requests.poll();//remove message
	    				System.out.println(requests.isEmpty());
	    				passenger = false;
	    				currState = ElevatorStates.STATIONARY;
	    			}
	    			else {
	    				currState = ElevatorStates.MOVING;
	    			}
	    			//send to scheduler
	    			break;
	    		case MOVING:
	    			//Keep Moving till floor reached
	    			if(!passenger) {
	    				if(currFloor != floor) {
	    					if(currFloor < floor) {//pickup floor above
	    						currFloor++;
	    					}
	    					else {//pickup floor below
	    						currFloor--;
	    					}
	    				}
	    				else {
	    					passenger = true;
	    					currState = ElevatorStates.OPENDOOR;
	    				}
	    			}
	    			else {
	    				if(lamp != -1) {
	    					System.out.println(dir);
	    					System.out.println(lamp);
	    					if(dir.equals("Up")) {//requested to go up
	    						currFloor++;
	    					}
	    					else {//requested to go down
	    						currFloor--;
	    					}
	    					
	    					if(currFloor == lamp) {
	    						lamp = -1;
	    					}
	    				}
	    				else {//requested floor reached
	    					//open door
	    					currState = ElevatorStates.OPENDOOR;
	    					//send reached
	    				}
	    			}
	    			//send to scheduler
	    			break;
	    		case OPENDOOR:
	    			//Close door
	    			currState = ElevatorStates.CLOSEDOOR;
	    			//send to scheduler
	    			break;
	    	}
            //send state to scheduler
            messageToSend = new StateMessage(currState, currFloor);
            sendToSystem(messageToSend, Systems.SCHEDULER);
            System.out.println("ELEVATOR: Packet sent.");
		    
		    
		    //receive message from scheduler
            byte[] bufReceived = new byte[1024];
            receivePacket = new DatagramPacket(bufReceived, bufReceived.length);
            System.out.println("ELEVATOR: Waiting for packet...\n");
            socket.receive(receivePacket);
            
            try {
                receivedMessage = (Data) Data.fromByteArray(receivePacket.getData());
            } catch (ClassNotFoundException ce) {
                ce.printStackTrace();
            }
            
            System.out.println("ELEVATOR: Packet received from: "+ receivedMessage.getSender());
            System.out.println("ELEVATOR: From host address: " + receivePacket.getAddress());
            System.out.println("ELEVATOR: From host port : " + receivePacket.getPort());
            //if type Request, add to queue
            //check data type
            if(receivedMessage.getType() == MessageType.REQUEST) {
            	requests.add((Message) receivedMessage);
            	System.out.println("ELEVATOR: Packet data: " + ((Message)receivedMessage) + "\n");
            }
            else {
            	System.out.println("ELEVATOR: Packet data: " + ((OkMessage)receivedMessage) + "\n");
            }
        }
//        System.out.println("Closing Elevator socket\n");
//        socket.close();
    }
    
    public void sendToSystem(Data message, Systems system) throws IOException, ClassNotFoundException {
    	DatagramPacket sendPacket = Helper.sendPacket(message, Systems.ELEVATOR, system, ip, port);
    	socket.send(sendPacket);
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        Elevator c = new Elevator();
        c.start();
    }

    @Override
    public void run() {
        try {
			start();
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}