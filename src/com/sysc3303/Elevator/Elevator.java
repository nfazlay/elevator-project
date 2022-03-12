package com.sysc3303.Elevator;

import com.sysc3303.properties.Message;

import com.sysc3303.properties.MessageType;
import com.sysc3303.properties.OkMessage;
import com.sysc3303.properties.StateMessage;
import com.sysc3303.properties.Systems;
import com.sysc3303.properties.Timing;
import com.sysc3303.properties.Data;
import com.sysc3303.properties.Helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.net.SocketException;
import java.util.Random;

/**
 * Elevator Class that implements the runnable interface
 * 
 * @version 1.2
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
	public ArrayList<StateMessage> outputs;
	private int id;
    

    /**
     * Elevator Class Constructor
     * 
     */
    public Elevator(int id) {
    	this.id = id;
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
    
    /**
     * Binds to server and sends and receives data
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public void start() throws IOException, ClassNotFoundException, InterruptedException {

        try {
            //Bind to IP and Port
            this.port = 8080;
            this.ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
                
        while(true) {
        	
        	Thread.sleep(Timing.DEFAULT.getTime()*1000);
	    	
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
	    				if(currFloor != floor) {  //Not in floor to pick up passenger
	    					currState = ElevatorStates.MOVING;
	    				}
	    				else {
	    					//Open door as currently in floor to pick up massenger
	    					currState = ElevatorStates.OPENDOOR;            					
	    					passenger = true;
	    				}
	    			}
	    			//send to scheduler
	    			break;
	    		case CLOSEDOOR:
	    			Thread.sleep(Timing.CLOSEDOOR.getTime()*1000);
	    			//MOVE or STATIONARY
	    			if(lamp == -1) {//passenger served
	    				requests.poll();//remove message
	    				passenger = false;
	    				currState = ElevatorStates.STATIONARY;
	    			}
	    			else {
	    				currState = ElevatorStates.MOVING;
	    			}
	    			//send to scheduler
	    			break;
	    		case MOVING:
	    			Thread.sleep(Timing.MOVE.getTime()*1000);
	    			//Keep Moving till floor reached
	    			if(!passenger) { //empty elevator
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
	    			else { //has passenger
	    				if(lamp != -1) {
	    					if(dir.equals("Up")) {//requested to go up
	    						currFloor++;
	    					}
	    					else {//requested to go down
	    						currFloor--;
	    					}
	    					
	    					if(currFloor == lamp) {
	    						currState = ElevatorStates.OPENDOOR;
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
	    			Thread.sleep(Timing.OPENDOOR.getTime()*1000);
	    			currState = ElevatorStates.CLOSEDOOR;
	    			//send to scheduler
	    			break;
	    	}
            //send state to scheduler
            messageToSend = new StateMessage(id, currState, currFloor, lamp);
            sendToSystem(messageToSend, Systems.SCHEDULER);
            System.out.println("ELEVATOR: " + id +  " Packet sent.");
		    
		    
		    //receive message from scheduler
            byte[] bufReceived = new byte[1024];
            receivePacket = new DatagramPacket(bufReceived, bufReceived.length);
            System.out.println("ELEVATOR: " + id +  " Waiting for packet...\n");
            socket.receive(receivePacket);
            
            try {
                receivedMessage = (Data) Data.fromByteArray(receivePacket.getData());
            } catch (ClassNotFoundException ce) {
                ce.printStackTrace();
            }
            
            System.out.println("ELEVATOR: " + id +  " Packet received from: "+ receivedMessage.getSender());
            System.out.println("ELEVATOR: " + id +  " From host address: " + receivePacket.getAddress());
            System.out.println("ELEVATOR: " + id +  " From host port : " + receivePacket.getPort());
            //check data type
            //if type Request, add to queue
            if(receivedMessage.getType() == MessageType.REQUEST) {
            	requests.add((Message) receivedMessage);
            	System.out.println("ELEVATOR: " + id +  " Packet data: " + ((Message)receivedMessage) + "\n");
            }
            else {
            	System.out.println("ELEVATOR: " + id +  " Packet data: " + ((OkMessage)receivedMessage) + "\n");
            }
        }
//        System.out.println("Closing Elevator socket\n");
//        socket.close();
    }
    
    /**
     * Packs data and sends to Server
     * 
     * @param message Data to send
     * @param system Receiving System
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void sendToSystem(Data message, Systems system) throws IOException, ClassNotFoundException {
    	DatagramPacket sendPacket = Helper.sendPacket(message, Systems.ELEVATOR, system, ip, port);
    	socket.send(sendPacket);
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
    	Random rand = new Random();
    	int id = rand.nextInt(100);
        Elevator c = new Elevator(id);
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