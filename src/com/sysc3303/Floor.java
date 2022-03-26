package com.sysc3303;

import java.util.Scanner;

import com.sysc3303.properties.Message;
import com.sysc3303.properties.Systems;
import com.sysc3303.properties.Data;


import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
 * The Floor class creates the floor subsystem.
 * @version 1.2
 *
 */
public class Floor implements Runnable {
    private InetAddress ip;
    private DatagramSocket socket;
    private int port;
    private DatagramPacket sendPacket;
    protected Message receivedMessage, messageToSend;
    private Scanner sc;
    private LocalTime prevLocalTime;

    /**
     * Constructor for Floor
     */
    public Floor() {
        try {
            // Construct a datagram socket and bind it to any available port on the local host machine. 
        	//This socket will be used to send and receive UDP Datagram packets.
            socket = new DatagramSocket();
            prevLocalTime = null;
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Binds to ip and starts the server
     * @throws InterruptedException 
     */
    public void start() throws InterruptedException {

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

        // Communicates with the server by sending and receiving 
        // data until all lines in the input file are read
        while (sc.hasNextLine()) {
            
        	// Parses data into string array
            String line = sc.nextLine();
            String[] tempData = line.split(" ");
            int waitTime = 0;

            final LocalTime time = LocalTime.parse(tempData[0]);
            final int floor = Integer.parseInt(tempData[1]);
            final int carButton = Integer.parseInt(tempData[3]);
            final String direction = (String) tempData[2];
            
            if (prevLocalTime == null) {
            	waitTime = 0;
            }
            else {
            	waitTime = (int) prevLocalTime.until(time, ChronoUnit.SECONDS);
            }
            
            prevLocalTime = time;
            Thread.sleep(waitTime*1000);
            System.out.println("FLOOR Waiting " + waitTime + "seconds");

            try {
                // Creates Message and converts it into a byte array
                messageToSend = new Message(time, floor, direction, carButton, Systems.FLOOR, Systems.SCHEDULER);
                byte[] bufSend = Data.toByteArray(messageToSend);

                sendPacket = new DatagramPacket(bufSend, bufSend.length, ip, port);

                System.out.println("FLOOR: Sending packet.....");
                System.out.println("FLOOR: Destination Host Address: " + sendPacket.getAddress());
                System.out.println("FLOOR: Destination host port: " + sendPacket.getPort());
                System.out.println("FLOOR: Packet data: " + messageToSend);

                socket.send(sendPacket);

                System.out.println("FLOOR: Packet sent\n");
                
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("FLOOR: Closing Floor socket\n");
        socket.close();
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        Floor c = new Floor();
        c.start();
    }

    @Override
    public void run() {
        try {
			start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}