package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Status extends SpecialFunctionRegister
{
    private byte value;

    public Status()
    {
        reset();
    }

    public void setBit(int bit)
    {
        value |= 1 << bit;
    }

    public void clearBit(int bit)
    {
        value &= ~(1 << bit);
    }

    @Override
    public void setValue(byte value)
    {
        // disallow setting bits 3 and 4 by the program 
        this.value = (byte) (value & 0xE7);
    }

    @Override
    public byte getValue()
    {
        return value;
    }

    @Override
    public void reset()
    {
        value = 0b11000;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
