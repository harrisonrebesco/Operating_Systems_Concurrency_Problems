/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The Farmer class used in A2A - utilizes semaphores to simulate a group of farmers crossing a bridge until neon = 100.
 */

import java.util.concurrent.Semaphore;

public class Farmer implements Runnable
{
	private String id; //identifies each farmer 
	private String location; //identifies farmer starting location 
	private String direction; //identifies direction farmer is heading 
	
	//these static variables are shared by all threads, enabling communication between different threads:
	
	private static Semaphore bridge = new Semaphore (1); //bridge semaphore, simulates the "bridge" allowing access to one farmer thread at a time 
	private static int neon; //neon identifies the number of farmers that have crossed the bridge 
	
	/** CONSTRUCTORS **/
	
	//defualt constructor 
	Farmer(){};
	
	//constructor taking id and location
	//used in A2A to create instances of farmers
	Farmer(String i, String l)
	{
		id = i; //set id
		location = l; //set location 
 		
		//farmer direction is opposite to location 
		if(location == "North") 
			direction = "South"; //if location = north, direction = south 
		else 
			direction = "North"; //if location = south, direction = north 
		
		System.out.println(id + ": Waiting for bridge. Going towards " + direction + "."); //notify farmer creation 
	}
	
	/** MAIN METHODS **/
	
	//simulate farmer crossing bridge - prints steps & neon, updates location, direction and neon 
	//used in run()
	public void crossBridge()
	{
		try 
		{
			//print steps and sleep for arbitrary amount of time so prints are readable  
			System.out.println(id + ": Crossing the bridge. Step 5.");
			Thread.sleep(350);
			System.out.println(id + ": Crossing the bridge. Step 10.");
			Thread.sleep(350);
			System.out.println(id + ": Across the bridge.");
			Thread.sleep(350);
			
			//update and print neon one farmer has crossed 
			neon++;
			System.out.println("NEON = " + neon);
			
			//update the location and direction of farmer, as they have just crossed
			if (location == "North")
			{
				location = "South"; 
				direction = "North";
			}
			else 
			{
				location = "North";
				direction = "South";
			}	
		} 
		catch(InterruptedException e){}
	}
	
	//override of run function to control farmer thread behaviour
	//interacts with semaphore to simulate crossing of bridge 
	@Override
	public void run()
	{
		while(neon < 100) //will stop once 100 farmers have crossed (as specified by Dan in tutorials)
		{
			try 
			{
				bridge.acquire(); //attempt to access bridge semaphore 
				crossBridge(); //once thread has access it will cross bridge 
				bridge.release(); //release access to bridge semaphore, allowing next thread to cross 
			} 
			catch(InterruptedException e){}
			
			System.out.println(id + ": Waiting for bridge. Going towards " + direction + "."); //print new direction of farmer 
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
	
	public void setLocation(String l)
	{
		location = l;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public void setDirection(String d)
	{
		direction = d;
	}
	
	public String getDirection()
	{
		return direction;
	}
}
