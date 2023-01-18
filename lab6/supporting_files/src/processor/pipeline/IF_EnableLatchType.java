package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean IF_busy=false;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public void setIF_busy(boolean iF_busy)	//lab5+
	{
		IF_busy = iF_busy;
	}

	public boolean isIF_busy() {	//lab5+
		return IF_busy;
	}

}
