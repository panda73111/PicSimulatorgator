package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;
import pic.simulator.specialfunctionregisters.Status;

public class Rrf extends Command
{
	private static final short argumentCount = 2;
	private static final short cycleCount = 1;
	private int cmdNumber;

	private short arg0, arg1;

	public Rrf(int cmdNumber, short arg0, short arg1) {
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
		short f = proc.getMemoryControl().getAt(arg0);

		boolean cWasSet = proc.getMemoryControl().getBitAt(SpecialFunctionRegister.STATUS, Status.STATUS_C);
		boolean setC = ((f & 0x01) != 0);
		
		f >>= 1;
		
		if(cWasSet)
			f|=0x80;
		
		if(arg1==0)
			proc.workRegister = f;
		else
			proc.getMemoryControl().setAt(arg0, f);
		
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
