package com.sysc3303.properties;

public enum Timing {
	OPENDOOR(4),
	CLOSEDOOR(4),
	MOVE(5),
	DEFAULT(1);
	
	private int value ;

	Timing(int i) {
		this.value = i;
	}
	
	public int getTime() {
		return this.value;
	}

}
