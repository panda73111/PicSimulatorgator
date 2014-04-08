package pic.simulator.parser.commands;

import javax.annotation.processing.Processor;

import pic.simulator.parser.Command;

public class Movwf extends Command
{
	private static final short argumentCount = 1;
	private static final short cycleCount = 1;

	private short arg0;

	public Movwf(short arg0) {
		this.arg0 = arg0;
	}

	@Override
	public short getArgumentCount() {
		return argumentCount;
	}

	@Override
	public short getCycleCount() {
		return cycleCount;
	}

	@Override
	public void execute(Processor proc) {
		// TODO Auto-generated method stub

	}
}
