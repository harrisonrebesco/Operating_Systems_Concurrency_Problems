/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The Client class used in A2C - utilizes monitors to simulate a group of clients brewing coffee 
 */

import java.util.concurrent.Semaphore;

public class Client implements Runnable
{
	private String id; //identifies client 
	private int temp; //identifies clients preferred brew temperature -- 0 for undefined : 1 for hot : -1 for cold
	private int brewTime; //identifies time required to brew coffee
	private int finishTime; //idenfities time client will finish brewing 
	private boolean brewing; //flag to show client is brewing 
	private CoffeeMachine machine; //links client to coffee machine, facilitaing communication between client and machine
	
	/** CONSTRUCTORS **/
	
	//default constructor 
	Client(){}
	
	//constructor taking id, brew time, and coffee machine 
	//used in A2C to create client while being read from file 
	Client(String i, int bt, CoffeeMachine cm)
	{
		//setting variables.. 
		id = i; 
		if (i.charAt(0) == 'H')
			temp = 1; //set temp hot if id contains H
		else 
			temp = -1; //set temp cold if id doesnt contain H 
		brewTime = bt;
		machine = cm;
	}
	
	/** MAIN FUNCTIONS **/
	
	//calculate time taken to brew coffee by adding brew time to the current time (which will be time thread is executed)
	//used in CoffeeMachine startBrewing() when client thread is about to be executed - synchronized to regulate thread access 
	public synchronized void calculateFinishTime(int ct)
	{
		finishTime = brewTime + ct;
	}
	
	//override of run method to control client thread behaviour 
	//used to simulate brewing of coffee 
	@Override 
	public void run()
	{
		while(brewing) //while thread is brewing 
		{
			if (machine.getCurrentTime() < finishTime) //check that it hasnt finished brewing 
			{
				machine.finishBrewing(this); //if it hasnt .. finish 
				try
				{
					Thread.sleep(200); //sleep for arbitrary amount of time so threads can catch up 
				}
				catch(InterruptedException e){}
			}
			else 
				brewing = false; //if finished --> set to not brewing 
		}
	}
	
	/** GETTERS & SETTERS **/
	
	public void setID(String i)
	{
		id = i;
	}
	
	public String getID()
	{
		return id;
	}
	
	public void setTemp(int t)
	{
		temp = t;
	}
	
	public int getTemp()
	{
		return temp;
	}
	
	public void setBrewing(boolean b)
	{
		brewing = b;
	}
	
	public boolean isBrewing()
	{
		return brewing;
	}

	public void setBrewTime(int bt)
	{
		brewTime = bt;
	}
	
	public int getBrewTime()
	{
		return brewTime;
	}
	
	public void setFinishTime(int ft)
	{
		finishTime = ft;
	}
	
	public int getFinishTime()
	{
		return finishTime;
	}
}