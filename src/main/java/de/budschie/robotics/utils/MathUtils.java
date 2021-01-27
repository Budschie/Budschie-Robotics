package de.budschie.robotics.utils;

public class MathUtils
{
	public static float linearInterpolation(float from, float to, float at)
	{
		return (1-at)*from + to * at;
	}
}
