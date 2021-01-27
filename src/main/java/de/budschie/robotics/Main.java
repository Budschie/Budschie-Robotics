package de.budschie.robotics;

import de.budschie.robotics.behaviours.ImplementedAdvancedFollowTrackBehaviour;
import de.budschie.robotics.behaviours.WheelBasedMovementController;
import de.budschie.robotics.profiling.Profiler;
import de.budschie.robotics.tasks.TaskExecutors;
import de.budschie.robotics.tasks.TaskManager;
import de.budschie.robotics.time.TimeManager;
import ev3dev.actuators.ev3.EV3Led;
import ev3dev.actuators.lego.motors.NXTRegulatedMotor;
import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class Main
{
	public static final EV3Led RIGHT = new EV3Led(EV3Led.RIGHT);
	public static final EV3Led LEFT = new EV3Led(EV3Led.LEFT);
	
	// Left; In driving dir
	public static final EV3ColorSensor SENSOR_1 = new EV3ColorSensor(SensorPort.S4);
	// Right
	public static final EV3ColorSensor SENSOR_2 = new EV3ColorSensor(SensorPort.S1);
	
	
	public static final NXTRegulatedMotor MOTOR_LEFT = new NXTRegulatedMotor(MotorPort.A);
	public static final NXTRegulatedMotor MOTOR_RIGHT = new NXTRegulatedMotor(MotorPort.D);
	
	public static final NXTRegulatedMotor ADDITIONAL_1 = new NXTRegulatedMotor(MotorPort.B);
	public static final NXTRegulatedMotor ADDITIONAL_2 = new NXTRegulatedMotor(MotorPort.C);

	public static void main(String[] args)
	{		
		
		
		// ZEIT: 2 min 30 sek
		
		// REIHENFOLGE: Von Homezone -> Innovatives Projekt(Schieben) von Baum auf Replay Logo ->
		// Schieber -> Tunnel -> Homezone (Transportaufsatz) -> Direkt zum Rahmen (ohne die Linien) -> hin und zurück -> Homezone (Turm Aufsatz) -> Basketballkorb -> Bank -> Rutsche -> Gewichtheben -> Robodance
		
		//System.out.println("Available modes: " + String.join(", ", SENSOR_1.getAvailableModes()));
		TimeManager timeManager = new TimeManager();
		timeManager.start();
		Profiler.start();
		
		// SENSOR_1.switchMode("REFLECT", 0);
		// SENSOR_2.switchMode("REFLECT", 0);
		
		System.out.println("Starting SUPER SAMPLER (DUN DUN DUUUUUN)");
		
		
		while(timeManager.getElapsedTime() < 60000)
		{
			float[] val = new float[1];
			SENSOR_1.fetchSample(val, 0);
			int yeet = (int) val[0];
			
			float[] val2 = new float[1];
			SENSOR_2.fetchSample(val2, 0);
			int yeet2 = (int)((val2[0]));
			
			Profiler.addRefresh();
		}
		
		Profiler.stop();
		
		//System.out.println("We were able to sample both sensors " + amount + "times in 60000 seconds.");
		
		// Maybe I should use the lejos pilot classes, but I don't know where to find them...
		// Which is a bit unfortunate...
		// Edit: Can't use them because of licence issues (we'd have to use same licence for that)
		
		WheelBasedMovementController movementController = new WheelBasedMovementController(MOTOR_LEFT, MOTOR_RIGHT);
		
		//SENSOR_1.switchMode("REFLECT", 0);
		//SENSOR_2.switchMode("REFLECT", 0);
		
		
		TaskManager concurrentTaskManager = new TaskManager(TaskExecutors.CONCURRENT_EXECUTOR);
		TaskManager sequentialTaskManager = new TaskManager(TaskExecutors.SEQUENTIAL_EXECUTOR);
		
		// Turn motor by fixed amount up, go back, turn motor down, go forward, turn motor up.
		concurrentTaskManager.addTask((task) -> 
		{
			System.out.println("YEET");
			return true;
		});
		
		// This code looks terrible
		// Eclipse is a sh***ole of a software. Why is the detection of compile errors in lambda expressions downright bad???
		
		// If this **** doesn't work, just make it slower...
		ImplementedAdvancedFollowTrackBehaviour implementedTrackManager = new ImplementedAdvancedFollowTrackBehaviour((value) -> (value < 380), movementController, () -> 
		{
			// We need to init this, so that fetchValue doesn't write to nothing
			// Why couldn't this stupid thing be done in one line??!? I DONT UNDERSTAND IT!
			float[] val = new float[1];
			SENSOR_1.fetchSample(val, 0);
			// System.out.println("Current sample: " + val[0]);
			return (int)((val[0]));
		},
		// Did I mention eclipse's handling of compile errors in lambda expressions is a pain in the a**?
		() -> 
		{
			float[] val = new float[1];
			SENSOR_2.fetchSample(val, 0);
			return (int)((val[0]));
		}, 250, 600, .75f, RelativeDirection.RIGHT, .5f, () -> true);
		
		ITask waitForButtonPress = (taskManager) ->
		{
			Button.waitForAnyPress();
			return true;
		};
		
		ITask slide = (taskManager) ->
		{
			int turnAmount = 880;
			
			movementController.backward();
			movementController.turnRight(1);
			movementController.setSpeed(360);
			movementController.updateMotorState();
			
			ADDITIONAL_1.setSpeed(880);
			ADDITIONAL_2.setSpeed(880);
			
			ADDITIONAL_1.rotate(-turnAmount, true);
			ADDITIONAL_2.rotate(turnAmount, false);
			
			movementController.turnLeft(0);
			movementController.forward();
			movementController.updateMotorState();
			Delay.msDelay(100);
			
			movementController.backward();
			movementController.turnRight(1);
			movementController.updateMotorState();
			
			Delay.msDelay(1000);
			
			movementController.stop();
			movementController.updateMotorState();
			
			return true;
		};
		
		ITask basketball = (taskManager) -> 
		{
			int turnAmount = 1250;
			
			// Up
			ADDITIONAL_1.setSpeed(760);
			ADDITIONAL_2.setSpeed(760);
			ADDITIONAL_1.rotate(-turnAmount, true);
			ADDITIONAL_2.rotate(turnAmount, false);

			// Drive forward
			movementController.setSpeed(360);
			movementController.turnLeft(0);
			movementController.forward();
			movementController.updateMotorState();
			
			// Down
			ADDITIONAL_1.rotate(turnAmount, true);
			ADDITIONAL_2.rotate(-turnAmount, true);
			Delay.msDelay(1400);
			
			// Drive backward
			movementController.stop();
			movementController.updateMotorState();
			Delay.msDelay(250);
			movementController.backward();
			movementController.updateMotorState();
			Delay.msDelay(1400);
			
			//Up
			movementController.stop();
			movementController.updateMotorState();
			ADDITIONAL_1.rotate(-turnAmount, true);
			ADDITIONAL_2.rotate(turnAmount, false);
			
			return true;
		};
		
		ITask goUnderPushupStick = (taskManager) ->
		{
			movementController.setSpeed(500);
			movementController.forward();
			movementController.updateMotorState();
			Delay.msDelay(3000);
			movementController.backward();
			movementController.updateMotorState();
			Delay.msDelay(3000);
			movementController.stop();
			movementController.updateMotorState();
			
			return true;
		};
		
		ITask pushDown = (taskManager) ->
		{
			int turnAmount = 1000;
			
			ADDITIONAL_1.setSpeed(turnAmount);
			ADDITIONAL_2.setSpeed(turnAmount);
			
			ADDITIONAL_1.rotate(-turnAmount, true);
			ADDITIONAL_2.rotate(turnAmount, false);
			
			movementController.setSpeed(150);
			movementController.backward();
			movementController.updateMotorState();
			
			ADDITIONAL_1.rotate(turnAmount, true);
			ADDITIONAL_2.rotate(-turnAmount, false);
			
			movementController.stop();
			movementController.updateMotorState();
			
			return true;
		};
		
		ITask stickpush = (taskManager) ->
		{
			System.out.println("Starting StickPush...");
			movementController.setSpeed(900);
			
			for(int i = 0; i < 17; i++)
			{
				movementController.forward();
				movementController.updateMotorState();
				Delay.msDelay(200);
				movementController.backward();
				movementController.updateMotorState();
				Delay.msDelay(100);
			}
			
			System.out.println("Finished StickPush...");
			return true;
		};
		
		/*
		TrackGuard trackGuard = new TrackGuard.Builder().setAdvancedFollowTrackBehaviour(implementedTrackManager)
				.addTrackExecutor((trackArgs) -> 
				{
					movementController.turnLeft(1);
					movementController.forward();
					movementController.updateMotorState();
					Delay.msDelay(5000);
					sequentialTaskManager.addTask(slide);
				}, 3).build();
				*/
		
		implementedTrackManager.setCurrentDirection(RelativeDirection.BACKWARD);
		
		sequentialTaskManager.addTask((taskManager) ->
		{
			return true;
		});
		
		TrackGuard trackGuard = new TrackGuard.Builder().setAdvancedFollowTrackBehaviour(implementedTrackManager)
				.addTrackExecutor((trackArgs) ->
				{
					sequentialTaskManager.addTask((taskManager) ->
					{
						movementController.forward();
						movementController.setSpeed(600);
						movementController.turnRight(1);
						movementController.updateMotorState();
						Delay.msDelay(600);
						movementController.turnRight(0);
						movementController.updateMotorState();
						Delay.msDelay(400);
						
						// Insert push thing here
						
						// Undo
						
						movementController.backward();
						movementController.updateMotorState();
						Delay.msDelay(400);
						movementController.backward();
						movementController.turnRight(1);
						movementController.updateMotorState();
						Delay.msDelay(600);
						
						return true;
					});
				}, 0).build();
		
		
		
		/*
		sequentialTaskManager.addTask((taskManager) ->
		{
			System.out.println("Started driving forward...");
			movementController.forward();
			movementController.setSpeed(600);
			movementController.updateMotorState();
			Delay.msDelay(2000);
			movementController.stop();
			movementController.updateMotorState();
			System.out.println("Finished driving forward.");
			return true;
		});
		
		sequentialTaskManager.addTask(stickpush);
		*/
		
		/*
		sequentialTaskManager.addTask((taskManager) ->
		{
			ADDITIONAL_1.setSpeed(500);
			ADDITIONAL_2.setSpeed(500);
			
			ADDI
			
			// Scrapped idea of macros, as its implementation is hard to get right
			// new MacroBuilder().registerMacro(null, null).callMacro("towerUp", 360).callMacro("driveRight", .7).callMa
			
			
			// Get onto the right height
			ADDITIONAL_1.setSpeed(600);
			ADDITIONAL_2.setSpeed(600);
			
			ADDITIONAL_1.rotate(-300, true);
			ADDITIONAL_2.rotate(300, false);
			
			// Drive forward(we use backward here as the tower is technically at the back of the robot)
			movementController.backward();
			movementController.setSpeed(500);
			movementController.updateMotorState();
			Delay.msDelay(500);
			
			// Stop
			movementController.stop();
			movementController.updateMotorState();
			
			// Push up
			ADDITIONAL_1.rotate(-300, true);
			ADDITIONAL_2.rotate(300, false);
			
			movementController.forward();
			movementController.turnLeft(0);
			movementController.updateMotorState();
			Delay.msDelay(100);
			
			movementController.stop();
			movementController.updateMotorState();
			
			// Go down again
			ADDITIONAL_1.rotate(300, true);
			ADDITIONAL_2.rotate(-300, false);
			
			movementController.backward();
			movementController.turnLeft(.5f);
			movementController.updateMotorState();
			
			Delay.msDelay(100);
			
			movementController.stop();
			movementController.updateMotorState();
			
			ADDITIONAL_1.rotate(-300, true);
			ADDITIONAL_2.rotate(300, false);
			
			movementController.forward();
			movementController.updateMotorState();
			
			Delay.msDelay(500);
			
			movementController.stop();
			movementController.updateMotorState();
			
			// Move tower down
			ADDITIONAL_1.rotate(600, true);
			ADDITIONAL_2.rotate(-600, false);
			
			
			// Drive forward
			movementController.backward();
			movementController.updateMotorState();
			
			return true;
		});
		*/
		
		// This stick push thingy (honestly I have no idea how it is called...)
		
		

		
		
		// Robodance
		/*
		sequentialTaskManager.addTask((taskManager) ->
		{
			movementController.setSpeed(600);
			movementController.turnLeft(1);
			movementController.forward();
			movementController.updateMotorState();
			Delay.msDelay(9000);
			movementController.stop();
			movementController.updateMotorState();
			return true;
		});
		*/
		
		// Slide

		
		// Basketball
		
		Behavior[] behaviours = new Behavior[] {
			concurrentTaskManager, sequentialTaskManager,
			TimedBehaviour.of(implementedTrackManager, timeManager, 0, 150000)
		};
		
			
		// This little program is used to determine the white and black values
		// We can use Math.min for white and Math.max for black (to determine the brightest black value). Then we cross-check both values,
		// so that they don't overlap
		
		// Lowest White is: 687
		// Highest Black is: 366.0
		
		/*
		long time = System.currentTimeMillis();
		
		float highest = 0;
		
		while((System.currentTimeMillis() - time) < 10000)
		{
			float[] val = new float[1];
			SENSOR_2.fetchSample(val, 0);
			highest = Math.max(val[0], highest);
		}
		
		System.out.println("The highest is: " + highest);
		*/
		
		// Note on performance: We have only 3 checks per second, which is very bad... 2.905259011227103 2.9798251184602607 
		Profiler.start();
		timeManager.start();
		Arbitrator arbitrator = new Arbitrator(behaviours, true);
		arbitrator.go();
		movementController.stop();
		movementController.updateMotorState();
		Profiler.stop();
		
		RIGHT.setPattern(1);
		LEFT.setPattern(1);
		
		System.out.println("Elapsed time is " + timeManager.getElapsedTime());
		
		System.out.println("Exited.");
	}
}
