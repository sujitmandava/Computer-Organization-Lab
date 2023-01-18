package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import processor.Clock;
import processor.Processor;

public class Simulator {

	static Processor processor;
	static boolean simulationComplete;

	public static void setupSimulation(String assemblyProgramFile, Processor p) {
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);

		simulationComplete = false;
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
		
		int instructions = 0;
		
		while (simulationComplete == false) {
			//System.out.println(processor.getRegisterFile().getContentsAsString());
			//System.out.println(processor.getMainMemory().getContentsAsString(0, 7));
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			
			instructions++;
			//System.out.println("<"+instructions+">");
		}

		// TODO
		// set statistics
		Statistics.setNumberOfCycles(instructions);
		Statistics.setNumberOfInstructions(instructions);
		Statistics.setTime((int) Clock.getCurrentTime());
		Statistics.setSpeedOfProcessor((float)instructions / (float) Clock.getCurrentTime());
	}

	public static void setSimulationComplete(boolean value) {
		simulationComplete = value;
	}
}
