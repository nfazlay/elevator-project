import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Test;
import com.sysc3303.Floor;
import com.sysc3303.Elevator.Elevator;
import com.sysc3303.Elevator.ElevatorStates;
import com.sysc3303.Scheduler.Scheduler;
import com.sysc3303.properties.StateMessage;
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
	private static ArrayList<Integer> floors = new ArrayList<Integer>();
	
	private Scanner sc;
	//Two boolean indicator of which element's door is properly closed or not.
	private boolean openClosedFloor;
	private boolean openClosedCar;
	
	@Test
	public void test() throws SocketException, UnknownHostException {
		System.out.println(String.format("----- Testing Elevator System -----"));
		
		final Scheduler server = new Scheduler(8080);
		final Elevator elevator = new Elevator(1);
		final Floor floor = new Floor();
		
		//The two boolean indicator initially set to true.
		openClosedFloor = true;
		openClosedCar = true;
		
		//Start three threads.
		new Thread(server).start();
		new Thread(elevator).start();
		new Thread(floor).start();
		
		//Only read the logs from the elevator after certain time interval, it's to make sure that 
		//the logs have enough data to analyze, if extra instruction applied, more time should be added too.
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Register the logs from the elevator, and extract the data of floor and State form it.
		log = elevator.outputs;
		System.out.println(log);
		
		for(int i = 0; i < log.size(); i++) {
			floors.add(log.get(i).getFloor());
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
        	ArrayList<Integer> inputFloors = new ArrayList<Integer>();
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
            while(difference > 1) {
            	
            	inputFloors.add(floorInt + j);
            	difference--;
            	j++;
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
            
            Assert.assertTrue(openClosedFloor);
            Assert.assertTrue(openClosedCar);
            //Use collection method to check if the floors' data is following the same sequence as it suppose to.
            Assert.assertTrue((Collections.indexOfSubList(floors, inputFloors)>= 0));
            }  
        }
    }
}
