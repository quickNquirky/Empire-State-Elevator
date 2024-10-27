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
}
