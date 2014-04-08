package pic.simulator.parser.commands;

import javax.annotation.processing.Processor;

import pic.simulator.parser.Command;

public class Sleep extends Command
{
	private static final short argumentCount = 0;
	private static final short cycleCount = 1;

	public Sleep() {
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
