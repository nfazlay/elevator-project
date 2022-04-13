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
		final Floor floor = new Floor();
		
		new Thread(server).start();
		for (int i = 1; i < 5; i++) {
			final Elevator elevator = new Elevator(i);
			new Thread(elevator).start();
		}
		new Thread(floor).start();
		
	}

}
