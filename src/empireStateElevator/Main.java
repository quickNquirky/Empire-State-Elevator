package empireStateElevator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import utility.CommandLineInput;

public class Main {
	public static void main(String[] args) throws Exception
	{
		DisplayElements.displaySplash(); // Print splash

		// Initialize in settings
		try {
			Settings.startup();
		} 
		catch (Exception e) {
			System.out.println("Could not find/initialize settings storage");
			e.printStackTrace();
		}
		
		boolean quit = false;

		while(!quit)
		{
			DisplayElements.displayMainMenu(); // Print main menu
			int selection = mainMenuInputLoop();
		
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
				List<Scheduler> schedulers = new ArrayList<>();
				//int usePreSeedFileField = Integer.parseInt(Settings.getSettingValue(Settings.usePreSeedFileField));
				//int preSeedFilePath = Integer.parseInt(Settings.getSettingValue(Settings.preSeedFilePathField));
				// TODO: topFloor
				// TODO: bottomFloor
				// TODO: spin up router
				for(int i = 1; i <= numberOfElevators; i++)
				{
					schedulers.add(runElevator("Elevator " + String.valueOf(i), defaultFloor, stopFloorWait, passFloorWait, sameFloorWait));
				}
				RequestRouter router = new RequestRouter(schedulers, topFloor, bottomFloor);
				router.userRequestLoop();
				quit = true;
				break;
			case 2: // Settings
				DisplayElements.displaySettingsMenu(); // Print settings menu
				Settings.run();
				break;
			default: // Should not be reachable
				quit = true;
				break;
			}
		}
		
		// Set up for user input
		// TODO: Check requests are within floor limits
	}
	
	//=== Private Methods ===
	
	private static Scheduler runElevator(String elevatorName, int defaultFloor, int stopFloorWait, int passFloorWait, int sameFloorWait)
	{
		Scheduler scheduler = new Scheduler(defaultFloor, elevatorName);
		Elevator elevator = new Elevator(elevatorName, scheduler, defaultFloor, stopFloorWait, passFloorWait, sameFloorWait);
		elevator.run();
		return scheduler;
	}
	
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
				mainMenuSelection = CommandLineInput.getCommandLineInput().toLowerCase();
				break;
			}
		}
		return 0; // Shouldn't be reachable
	}
}
