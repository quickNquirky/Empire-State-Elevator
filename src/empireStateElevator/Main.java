package empireStateElevator;

public class Main {
	public static void main(String[] args)
	{
		//print splash
		//print main menu
		//	start
		//	settings
		
		int defaultFloor = 1;
		long stopFloorWait = 3;
		long passFloorWait = 1;
		long sameFloorWait = 2;
		
		
		Scheduler scheduler = new Scheduler(defaultFloor);
		Elevator elevator = new Elevator("elevator 1", scheduler, defaultFloor, stopFloorWait, passFloorWait, sameFloorWait);
		elevator.run();
		
		scheduler.processNewRequest(2, 5);
		scheduler.processNewRequest(7, 30);
		scheduler.processNewRequest(20, 5);
		scheduler.processNewRequest(2, 5);
		scheduler.processNewRequest(3, 5);
		scheduler.processNewRequest(4, 5);
		scheduler.processNewRequest(9, 5);
		scheduler.processNewRequest(8, 5);
		scheduler.processNewRequest(7, 5);
		scheduler.processNewRequest(6, 5);
		scheduler.processNewRequest(5, 5);
		//Set up for user input
	}
}
