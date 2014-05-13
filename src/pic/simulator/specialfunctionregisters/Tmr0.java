package pic.simulator.specialfunctionregisters;

import pic.simulator.InterruptionHandler;
import pic.simulator.PicMemorycontrol;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.interrupts.Interruption;
import pic.simulator.pins.Pin;

public class Tmr0 extends SpecialFunctionRegister
{
    private final InterruptionHandler interruptionHandler;
    private final PicMemorycontrol    memCtrl;
    private Optionreg                 optionReg;
    private short                     value;
    private int                       cyclesSinceWrite;

    public Tmr0(PicMemorycontrol memCtrl, InterruptionHandler interruptionHandler)
    {
        this.memCtrl = memCtrl;
        this.interruptionHandler = interruptionHandler;

        reset();
    }

    public void onTick()
    {
        byte options = optionReg.getValue();
        if ((options & 0b10000) == 0)
        {
            // TMR0 operates as a timer
            if (cyclesSinceWrite < 2)
                // keep the value for 2 cycles after a write occurred
                cyclesSinceWrite++;
            else
                increment();
        }
    }

    public void onPinChange(int newState)
    {
        byte options = optionReg.getValue();
        if ((options & 0b10000) > 0)
        {
            // TMR0 operates as a counter
            if ((options & 0b1000) > 0)
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
    }

    private void increment()
    {
        if (value == 0xFF)
        {
            value = 0;
            interruptionHandler.causeInterruption(Interruption.TIMER0);
        }
        else
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
        return (byte) value;
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

    @Override
    public void onMemInitFinished()
    {
        this.optionReg = (Optionreg) memCtrl.getSFR(SpecialFunctionRegister.OPTION_REG);
    }
}
