package de.budschie.robotics.event_handling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

// Patented by Budschie Inc.
// TODO: Adding events to the event handler base class. (jk)
public abstract class AbstractEvent<E>
{
	/** Why doesn't java have unsigned integers??? Btw init with {@link Integer#MIN_VALUE} here because we can subscribe to the doubled amount of methods than with 0. **/
	private int methodId = Integer.MIN_VALUE;
	private HashMap<Integer, Consumer<E>> subscribedMethods = new HashMap<>();
	
	// We need this queue so that we can unsubscribe without creating an ConcurrentModificationException if we unsubscribe/subscribe while we process the events
	private ArrayList<SubscriptionPair<E>> subscribingQueue = new ArrayList<>();
	private ArrayList<Integer> unsubscribingQueue = new ArrayList<>();
	
	// Indicates if AbstractEvent#fire is currently processing all events. This is the case when block is true.
	private boolean block = false;
	
	// TACTICAL NUKE... INCOMING! This flag indicates whether we should unsubscribe all methods after AbstractEvent#fire has processed all events.
	private boolean tacticalNuke = false;
	
	/** Fires the given event, causing it to call all subscribed methods with the args as an parameter. **/
	public void fire(E args)
	{
		block = true;

		for(Consumer<E> method : subscribedMethods.values())
		{
			// Calling event handlers, listeners, whatever.
			method.accept(args);
		}
		
		block  = false;
		processQueues();
	}
	
	private void processQueues()
	{
		if(tacticalNuke)
		{
			unsubscribeAll();
			// Setting flag to false again
			tacticalNuke = false;
		}
		else
		{
			// Reason why we don't add after a tactical nuke is that if we want to clear everything, we really want to clean everything. Besides, the methodId is likely not valid anymore.
			for(SubscriptionPair<E> subscriptionPair : subscribingQueue)
			{
				// We don't use AbstractEvent#subscribe, as we already have calculated methodId.
				subscribedMethods.put(subscriptionPair.methodId, subscriptionPair.method);
			}
			
			for(int unsubscribeMethodId : unsubscribingQueue)
			{
				unsubscribe(unsubscribeMethodId);
			}
		}
	}
	
	/** Subscribes this method and returns the {@code methodId}, which is used to identify and unsubscribe from this event. **/
	public int subscribe(Consumer<E> method)
	{
		int usedId = methodId;
		methodId++;
		
		if(block)
		{
			SubscriptionPair<E> pair = new SubscriptionPair<>();
			pair.method = method;
			pair.methodId = usedId;
			subscribingQueue.add(pair);
		}
		else
		{
			subscribedMethods.put(methodId, method);
		}
		
		return usedId;
	}
	
	/** Unsubscribes the method with the given id. This causes the method to not be called from this{@link #fire(Object)} **/
	public void unsubscribe(int methodId)
	{
		// We are not decrementing methodId, as it is probably still used.
		if(block)
		{
			unsubscribingQueue.add(methodId);
		}
		else
		{
			subscribedMethods.remove(methodId);
		}
	}
	
	/** This unsubscribes all methods and resets the methodId counter. **/
	public void unsubscribeAll()
	{
		// Resetting methodId
		if(block)
		{
			tacticalNuke = true;
		}
		else
		{
			methodId = Integer.MIN_VALUE;
			subscribedMethods.clear();
		}
	}
	
	public static class SubscriptionPair<E>
	{
		int methodId;
		Consumer<E> method;
		
		private SubscriptionPair()
		{
			
		}
	}
}
