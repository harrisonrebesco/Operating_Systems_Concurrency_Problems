/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The Customer class used in A2B - utilizes semaphores to simulate a group of customers eating icecream at a table 
 */
 
import java.util.concurrent.Semaphore;
import java.util.*;

public class Customer implements Runnable  
{
	private String id; //identifies each customer 
	private int arrivalTime; //identifies time customer arrives 
	private int eatingTime; //identifies how long customer eats for 
	private int sitTime; //identifies time customer sits 
	private int leaveTime; //identifies time customer leaves 
	private boolean eating; //flag to show that customer is eating 
	private IceCreamParlour parlour; //links customer to ice cream parlour, facilitaing communication between customer and parlour 

	//these static variables are shared by all threads, enabling communication between different threads: 
	
	private static int customersWaiting; //total customers waiting 
	private static int customersEating; //total customers eating 
	private static boolean tableFull; //used as a flag to stop customers eating when table is full 
	private static Semaphore customer = new Semaphore(1, true); //fair semaphore managing customers 
	private static Semaphore table = new Semaphore(0, true); //fair semaphore managing table 
	
	/** CONSTRUCTORS **/
	
	//default constructor
	Customer(){}
	
	//constructor taking ID, arrivalTime, eatingTime, parlour
	//used to create instance of customer when reading from file 
	Customer(int a, String i, int e, IceCreamParlour p)
	{
		//setting variables... 
		id = i;
		arrivalTime = a;
		eatingTime = e;
		parlour = p;
	}
	
	/** MAIN FUNCTIONS **/
	
	//override of run method to control customer thread behaviour 
	//uses static variables and parlour to simulate ice cream comsumtion  
	@Override 
	public void run()
	{
		try
		{	
			//SEMAPHORE MANIPULATION: 
	
			customer.acquire(); //reserve customer semaphore 

			if (tableFull) //check table status --> if table semaphore is reserved, wait until available.
			{	
				customersWaiting++; //table is full, so customer is waiting --> update wait counter
				customer.release(); //release customer semaphore 
				table.acquire(); //reserve table semaphore --> this forces customer thread to wait until table is eventually available
				customersWaiting--; //once table is available, customer will be able to eat --> update wait counter
			} 
			
			customersEating++; //table isnt full, so customer isnt waiting --> update eat counter
			
			if (customersEating == 4) 
				tableFull = true; //if 4 customers are eating --> table is full 
			else 
				tableFull = false; //if < 4 customers are eating --> table isnt full 

			if (customersWaiting > 0 && !tableFull) 
				table.release(); //if there are customers waiting and table isnt full --> release table semaphore 
			else 
				customer.release(); //release customer semaphore 
			
			//SIMULATE EATING: 
		
			sitTime = parlour.getCurrentTime(); //get time customer is seated (used to calculate leave time)
			leaveTime = sitTime + eatingTime; //calculate leave time 
			eating = true; //flag customer as 'eating'
			
			while (eating) //make thread wait while customer is eating 
			{
				if (parlour.getCurrentTime() < leaveTime) //check current time against expected leave time 
					Thread.sleep(1);
				else 
					eating = false;
			}	
			
			//MORE SEMAPHORE MANIPULATION: 
			
			customer.acquire(); //reserve customer semaphore
			customersEating--; //customer should be finished --> update eat counter
			parlour.incrementFinished(); //notify parlour customer is finished 
			
			if (customersEating == 0) //if no customers are eating --> table is free 
				tableFull = false;
			
			if (customersWaiting > 0 && !tableFull) //if there are customers waiting & table is free --> release table semaphore allowing customers to be seated 
				table.release();
			else 
				customer.release();
		}
		catch(InterruptedException e){}
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

	public void setEatingTime(int et)
	{
		eatingTime = et;
	}
	
	public int getEatingTime()
	{
		return eatingTime;
	}
	
	public void setArrivalTime(int at)
	{
		arrivalTime = at;
	}
	
	public int getArrivalTime()
	{
		return arrivalTime;
	}

	public void setSitTime(int st)
	{
		sitTime = st;
	}
	
	public int getSitTime()
	{
		return sitTime;
	}
	
	public void setLeaveTime(int lt)
	{
		leaveTime = lt;
	}
	
	public int getLeaveTime()
	{
		return leaveTime;
	}

	public void setEating(boolean e)
	{
		eating = e;
	}
	
	public boolean isEating()
	{
		return eating;
	}
}