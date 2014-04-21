package pic.simulator.specialfunctionregisters;

import pic.simulator.Memorycontrol;
import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.pins.Pin;

public class Tmr0 extends SpecialFunctionRegister
{
    private final Memorycontrol memCtrl;
    private byte                value;
    private int                 cyclesSinceWrite;

    public Tmr0(Processor processor)
    {
        this.memCtrl = processor.getMemoryControl();
        reset();
    }

    public void onTick()
    {
        byte status = memCtrl.getAt(SpecialFunctionRegister.STATUS);
        if ((status & 0b10000) > 0)
        {
            // TMR0 operates as a counter
            //TODO: counter operation
        }
        else
        {
            // TMR0 operates as a timer
            if (cyclesSinceWrite < 2)
                // keep the value for 2 cycles after a write occured
                cyclesSinceWrite++;
            else
                increment();
        }
    }
    
    public void onPinChange(int newState)
    {
        byte status = memCtrl.getAt(SpecialFunctionRegister.STATUS);
        if ((status & 0b1000) > 0)
        {
            // react to falling edge
            if (newState == Pin.LOW)
                increment();
        }
        else
        {
            // react to rising edge
            if (newState == Pin.HIGH)
                increment();
        }
    }

    private void increment()
    {
        if (value == 0xFF)
        {
            //TODO: TMR0 overflow interrupt
        }
        value++;
    }
    
    @Override
    public void setValue(byte value)
    {
        this.value = value;
        cyclesSinceWrite = 0;
    }

    @Override
    public byte getValue()
    {
        return value;
    }

    @Override
    public void reset()
    {
        value = 0;
        cyclesSinceWrite = 2;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
