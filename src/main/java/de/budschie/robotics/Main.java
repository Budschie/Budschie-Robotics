package de.budschie.robotics;

import de.budschie.robotics.behaviours.ImplementedAdvancedFollowTrackBehaviour;
import de.budschie.robotics.behaviours.RelativeDirection;
import de.budschie.robotics.behaviours.TimedBehaviour;
import de.budschie.robotics.behaviours.WheelBasedMovementController;
import de.budschie.robotics.tasks.TaskExecutors;
import de.budschie.robotics.tasks.TaskManager;
import de.budschie.robotics.time.TimeManager;
import ev3dev.actuators.lego.motors.NXTRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import ev3dev.utils.PilotProps;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main
{
	// Left; In driving dir
	public static final EV3ColorSensor SENSOR_1 = new EV3ColorSensor(SensorPort.S4);
	// Right
	public static final EV3ColorSensor SENSOR_2 = new EV3ColorSensor(SensorPort.S1);
	
	
	public static final NXTRegulatedMotor MOTOR_LEFT = new NXTRegulatedMotor(MotorPort.A);
	public static final NXTRegulatedMotor MOTOR_RIGHT = new NXTRegulatedMotor(MotorPort.D);
	
	/** 
	 * I HATE C++ and IT'S FU**ING DENYING OF CIRCULAR DEPS
	 * @param args
	 */
	public static void main(String[] args)
	{		
		//System.out.println("Available modes: " + String.join(", ", SENSOR_1.getAvailableModes()));
		long startingTime = System.currentTimeMillis();
		
		/*
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
		*/
		
		TimeManager timeManager = new TimeManager();
		
		// Maybe I should use the lejos pilot classes, but I don't know where to find them...
		// Which is a bit unfortunate...
		// Edit: Can't use them because of licence issues (we'd have to use same licence for that)
		
		WheelBasedMovementController movementController = new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT);
		
		SENSOR_1.switchMode("REFLECT", 0);
		SENSOR_2.switchMode("REFLECT", 0);
		
		
		TaskManager concurrentTaskManager = new TaskManager(TaskExecutors.CONCURRENT_EXECUTOR);
		
		// Eclipse is a shithole of a software. Why is the detection of compile errors in lambda expressions downright bad???
		ImplementedAdvancedFollowTrackBehaviour implementedTrackManager = new ImplementedAdvancedFollowTrackBehaviour((value) -> (value < 330), movementController, () -> 
		{
			// We need to init this, so that fetchValue doesn't write nothing
			// Why couldn't this stupd thing be done in one line??!? I DONT UNDERSTAND IT!
			float[] value = new float[1];
			SENSOR_1.fetchSample(value, 0);
			// System.out.println("Left sensor value: " + value[0]);
			return (int)((value[0]) * 1000);
		},
		// Did I mention eclipse's handling of compile errors in lambda expressions is a pain in the ass?
		() -> 
		{
			// We need to init this, so that fetchValue doesn't write nothing
			// Why couldn't this stupd thing be done in one line??!? I DONT UNDERSTAND IT!
			float[] value = new float[1];
			SENSOR_2.fetchSample(value, 0);
			// System.out.println("Right sensor value: " + value[0]);
			return (int)((value[0]) * 1000);
		}, 360, RelativeDirection.LEFT, .25f, () -> true);
		
		implementedTrackManager.getFoundTrackEvent().subscribe((trackArgs) -> System.out.println("Event was called!!!"));
		
		Behavior[] behaviours = new Behavior[] {
			concurrentTaskManager,
			TimedBehaviour.of(implementedTrackManager, timeManager, 0, 20000)
		};
		
		
		
		
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
			
		/*
		long time = System.currentTimeMillis();
		
		float lowest = 2000;
		
		while((System.currentTimeMillis() - time) < 10000)
		{
			float[] val = new float[1];
			SENSOR_2.fetchSample(val, 0);
			lowest = Math.min(val[0], lowest);
		}
		
		System.out.println("The lowest is: " + lowest);
		*/
		
		/*
		Arbitrator arbitrator = new Arbitrator(behaviours, true);
		arbitrator.go();
		movementController.stop();
		movementController.updateMotorState();
		*/
		System.out.println("BYE FROM MAIN");
	}
}
