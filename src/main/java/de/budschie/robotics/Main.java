package de.budschie.robotics;

import java.util.Optional;

import de.budschie.robotics.behaviours.FollowTrackBehaviour;
import de.budschie.robotics.behaviours.WheelBasedMovementController;
import ev3dev.actuators.lego.motors.NXTRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main
{
	public static final EV3ColorSensor SENSOR_1 = new EV3ColorSensor(SensorPort.S1);
	public static final EV3ColorSensor SENSOR_2 = new EV3ColorSensor(SensorPort.S2);
	
	public static final NXTRegulatedMotor MOTOR_LEFT = new NXTRegulatedMotor(MotorPort.A);
	public static final NXTRegulatedMotor MOTOR_RIGHT = new NXTRegulatedMotor(MotorPort.B);
	
	public static void main(String[] args)
	{		
		System.out.println("HELLO FROM MAIN");
		Arbitrator arbitrator = new Arbitrator(new Behavior[] { new FollowTrackBehaviour(() -> {
			float[] value = null;
			SENSOR_1.getRGBMode().fetchSample(value, 0);
			
			return value[0] < 0.2 ? Optional.of(.5f) : Optional.empty();
		}, () -> {
			float[] value = null;
			SENSOR_2.getRGBMode().fetchSample(value, 0);
			
			return value[0] < 0.2 ? Optional.of(.5f) : Optional.empty();
		}, new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT))});
		arbitrator.go();
		System.out.println("BYE FROM MAIN");
	}
}
