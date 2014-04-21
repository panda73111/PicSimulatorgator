package pic.simulator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import pic.simulator.specialfunctionregisters.Eeadr;
import pic.simulator.specialfunctionregisters.Eecon1;
import pic.simulator.specialfunctionregisters.Eecon2;
import pic.simulator.specialfunctionregisters.Eedata;
import pic.simulator.specialfunctionregisters.Fsr;
import pic.simulator.specialfunctionregisters.Indf;
import pic.simulator.specialfunctionregisters.Intcon;
import pic.simulator.specialfunctionregisters.Optionreg;
import pic.simulator.specialfunctionregisters.Pcl;
import pic.simulator.specialfunctionregisters.Pclath;
import pic.simulator.specialfunctionregisters.Porta;
import pic.simulator.specialfunctionregisters.Portb;
import pic.simulator.specialfunctionregisters.Status;
import pic.simulator.specialfunctionregisters.Tmr0;
import pic.simulator.specialfunctionregisters.Trisa;
import pic.simulator.specialfunctionregisters.Trisb;
import pic.simulator.specialfunctionregisters.Unimplemented;

public class PicMemorycontrol implements Memorycontrol
{
    private byte[]                                    	memory;
    private HashMap<Integer, SpecialFunctionRegister> 	specialFunctionRegisters;
	private HashSet<SpecialFunctionRegister>			specialFunctionRegisterSet;
    private Stack<Integer>                           	stack;
    private Processor									processor;
	public static final int sfrLength				= 0x0C;
	public static final int unimplementedAreaBegin	= 0x50;
	public static final int bankLength				= 0x80;
	public static final int gpLength				= 0x43;
	public static final int memoryLength			= 0xFF;
	public static final short bankCount				= 2;
	
	public PicMemorycontrol(Processor proc)
	{
		this.memory 					= new byte[memoryLength];		
		this.specialFunctionRegisters	= new HashMap<>();
		this.specialFunctionRegisterSet = new HashSet<>();
		this.stack						= new Stack<>();
		this.processor					= proc;
		initSFR();
	}
	
	public void setAt(int address, byte value)
	{
		if(isSFR(address))
		{
			specialFunctionRegisters.get(address).setValue(value);
			return;
		}
		
		memory[address] = value;
	}
	public byte getAt(int address)
	{
		if(isSFR(address))
			return specialFunctionRegisters.get(address).getValue();
		if(isUnimplemented(address))
			return 0;
		
		return memory[address + getActiveBank()*bankLength];
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
	
	private int getActiveBank()
	{
		byte status = specialFunctionRegisters.get(SpecialFunctionRegister.STATUS0).getValue();
		return status & 0b00010000 >> 4;
	}
	
    public PicMemorycontrol(Processor processor, int memorySize, short bankCount)
    {
        this.processor = processor;
        this.memory = new byte[memorySize];
        this.specialFunctionRegisters = new HashMap<>();
        initSFR();
    }

    
	
	public void pushStack(int value)
	{
		if(stack.size()>8)
			throw new StackOverflowError("Stack overflow!");
		stack.push(value);
	}
	public int popStack()
	{
		return stack.pop();
	}
	
    
    public boolean getBitAt(int address, short bit)
    {
        return (getAt(address) & (1 << bit)) != 0;
    }

    public void setBitAt(int address, short bit)
    {
        byte val = (byte) (getAt(address) | (1 << bit));
        setAt(address, val);
    }

    public void clearBitAt(int address, short bit)
    {
        byte val = getAt(address);
        val &= 0xFF - (1 << bit);
        setAt(address, val);
    }


    public void setStatusBit(short bit)
    {
        setBitAt(SpecialFunctionRegister.STATUS, bit);
    }

    public void clearStatusBit(short bit)
    {
        clearBitAt(SpecialFunctionRegister.STATUS, bit);
    }
	
	
	public Collection<SpecialFunctionRegister> getSpecialFunctionRegisters()
	{
		return specialFunctionRegisters.values();
	}
	
	public SpecialFunctionRegister getSFR(int id)
	{
	    return specialFunctionRegisters.get(id);
	}
	
    private void initSFR()
    {
        SpecialFunctionRegister sfr = new Indf(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.INDF0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.INDF1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Tmr0(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.TMR0, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Pcl(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCL0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCL1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Status(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.STATUS0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.STATUS1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Fsr(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.FSR0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.FSR1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Porta(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.PORTA, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Portb(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.PORTB, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Unimplemented();
        specialFunctionRegisters.put(SpecialFunctionRegister.NONE0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.NONE1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Eedata(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.EEDATA, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Eeadr(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.EEADR, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Pclath(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCLATH0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCLATH1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Intcon(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.INTCON0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.INTCON1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Optionreg(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.OPTION_REG, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Trisa(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.TRISA, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Trisb(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.TRISB, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Eecon1(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.EECON1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Eecon2(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.EECON2, sfr);
        specialFunctionRegisterSet.add(sfr);
    }



    
    public HashSet<SpecialFunctionRegister> getSFRSet()
    {
    	return specialFunctionRegisterSet;
    }
}
