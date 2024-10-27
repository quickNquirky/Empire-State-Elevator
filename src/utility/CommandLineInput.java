package utility;

import java.util.Scanner;

public class CommandLineInput {
	
	private static Scanner userInputScanner = new Scanner(System.in);

	public static String getCommandLineInput()
	{
		String menuSelection = userInputScanner.nextLine();
		//TODO: sanitize input
		return menuSelection;
	}
	
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
				System.out.println("Invalid input (must be integer). Try again:");
			}
		}
		return input;
	}
}
