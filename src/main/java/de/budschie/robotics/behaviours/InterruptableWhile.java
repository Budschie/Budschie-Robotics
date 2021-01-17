package de.budschie.robotics.behaviours;

import java.util.function.Supplier;

public class InterruptableWhile implements Runnable
{
	private boolean interrupted;
	private Supplier<Boolean> whileHeader;
	private Runnable whileRunnable;
	
	public InterruptableWhile(Supplier<Boolean> whileHeader, Runnable whileRunnable)
	{
		this.whileHeader = whileHeader;
		this.whileRunnable = whileRunnable;
	}
	
	@Override
	public void run()
	{
		while(whileHeader.get() && !interrupted)
			whileRunnable.run();
		
		interrupted = false;
	}	
	
	/** Exits from the while loop. **/
	public void interrupt()
	{
		this.interrupted = true;
	}
}
