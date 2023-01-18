package generic;

import java.io.FileInputStream;
import java.io.PrintWriter;

public class Statistics {
	
	// TODO add your statistics here
	static int numberOfInstructions;
	static int numberOfCycles;
	static float speedOfProcessor;
	static int timeTaken;
	

	public static void printStatistics(String statFile)
	{
		try
		{
			PrintWriter writer = new PrintWriter(statFile);
			
			writer.println("Number of instructions executed = " + numberOfInstructions);
			writer.println("Number of cycles taken = " + numberOfCycles);
			
			// TODO add code here to print statistics in the output file
			writer.println(("Time/Clock cycles taken by processor = " + timeTaken));
			writer.println(("Speed of processor = " + speedOfProcessor));
			
			writer.close();
		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}
	
	// TODO write functions to update statistics
	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static void setSpeedOfProcessor(float speedOfProcessor) {
		Statistics.speedOfProcessor = speedOfProcessor;
	}

	public static void setTime(int timeTaken) {
		Statistics.timeTaken = timeTaken;
	}
}
