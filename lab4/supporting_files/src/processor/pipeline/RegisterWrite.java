package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performRW() {
		System.out.println("RW: "+MA_RW_Latch.isRW_enable());
		if (MA_RW_Latch.isRW_enable()) {
			// TODO

			// if instruction being processed is an end instruction, remember to call
			// Simulator.setSimulationComplete(true);

			int opCode = MA_RW_Latch.getInstruction() >>> 27;
			boolean isWb = false; // To check whether we need to write back or not; False for all controlflow
									// instructions
			boolean isLd = MA_RW_Latch.getIsLd();
			int opType = 0; // 3 for R3; 2 for R2I

			// if(opCode == 6 || opCode ==7)
			// 	containingProcessor.getRegisterFile().setValue(31, MA_RW_Latch.getRemainder()); // Setting remainder
			// 																				// bits to reg x31

			switch (opCode)
			{
				case 6:
				case 7:
				case 16:
				case 17:
				case 18:
				case 19:
				case 20:
				case 21:
					containingProcessor.getRegisterFile().setValue(31, MA_RW_Latch.getRemainder());
					break;
				default:
					break;
			}

			switch (opCode) {
				case 0:
				case 2:
				case 4:
				case 6:
				case 8:
				case 10:
				case 12:
				case 14:
				case 16:
				case 18:
				case 20:
					isWb = true;
					opType = 3;
					break;
				case 1:
				case 3:
				case 5:
				case 7:
				case 9:
				case 11:
				case 13:
				case 15:
				case 17:
				case 19:
				case 21:
				case 22:
					isWb = true;
					opType = 2;
					break;
				default:
					isWb = false;
					break;
			}

			if (isWb) {
				int rd = 0;
				if (!isLd) {
					if (opType == 3) {
						rd = MA_RW_Latch.getInstruction() << 15;
					} else {
						rd = MA_RW_Latch.getInstruction() << 10;
					}
					rd = rd >>> 27; // Shifting to get first 5 bits only
					containingProcessor.getRegisterFile().setValue(rd, MA_RW_Latch.getAluResult());
				}
				if (isLd) {
					rd = MA_RW_Latch.getInstruction() << 10;
					rd = rd >>> 27;
					containingProcessor.getRegisterFile().setValue(rd, MA_RW_Latch.getLdResult());
				}
			}
			if (opCode == 29) {
				Simulator.setSimulationComplete(true);
			}

			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);

			if (opCode == 29) {
				IF_EnableLatch.setIF_enable(false);
			}
			System.out.println(MA_RW_Latch.getInstruction());
		}
		if((MA_RW_Latch.getInstruction() >>> 27)!=29)
			IF_EnableLatch.setIF_enable(true);
			System.out.println("IF_enable TRUE from RW");
	}

}
