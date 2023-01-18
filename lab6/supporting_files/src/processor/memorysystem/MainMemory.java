package processor.memorysystem;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import generic.Event.EventType;
import processor.Clock;

public class MainMemory implements Element {

	int[] memory;

	public MainMemory() {
		memory = new int[65536];
	}

	public int getWord(int address) {
		return memory[address];
	}

	public void setWord(int address, int value) {
		memory[address] = value;
	}

	public String getContentsAsString(int startingAddress, int endingAddress) {
		if (startingAddress == endingAddress)
			return "";

		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for (int i = startingAddress; i <= endingAddress; i++) {
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	// lab5+
	@Override
	public void handleEvent(Event e) {

		if (e.getEventType() == EventType.MemoryRead) {

			MemoryReadEvent event = (MemoryReadEvent) e;

			Simulator.getEventQueue().addEvent(
					new MemoryResponseEvent(
							Clock.getCurrentTime(), // Latency is required
							this,
							event.getRequestingElement(),
							getWord(event.getAddressToReadFrom()),event.getAddressToReadFrom()));

			System.out.println("Memory Response event: Memory Read " + Clock.getCurrentTime());
		}

		else if (e.getEventType() == EventType.MemoryWrite) {
			MemoryWriteEvent event = (MemoryWriteEvent) e;

			setWord(event.getAddressToWriteTo(), event.getValue());

			Simulator.getEventQueue().addEvent(
					new MemoryResponseEvent(Clock.getCurrentTime(), this, event.getRequestingElement(), event.getValue(),event.getAddressToWriteTo()));

			System.out.println("Memory Response event: Memory Write fired " + Clock.getCurrentTime());
		}
		// else if (e.getEventType() == Event.EventType.MemoryWrite) TO IMPLEMENT FOR MA
	}
}
