package de.budschie.robotics.behaviours;

import ev3dev.actuators.lego.motors.NXTRegulatedMotor;

public class WheelBasedMovementController implements IAbstractMovementController
{
	NXTRegulatedMotor motorLeft, motorRight;
	float speed = 1;
	float percentage = 1;
	RelativeDirection turnDirection = RelativeDirection.LEFT;
	RelativeDirection driveDirection = RelativeDirection.FORWARD;
	
	public WheelBasedMovementController(NXTRegulatedMotor motorLeft, NXTRegulatedMotor motorRight)
	{
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	@Override
	public void forward()
	{
		driveDirection = RelativeDirection.FORWARD;
	}

	@Override
	public void backward()
	{
		driveDirection = RelativeDirection.BACKWARD;
	}
	
	@Override
	public void stop()
	{
		driveDirection = RelativeDirection.STOP;
	}

	@Override
	public void turnLeft(float percentage)
	{
		turnDirection = RelativeDirection.LEFT;
		this.percentage = percentage;
	}

	@Override
	public void turnRight(float percentage)
	{
		turnDirection = RelativeDirection.RIGHT;
		this.percentage = percentage;
	}
	
	public void updateMotorState()
	{
		if(driveDirection == RelativeDirection.STOP)
		{
			motorLeft.stop();
			motorRight.stop();
		}
		else
		{
			if(driveDirection == RelativeDirection.FORWARD)
			{
				motorLeft.forward();
				motorRight.forward();
			}
			// Technically not necessary, but calling this as it makes all of this clearer
			else if(driveDirection == RelativeDirection.BACKWARD)
			{
				motorLeft.backward();
				motorRight.backward();
			}
			
			// Update speed
			float percentageOfFull = speed * percentage;
			
			if(turnDirection == RelativeDirection.LEFT)
			{
				motorLeft.setSpeed((int) (percentageOfFull * 360));
				motorRight.setSpeed((int) (speed * 360));
			}
			// Technically not necessary, but calling this as it makes all of this clearer
			else if(turnDirection == RelativeDirection.RIGHT)
			{
				motorLeft.setSpeed((int) (speed * 360));
				motorRight.setSpeed((int) (percentageOfFull * 360));
			}
		}
	}
}
