package de.budschie.robotics.behaviours;

import java.util.function.Supplier;

import lejos.utility.Delay;

public class IdleBehaviour extends AbstractBehaviour
{
	long idleTime;
	
	public IdleBehaviour(Supplier<Boolean> shouldTakeControlOverrider)
	{
		super(shouldTakeControlOverrider);
		this.idleTime = 10;
	}
	
	public IdleBehaviour(long idleTime, Supplier<Boolean> shouldTakeControlOverrider)
	{
		this.idleTime = idleTime;
	}
	
	@Override
	public void action()
	{
		Delay.msDelay(idleTime);
	}

	@Override
	public void suppress()
	{
		
	}
}
