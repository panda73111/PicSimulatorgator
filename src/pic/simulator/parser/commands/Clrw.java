package pic.simulator.parser.commands;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Clrw extends Command
{
	private static final short argumentCount = 0;
	private static final short cycleCount = 1;
	private static int cmdNumber;

	public Clrw(int cmdNumber) {
		 Clrw.cmdNumber = cmdNumber;
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
		proc.workRegister = 0;
		proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_Z);
	}

	@Override
	public String getCmdName() {
		return getClass().getSimpleName().toLowerCase();
	}

	@Override
	public short getArg0() {
		return 0;
	}

	@Override
	public short getArg1() {
		return 0;
	}
}
