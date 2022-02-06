package com.sysc3303;

import java.util.Scanner;

import com.sysc3303.properties.Message;
import com.sysc3303.properties.Systems;
import com.sysc3303.properties.Data;


import java.time.LocalTime;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.io.File;

/**
 * 
 * The Floor class creates the floor subsystem.
 * It is responsible of retrieving elevator information by line from the input file, 
 * parsing an Event with the information into a byte array and
 * sending the Event to the Scheduler. Afterwards, it waits in response for the 
 * Scheduler to return the event. Once it is returned, the
 * Floor will repeat this cycle until there are no more 
 * lines to read from the input file.
 *
 */
public class Floor implements Runnable {
    private InetAddress ip;
    private DatagramSocket socket;
    private int port;
    private DatagramPacket sendPacket, receivePacket;
    protected Message receivedMessage, messageToSend;
    private Scanner sc;

    public Floor() {
        try {
            // Construct a datagram socket and bind it to any available port on the local host machine. 
        	//This socket will be used to send and receive UDP Datagram packets.
            socket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    
    public void start() {

        try {
            //Bind to IP and Port
            this.port = 8080;
            this.ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Reads the input data
        String filePath = "input.txt";

        try {
            sc = new Scanner(new FileReader(new File(filePath)));
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }

        // Communicates with the server by sending and receiving data until all lines in the input file are read
        while (sc.hasNextLine()) {
            
        	// Parses data into string array
            String line = sc.nextLine();
            String[] tempData = line.split(" ");

            final LocalTime time = LocalTime.parse(tempData[0]);
            final int floor = Integer.parseInt(tempData[1]);
            final int carButton = Integer.parseInt(tempData[3]);
            final String direction = (String) tempData[2];

            try {
                // Creates Message and converts it into a byte array
                messageToSend = new Message(time, floor, direction, carButton, Systems.FLOOR, Systems.SCHEDULER);
                byte[] bufSend = Data.toByteArray(messageToSend);

                sendPacket = new DatagramPacket(bufSend, bufSend.length, ip, port);

                System.out.println("Floor: Sending packet.....");
                System.out.println("Floor Host Address: " + sendPacket.getAddress());
                System.out.println("Floor Destination host port: " + sendPacket.getPort());
                System.out.println("Floor Message data: " + messageToSend);

                socket.send(sendPacket);

                System.out.println("Floor: Packet sent.\n");
                
                while(true) {
                	
                    byte[] bufReceived = new byte[1024];
                    receivePacket = new DatagramPacket(bufReceived, bufReceived.length);
                    socket.receive(receivePacket);
                    
                    // For iteration 1, received data is a Passenger event that was sent initially.
                    // This will be changed in future iterations.
                    try {
                        receivedMessage = (Message) Data.fromByteArray(receivePacket.getData());
                    } catch (ClassNotFoundException ce) {
                        ce.printStackTrace();
                    }
                    
                    if(receivedMessage.getReceiver() == Systems.FLOOR) {
                    	
                    	System.out.println("Floor: Packet received:");
                    	System.out.println("Floor From host: " + receivePacket.getAddress());
                    	System.out.println("Floor Host port: " + receivePacket.getPort());
                    	System.out.println(receivedMessage + "\n");
                    	break;
                    }
                	
                }
                
    

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("Closing Floor socket");
        socket.close();
    }

    public static void main(String args[]) throws IOException {
        Floor c = new Floor();
        c.start();
    }

    @Override
    public void run() {
        start();
    }
}