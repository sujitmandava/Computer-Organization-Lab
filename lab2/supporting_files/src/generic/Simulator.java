package generic;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import generic.Operand.OperandType;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;
	static String outputDestinationFile;

	public static void setupSimulation(String assemblyProgramFile, String objectProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();

		// Initialized address of output file; What we missed earlier
		outputDestinationFile = objectProgramFile;
	}

	/* 
		Format of output -> Header, Data, Text/Instructions

		TODO:
		Handle exceptions
		Read and write instructions in required format
	*/
	
	public static void assemble()
	{
		//TODO your assembler code
		FileOutputStream outputFile = null;
		DataOutputStream dataOutput = null;
		//for storing opcode
		int Opcode=0;
		//for storing encoded instruction
		int instructionEncoded=0;
		Instruction.OperationType opType;
		try {
			// outputFile writes to destination; dataOutput writes to outputFile is what I understood
			// Not fully sure, check out the functionality
			outputFile = new FileOutputStream(outputDestinationFile);
			dataOutput = new DataOutputStream(outputFile);

			try {
				// Getting address of first instruction in main
				int addressMain = ParsedProgram.symtab.get("main");
				// Writing address of first instruction in outputDestinationFile
				dataOutput.writeInt(addressMain);

				// Looping over ParsedProgram.data and ParsedProgram.code; Contains all the data from the assembly code
				// Check ParsedProgram.java for info
				for (int i : ParsedProgram.data) {
					dataOutput.writeInt(i);
				}

				for (Instruction i : ParsedProgram.code) {
					/*
					Each instruction has multiple parameters as listed in instruction.java
					We have to process each instruction using the binary conversion given;
					 */
					//since the enumeration is given in the increasing order of Opcode we can use the ordinal() function
					//which returns the ordinal status of a OperationType
					Opcode = i.getOperationType().ordinal();
					opType = i.getOperationType();
					//see the ToyRISC specification for understanding the why the shift operation and its values
					instructionEncoded = Opcode<<27;
					switch(opType)
					{
						// R3 Type
						case add:
						case sub:
						case mul:
						case div:
						case and:
						case or:
						case xor:
						case slt:
						case sll:
						case srl:
						case sra:{
							//i.getSourceOperand1().getValue returs the value of the source operand1
							//similarly for other instructions
							//see the ToyRISC specification for understanding the why the shift operation and its values
							instructionEncoded += i.getSourceOperand1().getValue()<<22;
							instructionEncoded += i.getSourceOperand2().getValue()<<17;
							instructionEncoded += i.getDestinationOperand().getValue()<<12;
							dataOutput.writeInt(instructionEncoded);
							break;
						}
						//R2I
						case addi:
						case subi:
						case muli:
						case divi:
						case andi:
						case ori:
						case xori:
						case slti:
						case slli:
						case srli:
						case srai:
						case load:
						case store:{
							instructionEncoded += i.getSourceOperand1().getValue()<<22;
							instructionEncoded += i.getDestinationOperand().getValue()<<17;
							if(i.getSourceOperand2().getOperandType()==OperandType.Immediate)
							{
								//(next to line) to the avoid the usage of more than 17 bit by the -ve immediate value 
								int t = i.getSourceOperand2().getValue()<<15;
								t = t>>>15;
								instructionEncoded += t;
							}
							else if (i.getSourceOperand2().getOperandType()==OperandType.Label)
							{
								instructionEncoded += ParsedProgram.symtab.get(i.getSourceOperand2().getLabelValue());
							}
							
							dataOutput.writeInt(instructionEncoded);
							break;
						}
						case beq:
						case bne:
						case blt:
						case bgt:{
							instructionEncoded += i.getSourceOperand1().getValue()<<22;
							instructionEncoded += i.getSourceOperand2().getValue()<<17;
							if(i.getDestinationOperand().getOperandType()==OperandType.Immediate)
							{
								int t = i.getDestinationOperand().getValue()<<15;
								t = t>>>15;
								instructionEncoded += t;
							}
							else if (i.getDestinationOperand().getOperandType()==OperandType.Label)
							{
								//ParsedProgram.symtab.get(i.getDestinationOperand().getLabelValue())-i.getProgramCounter() might prodcue negative values,
								//this created an error in the encoding. It is now fixed after adding the 2 statements (below) (similar edit was made in jmp instruction also)
								int t = ParsedProgram.symtab.get(i.getDestinationOperand().getLabelValue())-i.getProgramCounter()<<15;
								t =t>>>15;
								instructionEncoded += t;
							}
							
							dataOutput.writeInt(instructionEncoded);
							break;

						}
						//RI
						case jmp:{
							if(i.getDestinationOperand().getOperandType()==OperandType.Immediate)
							{
								int t= i.getDestinationOperand().getValue()<<10;
								t= t>>>10;
								instructionEncoded += t;
							}
							else if (i.getDestinationOperand().getOperandType()==OperandType.Label)
							{
								int t = ParsedProgram.symtab.get(i.getDestinationOperand().getLabelValue())-i.getProgramCounter()<<10;
								t= t>>>10;
								instructionEncoded += t;
							}
							else if (i.getDestinationOperand().getOperandType()==OperandType.Register)
							{
								instructionEncoded += i.getDestinationOperand().getValue()<<22;
							}
							dataOutput.writeInt(instructionEncoded);
							break;
						}
						case end:{
							dataOutput.writeInt(instructionEncoded);
							break;
						}
						default: {
							dataOutput.writeInt(0);
							break;
						}

					}
				}

			} catch (Exception e) {
				//TODO: handle exception
				Misc.printErrorAndExit(e.toString());
			}

		} catch (FileNotFoundException e) {
			//TODO: handle exception
			Misc.printErrorAndExit(e.toString());
		}
	}
	
}
