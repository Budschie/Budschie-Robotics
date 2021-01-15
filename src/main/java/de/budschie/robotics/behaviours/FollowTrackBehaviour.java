package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lejos.robotics.subsumption.Behavior;

public class FollowTrackBehaviour extends AbstractBehaviour
{
	Supplier<Optional<Float>> leftCorrection, rightCorrection;
	IAbstractMovementController movementController;
	
	public FollowTrackBehaviour(Supplier<Optional<Float>> leftCorrection, Supplier<Optional<Float>> rightCorrection, IAbstractMovementController movementController)
	{
		this.movementController = movementController;
		this.leftCorrection = leftCorrection;
		this.rightCorrection = rightCorrection;
	}
	
	public FollowTrackBehaviour(Supplier<Optional<Float>> leftCorrection, Supplier<Optional<Float>> rightCorrection, IAbstractMovementController movementController, Supplier<Boolean> shouldTakeControlOverrider)
	{
		super(shouldTakeControlOverrider);
		this.movementController = movementController;
		this.leftCorrection = leftCorrection;
		this.rightCorrection = rightCorrection;
	}

	@Override
	public void action()
	{
		Optional<Float> leftCorrectionCalculated = leftCorrection.get();
		Optional<Float> rightCorrectionCalculated = rightCorrection.get();
		
		System.out.println("Executing track following...");
		
		// We aren't right enough
		if(leftCorrectionCalculated.isPresent())
		{
			// Go right
			movementController.turnRight(leftCorrectionCalculated.get());
			System.out.printf("We're not right enough by %d.%n", leftCorrectionCalculated.get());
		}
		// We aren't left enough
		else if(rightCorrectionCalculated.isPresent())
		{
			// Go left
			movementController.turnLeft(rightCorrectionCalculated.get());
			System.out.printf("We're not left enough by %d.%n", leftCorrectionCalculated.get());
		}
		else
			movementController.turnLeft(0);
	}
	
	/** Ignoring the setting of a flag because action should exit pretty quickly. 
	 * NOTE: Im pretty dumb as I think I may have misunderstood this method. **/
	@Override
	public void suppress()
	{
		System.out.println("SUPPRESSING");
	}
}
