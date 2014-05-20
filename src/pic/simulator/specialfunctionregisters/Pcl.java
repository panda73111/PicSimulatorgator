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
        value = (short) ((value + 1) & 0x1FFF);
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
    public void setValue(short value)
    {
        this.value = value;
    }

    @Override
    public short getValue()
    {
        return value;
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
