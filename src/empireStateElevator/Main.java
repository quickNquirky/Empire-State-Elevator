package empireStateElevator;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import utility.CommandLineInput;

public class Main {
	public static void main(String[] args) throws Exception
	{
		DisplayElements.displaySplash(); // Print splash

		// Initialize settings
		try {
			Settings.startup();
		} 
		catch (Exception e) {
			System.out.println("Could not find/initialize settings storage");
			e.printStackTrace();
		}
		
		boolean quit = false;

		while(!quit) // Main application loop
		{
			DisplayElements.displayMainMenu(); // Print main menu
			int selection = mainMenuInputLoop(); // Get main menu selection from user
		
			switch(selection)
			{
			case 1: // Start elevator
				// Read settings
				int defaultFloor = Integer.parseInt(Settings.getSettingValue(Settings.defaultFloorField));
				int numberOfElevators = Integer.parseInt(Settings.getSettingValue(Settings.numberOfElevatorsField));
				int stopFloorWait = Integer.parseInt(Settings.getSettingValue(Settings.stopFloorWaitField));
				int passFloorWait = Integer.parseInt(Settings.getSettingValue(Settings.passFloorWaitField));
				int sameFloorWait = Integer.parseInt(Settings.getSettingValue(Settings.sameFloorWaitField));
				int topFloor = Integer.parseInt(Settings.getSettingValue(Settings.topFloorField));
				int bottomFloor = Integer.parseInt(Settings.getSettingValue(Settings.bottomFloorField));
				boolean usePreSeedFile = Boolean.parseBoolean(Settings.getSettingValue(Settings.usePreSeedFileField));
				String preSeedFilePath = Settings.getSettingValue(Settings.preSeedFilePathField);
				
				List<Scheduler> schedulers = new ArrayList<>(); // Collect schedulers for routing
				
				for(int i = 1; i <= numberOfElevators; i++) // Spin up elevators
				{
					schedulers.add(runElevator("Elevator " + String.valueOf(i), defaultFloor, stopFloorWait, passFloorWait, sameFloorWait));
				}
				
				if(usePreSeedFile)
				{
					// Parse pre-seed file
					List<List<Integer>> preSeededRequests = readRequestsFromFile(preSeedFilePath);
					// Spin up router with pre-seed requests
					RequestRouter router = new RequestRouter(schedulers, topFloor, bottomFloor, preSeededRequests);
					router.userRequestLoop(); // Start accepting user input
				}
				else
				{
					// Spin up router without pre-seed requests
					RequestRouter router = new RequestRouter(schedulers, topFloor, bottomFloor);
					router.userRequestLoop(); // Start accepting user input
				}
				quit = true;
				break;
			case 2: // Settings
				DisplayElements.displaySettingsMenu(); // Print settings menu
				Settings.run(); // Run settings subroutine
				break;
			default: // Should not be reachable
				quit = true;
				break;
			}
		}
	}
	
	//=== Private Methods ===
	
	// Spin up a new elevator/scheduler pair and return the scheduler for routing
	private static Scheduler runElevator(String elevatorName, int defaultFloor, int stopFloorWait, int passFloorWait, int sameFloorWait)
	{
		Scheduler scheduler = new Scheduler(defaultFloor, elevatorName);
		Elevator elevator = new Elevator(elevatorName, scheduler, defaultFloor, stopFloorWait, passFloorWait, sameFloorWait);
		elevator.run();
		return scheduler;
	}
	
	// Get valid main menu selection from user
	private static int mainMenuInputLoop()
	{
		boolean selectionValid = false;
		
		while(!selectionValid)
		{
			String mainMenuSelection = CommandLineInput.getCommandLineInput().toLowerCase();
			
			switch (mainMenuSelection) {
			case "1":
			case "start":
				selectionValid = true;
				return 1;
			case "2":
			case "settings":
				selectionValid = true;
				return 2;
			default:
				System.out.println("Selection not recognized. Try again:\n");
				break;
			}
		}
		return 0; // Shouldn't be reachable
	}
	
	// Read pre-seed file and parse out the requests
	private static List<List<Integer>> readRequestsFromFile(String preSeedFilePath)
	{
		List<List<Integer>> requests = new ArrayList<>(); // Data structure to be returned
		String request = ""; // Holds raw text from file
		String[] floors; // Holding variable for parsing
		try 
		{
			File preSeedFile = new File(preSeedFilePath);
			Scanner fileReader = new Scanner(preSeedFile);
			while (fileReader.hasNextLine()) 
			{
				request = fileReader.nextLine();
				List<Integer> parsedRequest = new ArrayList<>(); // Will be added as element of returned data structure
				try
				{
					floors = request.split(","); // Expected format: startfloor,endfloor
					parsedRequest.add(Integer.parseInt(floors[0]));
					parsedRequest.add(Integer.parseInt(floors[1]));
					requests.add(parsedRequest);
				}
				catch (Exception e) // If the parse fails for some reason, inform user and move on
				{
					System.out.println("Failed to parse '" + request + "'");
				}
				
			}
			fileReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Failed to find pre-seed file (skipping)");
			// Returning empty list is fine
		}
		return requests;
	}
}
