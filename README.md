# elevator-project

## File:
### Additionals
    Data: Interface that allows Messages to be converted to/from byteArray
    Message: Contains the input data and sender/receiver
    Systems: Enum that contains the subsystem names
    Helper: Utility Class
    MessageType: Enum for Message Types
    OkMessage: MessageType OK
    StateMessage: MessageType state
    Timing: Elevator Timings
    ElevatorStates: Contains the elevator States
    Simulator: Simulates the program

### Subsystems
    Elevator: Elevator client that received and sends data
    Floor: Floor client that reads data from input and sends to scheduler
    Scheduler: Receives and sends data to/from clients

#### input.txt: input file for Floor

## Set up instruction
    Run the Simulator.java file in eclipse

## FAQ
- The elevator takes 5 seconds to go between floors
- The elevator takes 4 seconds to open/close doors
- Each iteration of the elevator takes 1 second

## Test instructions
   The Test Domcument is located at the default package directory, After loading the project, use Run TestElevator of Unit JUnit to run. 
