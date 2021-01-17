package de.budschie.robotics;

import java.util.Optional;

import de.budschie.robotics.behaviours.CustomBehaviour;
import de.budschie.robotics.behaviours.FollowTrackBehaviour;
import de.budschie.robotics.behaviours.WheelBasedMovementController;
import ev3dev.actuators.lego.motors.NXTRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Main
{
	public static final EV3ColorSensor SENSOR_1 = new EV3ColorSensor(SensorPort.S1);
	public static final EV3ColorSensor SENSOR_2 = new EV3ColorSensor(SensorPort.S2);
	
	public static final NXTRegulatedMotor MOTOR_LEFT = new NXTRegulatedMotor(MotorPort.A);
	public static final NXTRegulatedMotor MOTOR_RIGHT = new NXTRegulatedMotor(MotorPort.D);
	
	public static void main(String[] args)
	{		
		System.out.println("Available modes: " + String.join(", ", SENSOR_1.getAvailableModes()));
		
//		Behavior[] behaviours = new Behavior[] { new FollowTrackBehaviour(() -> {
//			float[] value = null;
//			System.out.println("1: " + SENSOR_1);
//			SENSOR_1.fetchSample(value, 0);
//			System.out.println(value);
//			
//			return value[0] < 0.2 ? Optional.of(.5f) : Optional.empty();
//		}, () -> {
//			float[] value = null;
//			System.out.println("2: " + SENSOR_2);
//			SENSOR_2.fetchSample(value, 0);
//			
//			return value[0] < 0.2 ? Optional.of(.5f) : Optional.empty();
//		}, new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT), () -> true)};
		
		WheelBasedMovementController movementController = new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT);
		movementController.turnLeft(0.5f);
		movementController.setSpeed(300);
		movementController.updateMotorState();
		Delay.msDelay(6000);
		movementController.stop();
		movementController.updateMotorState();
		
		/*
		Arbitrator arbitrator = new Arbitrator(new Behavior[] {
		new CustomBehaviour(() -> 
		{
			MOTOR_LEFT.forward();
			MOTOR_LEFT.setSpeed(100);
			MOTOR_LEFT.startSynchronization();
		},
		() -> {}, () -> true)});
		arbitrator.go();
		System.out.println("BYE FROM MAIN");
		*/
	}
}
