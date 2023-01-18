package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	boolean isStall;
	int instruction;
	int op1;
	int op2;
	int immx;
	int branchTarget;
	int isImmediate;
	boolean EX_busy=false;
	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	
	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getOp1() {
		return op1;
	}

	public void setOp1(int op1) {
		this.op1 = op1;
	}

	public int getOp2() {
		return op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getImmx() {
		return immx;
	}

	public void setImmx(int immx) {
		this.immx = immx;
	}

	public int getIsImmediate() {
		return isImmediate;
	}

	public void setIsImmediate(int isImmediate) {
		this.isImmediate = isImmediate;
	}

	public int getBranchTarget() {
		return branchTarget;
	}

	public void setBranchTarget(int branchTarget) {
		this.branchTarget = branchTarget;
	}
	
	public boolean getStall()	{
		return isStall;
	}

	public void setStall(boolean isStall) {
		this.isStall = isStall;
	}

    public boolean isEX_busy() {
        return EX_busy;
    }

    public void setEX_Busy(boolean eX_busy) {
		EX_busy = eX_busy;
    }

	// public void 

}
