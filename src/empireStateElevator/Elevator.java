package empireStateElevator;

public class Elevator {
	
	private int currentFloor;
	
	public Elevator() 
	{
		currentFloor = 1;
	}

	public enum Direction {
		UP,
		DOWN
	}
	
	public Direction getCurrentDirection()
	{
		return Direction.UP;
	}
	
	public int getCurrentFloor()
	{
		return currentFloor;
	}
}
