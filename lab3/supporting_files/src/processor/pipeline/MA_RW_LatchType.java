package processor.pipeline;

public class MA_RW_LatchType {

	boolean RW_enable;
	int ldResult;
	int aluResult;
	int instruction;
	int remainder; // Set this to reg x31
	boolean isLd;

	public MA_RW_LatchType() {
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public int getLdResult() {
		return ldResult;
	}

	public void setLdResult(int ldResult) {
		this.ldResult = ldResult;
	}

	public int getAluResult() {
		return aluResult;
	}

	public void setAluResult(int aluResult) {
		this.aluResult = aluResult;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public int getRemainder() {
		return remainder;
	}

	public void setRemainder(int remainder) {
		this.remainder = remainder;
	}

	public boolean getIsLd() {
		return isLd;
	}

	public void setIsLd(boolean isLd) {
		this.isLd = isLd;
	}

}
