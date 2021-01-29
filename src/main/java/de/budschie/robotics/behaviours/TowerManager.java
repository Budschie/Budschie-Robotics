package de.budschie.robotics.behaviours;

import ev3dev.actuators.lego.motors.BaseRegulatedMotor;

public class TowerManager
{
	private RelativeDirection direction;
	private BaseRegulatedMotor first, second;
	private int speed;
	
	public TowerManager(BaseRegulatedMotor first, BaseRegulatedMotor second, int speed)
	{
		this.first = first;
		this.second = second;
		this.speed = speed;
		this.direction = RelativeDirection.STOP;
	}
	
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	
	public void up()
	{
		this.direction = RelativeDirection.UP;
	}
	
	public void down()
	{
		this.direction = RelativeDirection.DOWN;
	}
	
	public void rotateUp(int amount, boolean immediateReturn)
	{
		first.setSpeed(speed);
		second.setSpeed(speed);

		first.rotate(-amount, true);
		second.rotate(amount, immediateReturn);
	}
	
	public void rotateDown(int amount, boolean immediateReturn)
	{
		first.setSpeed(speed);
		second.setSpeed(speed);

		first.rotate(speed, true);
		second.rotate(-speed, immediateReturn);
	}
	
	public void stop()
	{
		this.direction = RelativeDirection.STOP;
	}
	
	public void updateMotorState()
	{
		if(direction == RelativeDirection.STOP)
		{
			first.stop();
			second.stop();
		}
		else
		{
			first.setSpeed(speed);
			second.setSpeed(speed);
			
			if(direction == RelativeDirection.UP)
			{
				first.rotate(-speed, true);
				second.rotate(speed, true);
			}
			else if(direction == RelativeDirection.DOWN)
			{
				first.rotate(speed, true);
				second.rotate(-speed, true);
			}
		}
	}
}
