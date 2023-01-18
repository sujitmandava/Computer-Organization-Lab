package generic;

public class ExecutionCompleteEvent extends Event {
	
	int instruction, aluResult, remainder,op2,branchTarget;
	boolean isBranchTaken;
	
	
	public ExecutionCompleteEvent(long eventTime, Element requestingElement, Element processingElement, 
	int instruction, int aluResult, int remainder, int op2, boolean isBranchTaken, int branchTarget)
	{
		super(eventTime, EventType.ExecutionComplete, requestingElement, processingElement);
		this.instruction = instruction;
		this.aluResult = aluResult;
		this.remainder =remainder;
		this.op2 =op2;
		this.isBranchTaken = isBranchTaken;
		this.branchTarget = branchTarget;
	}

	public int getInstruction ()
	{
		return instruction;
	}

	public int getAluResult ()
	{
		return aluResult;
	}

	public int getRemainder ()
	{
		return remainder;
	}

	public int getbranchTarget ()
	{
		return branchTarget;
	}

	public boolean isBranchTaken ()
	{
		return isBranchTaken;
	}

	public int getOp2 ()
	{
		return op2;
	}


}
