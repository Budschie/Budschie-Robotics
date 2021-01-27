package de.budschie.robotics.navigator;

import java.util.ArrayList;
import java.util.function.Consumer;

import de.budschie.robotics.behaviours.AdvancedFollowTrackBehaviour;
import de.budschie.robotics.event_handling.TrackEventArgs;

public class TrackGuard
{
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
		internalTrackCounter++;
		
		if(!trackList.isEmpty() && trackList.get(0).tracksDetected <= internalTrackCounter)
		{
			trackList.get(0).consumer.accept(args);
			internalTrackCounter = 0;
		}
	}
	
	public static class Builder
	{
		ArrayList<TrackPair> trackList = new ArrayList<>();
		AdvancedFollowTrackBehaviour advancedFollowTrackBehaviour;
		
		/** The given runnable will be called when the given amount of tracks was located. The, the track count will be resetted. **/
		public Builder addTrackExecutor(Consumer<TrackEventArgs> trackExecutor, int trackAmount)
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
		Consumer<TrackEventArgs> consumer;
		int tracksDetected;
	}
}
