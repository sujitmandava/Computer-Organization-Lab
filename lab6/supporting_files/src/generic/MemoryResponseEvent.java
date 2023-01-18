package generic;

public class MemoryResponseEvent extends Event {

	int value;
	int addressReadFrom;
	
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value,int addressReadFrom) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
		this.addressReadFrom = addressReadFrom;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	public int getAddressToReadFrom()
	{
		return addressReadFrom;
	}

}
