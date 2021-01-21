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

	public static void main(String[] args)
	{		
		//System.out.println("Available modes: " + String.join(", ", SENSOR_1.getAvailableModes()));
		long startingTime = System.currentTimeMillis();
		

		TimeManager timeManager = new TimeManager();
		
		// Maybe I should use the lejos pilot classes, but I don't know where to find them...
		// Which is a bit unfortunate...
		// Edit: Can't use them because of licence issues (we'd have to use same licence for that)
		
		WheelBasedMovementController movementController = new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT);
		
		SENSOR_1.switchMode("REFLECT", 0);
		SENSOR_2.switchMode("REFLECT", 0);
		
		
		TaskManager concurrentTaskManager = new TaskManager(TaskExecutors.CONCURRENT_EXECUTOR);
		
		// This code looks terrible
		// Eclipse is a shithole of a software. Why is the detection of compile errors in lambda expressions downright bad???
		ImplementedAdvancedFollowTrackBehaviour implementedTrackManager = new ImplementedAdvancedFollowTrackBehaviour((value) -> (value < 330), movementController, () -> 
		{
			// We need to init this, so that fetchValue doesn't write to nothing
			// Why couldn't this stupid thing be done in one line??!? I DONT UNDERSTAND IT!
			float[] value = new float[1];
			SENSOR_1.fetchSample(value, 0);
			return (int)((value[0]) * 1000);
		},
		// Did I mention eclipse's handling of compile errors in lambda expressions is a pain in the ass?
		() -> 
		{
			float[] value = new float[1];
			SENSOR_2.fetchSample(value, 0);
			return (int)((value[0]) * 1000);
		}, 360, RelativeDirection.LEFT, .25f, () -> true);
		
		implementedTrackManager.getFoundTrackEvent().subscribe((trackArgs) -> System.out.println("Event was called!!!"));
		
		Behavior[] behaviours = new Behavior[] {
			concurrentTaskManager,
			TimedBehaviour.of(implementedTrackManager, timeManager, 0, 20000)
		};
			
		// This little program is used to determine the white and black values
		// We can use Math.min for white and Math.max for black (to determine the brightest black value). Then we cross-check both values,
		// so that they don't overlap
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
		
		
		Arbitrator arbitrator = new Arbitrator(behaviours, true);
		arbitrator.go();
		movementController.stop();
		movementController.updateMotorState();
		
		System.out.println("Exited.");
	}
}
