package pic.simulator.parser.commands;

import pic.simulator.Processor;

import pic.simulator.parser.Command;

public class Incf extends Command
{
	private static final short argumentCount = 2;
	private static final short cycleCount = 1;
	private static int cmdNumber;

	private short arg0, arg1;

	public Incf(int cmdNumber, short arg0, short arg1) {
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
		// TODO Auto-generated method stub

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
