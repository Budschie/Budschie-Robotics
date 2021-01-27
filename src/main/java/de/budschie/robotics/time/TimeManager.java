package de.budschie.robotics.time;

public class TimeManager
{
	long ms;
	boolean hasStarted = false;	
	
	public void start()
	{
		// System.out.println("Starting time manager " + this);
		hasStarted = true;
		ms = System.currentTimeMillis();
	}
	
	public long getElapsedTime()
	{
		assertValid();
		long currentElapsedTime = System.currentTimeMillis() - ms;
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
