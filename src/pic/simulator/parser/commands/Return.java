package pic.simulator.parser.commands;

import javax.annotation.processing.Processor;

import pic.simulator.parser.Command;

public class Return extends Command
{
	private static final short argumentCount = 0;
	private static final short cycleCount = 2;

	public Return() {
	}

	public short getArgumentCount() {
		return argumentCount;
	}

	public short getCycleCount() {
		return cycleCount;
	}

	@Override
	public void execute(Processor proc) {
		// TODO Auto-generated method stub

	}
}
