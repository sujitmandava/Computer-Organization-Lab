package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}

	public void performMA() {
		// TODO
		if (EX_MA_Latch.isMA_enable()) {
			int ldResult = 0;
			boolean isLd = false;
			int opcode = EX_MA_Latch.getInstruction() >>> 27;

			if (opcode == 22) {
				isLd = true;
				ldResult = containingProcessor.getMainMemory().getWord(EX_MA_Latch.getAluResult()); // Getting data from
																									// the address
			}

			if (opcode == 23) {
				containingProcessor.getMainMemory().setWord(EX_MA_Latch.getAluResult(), EX_MA_Latch.getOp2());
			}

			MA_RW_Latch.setAluResult(EX_MA_Latch.getAluResult());
			MA_RW_Latch.setIsLd(isLd);
			MA_RW_Latch.setLdResult(ldResult);
			MA_RW_Latch.setRemainder(EX_MA_Latch.getRemainder());
			MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			//System.out.println("From MA: aluResult: "+EX_MA_Latch.getAluResult()+" isLd: "+isLd+" ldResult: "+ ldResult+" remainder: "+ EX_MA_Latch.getRemainder());

			// EX_MA_Latch.setMA_enable(false);
			// MA_RW_Latch.setRW_enable(true);
		}
		EX_MA_Latch.setMA_enable(false);
		MA_RW_Latch.setRW_enable(true);
	}

}
