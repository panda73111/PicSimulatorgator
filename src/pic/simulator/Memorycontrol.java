package pic.simulator;

import java.util.HashMap;
import java.util.Stack;

public class Memorycontrol
{
	private byte[] 					memory;
	private HashMap<Integer, SpecialFunctionRegister> 	specialFunctionRegisters;
	private Stack<Integer> 			stack;
	
	private final int sfrLength				= 0x0C;
	private final int unimplementedAreaBegin= 0x50;
	private final int bankLength			= 0x80;
	
	
	public Memorycontrol(int memorySize, short bankCount)
	{
		this.memory 					= new byte[memorySize];		
		this.specialFunctionRegisters	= new HashMap<>();
		initSFR();
	}
	
	public void setAt(int address, byte value)
	{
		if(isSFR(address))
		{
			specialFunctionRegisters.get(address).setValue(value);
			return;
		}
		
		memory[address - getBank()*bankLength] = value;
	}
	public byte getAt(int address)
	{
		if(isSFR(address))
			return specialFunctionRegisters.get(address).getValue();
		if(isUnimplemented(address))
			return 0;
		
		return memory[address - getBank()*bankLength];
	}
	
	private boolean isSFR(int address)
	{
		for(int i = 0; i < memory.length; i += bankLength)
		{
			if(i <= address && address < i+sfrLength)
				return true;
		}
		return false;
	}
	private boolean isUnimplemented(int address)
	{
		for(int i = 0; i < memory.length; i += bankLength)
		{
			if(address >= i+unimplementedAreaBegin && address < i+bankLength)
				return true;
		}
		return false;
	}

	protected byte getFromMemoryAt(int address) 
	{
		return memory[address];
	}
	
	private int getBank()
	{
		byte status = specialFunctionRegisters.get(SpecialFunctionRegister.STATUS0).getValue();
		return status & 0b00010000;
	}
	
	private void initSFR()
	{
		SpecialFunctionRegister sfr = new SpecialFunctionRegister(0x00, 0x00);
		specialFunctionRegisters.put(SpecialFunctionRegister.INDF0, sfr);
		specialFunctionRegisters.put(SpecialFunctionRegister.INDF1, sfr);
		
		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.TMR0, sfr);

		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.PCL0, sfr);
		specialFunctionRegisters.put(SpecialFunctionRegister.PCL1, sfr);

		sfr = new SpecialFunctionRegister(0xFF, 0b00011000);
		specialFunctionRegisters.put(SpecialFunctionRegister.STATUS0, sfr);
		specialFunctionRegisters.put(SpecialFunctionRegister.STATUS1, sfr);

		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.FSR0, sfr);
		specialFunctionRegisters.put(SpecialFunctionRegister.FSR1, sfr);
		
		sfr = new SpecialFunctionRegister(0b00011111, 0x00);
		specialFunctionRegisters.put(SpecialFunctionRegister.PORTA, sfr);

		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.PORTB, sfr);
		
		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.NONE0, sfr);
		specialFunctionRegisters.put(SpecialFunctionRegister.NONE1, sfr);
		
		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.EEDATA, sfr);

		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.EEADR, sfr);

		sfr = new SpecialFunctionRegister(0b00011111, 0x00);
		specialFunctionRegisters.put(SpecialFunctionRegister.PCLATH0, sfr);
		specialFunctionRegisters.put(SpecialFunctionRegister.PCLATH1, sfr);
		
		sfr = new SpecialFunctionRegister();
		specialFunctionRegisters.put(SpecialFunctionRegister.INTCON0, sfr);
		specialFunctionRegisters.put(SpecialFunctionRegister.INTCON1, sfr);
		
		sfr = new SpecialFunctionRegister(0xFF, 0xFF);
		specialFunctionRegisters.put(SpecialFunctionRegister.OPTION_REG, sfr);

		sfr = new SpecialFunctionRegister(0b00011111, 0b00011111);
		specialFunctionRegisters.put(SpecialFunctionRegister.TRISA, sfr);

		sfr = new SpecialFunctionRegister(0xFF, 0xFF);
		specialFunctionRegisters.put(SpecialFunctionRegister.TRISB, sfr);

		sfr = new SpecialFunctionRegister(0b00011111, 0x00);
		specialFunctionRegisters.put(SpecialFunctionRegister.EECON1, sfr);
		
		sfr = new SpecialFunctionRegister(0x00, 0x00);
		specialFunctionRegisters.put(SpecialFunctionRegister.EECON2, sfr);
	}
	
	public void push(int value)
	{
		stack.push(value);
	}
	public int pop()
	{
		return stack.pop();
	}
}
