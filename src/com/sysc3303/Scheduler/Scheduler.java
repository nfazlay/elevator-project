package com.sysc3303.Scheduler;

import java.io.*;



import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.sysc3303.Elevator.ElevatorStates;
import com.sysc3303.properties.BrokenMessage;
import com.sysc3303.properties.Data;
import com.sysc3303.properties.ElevatorData;
import com.sysc3303.properties.ElevatorList;
import com.sysc3303.properties.Helper;
import com.sysc3303.properties.StateMessage;
import com.sysc3303.properties.Message;
import com.sysc3303.properties.OkMessage;
import com.sysc3303.properties.Systems;
import com.sysc3303.properties.Timing;
 
/**
 * Scheduler Class that implements the runnable
 * 
 * @version 1.2
 */
public class Scheduler implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket receivePacket;
    protected Data message, done;
    private Queue<DatagramPacket> floorPackets = new LinkedList<DatagramPacket>();
    private ElevatorList elevators;
    public ElevatorList brokenElevators;
    private SchedulerState currState; 
    public ArrayList<Systems> broken;
    
    /**
     * Constructor for Scheduler server
     * 
     * @param port Port to int to
     * @throws SocketException
     * @throws UnknownHostException
     */
    public Scheduler(int port) throws SocketException, UnknownHostException {
    	elevators = new ElevatorList();
    	brokenElevators = new ElevatorList();
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
    
    /**
     * Sends and receives packets from clients
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("incomplete-switch")
	private void start() throws IOException, ClassNotFoundException{
    	try {
    		while (true) {
    			synchronized(this){
    			
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
							StateMessage elevPos = (StateMessage) Data.fromByteArray(receivePacket.getData());
							Message floorRequest = null;
							if(elevators.add(receivePacket)) {//adding to list of elevators
								currState = SchedulerState.ADD_ELEVATOR;
							}
							else { // Elevator already connected/ in list
								//give task
								if(!floorPackets.isEmpty() && (elevPos.getState() == ElevatorStates.STATIONARY ||
										elevPos.getState() == ElevatorStates.MOVING )) {
									floorRequest = (Message) Data.fromByteArray(floorPackets.peek().getData());
									currState = SchedulerState.SET_PROB;
								}
								else if (elevPos.getState() == ElevatorStates.STARTING) {
									elevators.startTimer(receivePacket , getTime(elevPos));
									currState = SchedulerState.SEND_OK;
								}
								else if (elevPos.getState() == ElevatorStates.MOVING) {
									if(!elevators.checkTimer(receivePacket)) {
										currState = SchedulerState.SEND_BROKEN;
									}
									else {
										currState = SchedulerState.SEND_OK;
									}
								}
								else {// no data/request to send
									currState = SchedulerState.SEND_OK;
								}
							}
							switch(currState) {
								case ADD_ELEVATOR:
									done  = new OkMessage("Cool");
									elevators.print();
									sendToSystem(done, Systems.ELEVATOR, receivePacket.getAddress(), 
											receivePacket.getPort());
									break;
								case SET_PROB:
									System.out.println(Data.fromByteArray(floorPackets.peek().getData()));
									double tempProb;
									if(elevPos.getState()== ElevatorStates.MOVING && !elevators.checkTimer(receivePacket)) {
										elevators.remove(receivePacket);
										brokenElevators.add(receivePacket);
										System.out.println("SCHEDULER: BROKEN ELEVATOR!!! STOPPED FOR SERVICE");
										Data brokenMsg = new BrokenMessage("Broken");
										sendToSystem(brokenMsg, Systems.ELEVATOR, receivePacket.getAddress(),
												receivePacket.getPort());
										broken.add(Systems.ELEVATOR);
									}
									else {
										//add probaility to elevator for the current data until
										//probaility added to all elevators
										tempProb = getProb(elevPos, floorRequest);
										elevators.setProbElev(tempProb, receivePacket.getPort(), 
												receivePacket.getAddress());//set the porbability for the specific elevator								
									}
									elevators.print();
									if(elevators.serveCheck()) {//probability for all elevators added wrt current data
										//send data to elevator that has highest prob and remove from queue
										System.out.println("SCHEDULER: IN SERVERCHECK");
										ElevatorData elev = elevators.getHighProbElev();
										//long time = getTime();
										sendToSystem(floorRequest, Systems.ELEVATOR,  elev.address, elev.port);
										floorPackets.remove();
										
										//send cool to the others
										for(int i =0; i < elevators.size(); i++) {
											if(!elevators.get(i).equals(elev)) {
												done  = new OkMessage("Cool");
												sendToSystem(done, Systems.ELEVATOR, elevators.get(i).address,
														elevators.get(i).port);
											}
										}
										
										//set all prob to -1d
										elevators.setDefault();
										System.out.print("Elevators prob set to Default: ");
										elevators.print();
									}
									break;
								case SEND_OK:
									done  = new OkMessage("Cool");
									sendToSystem(done, Systems.ELEVATOR, receivePacket.getAddress(), 
											receivePacket.getPort());
									break;
								case SEND_BROKEN:
									elevators.remove(receivePacket);
									brokenElevators.add(receivePacket);
									System.out.println("SCHEDULER: BROKEN ELEVATOR!!! STOPPED FOR SERVICE");
									Data brokenMsg = new BrokenMessage("Broken");
									sendToSystem(brokenMsg, Systems.ELEVATOR, receivePacket.getAddress(),
											receivePacket.getPort());
									broken.add(Systems.ELEVATOR);
							}
							break;
						case FLOOR:
							System.out.println("SCHEDULER: IN FLOOR CASE");
							floorPackets.add(receivePacket);
							break;
					}
	    		}
    		}
    	}
    	catch (SocketTimeoutException ex) {
            System.out.println("SCHEDULER: Closing Scheduler socket due to inactivity");
            socket.close();
    	}
        	
    }
    
    /**
     * Calls Helper class method to pack data and send
     * 
     * @param message Data object
     * @param system System to send data
     * @param packet Packet previously received from client
     * @throws IOException
     */
    public void sendToSystem(Data message, Systems system, InetAddress address, int port) throws IOException {
    	DatagramPacket sendPacket = Helper.sendPacket(message, Systems.SCHEDULER, system, address, port);
    	socket.send(sendPacket);
    }
    
    /**
     * Get the probability to send data to elevator depending to it's state
     * @param currPos StateMessage Position of the elevator
     * @param request Message Data
     * @return Double probablilty
     * @throws IOException
     */
    @SuppressWarnings("incomplete-switch")
	public double getProb(StateMessage currPos, Message request) throws IOException {
    	double prob = 0d;
		switch((currPos).getState()){
			case STATIONARY:
				if(currPos.getFloor() > request.getfloor()) {//Elevator is above the requested floor
					prob += 1/(currPos.getFloor()-request.getfloor());
				}
				else if(currPos.getFloor() == request.getfloor()) {//Elevator at the same floor
					prob = 1d;
				}
				else {//elevator below
					prob += 0.5/(request.getfloor() - currPos.getFloor());
				}
				break;
			case MOVING:
				if(currPos.getFloor() > request.getfloor()) {//Elevator is above the requested floor
					if(currPos.getDest() > currPos.getFloor()) {//Elevator is going up
						prob = 0.2/(currPos.getFloor() - request.getfloor());
					}
					else if (currPos.getDest() == currPos.getFloor()){//Elevator reached the destination floor and will open door
						prob = 0.1/(currPos.getFloor() - request.getfloor());
					}
					else {//elevator is going down
						prob += 0.8/(currPos.getFloor()-request.getfloor());						
					}
				}
				else if(currPos.getFloor() == request.getfloor()) {//Elevator at the same floor and Moving
					prob += 0.05/(currPos.getFloor() - request.getfloor());
				}
				else {//elevator below
					if(currPos.getDest() > currPos.getFloor()) {//Elevator is going up
						prob += 0.8/(request.getfloor() - currPos.getFloor());
					}
					else if (currPos.getDest() == currPos.getFloor()){//Elevator reached the destination floor and will open door
						prob = 0.1/(request.getfloor() - currPos.getFloor());
					}
					else {//elevator is going down
						prob = 0.2/(request.getfloor() - currPos.getFloor());					
					}
				}
				break;
		}
		return prob;
    }
    
    public long getTime(StateMessage elevPos) {
    	long time = (long) Math.abs(elevPos.getFloor() + elevPos.getDest())*(Timing.MOVE.getTime()*2) + 
    			Timing.OPENDOOR.getTime() + Timing.CLOSEDOOR.getTime() + (elevPos.getFloor() + elevPos.getDest());
    	return time;
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
