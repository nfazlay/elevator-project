package com.sysc3303.properties;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;
/**
 * Maintains a list of elevators
 *
 *
 */
public class ElevatorList {
	LinkedList<ElevatorData> list;
	int size;
	
	public ElevatorList() {
		list = new LinkedList<ElevatorData>();
		size = 0;
	}
	
	/**
	 * Checks and adds Elevator data to list
	 * @param packet DatagramPacket
	 * @return False if elevator already exist, True otherwise
	 */
	public boolean add(DatagramPacket packet) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).port == packet.getPort() && list.get(i).address.equals(packet.getAddress())) {
				return false;
			}
		}
		ElevatorData data = new ElevatorData(packet.getPort(), packet.getAddress());
		list.add(data);
		size++;
		return true;
	}
	
	public boolean serveCheck() {
		for(int i =0; i < list.size(); i++) {
			if(list.get(i).getProb() == -1d) {
				return false;
			}
		}
		return true;
	}
	
	public void setDefault() {
		for(int i =0; i < list.size(); i++) {
			list.get(i).setProb(-1d);
		}
	}
	
	/**
	 * Get the elevator with the hughest probabilty
	 * @return the elevator with the highest probaility 
	 */
	public ElevatorData getHighProbElev() {
		ElevatorData elev = list.getFirst();
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getProb() > elev.getProb()) {
				elev = list.get(i);
			}
		}
		return elev;
	}
	
	public void setProbElev(double prob, int port, InetAddress address) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).port == port && list.get(i).address.equals(address)) {
				list.get(i).setProb(prob);
			}
		}
	}
	
	/**
	 * Get elevator data at specifies index
	 * @param i index
	 * @return ElevatorData
	 */
	public ElevatorData get(int i) {
		return list.get(i);
	}
	
	public void startTimer(DatagramPacket packet, long time) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).port == packet.getPort() && list.get(i).address.equals(packet.getAddress())) {
				list.get(i).startTimer(time);
			}
		}
	}
	
	public boolean checkTimer(DatagramPacket packet) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).port == packet.getPort() && list.get(i).address.equals(packet.getAddress())) {
				return list.get(i).checkTimer();
			}
		}
		System.out.println(false);
		return false;
	}
	
	public ElevatorData remove(DatagramPacket packet) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).port == packet.getPort() && list.get(i).address.equals(packet.getAddress())) {
				this.size--;
				return list.remove(i);
			}
		}
		return null;
	}
	
	/**
	 * Size of list
	 * @return
	 */
	public int size() {
		return this.size;
	}
	
	public void print() {
		System.out.println(list.toString());
	}
}
