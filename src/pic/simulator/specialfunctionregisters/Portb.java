package pic.simulator.specialfunctionregisters;

import pic.simulator.PicProcessor;
import pic.simulator.PinHandler;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.pins.Pin;

public class Portb extends SpecialFunctionRegister
{
    private final PicProcessor proc;
    private short              value;

    public Portb(PicProcessor processor)
    {
        this.proc = processor;

        reset();
    }

    @Override
    public void setValue(short value)
    {
        this.value = (short) (value & 0x1f);

        PinHandler pinHandler = proc.getPinHandler();

        if ((value & 0b00000001) > 0)
            pinHandler.getPin(Pin.RB0).setInternally();
        else
            pinHandler.getPin(Pin.RB0).clearInternally();

        if ((value & 0b00000010) > 0)
            pinHandler.getPin(Pin.RB1).setInternally();
        else
            pinHandler.getPin(Pin.RB1).clearInternally();

        if ((value & 0b00000100) > 0)
            pinHandler.getPin(Pin.RB2).setInternally();
        else
            pinHandler.getPin(Pin.RB2).clearInternally();

        if ((value & 0b00001000) > 0)
            pinHandler.getPin(Pin.RB3).setInternally();
        else
            pinHandler.getPin(Pin.RB3).clearInternally();

        if ((value & 0b00010000) > 0)
            pinHandler.getPin(Pin.RB4).setInternally();
        else
            pinHandler.getPin(Pin.RB4).clearInternally();

        if ((value & 0b0010000) > 0)
            pinHandler.getPin(Pin.RB5).setInternally();
        else
            pinHandler.getPin(Pin.RB5).clearInternally();

        if ((value & 0b0100000) > 0)
            pinHandler.getPin(Pin.RB6).setInternally();
        else
            pinHandler.getPin(Pin.RB6).clearInternally();

        if ((value & 0b1000000) > 0)
            pinHandler.getPin(Pin.RB7).setInternally();
        else
            pinHandler.getPin(Pin.RB7).clearInternally();
    }

    @Override
    public short getValue()
    {
        value = 0;

        PinHandler pinHandler = proc.getPinHandler();

        if (pinHandler.getPin(Pin.RB7).getInternalState() != Pin.LOW)
            value |= 0b10000000;

        if (pinHandler.getPin(Pin.RB6).getInternalState() != Pin.LOW)
            value |= 0b1000000;

        if (pinHandler.getPin(Pin.RB5).getInternalState() != Pin.LOW)
            value |= 0b100000;

        if (pinHandler.getPin(Pin.RB4).getInternalState() != Pin.LOW)
            value |= 0b10000;

        if (pinHandler.getPin(Pin.RB3).getInternalState() != Pin.LOW)
            value |= 0b1000;

        if (pinHandler.getPin(Pin.RB2).getInternalState() != Pin.LOW)
            value |= 0b100;

        if (pinHandler.getPin(Pin.RB1).getInternalState() != Pin.LOW)
            value |= 0b10;

        if (pinHandler.getPin(Pin.RB0).getInternalState() != Pin.LOW)
            value |= 0b1;

        return value;
    }

    @Override
    public void reset()
    {
        value = 0;

        PinHandler pinHandler = proc.getPinHandler();

        pinHandler.getPin(Pin.RB0).clearInternally();
        pinHandler.getPin(Pin.RB1).clearInternally();
        pinHandler.getPin(Pin.RB2).clearInternally();
        pinHandler.getPin(Pin.RB3).clearInternally();
        pinHandler.getPin(Pin.RB4).clearInternally();
        pinHandler.getPin(Pin.RB5).clearInternally();
        pinHandler.getPin(Pin.RB6).clearInternally();
        pinHandler.getPin(Pin.RB7).clearInternally();
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
