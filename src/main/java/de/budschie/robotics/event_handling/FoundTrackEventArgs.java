package de.budschie.robotics.event_handling;

import de.budschie.robotics.behaviours.RelativeDirection;

public class FoundTrackEventArgs
{
	RelativeDirection trackDirection;
	
	public FoundTrackEventArgs(RelativeDirection trackDirection)
	{
		this.trackDirection = trackDirection;
	}
}
