package empireStateElevator;

import utility.CommandLineInput;
import java.util.ArrayList;
import java.util.List;

public class RequestRouter {
	
	private List<Scheduler> schedulers = new ArrayList<>();;
	private int topFloor;
	private int bottomFloor;
	

	public RequestRouter(List<Scheduler> schedulers, int topFloor, int bottomFloor)
	{
		this.schedulers = schedulers;
		this.topFloor = topFloor;
		this.bottomFloor = bottomFloor;
	}
	
	public void routeRequest(int startFloor, int endFloor)
	{
		if(startFloor == endFloor)
		{
			System.out.println("Ignoring request for same floor.");
			return;
		}
		
		Scheduler bestScheduler = schedulers.get(0);
		int bestSchedulerRequests = schedulers.get(0).getNumberOfRequests();
		
		for(Scheduler scheduler: schedulers)
		{
			if(scheduler.getNumberOfRequests() == 0) // If an elevator is free, use that one
			{
				System.out.println("Routing request to " + scheduler.getElevatorName());
				scheduler.processNewRequest(startFloor, endFloor);
				return;
			}
			else if(scheduler.getNumberOfRequests() < bestSchedulerRequests) // Find the scheduler with least load
			{
				bestScheduler = scheduler;
				bestSchedulerRequests = bestScheduler.getNumberOfRequests();
			}
		}
		System.out.println("Routing request to " + bestScheduler.getElevatorName());
		bestScheduler.processNewRequest(startFloor, endFloor);
	}
	
	public void userRequestLoop()
	{
		boolean quit = false;
		boolean inputValid = false;
		String input = "";
		int intFirstInput = 0;
		int intSecondInput = 0;
		
		while(!quit)
		{
			inputValid = false;
			while(!inputValid)
			{
				System.out.println("Enter the start floor or 'done' to quit:");
				input = CommandLineInput.getIntegerFromUser();
				intFirstInput = Integer.parseInt(input);
				if(input.toLowerCase().equals("done"))
				{
					return;
				}
				else if(input.equals("0") || input.equals("13"))
				{
					System.out.println("Input invalid (0 floor does not exist and the 13th floor is ghosts only)");
				}
				else if(intFirstInput > topFloor || intFirstInput < bottomFloor)
				{
					System.out.println("Input out of bounds (top floor is " + topFloor + " and bottom floor is " + bottomFloor + ")");
				}
				else
				{
					inputValid = true;
				}
			}
		
			inputValid = false;
			while(!inputValid)
			{
				System.out.println("Enter the stop floor or 'done' to quit:");
				input = CommandLineInput.getIntegerFromUser();
				intSecondInput = Integer.parseInt(input);
				if(input.toLowerCase().equals("done"))
				{
					return;
				}
				else if(input.equals("0") || input.equals("13"))
				{
					System.out.println("Input invalid (0 floor does not exist and the 13th floor is ghosts only)");
				}
				else if(intSecondInput > topFloor || intSecondInput < bottomFloor)
				{
					System.out.println("Input out of bounds (top floor is " + topFloor + " and bottom floor is " + bottomFloor + ")");
				}
				else
				{
					inputValid = true;
				}
			}
			routeRequest(intFirstInput, intSecondInput);
		}
	}
}
