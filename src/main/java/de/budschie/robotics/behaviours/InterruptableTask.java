package de.budschie.robotics.behaviours;

import java.util.Arrays;
import java.util.List;

public class InterruptableTask
{
	private boolean interrupted = true;
	private int progress;
	private List<Runnable> taskList;
	
	public InterruptableTask(Runnable...subTasks)
	{
		this.taskList = Arrays.asList(subTasks);
	}
	
	/** Starts or continues this task. Interruptions are possible. **/
	public void start()
	{
		for(; progress < taskList.size(); progress++)
		{
			taskList.get(progress).run();
			if(interrupted)
			{
				interrupted = false;
				return;
			}
		}
	}
	
	/** Starts and finishes this task. Interruptions are not possible. **/
	public void startAndFinish()
	{
		for(Runnable runnable : taskList)
			runnable.run();
	}
	
	/** Interrupts this task. When we get to the next task section, we will instad return. **/
	public void interrupt()
	{
		interrupted = true;
	}
	
	/** Resets the progress of this task. **/
	public void reset()
	{
		progress = 0;
	}
	
	/** Marks this task as done. **/
	public void markDone()
	{
		this.progress = taskList.size() - 1;
	}
}
