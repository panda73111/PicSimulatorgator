package pic.simulator.parser.commands;

import pic.simulator.Processor;

import pic.simulator.parser.Command;

public class Retfie extends Command
{
	private static final short argumentCount = 0;
	private static final short cycleCount = 2;
	private int cmdNumber;

	public Retfie(int cmdNumber) {
		 this.cmdNumber = cmdNumber;
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
		throw new  UnsupportedOperationException("Not yet implemented.");
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
