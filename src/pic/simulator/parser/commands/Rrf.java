package pic.simulator.parser.commands;

import javax.annotation.processing.Processor;

import pic.simulator.parser.Command;

public class Rrf extends Command
{
	private static final short argumentCount = 2;
	private static final short cycleCount = 1;

	private short arg0, arg1;

	public Rrf(short arg0, short arg1) {
		this.arg0 = arg0;
		this.arg1 = arg1;
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
