package pic.simulator.parser.commands;

import javax.annotation.processing.Processor;

import pic.simulator.parser.Command;

public class Return extends Command
{
	private static final short argumentCount = 0;
	private static final short cycleCount = 2;
	private static int cmdNumber;

	public Return(int cmdNumber) {
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
		// TODO Auto-generated method stub

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
