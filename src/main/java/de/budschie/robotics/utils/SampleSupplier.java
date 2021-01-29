package de.budschie.robotics.utils;

import java.lang.reflect.Array;
import java.util.Optional;
import java.util.function.Function;

import ev3dev.sensors.BaseSensor;
import lejos.robotics.SampleProvider;

public class SampleSupplier<A>
{
	private Optional<Function<A, A>> modifier;
	private BaseSensor baseSensor;
	private int sampleSize;
	private Class<A> clazz;
	
	private Optional<String> currentMode = Optional.empty();
	long timeToSwitch;
	long timeSinceSwitchCall;
	Function<Float, A> caster;
	
	public SampleSupplier(BaseSensor baseSensor, int sampleSize, Class<A> clazz, Function<Float, A> caster)
	{
		this(baseSensor, null, sampleSize, clazz, caster);
	}
	
	public SampleSupplier(BaseSensor baseSensor, Function<A, A> modifier, int sampleSize, Class<A> clazz, Function<Float, A> caster)
	{
		this.baseSensor = baseSensor;
		this.modifier = Optional.ofNullable(modifier);
		this.sampleSize = sampleSize;
		this.clazz = clazz;
		this.caster = caster;
	}
	
	/** This is a bit different to a direct call, as it only executes this in the future. **/
	public void setMode(String mode, long timeToSwitch)
	{
		this.currentMode = Optional.of(mode);
		this.timeSinceSwitchCall = System.currentTimeMillis();
		this.timeToSwitch = timeToSwitch;
	}
	
	public void unsetMode()
	{
		currentMode = Optional.empty();
	}
	
	public A[] getSample()
	{
		/** We first assert the mode here. **/
		assertMode();
		
		SampleProvider sampleProvider = baseSensor.getMode(baseSensor.getCurrentMode());
		
		float[] input = new float[sampleSize];
		sampleProvider.fetchSample(input, 0);
		
		@SuppressWarnings("unchecked")
		A[] castedArray = (A[]) Array.newInstance(clazz, sampleSize);
		
		// Please java. Tell me why I have to convert this to a Float first??!? This should all be done automatically...
		for(int i = 0; i < input.length; i++)
		{
			castedArray[i] = caster.apply(input[i]);
		}
		
		if(modifier.isPresent())
		{
			for(int i = 0; i < castedArray.length; i++)
			{
				/** Apply modifiers if necessary **/
				castedArray[i] = modifier.get().apply(castedArray[i]);
			}
		}
		
		return castedArray;
	}
	
	private void assertMode()
	{
		if(currentMode.isPresent())
		{
			if(System.currentTimeMillis() > (timeToSwitch + timeSinceSwitchCall))
			{
				baseSensor.switchMode(currentMode.get(), 0);
			}
		}
	}
}
