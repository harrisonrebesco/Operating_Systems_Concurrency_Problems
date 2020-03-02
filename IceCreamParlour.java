/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The IceCreamParlour class used in A2B - updates current time and regulates customer thread execution 
 */

import java.util.*;

public class IceCreamParlour
{
	private ArrayList<Customer> customerList; //list containing all customers 
	private int currentTime; //identifies current time 
	private int customersFinished; //tracks number of customers that have finished eating 
	
	/** MAIN METHODS **/
	
	//increments the total number of customers that have finished eating at the parlour 
	//used in customer class when a customer thread has finished eating - syncronized to regulate thread access 
	public synchronized void incrementFinished()
	{
		customersFinished++;
	}
	
	//prints the ID, arrival time, sit time, and leave time of every customer in the parlour. formatted to match provided output 
	//used in feedCustomers - once every customer has finished eating. 
	public void printStatistics()
	{
		System.out.println("Customer   arrives     Seats   Leaves");
		for (Customer c : customerList)
		{
			String output = "";
			output += String.format("%-15s", c.getID()); //format id 
			output += String.format("%-10s", c.getArrivalTime()); //format turn around time 
			output += String.format("%-10s", c.getSitTime()); //format turn around time 
			output += String.format("%-1s", c.getLeaveTime());//calculate & format waiting time (finish time - start time - size)
			System.out.println(output); //add together --> print 
		}
	}
	
	//utilizes current time to sequentially create threads ensuring they arrive at the correct time
	//used in A2B to start ice cream parlour simulation 
	public void feedCustomers()
	{
		while (customersFinished < customerList.size()) //uses customers finished to check that all customers have eated. (once customersFinished = list size --> simulation is over)
		{
			//STEP 1: check all customers in list to see if they have "arrived"
			
			for (Customer c : customerList) 
			{
				if (c.getArrivalTime() == currentTime) //check that customer arrives at correct time 
				{
					Thread thread = new Thread(c); //create new customer thread 
					thread.start(); //execute customer thread 
				}
			}
			
			//STEP 2: after each loop of customer list, increment current time to move simulation forward 
			
			try 
			{
				Thread.sleep(25); //sleep to ensure all threads are caught up 
				currentTime++; //increment time 
			}
			catch (InterruptedException e){}
		}
		
		//once simulation has completed (all customers are finished) print stats
		printStatistics();
	}
	
	/** GETTERS & SETTERS **/
	
	public void setList(ArrayList<Customer> cl)
	{
		customerList = cl;
	}
	
	public ArrayList<Customer> getList()
	{
		return customerList;
	}

	public void setCurrentTime(int ct)
	{
		currentTime = ct;
	}
	
	public int getCurrentTime()
	{
		return currentTime;
	}
	
	public void setCustomersFinished(int cf)
	{
		customersFinished = cf;
	}
	
	public int getCustomersFinished()
	{
		return customersFinished;
	}
}