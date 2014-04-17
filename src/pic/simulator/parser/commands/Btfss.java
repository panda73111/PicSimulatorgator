package pic.simulator.parser.commands;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Btfss extends Command
{
	private static final short argumentCount = 2;
	private static final short cycleCount = 2;
	private int cmdNumber;

	private short arg0, arg1;

	public Btfss(int cmdNumber, short arg0, short arg1) {
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
	public void execute(Processor proc) {
		byte val = proc.getMemoryControl().getAt(arg0);
		boolean bitIsSet = (val & (1<<arg1)) != 0;
		
		if(bitIsSet)
		{
			byte pcl = proc.getMemoryControl().getAt(SpecialFunctionRegister.PCL);
			pcl++;
			proc.getMemoryControl().setAt(SpecialFunctionRegister.PCL, pcl);
		}
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
