package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.budschie.robotics.event_handling.TrackEvent;
import de.budschie.robotics.event_handling.FoundTrackEventArgs;
import lejos.robotics.subsumption.Behavior;

public class AdvancedFollowTrackBehaviour extends FollowTrackBehaviour
{
	private boolean foundLeftTrackLastFrame = false;
	private boolean foundRightTrackLastFrame = false;
	private TrackEvent foundTrackEvent = new TrackEvent(), lostTrackEvent = new TrackEvent(), updateTrackEvent = new TrackEvent();
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
	
	public TrackEvent getFoundTrackEvent()
	{
		return foundTrackEvent;
	}
	
	public TrackEvent getLostTrackEvent()
	{
		return lostTrackEvent;
	}
	
	public TrackEvent getUpdateTrackEvent()
	{
		return updateTrackEvent;
	}
	
	public boolean hasFoundTrackLeft()
	{
		return foundTrackLeft.get();
	}
	
	public boolean hasFoundTrackRight()
	{
		return foundTrackRight.get();
	}
	
	@Override
	public void action()
	{		
		boolean movementCanceled = false;
		
		boolean hasFoundTrackLeft = hasFoundTrackLeft(), hasFoundTrackRight = hasFoundTrackRight();
		
		if(hasFoundTrackLeft || foundLeftTrackLastFrame)
		{
			FoundTrackEventArgs args = new FoundTrackEventArgs(RelativeDirection.LEFT); 
			
			if(hasFoundTrackLeft && foundLeftTrackLastFrame)
				updateTrackEvent.fire(args);
			else if(!hasFoundTrackLeft && foundLeftTrackLastFrame)
				lostTrackEvent.fire(args);
			else if(hasFoundTrackLeft && !foundLeftTrackLastFrame)
				foundTrackEvent.fire(args);
			
			movementCanceled |= args.isMovementCanceled();
			foundLeftTrackLastFrame = hasFoundTrackLeft;
		}

		
		if(hasFoundTrackRight || foundRightTrackLastFrame)
		{
			FoundTrackEventArgs args = new FoundTrackEventArgs(RelativeDirection.RIGHT); 

			if(hasFoundTrackRight && foundRightTrackLastFrame)
				updateTrackEvent.fire(args);
			else if(!hasFoundTrackRight && foundRightTrackLastFrame)
				lostTrackEvent.fire(args);
			else if(hasFoundTrackRight && !foundRightTrackLastFrame)
				foundTrackEvent.fire(args);
			
			movementCanceled |= args.isMovementCanceled();
			foundRightTrackLastFrame = hasFoundTrackRight;
		}
		
		if(!movementCanceled)
			super.action();
	}
}
