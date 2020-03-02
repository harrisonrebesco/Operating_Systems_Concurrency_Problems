/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The driving class for A2C - reads input from file to create clients, places them in a ice cream parlour which then "feeds customers". 
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class A2C
{
	public static void main(String args[]) throws Exception 
	{	
		ArrayList<Client> clientList = new ArrayList<Client>(); //list to store all clients read from file 
		CoffeeMachine machine = new CoffeeMachine(); //machine for customers to brew coffee with 
		
		readFile(clientList, machine, args[0]); //reads clients from file 
		machine.setList(clientList); //update client list in machine to give access to clients 
		
		machine.startBrewing(); //runs simulation, by creating and managing clients 
	}
	
	//reads clients from file to create client object, adds them to clientList
	//this is used in main, will be passed a valid file location (should always be args[0])
	public static void readFile(ArrayList<Client> cl, CoffeeMachine cm, String f)
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
        
		int total = Integer.parseInt(fileReader.next()); //read expected number of clients 
		for (int i = 0; i < total; i ++) //loops for total amount
		{
			String id = fileReader.next(); //read customer id 
			int bt = Integer.parseInt(fileReader.next()); //read brew time 
			
			Client c = new Client(id, bt, cm); //create client with id, brewtime, coffee machine
			cl.add(c); //add client to client list 
        }
	}
}