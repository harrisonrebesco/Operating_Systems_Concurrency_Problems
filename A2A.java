/* COMP2240 - ASSIGNMENT 2
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 7/10/19
 * Description: The driving class for A2A - reads input to create and populate a specified number of north and south Farmers, then executes them as threads. 
 */

import java.io.*;

public class A2A
{
	public static void main(String args[]) throws Exception 
	{	
		int n = Integer.parseInt(args[0].substring(2)); //reads the integer number of north farmers from console (N=X --> X)
		int s = Integer.parseInt(args[1].substring(2)); //reads the integer number of south farmers from console (S=X --> X)
		Farmer[] farmerPool = new Farmer [n + s]; //creates array of all north and south farmers 
		
		//create n number of north Farmers 
		for (int i = 0; i < n; i++)
			farmerPool[i] = new Farmer("N_Farmer" + (i + 1), "North"); //creates a farmer with id & starting location
		
		//create s number of south Farmers 
		for (int i = 0; i < s; i++)
			farmerPool[i + n] = new Farmer("S_Farmer" + (i + 1), "South"); //creates a farmer with id & starting location 
		
		//execute all farmers as threads 
		for (int i = 0; i < s + n; i++)
		{
			Thread thread = new Thread(farmerPool[i]); //create farmer thread 
			thread.start(); //execute farmer thread 
		}
	}
}