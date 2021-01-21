package de.budschie.robotics.util;

import java.util.ArrayList;

public class SafeReadWriteAccess<E>
{
	boolean currentlyReading = false;
	ArrayList<E> mainElements;
	ArrayList<E> removalQueue = new ArrayList<>();
	ArrayList<E> addingQueue = new ArrayList<>();
	
	public SafeReadWriteAccess()
	{
		this.mainElements = new ArrayList<>();
	}
	
	public SafeReadWriteAccess(ArrayList<E> elements)
	{
		this.mainElements = elements;
	}
	
	public ArrayList<E> getMainElements()
	{
		return mainElements;
	}
	
	public void setCurrentlyReading(boolean currentlyReading)
	{
		this.currentlyReading = currentlyReading;
	}
	
	public void add(E element)
	{
		if(currentlyReading)
			addingQueue.add(element);
		else
			mainElements.add(element);
	}
	
	public void remove(E element)
	{
		if(currentlyReading)
			removalQueue.add(element);
		else
			mainElements.remove(element);
	}
	
	public void process()
	{
		mainElements.addAll(addingQueue);
		addingQueue.clear();
		mainElements.removeAll(removalQueue);
		removalQueue.clear();
	}
}
