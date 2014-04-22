package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Pcl extends SpecialFunctionRegister
{
    private short value;

    public Pcl()
    {
        reset();
    }

    public void increment()
    {
        value = (byte) ((value + 1) & 0x1FFF);
    }

    public short get13BitValue()
    {
        return value;
    }

    public void set13BitValue(short value)
    {
        this.value = (short) (value & 0x1FFF);
    }

    @Override
    public void setValue(byte value)
    {
        this.value = value;
    }

    @Override
    public byte getValue()
    {
        return (byte) value;
    }

    @Override
    public void reset()
    {
        value = 0;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
