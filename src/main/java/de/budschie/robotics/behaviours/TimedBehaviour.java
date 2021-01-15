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
		return timePredicate.test(timeManager) && behaviour.takeControl();
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
