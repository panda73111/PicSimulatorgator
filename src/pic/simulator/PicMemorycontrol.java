package pic.simulator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import pic.simulator.interrupts.Interruption;
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
    private PicProcessor                              processor;

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

    private Status                                    statusReg;

    public PicMemorycontrol(PicProcessor proc)
    {
        this.memory = new byte[gpLength];
        this.specialFunctionRegisters = new HashMap<>();
        this.specialFunctionRegisterSet = new HashSet<>();
        this.stack = new Stack<>();
        this.processor = proc;

        initSFR();
    }

    public void setAt(int address, short value)
    {
        if (isSFR(address))
        {
            int sfrAddress = address | (getActiveBank() << 7);
            specialFunctionRegisters.get(sfrAddress).setValue(value);
            return;
        }

        memory[address - gpBegin] = (byte)(0xFF & value);
    }

    public short getAt(int address)
    {
        if (isSFR(address))
        {
            int sfrAddress = address | (getActiveBank() << 7);
            return specialFunctionRegisters.get(sfrAddress).getValue();
        }
        if (isUnimplemented(address))
            return 0;

        return (short)memory[address - gpBegin];
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
        while (address > bankLength)
            address -= bankLength;
        return address <= (sfrBegin + sfrLength);
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
            if (address >= i + unimplementedAreaBegin && address < i + bankLength)
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
    	short status = statusReg.getValue();
        return (status & 0b100000) >> 5;
    }

    public boolean pushStack(int value)
    {
        if (stack.size() > maxStackSize)
        {
            processor.getGuiHandler().showError("Stackoverflow", "Stackoverflow");
            processor.stopProgramExecution();
            processor.reset(PicProcessor.POWER_ON);
            return false;
        }
        stack.push(value);
        return true;
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
        short val = getAt(address);
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
        SpecialFunctionRegister sfr = new Indf(this);
        specialFunctionRegisters.put(SpecialFunctionRegister.INDF0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.INDF1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Tmr0(this, processor.getInterruptionHandler());
        specialFunctionRegisters.put(SpecialFunctionRegister.TMR0, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Pcl();
        specialFunctionRegisters.put(SpecialFunctionRegister.PCL0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.PCL1, sfr);
        specialFunctionRegisterSet.add(sfr);

        sfr = new Status();
        specialFunctionRegisters.put(SpecialFunctionRegister.STATUS0, sfr);
        specialFunctionRegisters.put(SpecialFunctionRegister.STATUS1, sfr);
        statusReg = (Status) sfr;
        specialFunctionRegisterSet.add(sfr);

        sfr = new Fsr();
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
        Arrays.fill(memory, (byte) 0);
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

    public void tryEepromWrite()
    {
        Eecon1 eecon1 = (Eecon1) getSFR(SpecialFunctionRegister.EECON1);
        byte eecon1Val = eecon1.getValue();
        // check for write attempt
        if ((eecon1Val & 0b110) == 0b110)
        {
            // WR and WREN bits set
            Eecon2 eecon2 = (Eecon2) getSFR(SpecialFunctionRegister.EECON2);
            if (eecon2.isWriteAllowed())
            {
                // clear WRERR bit
                eecon1Val &= ~0b1000;
                // set WR bit, writing is in progress
                eecon1Val |= 0b10;

                byte adr = getAt(SpecialFunctionRegister.EEADR);
                byte data = getAt(SpecialFunctionRegister.EEDATA);
                setEepromByte(adr, data);

                // write successful, clear WR bits
                // TODO: timer to delay the write process
                eecon1Val &= ~0b10;
                eecon1.setValue(eecon1Val);
                processor.getInterruptionHandler().causeInterruption(Interruption.EEPROM);
            }
        }
    }
}
