package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.robotics.subsumption.Behavior;

public class FollowTrackBehaviour extends AbstractBehaviour
{
	Supplier<Optional<Float>> leftCorrection, rightCorrection;
	IAbstractMovementController movementController;
	int speed;
	
	public FollowTrackBehaviour(Supplier<Optional<Float>> leftCorrection, Supplier<Optional<Float>> rightCorrection, IAbstractMovementController movementController, int speed)
	{
		this.movementController = movementController;
		this.leftCorrection = leftCorrection;
		this.rightCorrection = rightCorrection;
		this.speed = speed;
	}
	
	public FollowTrackBehaviour(Supplier<Optional<Float>> leftCorrection, Supplier<Optional<Float>> rightCorrection, IAbstractMovementController movementController, int speed, Supplier<Boolean> shouldTakeControlOverrider)
	{
		super(shouldTakeControlOverrider);
		this.movementController = movementController;
		this.leftCorrection = leftCorrection;
		this.rightCorrection = rightCorrection;
		this.speed = speed;
	}
	
	private boolean turnLeftFlag = false;
	private boolean init = true;

	@Override
	public void action()
	{
		Optional<Float> leftCorrectionCalculated = getLeftCorrection();
		Optional<Float> rightCorrectionCalculated = getRightCorrection();
		
		System.out.println("Executing track following...");
		
		boolean edited = false;
		
		// We aren't right enough
		if(leftCorrectionCalculated.isPresent())
		{
			// Go right
			movementController.turnRight(leftCorrectionCalculated.get());
			System.out.printf("We're not right enough by %f.%n", leftCorrectionCalculated.get());
			edited = true;
			turnLeftFlag = false;
		}
		// We aren't left enough
		else if(rightCorrectionCalculated.isPresent())
		{
			// Go left
			movementController.turnLeft(rightCorrectionCalculated.get());
			System.out.printf("We're not left enough by %f.%n", rightCorrectionCalculated.get());
			edited = true;
			turnLeftFlag = false;
		}
		else
		{
			if(!turnLeftFlag)
			{
				movementController.turnLeft(0);
				edited = true;
				turnLeftFlag = true;
			}
		}
		
		if(edited)
		{
			movementController.setSpeed(speed);
			movementController.updateMotorState();
		}
	}
	
	public Optional<Float> getLeftCorrection()
	{
		return leftCorrection.get();
	}
	
	public Optional<Float> getRightCorrection()
	{
		return rightCorrection.get();
	}
	
	/** Ignoring the setting of a flag because action should exit pretty quickly. 
	 * NOTE: Im pretty dumb as I think I may have misunderstood this method. **/
	@Override
	public void suppress()
	{
		System.out.println("SUPPRESSING");
	}
}
