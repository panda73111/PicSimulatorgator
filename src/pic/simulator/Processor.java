package pic.simulator;

import java.io.IOException;

import pic.simulator.parser.*;

public class Processor
{
	private int 			progCounterAddress	= SpecialFunctionRegister.PCL;
	private Program 		picProgram;
	private Memorycontrol 	memControl;
	
	public byte				workRegister		= 0x00;
	
	private boolean 		isInterrupted		= false;;

	public Processor(String programFileName) throws IOException
	{
		picProgram = new Program(programFileName);
		memControl = new Memorycontrol(0xFF, (short)2);
	}
	
	
	public void executeProgram() {
		byte progCounter;

		while ((progCounter = memControl.getAt(progCounterAddress)) < picProgram.length())
		{
			if (isInterrupted)
			{
				// TODO
			}
			
			Command cmd = fetch(progCounter);
			execute(cmd);
			
			System.out.println("---Executed " + cmd.toString() + "---");
			
			memControl.setAt(progCounterAddress, (byte)(progCounter+1));
			// TODO repaint
		}
	}

	private Command fetch(int cmdIndex) {
		return picProgram.getCommand(cmdIndex);
	}

	private void execute(Command cmd) {
		cmd.execute(this);
	}
	
	
	
	
	public byte getAtAddress(int address)
	{
		return memControl.getAt(address);
	}
	public void setAtAddress(int address, byte value)
	{
		memControl.setAt(address, value);
	}
	public boolean getBitAtAddress(int address, short bit)
	{
		return (memControl.getAt(address) & (1<<bit)) != 0;
	}
	public void setBitAtAddress(int address, short bit)
	{
		byte val = (byte) (memControl.getAt(address) | (1<<bit));
		memControl.setAt(address, val);
	}
	

	public void pushStack(int value)
	{
		memControl.push(value);
	}
	public int popStack()
	{
		return memControl.pop();
	}
	
	
	
	
	public void setStatusBit(short bit)
	{
		setBitAtAddress(SpecialFunctionRegister.STATUS, bit);
	}
}
