package processor.pipeline;

// import java.time.Clock;

import processor.Clock;
import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.ExecutionCompleteEvent;
import generic.Simulator;
import processor.Processor;

public class Execute implements Element {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch,IF_EnableLatchType if_EnableLatchType)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_EnableLatch = if_EnableLatchType;
	}
	
	public void performEX()
	{

		System.out.println("EX: "+OF_EX_Latch.isEX_enable());
		EX_MA_Latch.setStall(OF_EX_Latch.getStall());
		if(OF_EX_Latch.isEX_enable())
		{
			if (OF_EX_Latch.isEX_busy()) {
				return;
			}

			int op1 = OF_EX_Latch.getOp1();
			int op2 = OF_EX_Latch.getOp2();
			int imm = OF_EX_Latch.getImmx();
			int branchTarget = OF_EX_Latch.getBranchTarget();
			int A,B,aluResult=0;
			int opCode = OF_EX_Latch.getInstruction()>>>27;
			int result=0,remainder =0;
			int isBranchTaken=0;
			long latency=0;

			if(OF_EX_Latch.getIsImmediate()==1)
			{
				A = op1;
				B = imm; 	
			}
			
			else
			{
				A = op1;
				B = op2;
			}
			//for store instruction
			if((opCode)==23)
			{
				A = op2;
				B = imm;  
			}

			//ALU BLOCK
			
			switch(opCode)
			{
				//ADDER
				case 0://add
				case 1://addi
				case 22://load
				case 23://store
				{
					result = A+B;
					latency =  Configuration.ALU_latency;
					break;
				}
				case 2:
				case 3:
				{
					result = A-B;
					latency =  Configuration.ALU_latency;
					break;
				}
				case 14:
				case 15:
				{
					if((A-B) < 0)
					{
						result = 1;
					}
					else
					{
						result = 0;
					}
					latency =  Configuration.ALU_latency;
					break;
				}
				case 25:
				{
					if(A-B==0)
					{
						isBranchTaken =1;
					}
					latency =  Configuration.ALU_latency;
					break;
				}
				case 26:
				{
					if(A-B!=0)
					{
						isBranchTaken =1;
					}
					latency =  Configuration.ALU_latency;
					break;
				}
				case 27:
				{
					if(A-B<0)
					{
						isBranchTaken =1;
					}
					latency =  Configuration.ALU_latency;
					break;
				}
				case 28:
				{
					if(A-B>0)
					{
						isBranchTaken =1;
					}
					latency =  Configuration.ALU_latency;
					break;
				}
				//MULTIPLIER
				case 4:
				case 5:{
					result = A*B;
					latency =  Configuration.multiplier_latency;
					break;
				}
				//DIVIDER
				case 6:
				case 7:{
					if(B!=0)
					{
						result = A/B;
						remainder = A%B; 
					}
					latency =  Configuration.multiplier_latency;
					break;
				}
				//SHIFT UNIT
				case 16:
				case 17:
				{
					remainder = A&((-1)-((1<<(32-B)-1)));
					result = A<<B;
					latency =  Configuration.ALU_latency;
					break;
				}
				case 18:
				case 19:
				{
					remainder = A&((1<<B)-1);
					result = A>>>B;
					latency =  Configuration.ALU_latency;
					break;
				}
				case 20:
				case 21:
				{
					remainder = A&((1<<B)-1);
					result = A>>B;
					latency =  Configuration.ALU_latency;
					break;
				}
				//LOGICAL UNIT
				case 8:
				case 9:
				{
					result = A&B;
					latency =  Configuration.ALU_latency;
					break;
				}
				case 10:
				case 11:
				{
					result = A|B;
					latency =  Configuration.ALU_latency;
					break;
				}
				case 12:
				case 13:
				{
					result = A^B;
					latency =  Configuration.ALU_latency;
					break;
				}
				case 24:
				{
					isBranchTaken = 1;
					break;
				}
				case 29:
				{
					IF_EnableLatch.setIF_enable(false);
					System.out.println("3> IF DISABLED");
					break;
				}
			}

			aluResult = result;
			int instruction = OF_EX_Latch.getInstruction();


			if(isBranchTaken==1)
			{
				// EX_IF_Latch.setIsBranch(true);
				// EX_IF_Latch.setPC(branchTarget);
				
				Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(	//l5+
					Clock.getCurrentTime() + latency, this, this, instruction,
					aluResult, remainder, op1, true, branchTarget));
				OF_EX_Latch.setEX_Busy(true);
				
				System.out.println("FROM EX Registered event at "+Clock.getCurrentTime()+" for "+(Clock.getCurrentTime() + latency));
				System.out.println("EX: IS BRANCH true " + branchTarget);
				//containingProcessor.getRegisterFile().setProgramCounter(branchTarget);
			}
			else
			{
				// EX_IF_Latch.setIsBranch(false);
				
				Simulator.getEventQueue().addEvent(new ExecutionCompleteEvent(			//l5+
					Clock.getCurrentTime() + latency, this, this, instruction,
					aluResult, remainder, op1, false, branchTarget));
				OF_EX_Latch.setEX_Busy(true);
				System.out.println("FROM EX Registered event at "+Clock.getCurrentTime()+" for "+(Clock.getCurrentTime() + latency));
			}
			
			// EX_MA_Latch.setAluResult(aluResult);
			// EX_MA_Latch.setInstruction(instruction);
			// EX_MA_Latch.setOp2(op1); //did it intentionally
			// EX_MA_Latch.setRemainder(remainder);

			//System.out.println("From EX: aluResult: "+aluResult+" Op2: "+ op1+" remainder: "+ remainder);
			
			OF_EX_Latch.setEX_enable(false);
			// EX_MA_Latch.setMA_enable(true);

			System.out.println(OF_EX_Latch.getInstruction());
		}

	}

	@Override
	public void handleEvent(Event e) {
		if (EX_MA_Latch.getMABusy()) {
			e.setEventTime(processor.Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			ExecutionCompleteEvent exEvent = (ExecutionCompleteEvent) e;
			System.out.println("Execution Complete fired at time  "+Clock.getCurrentTime()+" for the instruction "+ exEvent.getInstruction());
			if(exEvent.isBranchTaken())
			{
				EX_IF_Latch.setIsBranch(true);
				EX_IF_Latch.setPC(exEvent.getbranchTarget());

				//System.out.println("EX: IS BRANCH true " + branchTarget);
				//containingProcessor.getRegisterFile().setProgramCounter(branchTarget);
			}
			else
			{
				EX_IF_Latch.setIsBranch(false);
			}
			
			
			EX_MA_Latch.setAluResult(exEvent.getAluResult());
			EX_MA_Latch.setInstruction(exEvent.getInstruction());
			EX_MA_Latch.setOp2(exEvent.getOp2()); //did it intentionally
			EX_MA_Latch.setRemainder(exEvent.getRemainder());
			
			// System.out.println(EX_MA_Latch.getAluResult()+"-"+EX_MA_Latch.getRemainder());
			OF_EX_Latch.setEX_Busy(false);
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);

			EX_MA_Latch.setInstruction(OF_EX_Latch.getInstruction());
			// EX_MA_Latch.setAluResult();
		}
	}
}