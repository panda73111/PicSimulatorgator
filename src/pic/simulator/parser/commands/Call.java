package pic.simulator.parser.commands;

import javax.annotation.processing.Processor;

import pic.simulator.parser.Command;

public class Call extends Command
{
	private static final short argumentCount = 1;
	private static final short cycleCount = 2;

	private short arg0;

	public Call(short arg0) {
		this.arg0 = arg0;
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
