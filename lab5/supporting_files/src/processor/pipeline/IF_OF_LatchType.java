package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	boolean OF_busy=false;
	
	public IF_OF_LatchType()
	{
		OF_enable = false;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

    public boolean isOF_busy() {	//lab5+
        return OF_busy;
    }

    public void setOF_busy(boolean oF_busy) {	//lab5+
        OF_busy = oF_busy;
    }
}
