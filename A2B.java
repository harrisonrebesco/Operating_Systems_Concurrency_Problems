/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The driving class for A2B - reads input from file to create customers, places them in a ice cream parlour which then "feeds customers". 
 */
 
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class A2B
{
	public static void main(String args[]) throws Exception 
	{
		ArrayList<Customer> customerList = new ArrayList<Customer>(); //list used to store all customers from file 
		IceCreamParlour parlour = new IceCreamParlour(); //parlour used to "seat" customers and help run simulation 
		
		readFile(customerList, parlour, args[0]); //read customers from file 
		parlour.setList(customerList); //update customer list in parlour to give access to customers 
		
		parlour.feedCustomers(); //runs simulation, by creating and managing customers 
	}
	
	//reads customers from file to create customer object, adds them to customerList
	//this is used in main, will be passed a valid file location (should always be args[0])
	public static void readFile(ArrayList<Customer> cl, IceCreamParlour icp, String f)
	{
		Scanner fileReader = null;
		
		//STEP 1: Open file
		
		try
        {
            fileReader = new Scanner(new File(f)); //opens file 
        }
        catch (Exception e)
        {
            System.out.println("File could not be found."); //accounts for missing file
			System.exit(0); //ends program if file not found
        }
		
		//STEP 2: Read file
        
		while (fileReader.hasNext()) //traverse file 
        {
			String input = fileReader.next(); //reads next input 

			if (!input.equals("END")) //loop to end of file 
			{	
				int arrivalTime = Integer.parseInt(input); //get customer arrival time 
				String id = fileReader.next(); //get customer id 
				int eatingTime = Integer.parseInt(fileReader.next()); //get customer eating time 
				Customer c = new Customer(arrivalTime, id, eatingTime, icp); //create customer with arrival time, id, eating time, and parlour 
				cl.add(c); //add customer to customer list 
			}
        }
	}
}