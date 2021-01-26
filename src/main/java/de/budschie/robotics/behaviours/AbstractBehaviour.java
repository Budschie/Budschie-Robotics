package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Supplier;

import de.budschie.robotics.event_handling.ControlEvent;
import de.budschie.robotics.event_handling.Void;
import lejos.robotics.subsumption.Behavior;

public abstract class AbstractBehaviour implements Behavior, Runnable
{
	// Pretty elegant name lol
	protected boolean disabled;
	private boolean lastGainedControl = false;
	protected Optional<Supplier<Boolean>> shouldTakeControlOverrider;
	protected ControlEvent gainedControlEvent = new ControlEvent(), lostControlEvent = new ControlEvent();
	
	public AbstractBehaviour(Supplier<Boolean> shouldTakeControlOverrider)
	{
		this.shouldTakeControlOverrider = Optional.of(shouldTakeControlOverrider);
	}
	
	public AbstractBehaviour()
	{
		this.shouldTakeControlOverrider = Optional.empty();
	}
	
	public void setShouldTakeControlOverrider(Optional<Supplier<Boolean>> shouldTakeControlOverrider)
	{
		this.shouldTakeControlOverrider = shouldTakeControlOverrider;
	}
	
	public ControlEvent getGainedControlEvent()
	{
		return gainedControlEvent;
	}
	
	public ControlEvent getLostControlEvent()
	{
		return lostControlEvent;
	}
	
	@Override
	public boolean takeControl()
	{
		boolean takeControl = overridableTakeControl();
		if(shouldTakeControlOverrider.isPresent())
			takeControl |= shouldTakeControlOverrider.get().get();
		boolean gainControlThisTime = !disabled && takeControl;
		
		// Fire events if necessary
		if(gainControlThisTime && !lastGainedControl)
		{
			onGainedControl();
			lastGainedControl = true;
		}
		else if(!gainControlThisTime && lastGainedControl)
		{
			onLostControl();
			lastGainedControl = false;
		}
		
		return gainControlThisTime;
	}
	
	public boolean overridableTakeControl()
	{
		return false;
	}
	
	public boolean isDisabled()
	{
		return disabled;
	}
	
	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}
	
	public void onGainedControl()
	{
		gainedControlEvent.fire(new Void());
	}
	
	public void onLostControl()
	{
		lostControlEvent.fire(new Void());
	}
	
	@Override
	public void run()
	{
		action();
	}
}
