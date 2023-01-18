package processor;

import processor.memorysystem.MainMemory;
import processor.pipeline.EX_IF_LatchType;
import processor.pipeline.EX_MA_LatchType;
import processor.pipeline.Execute;
import processor.pipeline.IF_EnableLatchType;
import processor.pipeline.IF_OF_LatchType;
import processor.pipeline.InstructionFetch;
import processor.pipeline.MA_RW_LatchType;
import processor.pipeline.MemoryAccess;
import processor.pipeline.OF_EX_LatchType;
import processor.pipeline.OperandFetch;
import processor.pipeline.RegisterFile;
import processor.pipeline.RegisterWrite;
import configuration.Configuration;
import processor.memorysystem.Cache;

public class Processor {
	
	RegisterFile registerFile;
	MainMemory mainMemory;
	
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	MA_RW_LatchType MA_RW_Latch;
	Cache L1i;
	Cache L1d;
	
	InstructionFetch IFUnit;
	OperandFetch OFUnit;
	Execute EXUnit;
	MemoryAccess MAUnit;
	RegisterWrite RWUnit;
	
	public Processor()
	{
		registerFile = new RegisterFile();
		mainMemory = new MainMemory();
		L1i = new Cache(Configuration.L1i_numberOfLines, this, 0);
		L1d = new Cache (Configuration.L1d_numberOfLines,this, 1);
		
		IF_EnableLatch = new IF_EnableLatchType();
		IF_OF_Latch = new IF_OF_LatchType();
		OF_EX_Latch = new OF_EX_LatchType();
		EX_MA_Latch = new EX_MA_LatchType();
		EX_IF_Latch = new EX_IF_LatchType();
		MA_RW_Latch = new MA_RW_LatchType();
		
		IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
		OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch, MA_RW_Latch ,EX_MA_Latch, EX_IF_Latch, IF_EnableLatch); // Added MA_RW Latch as trial in OF phase
		EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch,IF_EnableLatch);
		MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch, IF_EnableLatch);
		RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch);
	}
	
	public void printState(int memoryStartingAddress, int memoryEndingAddress)
	{
		System.out.println(registerFile.getContentsAsString());

		System.out.println(mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));		
	}

	public RegisterFile getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}

	public MainMemory getMainMemory() {
		return mainMemory;
	}

	public void setMainMemory(MainMemory mainMemory) {
		this.mainMemory = mainMemory;
	}

	public InstructionFetch getIFUnit() {
		return IFUnit;
	}

	public OperandFetch getOFUnit() {
		return OFUnit;
	}

	public Execute getEXUnit() {
		return EXUnit;
	}

	public MemoryAccess getMAUnit() {
		return MAUnit;
	}

	public RegisterWrite getRWUnit() {
		return RWUnit;
	}

	public Cache getL1iCache() {
		return L1i;
	}

	public void setL1iCache(Cache L1i) {
		this.L1i = L1i;
	}

	public Cache getL1dCache() {
		return L1d;
	}

	public void setL1dCache(Cache L1d) {
		this.L1d = L1d;
	}

}
