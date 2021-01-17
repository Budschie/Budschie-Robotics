package de.budschie.robotics.event_handling;

import de.budschie.robotics.behaviours.RelativeDirection;

public class FoundTrackEventArgs
{
	boolean cancelMovement;
	RelativeDirection trackDirection;
	
	public FoundTrackEventArgs(RelativeDirection trackDirection)
	{
		this.trackDirection = trackDirection;
	}
	
	public void cancelMovement()
	{
		cancelMovement = true;
	}
	
	public boolean isMovementCanceled()
	{
		return cancelMovement;
	}
}
