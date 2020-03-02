/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The CoffeeMachine class used in A2B - utilizes monitors to update time and regulate client thread execution 
 */

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class CoffeeMachine
{
	private ArrayList<Client> clientList; //stores all clients 
	private int dispensersOccupied; //identifies the number of dispensers being used 
	private int totalBrewing; //tracks the total number of clients that have brewed (used in regulating thread execution)
	private int machineTemp; //identifies machine temperature -- 0 for undefined : 1 for hot : -1 for cold
	private int currentTime; //identifies the current time within the simulation 
	private Client dispenser1 = new Client(); //identifies the client using dispenser1
	private Client dispenser2 = new Client(); //identifies the client using dispenser2
	
	
	/** MAIN METHODS **/
	
	//increments time based on the finish time of a client c, thus "finishing" their brew - updates related variables accordingly 
	//used in customer thread run() method - synchronized to regulate thread access - acts as a monitor 
	public synchronized void finishBrewing(Client c)
	{
		//CASE 1: Two dispensers occupied --> find which one has the client with the closest finish time and finish brewing

		if (dispensersOccupied == 2)
		{
			if ((dispenser1 != null) && (dispenser2 != null)) //null check to prevent NullPointerException 
			{
				if (dispenser1.getFinishTime() < dispenser2.getFinishTime()) //1 < 2 --> remove from 1 
				{
					if (dispenser1.getFinishTime() > currentTime) //ensure time should be updated 
						currentTime = dispenser1.getFinishTime(); //update current time 
					dispenser1 = null; //remove client from dispenser 1
					dispensersOccupied = 1; //update dispensersOccupied
				}
				else if (dispenser1.getFinishTime() > dispenser2.getFinishTime()) //1 > 2 --> remove from 2 
				{
					if (dispenser2.getFinishTime() > currentTime) //ensure time should be updated 
						currentTime = dispenser2.getFinishTime(); //update current time 
					dispenser2 = null; //remove client from dispenser 2
					dispensersOccupied = 1; //update dispensersOccupied
				}
				else //1 = 2 --> remove from both 
				{			 
					if (dispenser1.getFinishTime() > currentTime) //ensure time should be updated 
						currentTime = dispenser1.getFinishTime(); //set current time to dispenser1 finish time (does not matter which dispenser as they should both be the same in this case)
					dispenser1 = null; //remove client from dispenser 1
					dispenser2 = null; //remove client from dispenser 2
					dispensersOccupied = 0; //update dispensersOccupied
				}
			}
		}
	
		//CASE 2: One dispenser occupied --> find which one is occupied and finish brewing
		
		else if (dispensersOccupied == 1)
		{
			if (dispenser1 != null) //check if dispenser 1 is occupied
			{
				if (dispenser1.getFinishTime() > currentTime) //ensure time should be updated 
					currentTime = dispenser1.getFinishTime(); //update current time 
				dispenser1 = null; //remove client from dispenser 1
				dispensersOccupied = 0; //update dispensersOccupied
			}
			else if (dispenser2 != null)
			{
				if (dispenser1.getFinishTime() > currentTime) //ensure time should be updated 
					currentTime = dispenser1.getFinishTime(); //update current time 
				dispenser2 = null; //remove client from dispenser 2
				dispensersOccupied = 1; //update dispensersOccupied
			}
		}
	}

	//creates client threads and executes them in order of ID as specified in A2C specs & manages dispenser use - will execute until totalBrewing = clientList.size()
	//used in A2C to begin CoffeeMachine simulation - acts as a monitor 
	public void startBrewing()
	{
		do
		{	
			//CASE 1: One (or both) dispenser(s) available to use --> create client thread & run 
			if (dispensersOccupied < 2) //1 or 2 dispensers are available 
			{
				if (dispensersOccupied == 0) //if there are 2 dispensers available, try create two clients 
				{
					machineTemp = clientList.get(totalBrewing).getTemp();//set machine temperature 
					clientList.get(totalBrewing).setBrewing(true); //flag that client has started brewing 
					clientList.get(totalBrewing).calculateFinishTime(currentTime); //calculate the time it will take to brew (based on brew time + current time)
					
					dispenser1 = clientList.get(totalBrewing); //assign client to dispenser1 
					dispensersOccupied++; //update dispensersOccupied
					System.out.println("(" + currentTime + ") " + dispenser1.getID() + " uses dispenser 1 (time: " + dispenser1.getBrewTime() + ")"); //print current time, id, brewtime as per A2C specs 
					
					Thread thread = new Thread(clientList.get(totalBrewing)); //create client thread 
					thread.start(); //execute client thread 
					
					totalBrewing++; //increment total clients brewing 
					
					if (clientList.get(totalBrewing).getTemp() == machineTemp) //check that next client has the same brew temp as the machine 
					{
						clientList.get(totalBrewing).setBrewing(true); //flag that client has started brewing 
						clientList.get(totalBrewing).calculateFinishTime(currentTime); //calculate the time it will take to brew (based on brew time + current time)
						
						dispenser2 = clientList.get(totalBrewing); //assign client to dispenser2
						dispensersOccupied++; //increment dispensersOccupied
						System.out.println("(" + currentTime + ") " + dispenser2.getID() + " uses dispenser 2 (time: " + dispenser2.getBrewTime() + ")"); //print current time, id, brewtime as per A2C specs 
						
						thread = new Thread(clientList.get(totalBrewing)); //create client thread 
						thread.start(); //execute client thread 
						
						totalBrewing++; //increment total clients brewing 
					}
				}
				else if (clientList.get(totalBrewing).getTemp() == machineTemp) //else there is 1 available, check that client brew temp matches current machine temp 
				{
					clientList.get(totalBrewing).setBrewing(true); //flag that client has started brewing 
					clientList.get(totalBrewing).calculateFinishTime(currentTime); //calculate the time it will take to brew (based on brew time + current time)
					dispensersOccupied++; //increment dispensersOccupied
					
					if (dispenser1 != null) //check if dispenser1 is occupied --> assign to dispenser2
					{
						dispenser2 = clientList.get(totalBrewing); //assign client to dispenser2
						System.out.println("(" + currentTime + ") " + dispenser2.getID() + " uses dispenser 2 (time: " + dispenser2.getBrewTime() + ")"); //print current time, id, brewtime as per A2C specs 
					}
					else if (dispenser2 != null) //check if dispenser2 is occupied --> assign to dispenser1
					{
						dispenser1 = clientList.get(totalBrewing); //assign client to dispenser1
						System.out.println("(" + currentTime + ") " + dispenser1.getID() + " uses dispenser 1 (time: " + dispenser1.getBrewTime() + ")"); //print current time, id, brewtime as per A2C specs 
					}

					Thread thread = new Thread(clientList.get(totalBrewing)); //create client thread 
					thread.start(); //execute client thread
					
					totalBrewing++; //increment total clients brewing 
				}
				
				//CASE 2: Both dispensers unavailable --> wait until they are 
				else 
				{
					try
					{
						Thread.sleep(50); //sleep for arbitrary amount of time 
					}
					catch(InterruptedException e){}	
				}	
			}
		}
		while(totalBrewing < clientList.size());
		
		//find last client to finish and print "(N) DONE"
		int longestBrew = 0;
		for (Client c : clientList)
		{
			if (longestBrew < c.getFinishTime()) //finds last client to finish brewing from list 
				longestBrew = c.getFinishTime();
		}
		System.out.println("(" + longestBrew + ") DONE"); //prints finish time 
	}

	/** GETTERS & SETTERS **/

	public void setList(ArrayList<Client> cl)
	{
		clientList = cl;
	}
	
	public ArrayList<Client> getList()
	{
		return clientList;
	}
	
	public void setDispensersOccupied(int dsp)
	{
		dispensersOccupied = dsp;
	}
	
	public int getDispensersOccupied()
	{
		return dispensersOccupied;
	}
	
	public void setTotalBrewing(int tb)
	{
		totalBrewing = tb;
	}
	
	public int getTotalBrewing()
	{
		return totalBrewing;
	}
	
	public void setMachineTemp(int mt)
	{
		machineTemp = mt;
	}
	
	public int getMachineTemp()
	{
		return machineTemp;
	}
	
	public void setCurrentTime(int ct)
	{
		currentTime = ct;
	}
	
	public int getCurrentTime()
	{
		return currentTime;
	}
}