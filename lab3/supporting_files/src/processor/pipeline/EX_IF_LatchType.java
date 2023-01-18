package processor.pipeline;

public class EX_IF_LatchType {
	
	int PC;
	boolean isBranch;
	boolean IF_enable;

	public EX_IF_LatchType()
	{
		IF_enable = false;
		isBranch = false;
	}
	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}
	
	public int getPC() {
		return PC;
	}

	public void setPC(int PC) {
		this.PC = PC;
	}

	public boolean getIsBranch() {
		return isBranch;
	}

	public void setIsBranch(Boolean isBranch) {
		this.isBranch = isBranch;
	}

}
