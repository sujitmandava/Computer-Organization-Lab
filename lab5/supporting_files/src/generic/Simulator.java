package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import processor.Clock;
import processor.Processor;

public class Simulator {

	static Processor processor;
	static boolean simulationComplete;
	static int dataHazards;
	static int wrongBranchPaths;
	static int instructions;

	//+
	static EventQueue eventQueue;

	public static void setupSimulation(String assemblyProgramFile, Processor p) {
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);

		simulationComplete = false;
		
		//+
		eventQueue = new EventQueue();
	}

	static void loadProgram(String assemblyProgramFile) {
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 * in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 * x0 = 0
		 * x1 = 65535
		 * x2 = 65535
		 */

		// Setting registers x0, x1, x2
		Simulator.processor.getRegisterFile().setValue(0, 0);
		Simulator.processor.getRegisterFile().setValue(1, 65535);
		Simulator.processor.getRegisterFile().setValue(2, 65535);
		instructions = 0;
		dataHazards = 0;
		wrongBranchPaths = 0;
		try {
			DataInputStream dataInput = new DataInputStream(new FileInputStream(assemblyProgramFile));
			try {
				Simulator.processor.getRegisterFile().setProgramCounter(dataInput.readInt()); // First integer of file is header; set to PC
				int currentAddress = 0; // Current address variable
				while (dataInput.available() > 0) { // While integers are present to be read
					Simulator.processor.getMainMemory().setWord(currentAddress, dataInput.readInt()); // Storing integers in mainMemory
					currentAddress++;
				}
			} catch (Exception e) {
				Misc.printErrorAndExit(e.toString());
			}
			dataInput.close();
		} catch (Exception e) {
			Misc.printErrorAndExit(e.toString());
		}
	}

	public static void simulate() {
		
		int i =0;
		while (simulationComplete == false) {
			System.out.println("Simulation Complete: False");
			System.out.println(processor.getRegisterFile().getContentsAsString());
			System.out.println(processor.getMainMemory().getContentsAsString(0, 7));

			processor.getRWUnit().performRW();
			System.out.println("----------------");
			processor.getMAUnit().performMA();
			System.out.println("----------------");
			processor.getEXUnit().performEX();
			System.out.println("----------------");
			
			//+
			eventQueue.processEvents();
			// System.out.println("----------------");
			processor.getOFUnit().performOF();
			System.out.println("----------------");
			processor.getIFUnit().performIF();
			System.out.println("////////////////\n");
			Clock.incrementClock();
			
			i++;
			// instructions++;
			System.out.println("<"+i+">");
			// if(i==80)
			// {
			// 	break;
			// }
		}

		// TODO
		// set statistics
		// Redo statistics
		Statistics.setNumberOfCycles((int) Clock.getCurrentTime());
		Statistics.setNumberOfInstructions(instructions);
		Statistics.setDataHazards(dataHazards);
		Statistics.setWBPs(wrongBranchPaths);
		// Statistics.setTime((int) Clock.getCurrentTime());
		Statistics.setSpeedOfProcessor((float)instructions / (float) Clock.getCurrentTime());
	}

	public static void setSimulationComplete(boolean value) {
		simulationComplete = value;
	}

	public static void incrementInstructions() {
		instructions++;
	}

	public static void incrementDataHazards() {
		dataHazards++;
	}

	public static void incrementWBP() {
		wrongBranchPaths++;
	}

	//+
	public static EventQueue getEventQueue(){
		return eventQueue;
	}
}
