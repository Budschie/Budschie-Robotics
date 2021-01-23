package de.budschie.robotics.behaviours;

public interface IAbstractMovementController
{
	/** Calling this causes the robot to drive forwards. **/
	void forward();
	
	/** Calling this causes the robot to drive backwards **/
	void backward();
	
	/** This temporarily stops the movement, but preserves the speed when activated again. **/
	void stop();
	
	/** This temporarily flt-stops the movement, but preserves the speed when activated again. **/
	void fltStop();
	
	/** The amount should be implemented as a percentage decrease of the other motor**/
	void turnLeft(float amount);
	
	/** The amount should be implemented as a percentage decrease of the other motor**/
	void turnRight(float amount);
	
	/** This is the speed. It is in degrees per second. **/
	void setSpeed(int amount);
	
	/** Returns the speed that is currently set. **/
	int getSpeed();
	
	/** Updates the current state of the movement controller. **/
	void updateMotorState();
}