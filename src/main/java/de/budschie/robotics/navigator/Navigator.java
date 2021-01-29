package de.budschie.robotics.navigator;

import de.budschie.robotics.utils.SampleSupplier;
import ev3dev.hardware.EV3DevDevice;
import ev3dev.sensors.ev3.EV3GyroSensor;

public class Navigator
{
	EV3GyroSensor gyro;
	SampleSupplier<Integer> sampleSupplier;
	
	public Navigator(EV3GyroSensor gyro)
	{
		this.sampleSupplier = new SampleSupplier<>(gyro, 1, Integer.class, (floatIn) -> 
		{
			int intValue = floatIn.intValue();
			System.out.println("Float value " + floatIn);
			return intValue;
		});
		this.sampleSupplier.setMode("GYRO-ANG", 0);
	}
	
	/** This method will not return until we have rotated. **/
	public void rotateAngle(int angle)
	{
		rotateAngle(angle, 0);
	}
	
	/** This method will not return until we have rotated. **/
	public void rotateAngle(int angle, long timeout)
	{
		long currentMS = System.currentTimeMillis();
		int startValue = sampleSupplier.getSample()[0];
		
		// Calculate delta
		if(angle > 0)
			// while((sampleSupplier.getSample()[0] - startValue) < angle);
			
			whileLoop:
			while((System.currentTimeMillis() - currentMS) < timeout)
			{
				int delta = (sampleSupplier.getSample()[0] - startValue);
				System.out.println(sampleSupplier.getSample()[0]);
				
				if(delta >= angle)
					break whileLoop;
			}
		else
			whileLoop:
			while((System.currentTimeMillis() - currentMS) < timeout)
			{
				int delta = (sampleSupplier.getSample()[0] - startValue);
				
				System.out.println(sampleSupplier.getSample()[0]);
				
				if(delta <= angle)
					break whileLoop;
			}
			// while((sampleSupplier.getSample()[0] - startValue) > angle);
	}
}
