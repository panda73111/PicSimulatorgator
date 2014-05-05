package pic.simulator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import pic.simulator.specialfunctionregisters.*;

/**
 * @author Lorenzo Toso An implementation of the Memorycontrol-interface that
 *         describes the memory-unit of the PIC16f84.
 */
public class PicMemorycontrol implements Memorycontrol
{
    /**
     * The two dimensional array that contains the General-purpose registers.
     */
    private byte[]                                    memory;

    /**
     * A Hashmap that maps all addresses of special function registers to the
     * specified registers.
     */
    private HashMap<Integer, SpecialFunctionRegister> specialFunctionRegisters;
    /**
     * A set that simplifies access to the special function registers.
     */
    private HashSet<SpecialFunctionRegister>          specialFunctionRegisterSet;
    /**
     * An Implementation of the stack using the standard-java stack class.
     */
    private Stack<Integer>                            stack;
    /**
     * A pointer to the processor class this memory unit belongs to.
     */
    private Processor                                 processor;

    // These are just a bunch of constants for the PIC

    public static final int                           sfrBegin                = 0x00;
    public static final int                           sfrLength               = 0x0B;

    public static final int                           gpBegin                 = 0x0C;
    public static final int                           gpLength                = 0x43;

    public static final int                           unimplementedAreaBegin  = 0x50;
    public static final int                           unimplementedAreaLength = 0x2F;

    public static final int                           bankLength              = 0x80;
    public static final int                           totalMemory             = 0xFF;
    public static final int                           maxStackSize            = 8;
    public static final short                         bankCount               = 2;

    private final byte[]                              eepromData              = new byte[64];

    public PicMemorycontrol(Processor proc)
    {
        this.memory = new byte[gpLength];
        this.specialFunctionRegisters = new HashMap<>();
        this.specialFunctionRegisterSet = new HashSet<>();
        this.stack = new Stack<>();
        this.processor = proc;

        initSFR();
    }

    public void setAt(int address, byte value)
    {
        if (isSFR(address))
        {
            specialFunctionRegisters.get(address).setValue(value);
            return;
        }

        memory[address - gpBegin] = value;
    }

    public byte getAt(int address)
    {
        if (isSFR(address))
            return specialFunctionRegisters.get(address).getValue();
        if (isUnimplemented(address))
            return 0;

        return memory[address - gpBegin];
    }

    /**
     * Checks whether or not a specified address points to a SFR.
     * 
     * @param address
     *            The address to be checked.
     * @return Whether or not an address is a SFR.
     */
    private boolean isSFR(int address)
    {
        for (int i = 0; i < memory.length; i += bankLength)
        {
            if (i <= address && address < i + sfrLength)
                return true;
        }
        return false;
    }

    /**
     * Checks whether or not a specified address is unimplemented.
     * 
     * @param address
     *            The address to be checked.
     * @return Whether or not an address is unimplemented.
     */
    private boolean isUnimplemented(int address)
    {
        if (address < 0)
            return true;

        for (int i = 0; i < memory.length; i += bankLength)
        {
            if (address >= i + unimplementedAreaBegin
                    && address < i + bankLength)
                return true;
        }
        return false;
    }

    /**
     * This method allows to read directly from the memory without checking
     * whether the specified address is unimplemented or an SFR.
     * 
     * @param address
     *            The address of the specified byte
     * @return The byte at the given address
     */
    protected byte getFromMemoryAt(int address)
    {
        return memory[address];
    }

    /**
     * Returns the active bank.
     * 
     * @return The active bank.
     */
    private int getActiveBank()
    {
        byte status = specialFunctionRegisters.get(
                SpecialFunctionRegister.STATUS0).getValue();
        return status & 0b00010000 >> 4;
    }

    public void pushStack(int value)
    {
        if (stack.size() > maxStackSize)
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

    public SpecialFunctionRegister getSFR(int id)
    {
        return specialFunctionRegisters.get(id);
    }

    /**
     * Initializes all Special function registers
     */
    private void initSFR()
    {
        SpecialFunctionRegister sfr = new Indf(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.INDF0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.INDF1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Tmr0(this);
        specialFunctionRegisters.put(SpecialFunctionRegister.TMR0, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Pcl();
        specialFunctionRegisters.put(SpecialFunctionRegister.PCL0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCL1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Status();
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

        sfr = new Eedata();
        specialFunctionRegisters.put(SpecialFunctionRegister.EEDATA, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Eeadr();
        specialFunctionRegisters.put(SpecialFunctionRegister.EEADR, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Pclath(this);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCLATH0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCLATH1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Intcon(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.INTCON0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.INTCON1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Optionreg();
        specialFunctionRegisters.put(SpecialFunctionRegister.OPTION_REG, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Trisa(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.TRISA, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Trisb(processor);
        specialFunctionRegisters.put(SpecialFunctionRegister.TRISB, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Eecon1(this);
        specialFunctionRegisters.put(SpecialFunctionRegister.EECON1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Eecon2(this);
        specialFunctionRegisters.put(SpecialFunctionRegister.EECON2, sfr);
        specialFunctionRegisterSet.add(sfr);

        for (SpecialFunctionRegister currentSfr : specialFunctionRegisterSet)
        {
            currentSfr.onMemInitFinished();
        }
    }

    /**
     * Returns an easy-access set of all SFR
     * 
     * @return An easy-access set of all SFR
     */
    public HashSet<SpecialFunctionRegister> getSFRSet()
    {
        return specialFunctionRegisterSet;
    }

    public Stack<Integer> getStack()
    {
        return stack;
    }

    public void clearGP()
    {
        memory = new byte[gpLength];
    }

    public void reset()
    {
        for (SpecialFunctionRegister sfr : specialFunctionRegisterSet)
        {
            sfr.reset();
        }
    }

    public byte getEepromByte(int index)
    {
        if (index < 0 || index > eepromData.length)
            throw new IllegalArgumentException();

        return eepromData[index];
    }

    public void setEepromByte(int index, byte value)
    {
        if (index < 0 || index > eepromData.length)
            throw new IllegalArgumentException();

        eepromData[index] = value;
    }
}
