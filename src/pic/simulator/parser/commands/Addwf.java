package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Addwf extends Command
{
	private static final short argumentCount = 2;
	private static final short cycleCount = 1;
	private int cmdNumber;
	
	private short arg0, arg1;
	
	public Addwf(int cmdNumber, short arg0, short arg1)
	{
		this.cmdNumber = cmdNumber;
		this.arg0 = arg0;
		this.arg1 = arg1;
	}
	
	public short getArgumentCount()
	{
		return argumentCount;
	}
	
	public short getCycleCount()
	{
		return cycleCount;
	}

	public int getCmdNumber() {
		return cmdNumber;
	}

	@Override
	public void execute(PicProcessor proc) {	
		int val2 = proc.getMemoryControl().getAt(arg0);
		int w = proc.workRegister;
		
		int newValue	= w + val2;
		
		boolean setDC 	= (((proc.workRegister&0x0F) + (val2&0x0F))&0x10) != 0;
		boolean setC 	= (newValue&0x10000) != 0;
		
		if(arg1==0)
			proc.workRegister=(byte) newValue;
		else
			proc.getMemoryControl().setAt(arg0,(byte) newValue);
			
		
		if(setDC)
			proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_DC);
		else
			proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_DC);
			
		if(setC)
			proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_C);
		else
			proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_C);
			

		affectZeroBit(proc, (byte)newValue);	
	}

	@Override
	public String getCmdName() {
		return getClass().getSimpleName().toLowerCase();
	}

	@Override
	public short getArg0() {
		return arg0;
	}

	@Override
	public short getArg1() {
		return arg1;
	}
}
