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
			Supplier<Boolean> foundTrackLeft, Supplier<Boolean> foundTrackRight, int speed)
	{
		super(correctionLeft, correctionRight, movementController, speed);
		this.foundTrackLeft = foundTrackLeft;
		this.foundTrackRight = foundTrackRight;
	}
	
	public AdvancedFollowTrackBehaviour(Supplier<Optional<Float>> correctionLeft,
			Supplier<Optional<Float>> correctionRight, IAbstractMovementController movementController,
			Supplier<Boolean> foundTrackLeft, Supplier<Boolean> foundTrackRight, int speed, Supplier<Boolean> shouldTakeControllOverrider)
	{
		super(correctionLeft, correctionRight, movementController, speed, shouldTakeControllOverrider);
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
		boolean movementCanceled = false;
		
		if(foundTrackLeft.get())
		{
			FoundTrackEventArgs args = new FoundTrackEventArgs(RelativeDirection.LEFT); 
			foundTrackEvent.fire(args);
			
			movementCanceled |= args.isMovementCanceled();
		}
		
		if(foundTrackRight.get())
		{
			FoundTrackEventArgs args = new FoundTrackEventArgs(RelativeDirection.RIGHT); 
			foundTrackEvent.fire(args);
			
			movementCanceled |= args.isMovementCanceled();
		}
		
		if(!movementCanceled)
			super.action();
	}
}
