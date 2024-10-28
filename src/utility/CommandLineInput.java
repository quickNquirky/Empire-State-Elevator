package utility;

import java.util.Scanner;

public class CommandLineInput {
	
	private static Scanner userInputScanner = new Scanner(System.in);

	// Get an un-sanitized string from user
	public static String getCommandLineInput()
	{
		String menuSelection = userInputScanner.nextLine();
		return menuSelection;
	}
	
	// Get a integer from user (still in string format)
	public static String getIntegerFromUser()
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
	
	// Get a boolean from user (still in string format)
	public static String getBooleanFromUser()
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
				System.out.println("Invalid input (must be 'true' or 'false'). Try again:");
			}
		}
		return input;
	}
}
