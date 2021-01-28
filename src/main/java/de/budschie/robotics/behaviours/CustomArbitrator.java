package de.budschie.robotics.behaviours;

import lejos.robotics.subsumption.Behavior;

public class CustomArbitrator
{
	Behavior[] behaviours;
	
	public CustomArbitrator(Behavior...behaviours)
	{
		this.behaviours = behaviours;
	}
	
	public void start()
	{
		flag:
		while(true)
		{
			int currentActiveBehaviour = -1;
			
			loop:
			for(int i = 0; i < behaviours.length; i++)
			{
				if(behaviours[i].takeControl())
				{
					//System.out.println("Taking control");
					//currentActiveBehaviour = Math.max(currentActiveBehaviour, i);
					
					currentActiveBehaviour = i;
					break loop;
				}
			}
			
			if(currentActiveBehaviour == -1)
			{
				System.out.println("Exit because none was true");
				break flag;
			}
			else
				behaviours[currentActiveBehaviour].action();
		}
	}
}
