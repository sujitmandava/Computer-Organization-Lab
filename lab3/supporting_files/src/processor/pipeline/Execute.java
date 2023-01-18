package processor.pipeline;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		//TODO
		if(OF_EX_Latch.isEX_enable())
		{
			int op1 = OF_EX_Latch.getOp1();
			int op2 = OF_EX_Latch.getOp2();
			int imm = OF_EX_Latch.getImmx();
			int branchTarget = OF_EX_Latch.getBranchTarget();
			int A,B,aluResult=0;
			int opCode = OF_EX_Latch.getInstruction()>>>27;
			int result=0,remainder =0;
			int isBranchTaken=0;

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
					break;
				}
				case 2:
				case 3:
				{
					result = A-B;
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
					break;
				}
				case 25:
				{
					if(A-B==0)
					{
						isBranchTaken =1;
					}
					break;
				}
				case 26:
				{
					if(A-B!=0)
					{
						isBranchTaken =1;
					}
					break;
				}
				case 27:
				{
					if(A-B<0)
					{
						isBranchTaken =1;
					}
					break;
				}
				case 28:
				{
					if(A-B>0)
					{
						isBranchTaken =1;
					}
					break;
				}
				//MULTIPLIER
				case 4:
				case 5:{
					result = A*B;
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
					break;
				}
				//SHIFT UNIT
				case 16:
				case 17:
				{
					remainder = A&((-1)-((1<<(32-B)-1)));
					result = A<<B;
					break;
				}
				case 18:
				case 19:
				{
					remainder = A&((1<<B)-1);
					result = A>>>B;
					break;
				}
				case 20:
				case 21:
				{
					remainder = A&((1<<B)-1);
					result = A>>B;
					break;
				}
				//LOGICAL UNIT
				case 8:
				case 9:
				{
					result = A&B;
					break;
				}
				case 10:
				case 11:
				{
					result = A|B;
					break;
				}
				case 12:
				case 13:
				{
					result = A^B;
					break;
				}
				case 24:
				{
					isBranchTaken = 1;
					break;
				}
			}

			aluResult = result;
			int instruction = OF_EX_Latch.getInstruction();
			if(isBranchTaken==1)
			{
				EX_IF_Latch.setIsBranch(true);
				EX_IF_Latch.setPC(branchTarget);
				//System.out.println("EX: IS BRANCH true " + branchTarget);
				//containingProcessor.getRegisterFile().setProgramCounter(branchTarget);
			}
			else
			{
				EX_IF_Latch.setIsBranch(false);
			}
			
			EX_MA_Latch.setAluResult(aluResult);
			EX_MA_Latch.setInstruction(instruction);
			EX_MA_Latch.setOp2(op1); //did it intentionally
			EX_MA_Latch.setRemainder(remainder);

			//System.out.println("From EX: aluResult: "+aluResult+" Op2: "+ op1+" remainder: "+ remainder);
			
			OF_EX_Latch.setEX_enable(false);
			if(isBranchTaken==1)
			{
				EX_MA_Latch.setMA_enable(false);
			}
			EX_MA_Latch.setMA_enable(true);
		}
		
	}

}
