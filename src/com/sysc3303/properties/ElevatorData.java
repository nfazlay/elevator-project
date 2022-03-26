package com.sysc3303.properties;
import java.net.InetAddress;

public class ElevatorData{
	public int port;
	public InetAddress address;
	private double prob;
	private Stopwatch timer;
	
	public ElevatorData(int port, InetAddress address) {
		this.prob = -1d;
		this.port = port;
		this.address = address;
		timer = null;
	}

	public boolean equals(ElevatorData o) {
		if(this.port == o.port && this.address == o.address) {
			return true;
		}
		return false;
	}
	
	public double getProb() {
		return this.prob;
	}
	
	public void setProb(double prob) {
		this.prob = prob;
	}
	
	public void startTimer(long time) {
		timer = new Stopwatch(time);
	}
	
	public boolean checkTimer() {
		return timer.check();
	}
	
	public void removeTimer() {
		timer = null;
	}
	
   @Override
   public String toString() { 
	   return "Port: " + port + " Address: " + address + " Probability: " + prob; 
   }
   
   
	
	
}
