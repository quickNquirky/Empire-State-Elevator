package empireStateElevator;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import empireStateElevator.Elevator.Direction;

public class Scheduler {

	private Map<UUID, Map<String, Integer>> upRequests = new HashMap<>(); // Outstanding requests going up
	private Map<UUID, Map<String, Integer>> downRequests = new HashMap<>(); // Outstanding requests going down
	private List<UUID> processingRequests = new ArrayList<>(); // Requests currently being handled
	private List<Integer> stops = new ArrayList<>(); // Ordered list of stops. If not for the hot scheduling, this could be a queue
	private Elevator elevator;
	private int nextStop;
	
	public Scheduler(Elevator elevator) 
	{
		this.elevator = elevator;
		nextStop = elevator.getCurrentFloor();
	}
	
	// Handle new request from user
	public synchronized void processNewRequest(int startFloor, int endFloor)
	{	
		//TODO: MUTEX LOCKS
		//TODO: CHECK IF VALID REQUEST (within floor limits)
		if (startFloor > endFloor)
		{
			UUID uuid = UUID.randomUUID();
			downRequests.put(uuid, getStartEndMap(startFloor, endFloor));
			
			// If current direction matches request and startFloor is on the way, hot update the stop list
			if (elevator.getCurrentDirection() == Direction.DOWN && startFloor <= nextStop)
			{
				hotUpdateStops(startFloor, endFloor, uuid);
			}
		}
		else if (startFloor < endFloor)
		{
			UUID uuid = UUID.randomUUID();
			upRequests.put(uuid, getStartEndMap(startFloor, endFloor));
			
			// If current direction matches request and startFloor is on the way, hot update the stop list
			if (elevator.getCurrentDirection() == Direction.UP && startFloor >= nextStop)
			{
				hotUpdateStops(startFloor, endFloor, uuid);
			}
		}
		else // same floor: toss request
		{
			System.out.println("Ignoring request for same floor");
		}
	}
	
	// Handle request from Elevator for next stop
	public void getNextFloor(Direction direction) throws InterruptedException
	{
		while (stops.isEmpty()) // If we don't have any requests, wait for the next one
		{
			TimeUnit.SECONDS.sleep(1);
		}
		
		// up or down? (grab from either end of list)
		// remove floor from list
		// check 
		
		//If stops list is empty, enter hold cycle
		//Once requests are received, return first number in stops list
		//If current floor is the last floor in the stops list, clear fulfilled requests, re-populate list with existing requests, return first number in stops list
		//If no more requests exist enter hold cycle
		//Return next floor in list of stops
	}
	
	//=== Private Methods ===
	
	private void hotUpdateStops (int startFloor, int endFloor, UUID uuid)
	{
		addStop(startFloor);
		addStop(endFloor);
		processingRequests.add(uuid);
	}
	
	private Map<String, Integer> getStartEndMap(int startFloor, int endFloor)
	{
		Map<String, Integer> tempMap = new HashMap<>();
		tempMap.put("startFloor", startFloor);
		tempMap.put("endFloor", endFloor);
		return tempMap;
	}
	
	private void addStop(int floor)
	{
		// If the stop is on the list or the elevator is currently at or heading to the same floor, ignore
		if (!(stops.contains(floor) || floor == nextStop))
		{
			stops.add(floor);
			stops.sort(null);
		}
	}
}

/*
 * - Scheduler:
 * 		- Mutex locked up dictionary, down dictionary {uuid:{startFloor: int, endFloor: int}}
 * 		- Mutex locked list of request UUIDs being fulfilled
 * 		- Mutex locked ordered list of stops
 * 
 * 		- processNewRequest(startFloor, endFloor) from user:
 * 			- Assign request uuid and store in respective dictionary
 * 			- If request direction matches, can we add to current stop schedule?
 * 				- What floor is elevator currently on?
 * 				- If so, add stops to stop list, and add uuid to list of requests being fulfilled
 * 
 * 		- getNextStop(Current floor) from elevator
 * 			- If stops list is empty, enter hold cycle
 * 				- Once requests are received, return first number in stops list
 * 			- If current floor is the last floor in the stops list, clear fulfilled requests, re-populate list with existing requests, return first number in stops list
 * 				- If no more requests exist enter hold cycle
 * 			- Return next floor in list of stops
 * 
 * - Elevator:
 * 		- Current floor
 * 		- GetCurrentFloor
 * */