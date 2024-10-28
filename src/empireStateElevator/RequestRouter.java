package empireStateElevator;

import utility.CommandLineInput;
import java.util.ArrayList;
import java.util.List;

public class RequestRouter {
	
	private List<Scheduler> schedulers = new ArrayList<>(); // List of schedulers available for routing
	// Router is doubling as a request validator so it needs the floor constraints (kinda lazy, could be a separate validator class)
	private int topFloor;
	private int bottomFloor;
	
	// Constructor without pre-seed requests
	public RequestRouter(List<Scheduler> schedulers, int topFloor, int bottomFloor)
	{
		this.schedulers = schedulers;
		this.topFloor = topFloor;
		this.bottomFloor = bottomFloor;
	}
	
	// Constructor with pre-seed requests
	public RequestRouter(List<Scheduler> schedulers, int topFloor, int bottomFloor, List<List<Integer>> preSeededRequests)
	{
		this.schedulers = schedulers;
		this.topFloor = topFloor;
		this.bottomFloor = bottomFloor;
		
		for(List<Integer> request: preSeededRequests)
		{
			routeRequest(request.get(0), request.get(1));
		}
	}
	
	// Send new requests to an elevator
	public void routeRequest(int startFloor, int endFloor)
	{
		if(startFloor == endFloor)
		{
			System.out.println("Ignoring request for same floor.");
			return;
		}
		
		Scheduler bestScheduler = schedulers.get(0); // Stores current best elevator
		int bestSchedulerRequests = schedulers.get(0).getNumberOfRequests();
		
		for(Scheduler scheduler: schedulers)
		{
			if(scheduler.getNumberOfRequests() == 0) // If an elevator is free, use that one
			{
				System.out.println("Routing request to " + scheduler.getElevatorName());
				scheduler.processNewRequest(startFloor, endFloor);
				return;
			}
			// This is pretty simple and could be built out with more sophisticated logic to boost efficiency
			else if(scheduler.getNumberOfRequests() < bestSchedulerRequests) // Find the scheduler with least load
			{
				bestScheduler = scheduler;
				bestSchedulerRequests = bestScheduler.getNumberOfRequests();
			}
		}
		System.out.println("Routing request to " + bestScheduler.getElevatorName());
		bestScheduler.processNewRequest(startFloor, endFloor);
	}
	
	// Once the elevator application is started in earnest, this loop takes over the UI for the duration
	public void userRequestLoop()
	{
		boolean inputValid = false;
		String input = "";
		int intFirstInput = 0; // Integer conversion of start floor
		int intSecondInput = 0; // Integer conversion of end floor
		
		while(true)
		{
			inputValid = false; // Reset validation status
			while(!inputValid)
			{
				System.out.println("\nEnter the start floor:");
				input = CommandLineInput.getIntegerFromUser();
				intFirstInput = Integer.parseInt(input);
				// This validation stuff could be broken out into a separate 'validator' class
				if(input.equals("0") || input.equals("13")) // Check for special cases
				{
					System.out.println("Input invalid (0 floor does not exist and the 13th floor is ghosts only)");
				}
				else if(intFirstInput > topFloor || intFirstInput < bottomFloor) // Check boundaries
				{
					System.out.println("Input out of bounds (top floor is " + topFloor + " and bottom floor is " + bottomFloor + ")");
				}
				else
				{
					inputValid = true;
				}
			}
			inputValid = false; // Reset validation status
			while(!inputValid)
			{
				System.out.println("\nEnter the stop floor:");
				input = CommandLineInput.getIntegerFromUser();
				intSecondInput = Integer.parseInt(input);
				// This validation stuff could be broken out into a separate 'validator' class
				if(input.equals("0") || input.equals("13")) // Check for special cases
				{
					System.out.println("Input invalid (0 floor does not exist and the 13th floor is ghosts only)");
				}
				else if(intSecondInput > topFloor || intSecondInput < bottomFloor)  // Check boundaries
				{
					System.out.println("Input out of bounds (top floor is " + topFloor + " and bottom floor is " + bottomFloor + ")");
				}
				else
				{
					inputValid = true;
				}
			}
			routeRequest(intFirstInput, intSecondInput); // Once we have good input, route the request
		}
	}
}
