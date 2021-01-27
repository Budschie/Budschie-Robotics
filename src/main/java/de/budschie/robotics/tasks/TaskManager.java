package de.budschie.robotics.tasks;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import de.budschie.robotics.utils.SafeReadWriteAccess;
import lejos.robotics.subsumption.Behavior;

public class TaskManager implements Behavior
{
	private SafeReadWriteAccess<ITask> tasks;
	private boolean halted = false;
	private BiConsumer<TaskManager, SafeReadWriteAccess<ITask>> taskExecutor;
	
	public TaskManager(BiConsumer<TaskManager, SafeReadWriteAccess<ITask>> taskExecutor)
	{
		this.tasks = new SafeReadWriteAccess<>();
		this.taskExecutor = taskExecutor;
	}
	
	public TaskManager(BiConsumer<TaskManager, SafeReadWriteAccess<ITask>> taskExecutor, ArrayList<ITask> tasks)
	{
		this.taskExecutor = taskExecutor;
		this.tasks = new SafeReadWriteAccess<>(tasks);
	}
	
	public void removeTask(ITask task)
	{
		tasks.remove(task);
	}
	
	public void addTask(ITask task)
	{
		tasks.add(task);
	}
	
	public boolean isHalted()
	{
		System.out.println("Was halted.");
		return halted;
	}
	
	public void halt()
	{
		halted = true;
	}
	
	public void release()
	{
		halted = false;
	}
	
	@Override
	public boolean takeControl()
	{
		return !tasks.getMainElements().isEmpty() && !halted;
	}

	@Override
	public void action()
	{
		taskExecutor.accept(this, tasks);
	}

	@Override
	public void suppress()
	{
		// We do not really need to implement this, right?
	}
}
