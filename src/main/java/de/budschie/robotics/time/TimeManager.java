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
		return System.currentTimeMillis() - ms;
	}
	
	private void assertValid()
	{
		if(!hasStarted)
			throw new IllegalStateException("The time manager hasn't started yet.");
	}
}
