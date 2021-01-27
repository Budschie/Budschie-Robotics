package de.budschie.robotics.event_handling;

import de.budschie.robotics.behaviours.RelativeDirection;

public class TrackEventArgs
{
	boolean cancelMovement;
	RelativeDirection trackDirection;
	
	public TrackEventArgs(RelativeDirection trackDirection)
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
	
	public RelativeDirection getTrackDirection()
	{
		return trackDirection;
	}
}
