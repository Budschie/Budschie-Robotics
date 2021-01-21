package de.budschie.robotics.tasks;

import java.util.function.BiConsumer;

import de.budschie.robotics.util.SafeReadWriteAccess;

public class TaskExecutors
{
	public static final BiConsumer<TaskManager, SafeReadWriteAccess<ITask>> SEQUENTIAL_EXECUTOR = (thisReference, tasks) ->
	{
		// We don't need processing here
		ITask currentTask = tasks.getMainElements().get(0);
		currentTask.execute(thisReference);
	};
	
	public static final BiConsumer<TaskManager, SafeReadWriteAccess<ITask>> CONCURRENT_EXECUTOR = (thisReference, tasks) ->
	{
		// We do need processing here, so set writing to true
		tasks.setCurrentlyReading(true);
		
		for(ITask task : tasks.getMainElements())
			task.execute(thisReference);
		
		tasks.setCurrentlyReading(false);
		tasks.process();
	};
}
