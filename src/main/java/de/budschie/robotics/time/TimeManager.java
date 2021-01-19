package de.budschie.robotics.time;

public class TimeManager
{
	long ms;
	boolean hasStarted = false;	
	
	public void start()
	{
		hasStarted = true;
		ms = System.currentTimeMillis();
	}
	
	public long getElapsedTime()
	{
		assertValid();
		long currentElapsedTime = System.currentTimeMillis() - ms;
		System.out.println("Fetching current elapsed time: " + currentElapsedTime);
		return currentElapsedTime;
	}
	
	private void assertValid()
	{
		if(!hasStarted)
		{
			start();
		}
	}
}
