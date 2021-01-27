package de.budschie.robotics.tasks;

import java.util.function.BiConsumer;

import de.budschie.robotics.utils.SafeReadWriteAccess;

public class TaskExecutors
{
	public static final BiConsumer<TaskManager, SafeReadWriteAccess<ITask>> SEQUENTIAL_EXECUTOR = (thisReference, tasks) ->
	{
		// We don't need processing here
		if(tasks.getMainElements().size() > 0)
		{
			ITask currentTask = tasks.getMainElements().get(0);
			boolean shouldRemove = currentTask.execute(thisReference);
			
			if(shouldRemove)
				tasks.remove(currentTask);
		}
	};
	
	public static final BiConsumer<TaskManager, SafeReadWriteAccess<ITask>> CONCURRENT_EXECUTOR = (thisReference, tasks) ->
	{
		// We do need processing here, so set writing to true
		tasks.setCurrentlyReading(true);
		
		for(ITask task : tasks.getMainElements())
		{
			boolean shouldRemove = task.execute(thisReference);
			if(shouldRemove)
				tasks.remove(task);
		}
		
		tasks.setCurrentlyReading(false);
		tasks.process();
	};
}
