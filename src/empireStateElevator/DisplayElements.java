package empireStateElevator;

import java.io.InputStream;

public class DisplayElements {

	public static void displaySplash()
	{
		try {
			InputStream in = DisplayElements.class.getResourceAsStream("/Resources/splash.txt");
			int content;
            while ((content = in.read()) != -1) {
                System.out.print((char)content);
            }
		} catch (Exception e) {
			System.out.println("Error rendering splash screen");
			e.printStackTrace();
		}
	}
	
	public static void displayMainMenu()
	{
		System.out.println("MAIN MENU:");
		System.out.println("\t1: Start");
		System.out.println("\t2: Settings\n");
	}
	
	public static void displaySettingsMenu()
	{
		System.out.println("SETTINGS MENU:");
		System.out.println("\t1: " + Settings.defaultFloorField + "\t\t" + Settings.getSettingValue(Settings.defaultFloorField));
		System.out.println("\t2: " + Settings.numberOfElevatorsField + "\t\t" + Settings.getSettingValue(Settings.numberOfElevatorsField));
		System.out.println("\t3: " + Settings.stopFloorWaitField + "\t\t" + Settings.getSettingValue(Settings.stopFloorWaitField));
		System.out.println("\t4: " + Settings.passFloorWaitField + "\t\t" + Settings.getSettingValue(Settings.passFloorWaitField));
		System.out.println("\t5: " + Settings.sameFloorWaitField + "\t\t" + Settings.getSettingValue(Settings.sameFloorWaitField));
		System.out.println("\t6: " + Settings.usePreSeedFileField + "\t\t" + Settings.getSettingValue(Settings.usePreSeedFileField));
		System.out.println("\t7: " + Settings.preSeedFilePathField + "\t\t" + Settings.getSettingValue(Settings.preSeedFilePathField));
		System.out.println("\t8: " + Settings.topFloorField + "\t\t\t" + Settings.getSettingValue(Settings.topFloorField));
		System.out.println("\t9: " + Settings.bottomFloorField + "\t\t\t" + Settings.getSettingValue(Settings.bottomFloorField));
		System.out.println("\n\tEnter the setting number you wish to update or 'done' to return to main menu:");
	}
}