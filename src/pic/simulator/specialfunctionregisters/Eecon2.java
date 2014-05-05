package pic.simulator.specialfunctionregisters;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;

public class Eecon2 extends SpecialFunctionRegister
{
    private final Processor processor;
    private byte            value;
    private boolean         writeAllowed;

    public Eecon2(Processor processor)
    {
        this.processor = processor;
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        writeAllowed = false;
        if (this.value == 0x55 && value == 0xAA)
            writeAllowed = true;
        this.value = value;
    }

    @Override
    public byte getValue()
    {
        return 0;
    }

    @Override
    public void reset()
    {
        value = 0;
        writeAllowed = false;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
