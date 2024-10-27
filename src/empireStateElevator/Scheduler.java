package empireStateElevator;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Scheduler {
	
	public enum Direction {
		UP,
		DOWN
	}

	private Map<UUID, Map<String, Integer>> upRequests = new HashMap<>(); // Outstanding requests going up
	private Map<UUID, Map<String, Integer>> downRequests = new HashMap<>(); // Outstanding requests going down
	private List<UUID> processingRequests = new ArrayList<>(); // Requests currently being handled
	private List<Integer> stops = new ArrayList<>(); // Ordered list of stops. If not for the hot scheduling, this could be a queue
	private int nextStop;
	private int defaultFloor;
	private Direction currentDirection = Direction.UP;
	
	public Scheduler(int defaultFloor) 
	{
		System.out.println("scheduler constructor");
		nextStop = defaultFloor;
		this.defaultFloor = defaultFloor;
	}
	
	// Handle new request from user
	public synchronized void processNewRequest(int startFloor, int endFloor)
	{	
		//TODO: CHECK IF VALID REQUEST (within floor limits)
		if (startFloor > endFloor)
		{
			UUID uuid = UUID.randomUUID();
			downRequests.put(uuid, getStartEndMap(startFloor, endFloor));
			
			// If current direction matches request and startFloor is on the way, hot update the stop list
			if (currentDirection == Direction.DOWN && startFloor <= nextStop)
			{
				updateStops(startFloor, endFloor, uuid);
			}
		}
		else if (startFloor < endFloor)
		{
			UUID uuid = UUID.randomUUID();
			upRequests.put(uuid, getStartEndMap(startFloor, endFloor));
			
			// If current direction matches request and startFloor is on the way, hot update the stop list
			if (currentDirection == Direction.UP && startFloor >= nextStop)
			{
				updateStops(startFloor, endFloor, uuid);
			}
		}
		else // same floor: toss request
		{
			System.out.println("Ignoring request for same floor");
		}
	}
	
	// Handle request from Elevator for next stop
	public synchronized int getNextFloor() throws InterruptedException
	{
		if (stops.isEmpty()) // If we don't have any requests, reset and try to re-populate with active requests
		{
			// Clear out fulfilled requests and reset processing requests list
			removeFulfilledRequests();
			processingRequests.clear();
			repopulateStops(); // Attempt to re-populate stops after reset
			if(stops.isEmpty()) // If still no stops (have no requests), back to default floor
			{
				return defaultFloor;
			}
		}
		
		if(currentDirection == Direction.UP)
		{
			// If going up, return first element in ordered list (return lowest to highest)
			return stops.remove(0);
		}
		else
		{
			// If going down, return last element in ordered list (return highest to lowest)
			return stops.remove(stops.size()-1);
		}

	}
	
	//=== Private Methods ===
	
	private void removeFulfilledRequests()
	{
		if(currentDirection == Direction.UP)
		{
			for(UUID uuid: processingRequests)
			{
				upRequests.remove(uuid);
			}
		}
		else
		{
			for(UUID uuid: processingRequests)
			{
				downRequests.remove(uuid);
			}
		}
	}
	
	private void repopulateStops()
	{
		if(currentDirection == Direction.UP)
		{
			if(!downRequests.isEmpty()) // Switch to down if we have any
			{
				for(Map.Entry<UUID, Map<String, Integer>> request: downRequests.entrySet())
				{
					updateStops(request.getValue().get("startFloor"), request.getValue().get("endFloor"), request.getKey());
				}
				currentDirection = Direction.DOWN;
			}
			else if(!upRequests.isEmpty()) // Otherwise check for more up requests
			{
				for(Map.Entry<UUID, Map<String, Integer>> request: upRequests.entrySet())
				{
					updateStops(request.getValue().get("startFloor"), request.getValue().get("endFloor"), request.getKey());
				}
			}
			// Else no requests. Direction already up
		}
		else // currentDirection DOWN
		{
			if(!upRequests.isEmpty()) // Switch to up if we have any
			{
				for(Map.Entry<UUID, Map<String, Integer>> request: upRequests.entrySet())
				{
					updateStops(request.getValue().get("startFloor"), request.getValue().get("endFloor"), request.getKey());
				}
				currentDirection = Direction.UP;
			}
			else if(!downRequests.isEmpty()) // Otherwise check for more down requests
			{
				for(Map.Entry<UUID, Map<String, Integer>> request: downRequests.entrySet())
				{
					updateStops(request.getValue().get("startFloor"), request.getValue().get("endFloor"), request.getKey());
				}
			}
			else // No requests. Default direction to up
			{
				currentDirection = Direction.UP;
			}
		}
	}
	
	private void updateStops (int startFloor, int endFloor, UUID uuid)
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