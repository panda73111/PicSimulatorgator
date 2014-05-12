package pic.simulator.parser.commands;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;
import pic.simulator.specialfunctionregisters.Intcon;

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
	public void execute(Processor proc) 
	{
		int val = proc.getMemoryControl().popStack();
		proc.getMemoryControl().setAt(SpecialFunctionRegister.PCL, (byte) val);
		proc.getMemoryControl().setBitAt(SpecialFunctionRegister.INTCON, Intcon.GENERAL_INTERRUPT_ENABLE);
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
