package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.budschie.robotics.event_handling.FoundTrackEvent;
import de.budschie.robotics.event_handling.FoundTrackEventArgs;
import lejos.robotics.subsumption.Behavior;

public class AdvancedFollowTrackBehaviour extends FollowTrackBehaviour
{
	private FoundTrackEvent foundTrackEvent = new FoundTrackEvent();
	private Supplier<Boolean> foundTrackLeft, foundTrackRight;
	
	public AdvancedFollowTrackBehaviour(Supplier<Optional<Float>> correctionLeft,
			Supplier<Optional<Float>> correctionRight, IAbstractMovementController movementController,
			Supplier<Boolean> foundTrackLeft, Supplier<Boolean> foundTrackRight)
	{
		super(correctionLeft, correctionRight, movementController);
		this.foundTrackLeft = foundTrackLeft;
		this.foundTrackRight = foundTrackRight;
	}
	
	public AdvancedFollowTrackBehaviour(Supplier<Optional<Float>> correctionLeft,
			Supplier<Optional<Float>> correctionRight, IAbstractMovementController movementController,
			Supplier<Boolean> foundTrackLeft, Supplier<Boolean> foundTrackRight, Supplier<Boolean> shouldTakeControllOverrider)
	{
		super(correctionLeft, correctionRight, movementController, shouldTakeControllOverrider);
		this.foundTrackLeft = foundTrackLeft;
		this.foundTrackRight = foundTrackRight;
	}
	
	public FoundTrackEvent getFoundTrackEvent()
	{
		return foundTrackEvent;
	}
	
	@Override
	public void action()
	{
		super.action();
		
		if(foundTrackLeft.get())
		{
			foundTrackEvent.fire(new FoundTrackEventArgs(RelativeDirection.LEFT));
		}
		
		if(foundTrackRight.get())
		{
			foundTrackEvent.fire(new FoundTrackEventArgs(RelativeDirection.RIGHT));
		}
	}
}
