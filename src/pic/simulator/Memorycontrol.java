package pic.simulator;

public class Memorycontrol
{
	private byte[] memory;

	private final int sfrLength				= 0x0C;
	private final int unimplementedArea		= 0x50;
	
	private short currentBank;
	private final short bankCount;
	private final int bankLength;
	
	private SpecialFunctionRegister memSFR;
	
	public Memorycontrol(int memorySize, short bankCount)
	{
		this.memory 		= new byte[memorySize];
		this.bankCount 		= bankCount;
		this.currentBank 	= 0;
		this.bankLength		= memorySize/bankCount;
		
		this.memSFR			= new SpecialFunctionRegister(this);
	}
	
	public void setAt(int address, byte value)
	{
		memory[currentBank*bankLength + address] = value;
	}
	public byte getAt(int address)
	{
		if(isSFR(address))
			return memSFR.getAt(address);
		if(isUnimplemented(address))
			return 0;
		
		return memory[currentBank*bankLength + address];
	}
	
	public void switchBank(short bank) throws IllegalArgumentException
	{
		if(bank > bankCount || bank < 0)
			throw new IllegalArgumentException("Bankindex invalid!");
		
		this.currentBank = bank;
	}
	private boolean isSFR(int address)
	{
		for(int i = 0; i < memory.length; i += bankLength)
		{
			if(i >= address && i < sfrLength)
				return true;
		}
		return false;
	}
	private boolean isUnimplemented(int address)
	{
		for(int i = 0; i < memory.length; i += bankLength)
		{
			if(address >= i+unimplementedArea && address < i+bankLength)
				return true;
		}
		return false;
	}

	protected byte getFromMemoryAt(int address) 
	{
		return memory[address];
	}
	
}
