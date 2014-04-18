package pic.simulator;


public interface Memorycontrol
{	
	public void setAt(int address, byte value);
	public byte getAt(int address);
	
	public void pushStack(int value);
	public int popStack();
	
	public boolean getBitAt(int address, short bit);
	public void setBitAt(int address, short bit);    
	public void clearBitAt(int address, short bit);

    public void setStatusBit(short bit);
    public void clearStatusBit(short bit);	

}
