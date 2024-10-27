package empireStateElevator;


import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Elevator implements Runnable {
	
	private final String elevatorName;
	private final Scheduler scheduler;
	private final long stopFloorWait;
	private final long passFloorWait;
	private final long sameFloorWait;
	private int currentFloor;
	private Direction currentDirection;
	
	public Elevator(String elevatorName, Scheduler scheduler, int defaultFloor, long stopFloorWait, long passFloorWait, long sameFloorWait) 
	{
		this.elevatorName = elevatorName;
		this.scheduler = scheduler;
		this.stopFloorWait = stopFloorWait;
		this.passFloorWait = passFloorWait;
		this.sameFloorWait = sameFloorWait;
		
		currentFloor = defaultFloor;
		currentDirection = Direction.STOPPED;
	}

	public enum Direction {
		UP,
		DOWN,
		STOPPED
	}
	
	public void run()
	{
		// For the sake of time, 
		SwingUtilities.invokeLater(() -> {
			JFrame elevatorGui = new JFrame(elevatorName);
			elevatorGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			elevatorGui.setSize(300, 600);

            JTextArea elevatorLog = new JTextArea();
            elevatorLog.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(elevatorLog);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            elevatorGui.getContentPane().add(scrollPane, BorderLayout.CENTER);
            elevatorGui.setVisible(true);
            
            new Thread(() -> {
            	elevatorLog.append("Starting at home floor: " + currentFloor + "\n");
            	while(true)
				{
					try {
						int nextFloor = scheduler.getNextFloor();
					
						if(currentFloor == nextFloor)
						{
							TimeUnit.SECONDS.sleep(sameFloorWait);
							currentDirection = Direction.STOPPED;
						}
						else if(currentFloor > nextFloor)
						{
							if(currentDirection != Direction.DOWN)
							{
								elevatorLog.append("Going Down!\n");
								elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
								currentDirection = Direction.DOWN;
							}
							while(currentFloor > nextFloor)
							{
								currentFloor--;
								elevatorLog.append(String.valueOf(currentFloor) + "\n");
								elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
								if(currentFloor == nextFloor)
								{
									elevatorLog.append("Stopping\n");
									elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
									TimeUnit.SECONDS.sleep(stopFloorWait);
								}
								else
								{
									TimeUnit.SECONDS.sleep(passFloorWait);
								}
							}
						}
						else // Current floor is less than next floor
						{
							if(currentDirection != Direction.UP)
							{
								elevatorLog.append("Going Up!\n");
								elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
								currentDirection = Direction.UP;
							}
							while(currentFloor < nextFloor)
							{
								currentFloor++;
								elevatorLog.append(String.valueOf(currentFloor) + "\n");
								elevatorLog.setCaretPosition(elevatorLog.getDocument().getLength());
								if(currentFloor == nextFloor)
								{
									elevatorLog.append("Stopping\n");
									TimeUnit.SECONDS.sleep(stopFloorWait);
								}
								else
								{
									TimeUnit.SECONDS.sleep(passFloorWait);
								}
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
            }).start();
        });
	}
}
