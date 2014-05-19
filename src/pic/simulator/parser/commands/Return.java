package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Return extends Command
{
	private static final short argumentCount = 0;
	private static final short cycleCount = 2;
	private int cmdNumber;

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
	public void execute(PicProcessor proc) {
		short val = (short) proc.getMemoryControl().popStack();
		proc.getMemoryControl().setAt(SpecialFunctionRegister.PCL, val);
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
