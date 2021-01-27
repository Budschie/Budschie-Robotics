package de.budschie.robotics.profiling;

import de.budschie.robotics.time.TimeManager;

public class Profiler
{
	private static TimeManager timeManager = new TimeManager();
	private static int refresh = 0;
	
	public static void start()
	{
		timeManager.start();
		System.out.println("Started profiler.");
	}
	
	public static void addRefresh()
	{
		refresh++;
	}
	
	public static void stop()
	{
		long elapsedTime = timeManager.getElapsedTime();
		System.out.println("We have " + (((double)(refresh))/(elapsedTime/1000d)) + " elapses per second on average.");
	}
}
