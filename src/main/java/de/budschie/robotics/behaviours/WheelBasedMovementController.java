package de.budschie.robotics.behaviours;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;

public class WheelBasedMovementController implements IMovementController
{
	BaseRegulatedMotor motorLeft, motorRight;
	int speed = 1;
	int accelaration = 6000;
	float percentage = 1;
	RelativeDirection turnDirection = RelativeDirection.LEFT;
	RelativeDirection driveDirection = RelativeDirection.FORWARD;
	
	public WheelBasedMovementController(BaseRegulatedMotor motorLeft, BaseRegulatedMotor motorRight)
	{
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(int speed)
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
	public void fltStop()
	{
		driveDirection = RelativeDirection.FLT;
	}

	@Override
	public void turnLeft(float percentage)
	{
		turnDirection = RelativeDirection.LEFT;
		this.percentage = 1 - percentage;
	}

	@Override
	public void turnRight(float percentage)
	{
		turnDirection = RelativeDirection.RIGHT;
		this.percentage = 1 - percentage;
	}
	
	@Override
	public void setAccelaration(int amount)
	{
		this.accelaration = amount;
	}
	
	public void updateMotorState()
	{
		if(driveDirection == RelativeDirection.STOP)
		{
			motorLeft.stop();
			motorRight.stop();
		}
		else if(driveDirection == RelativeDirection.FLT)
		{
			motorLeft.flt();
			motorRight.flt();
		}
		else
		{
			// Update speed
			float percentageOfFull = speed * percentage;
			//System.out.printf("Speed: %f%nPercentage: %f%nPercentage of speed: %f", speed, percentage, percentageOfFull);
			
			motorLeft.setAcceleration(accelaration);
			motorRight.setAcceleration(accelaration);
			
			if(turnDirection == RelativeDirection.LEFT)
			{
				motorLeft.setSpeed((int) (percentageOfFull));
				motorRight.setSpeed((int) (speed));
			}
			// Technically not necessary, but calling this as it makes all of this clearer
			else if(turnDirection == RelativeDirection.RIGHT)
			{
				motorLeft.setSpeed((int) (speed));
				motorRight.setSpeed((int) (percentageOfFull));
			}
			
			if(driveDirection == RelativeDirection.FORWARD)
			{
				// System.out.println("Forward");
				motorLeft.forward();
				motorRight.forward();
			}
			// Technically not necessary, but calling this as it makes all of this clearer
			else if(driveDirection == RelativeDirection.BACKWARD)
			{
				// System.out.println("Backward");
				motorLeft.backward();
				motorRight.backward();
			}
		}
	}
}
