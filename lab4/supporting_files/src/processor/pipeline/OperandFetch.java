package processor.pipeline;

import generic.Simulator;
import generic.Statistics;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	MA_RW_LatchType MA_RW_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, MA_RW_LatchType mA_RW_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType ex_IF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.MA_RW_Latch = mA_RW_Latch; 
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = ex_IF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performOF()
	{
		System.out.println("OF: "+IF_OF_Latch.isOF_enable());
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			int instruction = IF_OF_Latch.getInstruction();
			int opcode = instruction>>>27;
			int rs1,rs2,imm;
			int rd = 0;
			int op1=0,op2=0,immx=0,branchTarget=0;
			int isImmediate=0;
			
			int opcodeEX = EX_MA_Latch.getInstruction()>>>27;	//+
			int opcodeMA = MA_RW_Latch.getInstruction()>>>27;	//+
			boolean dataConflictExist = false; 	//+
			boolean controlConflictExist = false;
			boolean hasSrc2=false,hasSrc1=false,hasRdEx=false,hasRdMa=false;				//+
			int src1=0,src2=0;					//+
			int rdMA=-1, rdEX=-1;
			int rdMA2=-1, rdEX2=-1;
			
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

							src1 = rs1;	//+
							src2 = rs2;	//+
							hasSrc1=true;	//+
							hasSrc2 = true;	//+

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

							src1 = rs1;	//+
							hasSrc1=true;	//+
							hasSrc2 = false;	//+
							
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
							
							src1 = rs1;	//+
							src2 = rd;	//+
							hasSrc1=true;	//+
							hasSrc2 = true;	//+

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
			
			if(opcode == 23){		//+
				src2= rd;
				hasSrc2 = true;
			}

			hasRdEx = true;
			if((23<=opcodeEX)&&(opcodeEX<=29))	//+
			{
				hasRdEx = false;
			}

			hasRdMa = true;
			if((23<=opcodeMA)&&(opcodeMA<=29))	//+
			{
				hasRdMa = false;
			}

			if(hasRdEx)
			{
				switch (opcodeEX)
				{
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
						rdEX = (EX_MA_Latch.getInstruction()>>12)&31;
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
					case 17:					case 19:
					case 21:
					case 22:{
						rdEX = (EX_MA_Latch.getInstruction()>>17)&31;
						break;
					}
					default:
						break;
				}
				switch(opcodeEX)
				{
					case 6:
					case 7:
					case 16:
					case 17:
					case 18:
					case 19:
					case 20:
					case 21:
					{
						rdEX2 = 31;
						break;
					}
					default:
					{
						break;
					}
				}
			}
			
			if(hasRdMa)
			{
				switch (opcodeMA)
				{
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
						rdMA = (MA_RW_Latch.getInstruction()>>12)&31;
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
					case 22:{
						rdMA = (MA_RW_Latch.getInstruction()>>17)&31;
						break;
					}
					default:
						break;
				}

				switch(opcodeMA)
				{
					case 6:
					case 7:
					case 16:
					case 17:
					case 18:
					case 19:
					case 20:
					case 21:
					{
						rdMA2 = 31;
						break;
					}
					default:
					{
						break;
					}
				}
			}	
			if(EX_MA_Latch.getStall())
			{
				rdEX =-1;
				rdEX2 = -1;
				System.out.println("EX_MA stall true");
			}
			if(MA_RW_Latch.getStall())
			{
				rdMA =-1;
				rdMA2 =-1;
				System.out.println("MA_RW stall true");
			}
			if(EX_IF_Latch.getIsBranch())
			{
				controlConflictExist = true;
				Simulator.incrementWBP();
			}
			else if((hasSrc1)&&(hasRdEx)&&(((rdEX==src1)&&(rdEX!=0))||(rdEX2==src1)))
			{
				dataConflictExist = true;
				Simulator.incrementDataHazards();
			}
			else if((hasSrc2)&&(hasRdEx)&&(((rdEX==src2)&&(rdEX!=0))||(rdEX2==src2)))
			{
				dataConflictExist = true;
				Simulator.incrementDataHazards();

			}
			else if((hasSrc1)&&(hasRdMa)&&(((rdMA==src1)&&(rdMA!=0))||(rdMA2==src1)))
			{
				dataConflictExist = true;
				Simulator.incrementDataHazards();

			}
			else if((hasSrc2)&&(hasRdMa)&&(((rdMA==src2)&&(rdMA!=0))||(rdMA2==src2)))
			{
				dataConflictExist = true;
				Simulator.incrementDataHazards();
			}

			if(controlConflictExist&&(!MA_RW_Latch.getStall()))
			{
				//do something
				OF_EX_Latch.setEX_enable(false);
				OF_EX_Latch.setStall(true);
				// IF_OF_Latch.setOF_enable(false);

				System.out.println("Control conflict detected");
			}
			else if(dataConflictExist)
			{
				IF_OF_Latch.setOF_enable(true);
				OF_EX_Latch.setEX_enable(false);
				IF_EnableLatch.setIF_enable(false);
				System.out.println("1");
				OF_EX_Latch.setStall(true);
				System.out.println("Data conflict detected");
			}
			else{
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(true);
				OF_EX_Latch.setStall(false);
				EX_MA_Latch.setStall(false);
				MA_RW_Latch.setStall(false);
				
				System.out.println("No conflict detected");
			}

			if ((opcode == 29)&&(!controlConflictExist)) {
				IF_EnableLatch.setIF_enable(false);
				System.out.println("2");
			}
			System.out.println(IF_OF_Latch.getInstruction());
		}
	}

}
