package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Eedata extends SpecialFunctionRegister
{
    private byte value;

    public Eedata()
    {
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        this.value = value;
    }

    @Override
    public byte getValue()
    {
        return value;
    }

    @Override
    public void reset()
    {
        this.value = 0;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
