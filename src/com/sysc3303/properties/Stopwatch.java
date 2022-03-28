package com.sysc3303.properties;


public class Stopwatch { 

    private final long start;
    private long time;

    /**
     * Initializes a new stopwatch.
     */
    public Stopwatch(long timeInSeconds) {
    	time = timeInSeconds;
        start = System.currentTimeMillis();
    } 


    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in seconds) since the stopwatch was created
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
    
    public boolean check() {
    	System.out.println(elapsedTime() + " > " + time);
    	if(elapsedTime() > time) {
    		return false;
    	}
    	return true;
    }
}
