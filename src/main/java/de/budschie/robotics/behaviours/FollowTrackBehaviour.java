package de.budschie.robotics.behaviours;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.budschie.robotics.profiling.Profiler;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.robotics.subsumption.Behavior;

public class FollowTrackBehaviour extends AbstractBehaviour
{
	Supplier<Optional<Float>> leftCorrection, rightCorrection;
	IAbstractMovementController movementController;
	protected int speed;
	protected RelativeDirection currentDirection = RelativeDirection.FORWARD;
	
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
		
		Optional<Float> bias = getBias();
		
		// Somehow we don't end up removing the others strength
		if(bias.isPresent())
		{
			float biasValue = bias.get();
			
			 //System.out.println("LeftCorrection: " + leftCorrectionCalculated.orElse(0f) + "; RightCorrection: " + rightCorrectionCalculated.orElse(0f));
			

			rightCorrectionCalculated = Optional.of(Math.min(Math.max(rightCorrectionCalculated.orElse(0f) + biasValue, 0), 1));
			leftCorrectionCalculated = Optional.of(Math.min(Math.max(leftCorrectionCalculated.orElse(0f) - biasValue, 0), 1));
						 
			 float rightCorrection = rightCorrectionCalculated.get();
			 float leftCorrection = leftCorrectionCalculated.get();
			 
			 if(rightCorrection > leftCorrection)
			 {
				 rightCorrectionCalculated = Optional.of(rightCorrection - leftCorrection);
				 leftCorrectionCalculated = Optional.empty();
			 }
			 else
			 {
				 rightCorrectionCalculated = Optional.empty();
				 leftCorrectionCalculated = Optional.of(leftCorrection - rightCorrection);
			 }
		
			
			 //System.out.println("Currently applying bias of " + bias + ".");
			 //System.out.println("AFTER LeftCorrection: " + leftCorrectionCalculated.orElse(0f) + "; RightCorrection: " + rightCorrectionCalculated.orElse(0f));
		}
				
		boolean edited = false;
		
		// We aren't right enough
		if(leftCorrectionCalculated.isPresent())
		{
			// Go right
			if(currentDirection == RelativeDirection.FORWARD)
				movementController.turnRight(leftCorrectionCalculated.get());
			else
				movementController.turnLeft(leftCorrectionCalculated.get());
			
			movementController.updateMotorState();
			
			// System.out.printf("We're not right enough by %f.%n", leftCorrectionCalculated.get());
			edited = true;
			turnLeftFlag = false;
		}
		// We aren't left enough
		else if(rightCorrectionCalculated.isPresent())
		{
			// Go left
			if(currentDirection == RelativeDirection.FORWARD)
				movementController.turnLeft(rightCorrectionCalculated.get());
			else
				movementController.turnRight(rightCorrectionCalculated.get());
			
			movementController.updateMotorState();
			
			// System.out.printf("We're not left enough by %f.%n", rightCorrectionCalculated.get());
			edited = true;
			turnLeftFlag = false;
		}
		else
		{
			if(!turnLeftFlag)
			{
				// System.out.println("Reset");
				//movementController.turnLeft(0);
				edited = true;
				turnLeftFlag = true;
			}
		}
		
		if(edited)
		{
			movementController.setSpeed(speed);
			if(this.currentDirection == RelativeDirection.FORWARD)
				movementController.forward();
			else
				movementController.backward();
			movementController.updateMotorState();
		}
		
		Profiler.addRefresh();
	}
	
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	
	public void setCurrentDirection(RelativeDirection currentDirection)
	{
		this.currentDirection = currentDirection;
	}
	
	public Optional<Float> getLeftCorrection()
	{
		return leftCorrection.get();
	}
	
	public Optional<Float> getRightCorrection()
	{
		return rightCorrection.get();
	}
	
	/** Negative values should cause this to call turnRight, positive values should turn this thing left. **/
	public Optional<Float> getBias()
	{
		return Optional.empty();
	}
	
	/** Ignoring the setting of a flag because action should exit pretty quickly. 
	 * NOTE: Im pretty dumb as I think I may have misunderstood this method. **/
	@Override
	public void suppress()
	{
		// System.out.println("SUPPRESSING");
	}
}
