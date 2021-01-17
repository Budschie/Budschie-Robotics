package de.budschie.robotics;

import java.util.Optional;

import de.budschie.robotics.behaviours.CustomBehaviour;
import de.budschie.robotics.behaviours.FollowTrackBehaviour;
import de.budschie.robotics.behaviours.WheelBasedMovementController;
import de.budschie.robotics.sensors.ImplementedLightSensor;
import ev3dev.actuators.lego.motors.NXTRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class Main
{
	// Left; maybe switched with SENSOR_2
	public static final EV3ColorSensor SENSOR_1 = new EV3ColorSensor(SensorPort.S1);
	
	// Right
	public static final EV3ColorSensor SENSOR_2 = new EV3ColorSensor(SensorPort.S4);
	
	public static final NXTRegulatedMotor MOTOR_LEFT = new NXTRegulatedMotor(MotorPort.A);
	public static final NXTRegulatedMotor MOTOR_RIGHT = new NXTRegulatedMotor(MotorPort.D);
	
	public static void main(String[] args)
	{		
		//System.out.println("Available modes: " + String.join(", ", SENSOR_1.getAvailableModes()));
		
		long startingTime = System.currentTimeMillis();
		
		Behavior[] behaviours = new Behavior[] { new FollowTrackBehaviour(() -> {
			float[] value = new float[1];
			SENSOR_1.fetchSample(value, 0);
			System.out.println(value);
			System.out.println("1: " + value[0]);
			
			return value[0] < 400 ? Optional.of(.5f) : Optional.empty();
		}, () -> {
			float[] value = new float[1];
			SENSOR_2.fetchSample(value, 0);
			System.out.println("2: " + value[0]);
			
			return value[0] < 400 ? Optional.of(.5f) : Optional.empty();
		}, new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT), 500, () -> (System.currentTimeMillis() - startingTime) < 8000)};
		
		
		
//		WheelBasedMovementController movementController = new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT);
//		movementController.turnLeft(1);
//		movementController.setSpeed(500);
//		movementController.updateMotorState();
//		Delay.msDelay(8000);
//		movementController.stop();
//		movementController.updateMotorState();
		
		
//		MOTOR_LEFT.setSpeed(1000);
//		MOTOR_RIGHT.setSpeed(30);
//		MOTOR_LEFT.backward();
//		MOTOR_RIGHT.backward();
//		Delay.msDelay(8000);
//		MOTOR_LEFT.stop();
//		MOTOR_RIGHT.stop();
			
		
		Arbitrator arbitrator = new Arbitrator(behaviours, true);
		arbitrator.go();
		System.out.println("BYE FROM MAIN");
	}
}
