package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.budschie.robotics.event_handling.TrackEvent;
import de.budschie.robotics.event_handling.TrackEventArgs;
import lejos.robotics.subsumption.Behavior;

public class AdvancedFollowTrackBehaviour extends FollowTrackBehaviour
{
	private boolean isPassive = false;
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
	
	public void setPassive(boolean isPassive)
	{
		this.isPassive = isPassive;
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
	
	protected void onUpdateTrack(TrackEventArgs args)
	{
		updateTrackEvent.fire(args);
	}
	
	protected void onFoundTrack(TrackEventArgs args)
	{
		foundTrackEvent.fire(args);
	}
	
	protected void onLostTrack(TrackEventArgs args)
	{
		lostTrackEvent.fire(args);
	}
	
	@Override
	public void action()
	{		
		boolean movementCanceled = false;
		
		boolean hasFoundTrackLeft = hasFoundTrackLeft(), hasFoundTrackRight = hasFoundTrackRight();
		
		if(hasFoundTrackLeft || foundLeftTrackLastFrame)
		{
			TrackEventArgs args = new TrackEventArgs(RelativeDirection.LEFT); 
			
			if(hasFoundTrackLeft && foundLeftTrackLastFrame)
				onUpdateTrack(args);
			else if(!hasFoundTrackLeft && foundLeftTrackLastFrame)
				onLostTrack(args);
			else if(hasFoundTrackLeft && !foundLeftTrackLastFrame)
				onFoundTrack(args);
			
			movementCanceled |= args.isMovementCanceled();
			foundLeftTrackLastFrame = hasFoundTrackLeft;
		}

		
		if(hasFoundTrackRight || foundRightTrackLastFrame)
		{
			TrackEventArgs args = new TrackEventArgs(RelativeDirection.RIGHT); 

			if(hasFoundTrackRight && foundRightTrackLastFrame)
				onUpdateTrack(args);
			else if(!hasFoundTrackRight && foundRightTrackLastFrame)
				onLostTrack(args);
			else if(hasFoundTrackRight && !foundRightTrackLastFrame)
				onFoundTrack(args);
			
			movementCanceled |= args.isMovementCanceled();
			foundRightTrackLastFrame = hasFoundTrackRight;
		}
		
		if(!movementCanceled && !isPassive)
			super.action();
	}
}
