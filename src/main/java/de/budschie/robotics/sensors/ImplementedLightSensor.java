package de.budschie.robotics.sensors;

import ev3dev.sensors.BaseSensor;
import ev3dev.sensors.GenericMode;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.LightDetector;
import lejos.robotics.SampleProvider;

/** This isn't necessary, as we already have {@link EV3ColorSensor}**/
@Deprecated
public class ImplementedLightSensor extends BaseSensor implements LightDetector
{
	public static final String LEGO_NXT_LIGHT = "lego-nxt-light";
	
	public static final String REFLECT_MODE = "REFLECT";
	public static final String AMBIENT_MODE = "AMBIENT";
	
	public ImplementedLightSensor(final Port sensorPort)
	{
		super(sensorPort, REFLECT_MODE, LEGO_NXT_LIGHT);
		setModes(new SensorMode[] { new GenericMode(PATH_DEVICE, 1, AMBIENT_MODE), new GenericMode(PATH_DEVICE, 1, REFLECT_MODE)});
	}

	@Override
	public float getLightValue()
	{
		return 0;
	}

	@Override
	public float getNormalizedLightValue()
	{
		return 0;
	}

	@Override
	public float getHigh()
	{
		return 0;
	}

	@Override
	public float getLow()
	{
		return 300;
	}
}
