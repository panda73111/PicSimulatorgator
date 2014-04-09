package pic.simulator.parser.commands;

import pic.simulator.Processor;

import pic.simulator.parser.Command;

public class Nop extends Command
{
	private static final short argumentCount = 0;
	private static final short cycleCount = 1;
	private int cmdNumber;

	public Nop(int cmdNumber) {
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
	public void execute(Processor proc) 
	{
		// Nothing to do
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
