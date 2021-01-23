package de.budschie.robotics.tasks;

public interface ITask
{
	/** Return true when you want to exit this task. **/
	boolean execute(TaskManager executor);
}
