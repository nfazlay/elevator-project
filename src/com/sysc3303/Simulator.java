package com.sysc3303;

import java.net.SocketException;

import java.net.UnknownHostException;

import com.sysc3303.Elevator.Elevator;
import com.sysc3303.Scheduler.Scheduler;

/**
 * Simulates the elevator by running threads
 *
 */
public class Simulator {

	public static void main(String[] args) throws SocketException, UnknownHostException {
		System.out.println(String.format("----- Simulating Elevator System -----"));
		final Scheduler server = new Scheduler(8080);
		final Elevator elevator = new Elevator(1);
		final Elevator elevator2 = new Elevator(2);
		final Floor floor = new Floor();
		
		new Thread(server).start();
		new Thread(elevator).start();
		new Thread(elevator2).start();
		new Thread(floor).start();
		
	}

}
