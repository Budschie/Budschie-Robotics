package de.budschie.robotics.navigator;

import java.util.ArrayList;
import java.util.function.Consumer;

import de.budschie.robotics.behaviours.AdvancedFollowTrackBehaviour;
import de.budschie.robotics.event_handling.TrackEventArgs;

public class TrackGuard
{
	private boolean halted;
	int internalTrackCounter;
	ArrayList<TrackPair> trackList;
	int previousEventId;
	AdvancedFollowTrackBehaviour advancedFollowTrackBehaviour;
	
	private TrackGuard(AdvancedFollowTrackBehaviour advancedFollowTrackBehaviour, ArrayList<TrackPair> trackList)
	{
		this.previousEventId = advancedFollowTrackBehaviour.getFoundTrackEvent().subscribe(this::onDetectedTrack);
		this.trackList = trackList;
		this.advancedFollowTrackBehaviour = advancedFollowTrackBehaviour;
	}
	
	public void setAdvancedFollowTrackBehaviour(AdvancedFollowTrackBehaviour advancedFollowTrackBehaviour)
	{
		if(this.advancedFollowTrackBehaviour != null && advancedFollowTrackBehaviour != null)
		{
			this.advancedFollowTrackBehaviour.getFoundTrackEvent().unsubscribe(previousEventId);
			this.advancedFollowTrackBehaviour = advancedFollowTrackBehaviour;
			this.previousEventId = advancedFollowTrackBehaviour.getFoundTrackEvent().subscribe(this::onDetectedTrack);
		}
	}
	
	private void onDetectedTrack(TrackEventArgs args)
	{
		// Not feeling about doing something fancy with preIncrement today...
		
		if(!halted)
		{
			internalTrackCounter++;
			
			if(!trackList.isEmpty() && trackList.get(0).tracksDetected <= internalTrackCounter)
			{
				TrackGuardArgs guardArgs = new TrackGuardArgs();
				guardArgs.args = args;
				guardArgs.sender = this;
				
				trackList.get(0).consumer.accept(guardArgs);
				trackList.remove(0);
				internalTrackCounter = 0;
			}
		}
	}
	
	public void setHalted(boolean halted)
	{
		this.halted = halted;
	}
	
	public static class TrackGuardArgs
	{
		private TrackEventArgs args;
		private TrackGuard sender;
		
		private TrackGuardArgs()
		{
			
		}
		
		public TrackEventArgs getArgs()
		{
			return args;
		}
		
		public TrackGuard getSender()
		{
			return sender;
		}
	}
	
	public static class Builder
	{
		ArrayList<TrackPair> trackList = new ArrayList<>();
		AdvancedFollowTrackBehaviour advancedFollowTrackBehaviour;
		
		/** The given runnable will be called when the given amount of tracks was located. The, the track count will be resetted. **/
		public Builder addTrackExecutor(Consumer<TrackGuardArgs> trackExecutor, int trackAmount)
		{
			TrackPair pair = new TrackPair();
			pair.consumer = trackExecutor;
			pair.tracksDetected = trackAmount;
			trackList.add(pair);
			return this;
		}
		
		/** This method sets the used AdvancedFollowTrackBehaviour. It can be changed later when the TrackGuard is built. **/
		public Builder setAdvancedFollowTrackBehaviour(AdvancedFollowTrackBehaviour advancedFollowTrackBehaviour)
		{
			this.advancedFollowTrackBehaviour = advancedFollowTrackBehaviour;
			return this;
		}
		
		/** This method builds the AdvancedFollowTrackBehaviour. **/
		public TrackGuard build()
		{
			return new TrackGuard(advancedFollowTrackBehaviour, trackList);
		}
	}
	
	private static class TrackPair
	{
		Consumer<TrackGuardArgs> consumer;
		int tracksDetected;
	}
}
