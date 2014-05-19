package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.parser.Command;

public class Movwf extends Command
{
	private static final short argumentCount = 1;
	private static final short cycleCount = 1;
	private int cmdNumber;

	private short arg0;

	public Movwf(int cmdNumber, short arg0) {
		this.cmdNumber = cmdNumber;
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

	public int getCmdNumber() {
		return cmdNumber;
	}

	@Override
	public void execute(PicProcessor proc) {
		proc.getMemoryControl().setAt(arg0, proc.workRegister);
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
		return 0;
	}
}
