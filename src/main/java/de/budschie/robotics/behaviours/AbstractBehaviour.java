package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Supplier;

import lejos.robotics.subsumption.Behavior;

public abstract class AbstractBehaviour implements Behavior
{
	// Pretty elegant name lol
	protected boolean disabled;
	protected Optional<Supplier<Boolean>> shouldTakeControlOverrider;
	
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
	
	@Override
	public boolean takeControl()
	{
		boolean takeControl = overridableTakeControl();
		if(shouldTakeControlOverrider.isPresent())
			takeControl |= shouldTakeControlOverrider.get().get();
		return !disabled && takeControl;
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
}
