package processor.pipeline;

import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			int instruction = IF_OF_Latch.getInstruction();
			int opcode = instruction>>>27;
			int rs1,rs2,rd,imm;
			int op1=0,op2=0,immx=0,branchTarget=0;
			int isImmediate=0;
			switch(opcode)
					{
						// R3 Type
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
						case 20:{

							rs1 = (instruction>>22)&31;
							rs2 = (instruction>>17)&31;
							rd = (instruction>>12)&31;
							op1 = containingProcessor.getRegisterFile().getValue(rs1);
							op2 = containingProcessor.getRegisterFile().getValue(rs2);
							break;
						}
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
						case 23:{
							rs1 = (instruction>>22)&31;
							rd = (instruction>>17)&31;
							imm = (instruction<<15)>>15;
							isImmediate =1;
							immx = imm;
							op1 = containingProcessor.getRegisterFile().getValue(rs1);
							op2 = containingProcessor.getRegisterFile().getValue(rd);
							break;
						}
						case 25:
						case 26:
						case 27:
						case 28:{
							rs1 = (instruction>>22)&31;
							rd = (instruction>>17)&31;
							imm = (instruction<<15)>>15;
							immx = imm;
							branchTarget = imm + containingProcessor.getRegisterFile().getProgramCounter()-1;
							op1 = containingProcessor.getRegisterFile().getValue(rs1);
							op2 = containingProcessor.getRegisterFile().getValue(rd);
							break;
						}
						case 24:{
							rd = (instruction>>22)&31;
							imm = (instruction<<10)>>10;
							immx = imm;
							//because the either rd or imm can be used to set the offset and the other will be zero
							branchTarget = imm + containingProcessor.getRegisterFile().getValue(rd) + containingProcessor.getRegisterFile().getProgramCounter()-1;
						}
						case 29:
						{
							break;
						}
						default:{
							break;
						}

					} 
			OF_EX_Latch.setOp1(op1);		
			OF_EX_Latch.setOp2(op2);
			OF_EX_Latch.setImmx(immx);
			OF_EX_Latch.setBranchTarget(branchTarget);
			OF_EX_Latch.setInstruction((instruction));
			OF_EX_Latch.setIsImmediate(isImmediate);
			//System.out.println("From OF: Op1: "+op1+" Op2: "+ op2+" immx: "+ immx+" branchTarget: "+" isImmediate: "+isImmediate);

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
