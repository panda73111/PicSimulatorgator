package pic.simulator.specialfunctionregisters;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;

public class Eecon1 extends SpecialFunctionRegister
{
    private final Processor processor;
    private byte            value;

    public Eecon1(Processor processor)
    {
        this.processor = processor;
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
        return (byte) (value & 0b11100);
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
