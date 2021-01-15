package de.budschie.robotics.behaviours;

import java.util.function.Supplier;

public class CustomBehaviour extends AbstractBehaviour
{	
	private Runnable actionRunnable;
	private Runnable surpressRunnable;
	
	public CustomBehaviour(Runnable actionRunnable, Runnable surpressRunnable, Supplier<Boolean> shouldTakeControlOverrider)
	{
		super(shouldTakeControlOverrider);
		this.actionRunnable = actionRunnable;
		this.surpressRunnable = surpressRunnable;
	}
	
	@Override
	public void action()
	{
		actionRunnable.run();
	}

	@Override
	public void suppress()
	{
		surpressRunnable.run();
	}
}
