package pic.simulator;

/**
 * @author Lorenzo Toso An Interface that contains all methods that a
 *         Memory-control-unit has to implement
 */
public interface Memorycontrol {
	/**
	 * Sets a specific byte at a specified address
	 * 
	 * @param address
	 *            The address of the byte to be set
	 * @param value
	 *            The new value of the byte
	 */
	public void setAt(int address, byte value);

	/**
	 * Returns the value of a specific byte from the memory-unit.
	 * 
	 * @param address
	 *            The address of the specified value
	 * @return The value of the byte at the given address
	 */
	public byte getAt(int address);

	/**
	 * Pushes a value to the stack
	 * 
	 * @param value
	 *            The value that is supposed to be pushed to the stack.
	 */
	public void pushStack(int value);

	/**
	 * Pops the top value from the stack.
	 * 
	 * @return The top value from the stack.
	 */
	public int popStack();

	/**
	 * Returns whether or not a specific bit is set.
	 * 
	 * @param address
	 *            The address of the specific byte
	 * @param bit
	 *            The bit in the specific byte
	 * @return Whether or not the specified bit is set.
	 */
	public boolean getBitAt(int address, short bit);

	/**
	 * Sets a specific bit at a given address.
	 * 
	 * @param address
	 *            The address of the bit to be set
	 * @param bit
	 *            The bit of the specified byte to be set
	 */
	public void setBitAt(int address, short bit);

	/**
	 * Clears a specific bit at a given address.
	 * 
	 * @param address
	 *            The address of the bit to be cleared
	 * @param bit
	 *            The bit of the specified byte to be cleared
	 */
	public void clearBitAt(int address, short bit);

	/**
	 * Sets a bit in the status register
	 * 
	 * @param bit
	 *            The bit to be set
	 */
	public void setStatusBit(short bit);

	/**
	 * Clears a bit in the status register
	 * 
	 * @param bit
	 *            The bit to be cleared
	 */
	public void clearStatusBit(short bit);
	
	
	/**
	 * Clears the complete general purpose registers
	 */
	public void clearGP();

}
