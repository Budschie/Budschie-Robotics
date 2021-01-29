package de.budschie.robotics.navigator;

import de.budschie.robotics.behaviours.IMovementController;
import de.budschie.robotics.utils.SampleSupplier;
import ev3dev.sensors.ev3.EV3GyroSensor;

public class Navigator
{
	EV3GyroSensor gyro;
	SampleSupplier<Integer> sampleSupplier;
	
	public Navigator(EV3GyroSensor gyro)
	{
		this.sampleSupplier = new SampleSupplier<>(gyro, 1, Integer.class);
		this.sampleSupplier.setMode("Angle and Rate", 0);
	}
	
	/** This method will not return until we have rotated. **/
	public void rotateAngle(int angle)
	{
		int startValue = sampleSupplier.getSample()[0];
		
		// Calculate delta
		while((sampleSupplier.getSample()[0] - startValue) >= angle);
	}
}
