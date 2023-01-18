package processor.pipeline;

import generic.CacheReadEvent;
import generic.CacheResponseEvent;
import generic.CacheWriteEvent;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import configuration.Configuration;
// import generic.*;
import processor.Processor;

public class MemoryAccess implements Element {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	boolean isLoad ;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType if_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = if_EnableLatch;
		this.isLoad = false;
	}

	public void performMA() {
		// TODO
		MA_RW_Latch.setStall(EX_MA_Latch.getStall());
		boolean isLd = false;
		int ldResult = 0;
		System.out.println("MA: " + EX_MA_Latch.isMA_enable());
		if (EX_MA_Latch.isMA_enable()) {

			if (!EX_MA_Latch.getMABusy()) {
				int opcode = EX_MA_Latch.getInstruction() >>> 27;

				if (opcode == 22) {
					isLd = true;
					this.isLoad = true;
					// ldResult = containingProcessor.getMainMemory().getWord(EX_MA_Latch.getAluResult()); // Getting data
																										// from
																										// the address
					Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime() + Configuration.L1d_latency, this, containingProcessor.getL1dCache(), EX_MA_Latch.getAluResult()));
					EX_MA_Latch.setMABusy(true);

					System.out.println("From MA: Mem Write added: addre " + EX_MA_Latch.getAluResult());
				}

				else if (opcode == 23) {
					// containingProcessor.getL1dCache().cacheWrite(EX_MA_Latch.getAluResult(), EX_MA_Latch.getOp2()); // get cache, set word???
					
					Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime() + Configuration.L1d_latency, this, containingProcessor.getL1dCache(), EX_MA_Latch.getAluResult(), EX_MA_Latch.getOp2()));
					EX_MA_Latch.setMABusy(true);

					System.out.println("From MA: Mem Write added: addre " +EX_MA_Latch.getAluResult() );

				}
				else{
					MA_RW_Latch.setRW_enable(true);
				}

				if (opcode == 29) {
					EX_MA_Latch.setMABusy(false);
					EX_MA_Latch.setMA_enable(false);
					IF_EnableLatch.setIF_enable(false);
					System.out.println("4> IF DISABLED");
				}

				MA_RW_Latch.setAluResult(EX_MA_Latch.getAluResult());
				System.out.println(EX_MA_Latch.getAluResult()+"-"+EX_MA_Latch.getRemainder());
				MA_RW_Latch.setIsLd(isLd);
				// MA_RW_Latch.setLdResult(ldResult);
				MA_RW_Latch.setRemainder(EX_MA_Latch.getRemainder());
				MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			}

			// int ldResult = 0;
			// boolean isLd = false;
			// int opcode = EX_MA_Latch.getInstruction() >>> 27;

			// if (opcode == 22) {
			// isLd = true;
			// ldResult =
			// containingProcessor.getMainMemory().getWord(EX_MA_Latch.getAluResult()); //
			// Getting data from
			// // the address
			// }

			// if (opcode == 23) {
			// containingProcessor.getMainMemory().setWord(EX_MA_Latch.getAluResult(),
			// EX_MA_Latch.getOp2());
			// }

			// if (opcode == 29) {
			// IF_EnableLatch.setIF_enable(false);
			// System.out.println("4> IF DISABLED");
			// }
			// MA_RW_Latch.setAluResult(EX_MA_Latch.getAluResult());
			// MA_RW_Latch.setIsLd(isLd);
			// MA_RW_Latch.setLdResult(ldResult);
			// MA_RW_Latch.setRemainder(EX_MA_Latch.getRemainder());
			// MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			// System.out.println("From MA: aluResult: "+EX_MA_Latch.getAluResult()+" isLd:
			// "+isLd+" ldResult: "+ ldResult+" remainder: "+ EX_MA_Latch.getRemainder());
			
			// MA_RW_Latch.setAluResult(EX_MA_Latch.getAluResult());
			// MA_RW_Latch.setIsLd(isLd);
			// MA_RW_Latch.setLdResult(ldResult);
			// MA_RW_Latch.setRemainder(EX_MA_Latch.getRemainder());

			EX_MA_Latch.setMA_enable(false);
			// MA_RW_Latch.setRW_enable(true);
			System.out.println(EX_MA_Latch.getInstruction());
		}
		// EX_MA_Latch.setMA_enable(false);
		// MA_RW_Latch.setRW_enable(true);

	}

	@Override
	public void handleEvent(Event e) {
		MemoryResponseEvent event = (MemoryResponseEvent) e;

		EX_MA_Latch.setMABusy(false);
		// EX_MA_Latch.setInstruction(instruction);
		MA_RW_Latch.setAluResult(EX_MA_Latch.getAluResult());
		MA_RW_Latch.setLdResult(event.getValue());
		MA_RW_Latch.setRemainder(this.EX_MA_Latch.getRemainder());
		MA_RW_Latch.setInstruction(this.EX_MA_Latch.getInstruction());
		// MA_RW_Latch.setIsLd(this.isLoad);

		MA_RW_Latch.setRW_enable(true);
		System.out.println("From MA: Mem Res received: ins: " + this.EX_MA_Latch.getInstruction());
	}

}
