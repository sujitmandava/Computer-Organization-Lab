package processor.pipeline;

import processor.Clock;
import processor.Processor;
import configuration.Configuration;
import generic.CacheReadEvent;
import generic.CacheResponseEvent;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import processor.memorysystem.Cache;
import generic.Simulator;

public class InstructionFetch implements Element {

	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;

	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch,
			IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch) {
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void performIF() {
		System.out.println("IF: " + IF_EnableLatch.isIF_enable());
		if (IF_EnableLatch.isIF_enable()) {
			if (IF_EnableLatch.isIF_busy()) // lab5+
			{
				return;
			}

			if (EX_IF_Latch.getIsBranch()) {
				containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.getPC());
				// System.out.println("IF: IS BRANCH true " + EX_IF_Latch.getPC());
			}
			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();

			// int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
			// REMOVED
			// IF_OF_Latch.setInstruction(newInstruction); REMOVED
			Simulator.getEventQueue().addEvent( // lab5+
					new MemoryReadEvent(
							Clock.getCurrentTime() + Configuration.L1i_latency,
							this,
							containingProcessor.getL1iCache(),
							containingProcessor.getRegisterFile().getProgramCounter()));

			System.out.println("New Memory Read event added to L1i from IF for addre " + containingProcessor.getRegisterFile().getProgramCounter());

			IF_EnableLatch.setIF_busy(true); // lab5+

			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			// System.out.println("IF: IS BRANCH false");

			Simulator.incrementInstructions();
			IF_EnableLatch.setIF_enable(false);
			// IF_OF_Latch.setOF_enable(true);

			// System.out.println(newInstruction);
		}
	}

	@Override // lab5+
	public void handleEvent(Event e) {
		if (IF_OF_Latch.isOF_busy()) {
			e.setEventTime(Clock.getCurrentTime() + 1);

			Simulator.getEventQueue().addEvent(e);
		} else {
			MemoryResponseEvent event = (MemoryResponseEvent) e; // Cache response event


			if (EX_IF_Latch.getIsBranch()) {
				if (event.getAddressToReadFrom() == EX_IF_Latch.getPC()) {
					EX_IF_Latch.setIsBranch(false);
					IF_OF_Latch.setInstruction(event.getValue());
					IF_OF_Latch.setInstructionLocation(event.getAddressToReadFrom());
					
					System.out.println("IR");
					System.out.println(IF_OF_Latch.getInstruction());
				}
			}
			else{
				IF_OF_Latch.setInstruction(event.getValue());
				IF_OF_Latch.setInstructionLocation(event.getAddressToReadFrom());
				
				System.out.println("IR");
				System.out.println(IF_OF_Latch.getInstruction());
			}

			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.setIF_busy(false);
		}
	}

}
