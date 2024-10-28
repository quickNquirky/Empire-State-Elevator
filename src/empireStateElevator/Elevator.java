package empireStateElevator;

import java.awt.*;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Elevator implements Runnable {
	
	private final String elevatorName; // Used for naming GUI
	private final Scheduler scheduler; // Instance of Scheduler associated with elevator
	private final long stopFloorWait; // Time to wait at a floor when stopping
	private final long passFloorWait; // Time to wait at a floor when not stopping (passing)
	private final long sameFloorWait; // Time to wait when told to go to currentFloor
	private int currentFloor;
	private Direction currentDirection;
	
	public enum Direction
	{
		UP,
		DOWN,
		STOPPED
	}
	
	public Elevator(String elevatorName, Scheduler scheduler, int defaultFloor, long stopFloorWait, long passFloorWait, long sameFloorWait) 
	{
		this.elevatorName = elevatorName;
		this.scheduler = scheduler;
		this.stopFloorWait = stopFloorWait;
		this.passFloorWait = passFloorWait;
		this.sameFloorWait = sameFloorWait;
		
		currentFloor = defaultFloor; // Start at default floor
		currentDirection = Direction.STOPPED; // Stopped by default
	}
	
	public void run()
	{
		// Spin up GUI to display elevator's progress
		SwingUtilities.invokeLater(() -> {
			JFrame elevatorGui = new JFrame(elevatorName);
			elevatorGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Kill thread on close
			elevatorGui.setSize(300, 600);

            JTextArea elevatorLog = new JTextArea();
            elevatorLog.setEditable(false); // Not for user input, just display
            JScrollPane scrollLog = new JScrollPane(elevatorLog);
            scrollLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            elevatorGui.getContentPane().add(scrollLog, BorderLayout.CENTER); // Add scroll log to Window
            elevatorGui.setVisible(true); // Show window
            
            // Spin up new thread to run elevator simulation and update GUI
            new Thread(() -> {
            	elevatorLog.append("Starting at home floor: " + currentFloor + "\n");
            	while(true) // Elevator continuously runs
				{
					try {
						int nextFloor = scheduler.getNextFloor();
					
						if(currentFloor == nextFloor) // Hang out
						{
							TimeUnit.SECONDS.sleep(sameFloorWait);
							currentDirection = Direction.STOPPED;
						}
						else if(currentFloor > nextFloor) // Go down
						{
							// If we're changing directions, display change
							if(currentDirection != Direction.DOWN)
							{
								elevatorLog.append("Going Down!\n");
								// Ensure we scroll with new text
								elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
								currentDirection = Direction.DOWN;
							}
							while(currentFloor > nextFloor) // Go to requested floor
							{
								currentFloor--;
								if(currentFloor != 0 && currentFloor != 13) // Floors 0 and 13 don't exist
								{
									elevatorLog.append(String.valueOf(currentFloor) + "\n");
									// Ensure we scroll with new text
									elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
									if(currentFloor == nextFloor) // Arrived
									{
										elevatorLog.append("Stopping\n");
										// Ensure we scroll with new text
										elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
										TimeUnit.SECONDS.sleep(stopFloorWait);
									}
									else // Skip floor
									{
										TimeUnit.SECONDS.sleep(passFloorWait);
									}
								}
							}
						}
						else // Current floor is less than next floor (go up)
						{
							// If we're changing directions, display change
							if(currentDirection != Direction.UP)
							{
								elevatorLog.append("Going Up!\n");
								// Ensure we scroll with new text
								elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
								currentDirection = Direction.UP;
							}
							while(currentFloor < nextFloor) // Go to requested floor
							{
								currentFloor++;
								if(currentFloor != 0 && currentFloor != 13) // Floors 0 and 13 don't exist
								{
									elevatorLog.append(String.valueOf(currentFloor) + "\n");
									// Ensure we scroll with new text
									elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
									if(currentFloor == nextFloor) // Arrived
									{
										elevatorLog.append("Stopping\n");
										TimeUnit.SECONDS.sleep(stopFloorWait);
									}
									else // Skip floor
									{
										TimeUnit.SECONDS.sleep(passFloorWait);
									}
								}
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
            }).start();
        });
	}
}
