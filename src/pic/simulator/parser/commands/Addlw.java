package pic.simulator.parser.commands;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Addlw extends Command
{
	private static final short argumentCount = 1;
	private static final short cycleCount = 1;
	private int cmdNumber;
	
	private short arg0;
	
	public Addlw(int cmdNumber, short arg0)
	{
		this.cmdNumber = cmdNumber;
		this.arg0 = arg0;
	}
	
	public short getArgumentCount()
	{
		return argumentCount;
	}
	
	public short getCycleCount()
	{
		return cycleCount;
	}

	public int getCmdNumber() {
		return cmdNumber;
	}

	@Override
	public void execute(Processor proc) {
		
		int newValue	= ((int)proc.workRegister) + arg0;
		
		boolean setDC 	= (((proc.workRegister&0x0F) + (arg0&0x0F))&0x10) != 0;
		boolean setC 	= (newValue&0x10000) != 0;
		
		proc.workRegister=(byte) newValue;
		
		if(setDC)
			proc.setStatusBit(SpecialFunctionRegister.STATUS_DC);
		if(setC)
			proc.setStatusBit(SpecialFunctionRegister.STATUS_C);
		if(newValue == 0)
			proc.setStatusBit(SpecialFunctionRegister.STATUS_Z);
			
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
