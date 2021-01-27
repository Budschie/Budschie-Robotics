package de.budschie.robotics.behaviours;

import java.util.function.Predicate;

import de.budschie.robotics.time.TimeManager;
import lejos.robotics.subsumption.Behavior;

public class TimedBehaviour implements Behavior
{
	Behavior behaviour;
	TimeManager timeManager;
	Predicate<TimeManager> timePredicate;
	
	private TimedBehaviour(Behavior behaviour, TimeManager timeManager, Predicate<TimeManager> timePredicate)
	{
		this.timeManager = timeManager;
		this.behaviour = behaviour;
		this.timePredicate = timePredicate;
	}
	
	@Override
	public boolean takeControl()
	{
		boolean timePredicate = this.timePredicate.test(timeManager);
		boolean takeControl = behaviour.takeControl();
		
		// System.out.println("TimePredicate is " + timePredicate);
		// System.out.println("TakeControl is " + takeControl);
		
		return timePredicate && takeControl;
	}

	@Override
	public void action()
	{
		behaviour.action();
	}

	@Override
	public void suppress()
	{
		behaviour.suppress();
	}
	
	public static TimedBehaviour of(Behavior behaviour, TimeManager timeManager, long lower, long greater)
	{
		return new TimedBehaviour(behaviour, timeManager, (time) ->
		{
			long elapsedTime = time.getElapsedTime();
			//System.out.println("Currently elapsed time: " + elapsedTime + "; lower " + lower + "; greater " + greater);
			return elapsedTime > lower && elapsedTime < greater;
		});
	}
	
	public static TimedBehaviour ofLower(Behavior behaviour, TimeManager timeManager, long lower)
	{
		return new TimedBehaviour(behaviour, timeManager, (time) ->
		{
			long elapsedTime = time.getElapsedTime();
			return elapsedTime > lower;
		});
	}
	
	public static TimedBehaviour ofGreater(Behavior behaviour, TimeManager timeManager, long greater)
	{
		return new TimedBehaviour(behaviour, timeManager, (time) ->
		{
			long elapsedTime = time.getElapsedTime();
			return elapsedTime < greater;
		});
	}
}
