package generic;

public class CacheResponseEvent extends Event {

	int value;
	int addressReadFrom;
	
	public CacheResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value,int addressReadFrom) {
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
