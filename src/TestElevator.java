import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Test;
import com.sysc3303.Floor;
import com.sysc3303.Elevator.Elevator;
import com.sysc3303.Elevator.ElevatorStates;
import com.sysc3303.Scheduler.Scheduler;
import com.sysc3303.properties.Data;
import com.sysc3303.properties.ElevatorList;
import com.sysc3303.properties.StateMessage;
import com.sysc3303.properties.Systems;
/**
*
* The TestElevator class creates the program to run all threads of the elevator and then check the logs
* that inside the Elevator to test if it's been through the correct states, such as elevator properly closed
* and stopped at correct floor.
*
*/
public class TestElevator {
	//Two array list to keep track of elevator's states and floors.
	public static ArrayList<StateMessage> log = new ArrayList<StateMessage>();
	public static ArrayList<StateMessage> log2 = new ArrayList<StateMessage>();
	private static ArrayList<Integer> floors = new ArrayList<Integer>();
	private static ArrayList<Integer> floors2 = new ArrayList<Integer>();
	
	private Scanner sc;
	//Two boolean indicator of which element's door is properly closed or not.
	private boolean openClosedFloor;
	private boolean openClosedCar;
	ArrayList<Integer> inputFloors = new ArrayList<Integer>();
	private Data message;
	
	//Test for past iterations to test walked floors of elevator.
	/**
	 * @Test
	public void test1() throws SocketException, UnknownHostException {
	 
		System.out.println(String.format("----- Testing Elevator System -----"));
		
		final Scheduler server = new Scheduler(8080);
		final Elevator elevator = new Elevator(1);
		final Floor floor = new Floor();
		final Elevator elevator2 = new Elevator(2);
		//The two boolean indicator initially set to true.
		openClosedFloor = true;
		openClosedCar = true;
		
		//Start three threads.
		new Thread(server).start();
		new Thread(elevator).start();
		new Thread(floor).start();
		new Thread(elevator2).start();
		
		//Only read the logs from the elevator after certain time interval, it's to make sure that 
		//the logs have enough data to analyze, if extra instruction applied, more time should be added too.
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Register the logs from the elevator, and extract the data of floor and State form it.
		log = elevator.outputs;
		log2 = elevator2.outputs;
		
		for(int i = 0; i < log.size(); i++) {
			floors.add(log.get(i).getFloor());
		}
		
		for(int i = 0; i < log2.size(); i++) {
			floors2.add(log2.get(i).getFloor());
		}
		
		
		//Testing the actual log of the elevator with the initial instruction in the input.txt file.
		String filePath = "input.txt";
		try {
            sc = new Scanner(new FileReader(new File(filePath)));
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }
		
		
		//Testing through each line of command in the input file.
        while (sc.hasNextLine()) {
        	
        	// Parses data into string array
            String line = sc.nextLine();
            String[] tempData = line.split(" ");

            int floorInt = Integer.parseInt(tempData[1]);
            int carButton = Integer.parseInt(tempData[3]);
            int difference = carButton - floorInt;
            
            //The elevator should stay at start floor four times
            //because four actions need: Moving, Open door, close door, and Moving again.
            inputFloors.add(floorInt);
            inputFloors.add(floorInt);
            inputFloors.add(floorInt);
            inputFloors.add(floorInt);
            //Using the while loop to fill the passed floor with only one move state each.
            int j = 1;
            if(difference > 0) {
	            while(difference > 1) {
	            	inputFloors.add(floorInt + j);
	            	difference--;
	            	j++;
	            }
            }
            if(difference < 0) {
	            while(difference < 0) {
	            	inputFloors.add(floorInt - j);
	            	difference++;
	            	j++;
	            }
	            
	        
            }
            //At the destination floor, it only takes two states: open and close door.            
            inputFloors.add(Integer.valueOf(carButton));
            inputFloors.add(Integer.valueOf(carButton));
            
            
            for(int i = 0; i < log.size(); i++) {
            	//If it's start floor, check if once the door is opened, is it closed properly.
    			if(log.get(i).getFloor() == floorInt) {
    				if(log.get(i).getState() == ElevatorStates.OPENDOOR) {
    					if(log.get(i+1).getState() == ElevatorStates.CLOSEDOOR) {
    						openClosedFloor = true;
    			        } else {
    				        openClosedFloor = false;
    			        }
    		         }
    			}
    			
    			//If it's destination floor, check if once the door is opened, is it closed properly.
    			if(log.get(i).getFloor() == carButton) {
    				if(log.get(i).getState() == ElevatorStates.OPENDOOR) {
    					if(log.get(i+1).getState() == ElevatorStates.CLOSEDOOR) {
    						openClosedCar = true;
    			        } else {
    				        openClosedCar = false;
    			        }
    		         }
    			}
    	    System.out.println(inputFloors);
    	    System.out.println(floors);
    	    System.out.println(floors2);
    	    
    	    
            
            }  
        }
        Assert.assertTrue(openClosedFloor);
        Assert.assertTrue(openClosedCar);
        //Use collection method to check if the floors' data is following the same sequence as it suppose to.
        Assert.assertTrue((Collections.indexOfSubList(floors, inputFloors)>= 0));
        Assert.assertTrue((Collections.indexOfSubList(floors2, inputFloors)>= 0));
    }
	*/
	
	//Test to check if elevators stopped for detected broken messages.
	@Test
	public void test2() throws SocketException, UnknownHostException {
    System.out.println(String.format("----- Testing Elevator System -----"));
		
		final Scheduler server = new Scheduler(8080);
		final Elevator elevator = new Elevator(1);
		final Floor floor = new Floor();
		final Elevator elevator2 = new Elevator(2);
		
		//Start three threads.
		new Thread(server).start();
		new Thread(elevator).start();
		new Thread(floor).start();
		new Thread(elevator2).start();
		
		//Initialize the counter for broken elevators.
		int broken = 0;
		//The list of broken elevators from scheduler side
		ElevatorList brokenList = server.brokenElevators;
		
		//Only read the logs from the elevator after certain time interval, it's to make sure that 
		//the logs have enough data to analyze, if extra instruction applied, more time should be added too.
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//compare the number of broken elevators with scheduler.
		broken += elevator.broken;
		broken += elevator2.broken;
		Assert.assertEquals(broken, brokenList.size());			
			
		
	}
	
	
}
