package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

// OK?
public class ImplementedAdvancedFollowTrackBehaviour extends AdvancedFollowTrackBehaviour
{
	//TODO: Make this safe to set the path
	
	/** This indicates whether we use the right sensor to look after new tracks({@link RelativeDirection.RIGHT} or the left sensor({@link RelativeDirection.LEFT}. **/
	private RelativeDirection currentTrackDetection;
	private boolean transitioning;
	private Predicate<Integer> isBlack;
	private Supplier<Integer> valueLeft, valueRight;
	private float correctionStrength;
	
	public ImplementedAdvancedFollowTrackBehaviour(Predicate<Integer> isBlack, IAbstractMovementController movementController,
			Supplier<Integer> valueLeft, Supplier<Integer> valueRight, int speed, RelativeDirection currentTrackDetection, float correctionStrength,
			Supplier<Boolean> shouldTakeControllOverrider)
	{
		// We can internally use null here, as we can override these methods below
		super(null, null, movementController, null, null, speed, shouldTakeControllOverrider);
		this.currentTrackDetection = currentTrackDetection;
		this.valueLeft = valueLeft;
		this.valueRight = valueRight;
		this.correctionStrength = correctionStrength;
		this.isBlack = isBlack;
	}
	
	@Override
	public Optional<Float> getLeftCorrection()
	{
		// A bit unituitive, but this is the right way to do it, as RelativeDirection.RIGHT means that we have to read the left value
		if(currentTrackDetection == RelativeDirection.LEFT)
		{
			// lol
			boolean isTestedAsBlack = isBlack.test(valueRight.get());
			
			if(isTestedAsBlack)
				return Optional.of(correctionStrength);
		}
		// We are doing this because it is much more clear
		else if(currentTrackDetection == RelativeDirection.RIGHT)
		{
			// Tbh this code looks very similar to the code above, we should maybe avoid this
			boolean isTestedAsBlack = isBlack.test(valueLeft.get());
			
			if(!isTestedAsBlack)
				return Optional.of(correctionStrength);
		}
		
		return Optional.empty();
	}
	
	@Override
	public Optional<Float> getRightCorrection()
	{
		// Before you wonder WTF is going on here, please take a moment to grasp whats going on (essentially what's going on is that I am to lazy to copy above code, so I just make it worse
		// Edit nvm I'm going to copy the code from above as it is more clear to what is going on (plz forgive me)
		// Another edit hm fuck it I'm not going to copy the code
		// Alright this took a long time (1min) to get here, but what we are essentially doing is we "invert" the left correction value(empty gets correctionStrength and correctionStrength gets empty)
		
		// Btw this can be optimized, but then the code would look ugly etc.
		Optional<Float> leftCorrection = getLeftCorrection();
		
		return leftCorrection.isPresent() ? Optional.empty() : Optional.of(correctionStrength);
	}
	
	@Override
	public boolean hasFoundTrackLeft()
	{
		return currentTrackDetection == RelativeDirection.LEFT && isBlack.test(valueLeft.get());
	}
	
	@Override
	public boolean hasFoundTrackRight()
	{
		return currentTrackDetection == RelativeDirection.RIGHT && isBlack.test(valueRight.get());
	}
}
