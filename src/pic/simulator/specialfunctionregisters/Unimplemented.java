package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Unimplemented extends SpecialFunctionRegister
{
    @Override
    public void setValue(short value)
    {
    }

    @Override
    public short getValue()
    {
        return 0;
    }

    @Override
    public void reset()
    {
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
