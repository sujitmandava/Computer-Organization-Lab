package processor.pipeline;

public class EX_MA_LatchType {

	boolean MA_enable;
	boolean isStall;
	boolean isBusy;
	int aluResult, op2, instruction,remainder;
	// boolean isLd, isSt;

	public EX_MA_LatchType() {
		MA_enable = false;
		isStall = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public int getOp2() {
		return op2;
	}

	public void setOp2(int op2) {
		this.op2 = op2;
	}

	public int getAluResult() {
		return aluResult;
	}

	public void setAluResult(int aluResult) {
		this.aluResult = aluResult;
	}

	public int getRemainder() {
		return remainder;
	}

	public void setRemainder(int remainder) {
		this.remainder = remainder;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean getStall()	{
		return isStall;
	}

	public void setStall(boolean isStall) {
		this.isStall = isStall;
	}

	public boolean getMABusy()	{
		return isBusy;
	}

	public void setMABusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

}
