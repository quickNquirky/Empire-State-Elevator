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
					System.out.println("Enter the desired default floor:");
					fieldName = defaultFloorField;
					value = getIntegerFromUser();
					break;
				case "2":
					System.out.println("Enter the desired number of elevators:");
					fieldName = numberOfElevatorsField;
					value = getIntegerFromUser();
					break;
				case "3":
					System.out.println("Enter the number of seconds to wait when stopping at a floor:");
					fieldName = stopFloorWaitField;
					value = getIntegerFromUser();
					break;
				case "4":
					System.out.println("Enter the number of seconds to wait when passing a floor:");
					fieldName = passFloorWaitField;
					value = getIntegerFromUser();
					break;
				case "5":
					System.out.println("Enter the number of seconds to wait when stopped:");
					fieldName = sameFloorWaitField;
					value = getIntegerFromUser();
					break;
				case "6":
					System.out.println("Use pre seed File? (true or false):");
					fieldName = usePreSeedFileField;
					value = getBooleanFromUser();
					break;
				case "7":
					System.out.println("Enter the file path of the desired pre seed file:");
					fieldName = preSeedFilePathField;
					value = CommandLineInput.getCommandLineInput();
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
			writeSettings();
		}
	}
	
	public static String getSettingValue(String key)
	{
		return settings.getProperty(key);
	}
	
	//=== Private Methods ===
	
	private static String getIntegerFromUser()
	{
		String input = "";
		boolean isValid = false;
		while(!isValid)
		{
			input = CommandLineInput.getCommandLineInput();
			try 
			{
				Integer.parseInt(input);
				isValid = true;
			} 
			catch (Exception e) {
				System.out.println("Invalid input (must be integer). Try again:");
			}
		}
		return input;
	}
	
	private static String getBooleanFromUser()
	{
		String input = "";
		boolean isValid = false;
		while(!isValid)
		{
			input = CommandLineInput.getCommandLineInput();
			try 
			{
				Boolean.parseBoolean(input);
				isValid = true;
			} 
			catch (Exception e) {
				System.out.println("Invalid input (must be integer). Try again:");
			}
		}
		return input;
	}
	
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