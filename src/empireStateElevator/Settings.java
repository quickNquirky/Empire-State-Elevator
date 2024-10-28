package empireStateElevator;

import java.util.Properties;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import utility.CommandLineInput;

public class Settings {

	private static Properties settings;
	private static final Properties defaultSettings = new Properties();
	private static final String settingsFilePath = "app_settings.properties";
	private static final File settingsFile = new File(settingsFilePath);
	
	public static final String defaultFloorField = "Default_Floor";
	public static final String numberOfElevatorsField = "Number_Of_Elevators";
	public static final String stopFloorWaitField = "Stop_Floor_Wait";
	public static final String passFloorWaitField = "Pass_Floor_Wait";
	public static final String sameFloorWaitField = "Same_Floor_Wait";
	public static final String usePreSeedFileField = "Use_Pre_Seed_File";
	public static final String preSeedFilePathField = "Pre_Seed_File_Path";
	public static final String topFloorField = "Top_Floor";
	public static final String bottomFloorField = "Bottom_Floor";

	// Run the settings subroutine
	public static void run() throws Exception {
		boolean quit = false;
		
		while(!quit)
		{
			String settingSelection = CommandLineInput.getCommandLineInput();
			if(!settingSelection.toLowerCase().equals("done"))
			{
				String fieldName = "";
				String value = "";
				switch (settingSelection)
				{
				case "1":
					System.out.println("\nEnter the desired default floor:");
					fieldName = defaultFloorField;
					value = CommandLineInput.getIntegerFromUser();
					break;
				case "2":
					System.out.println("\nEnter the desired number of elevators:");
					fieldName = numberOfElevatorsField;
					value = CommandLineInput.getIntegerFromUser();
					break;
				case "3":
					System.out.println("\nEnter the number of seconds to wait when stopping at a floor:");
					fieldName = stopFloorWaitField;
					value = CommandLineInput.getIntegerFromUser();
					break;
				case "4":
					System.out.println("\nEnter the number of seconds to wait when passing a floor:");
					fieldName = passFloorWaitField;
					value = CommandLineInput.getIntegerFromUser();
					break;
				case "5":
					System.out.println("\nEnter the number of seconds to wait when stopped:");
					fieldName = sameFloorWaitField;
					value = CommandLineInput.getIntegerFromUser();
					break;
				case "6":
					System.out.println("\nUse pre seed File? (true or false):");
					fieldName = usePreSeedFileField;
					value = CommandLineInput.getBooleanFromUser();
					break;
				case "7":
					System.out.println("\nEnter the file path of the desired pre seed file:");
					fieldName = preSeedFilePathField;
					value = CommandLineInput.getCommandLineInput();
					break;
				case "8":
					System.out.println("\nEnter the top floor:");
					fieldName = topFloorField;
					value = CommandLineInput.getIntegerFromUser();
					break;
				case "9":
					System.out.println("\nEnter the bottom floor:");
					fieldName = bottomFloorField;
					value = CommandLineInput.getIntegerFromUser();
					break;
				default:
					fieldName = "Invalid";
					break;
				}
				
				if(!fieldName.equals("Invalid"))
				{
					updateSetting(fieldName, value);
					DisplayElements.displaySettingsMenu();
				}
				else
				{
					System.out.println("'" + settingSelection + "' is invalid. Try again.");
				}
			}
			else
			{
				quit = true;
			}
		}
	}
	
	public static void startup() throws Exception {
		defaultSettings.setProperty(defaultFloorField, "1");
		defaultSettings.setProperty(numberOfElevatorsField, "1");
		defaultSettings.setProperty(stopFloorWaitField, "3");
		defaultSettings.setProperty(passFloorWaitField, "1");
		defaultSettings.setProperty(sameFloorWaitField, "2");
		defaultSettings.setProperty(usePreSeedFileField, "false");
		defaultSettings.setProperty(preSeedFilePathField, "preSeed.txt");
		defaultSettings.setProperty(topFloorField, "102");
		defaultSettings.setProperty(bottomFloorField, "-2");
		settings = new Properties(defaultSettings);
		
		if (settingsFile.exists()) {
			readSettings();
		} else {
			settings.setProperty(defaultFloorField, "1");
			settings.setProperty(numberOfElevatorsField, "1");
			settings.setProperty(stopFloorWaitField, "3");
			settings.setProperty(passFloorWaitField, "1");
			settings.setProperty(sameFloorWaitField, "2");
			settings.setProperty(usePreSeedFileField, "false");
			settings.setProperty(preSeedFilePathField, "preSeed.txt");
			defaultSettings.setProperty(topFloorField, "102");
			defaultSettings.setProperty(bottomFloorField, "-2");
			writeSettings();
		}
	}
	
	public static String getSettingValue(String key)
	{
		return settings.getProperty(key);
	}
	
	//=== Private Methods ===
	
	private static void updateSetting(String fieldName, String value) throws Exception
	{
		if(!getSettingValue(fieldName).equals(value))
		{
			settings.setProperty(fieldName, value);
			writeSettings();
		}
	}
	
	private static void readSettings() throws Exception {
		settings.load(new FileReader(settingsFile));
	}
	
	private static void writeSettings() throws Exception {
		settings.store(new FileWriter(settingsFilePath), "Empire State Elevator Settings");
	}
}