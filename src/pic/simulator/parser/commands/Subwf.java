package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Subwf extends Command
{
	private static final short argumentCount = 2;
	private static final short cycleCount = 1;
	private int cmdNumber;

	private short arg0, arg1;

	public Subwf(int cmdNumber, short arg0, short arg1) {
		this.cmdNumber = cmdNumber;
		this.arg0 = arg0;
		this.arg1 = arg1;
	}

	public short getArgumentCount() {
		return argumentCount;
	}

	public short getCycleCount() {
		return cycleCount;
	}

	public int getCmdNumber() {
		return cmdNumber;
	}

	@Override
	public void execute(PicProcessor proc) {

		int w 				= proc.workRegister;
		int f				= proc.getMemoryControl().getAt(arg0);
		int res				= w-f;
		int resLowerNibble	= (w & 0x0F) - (f & 0x0F);
		
		
		boolean setC		= res < 0;
		boolean setDC		= resLowerNibble < 0;


		if(arg1==0)
			proc.workRegister=(byte) res;
		else
			proc.getMemoryControl().setAt(arg0,(byte) res);
		
		
		affectZeroBit(proc, (byte) res);

		if(setDC)
			proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_DC);
		else
			proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_DC);
			
		if(setC)
			proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_C);
		else
			proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_C);
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
