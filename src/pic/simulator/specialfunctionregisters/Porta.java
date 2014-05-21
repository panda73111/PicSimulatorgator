package pic.simulator.specialfunctionregisters;

import pic.simulator.PicProcessor;
import pic.simulator.PinHandler;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.pins.Pin;

public class Porta extends SpecialFunctionRegister
{
    private final PicProcessor proc;
    private short              value;

    public Porta(PicProcessor processor)
    {
        this.proc = processor;

        reset();
    }

    @Override
    public void setValue(short value)
    {
        this.value = (short) (value & 0x1f);

        PinHandler pinHandler = proc.getPinHandler();

        if ((value & 0b00001) > 0)
            pinHandler.getPin(Pin.RA0).setInternally();
        else
            pinHandler.getPin(Pin.RA0).clearInternally();

        if ((value & 0b00010) > 0)
            pinHandler.getPin(Pin.RA1).setInternally();
        else
            pinHandler.getPin(Pin.RA1).clearInternally();

        if ((value & 0b00100) > 0)
            pinHandler.getPin(Pin.RA2).setInternally();
        else
            pinHandler.getPin(Pin.RA2).clearInternally();

        if ((value & 0b01000) > 0)
            pinHandler.getPin(Pin.RA3).setInternally();
        else
            pinHandler.getPin(Pin.RA3).clearInternally();

        if ((value & 0b10000) > 0)
            pinHandler.getPin(Pin.RA4).setInternally();
        else
            pinHandler.getPin(Pin.RA4).clearInternally();
    }

    @Override
    public short getValue()
    {
        value = 0;

        PinHandler pinHandler = proc.getPinHandler();

        if (pinHandler.getPin(Pin.RA4).getInternalState() != Pin.LOW)
            value |= 0b10000;

        if (pinHandler.getPin(Pin.RA3).getInternalState() != Pin.LOW)
            value |= 0b1000;

        if (pinHandler.getPin(Pin.RA2).getInternalState() != Pin.LOW)
            value |= 0b100;

        if (pinHandler.getPin(Pin.RA1).getInternalState() != Pin.LOW)
            value |= 0b10;

        if (pinHandler.getPin(Pin.RA0).getInternalState() != Pin.LOW)
            value |= 0b1;

        return value;
    }

    @Override
    public void reset()
    {
        value = 0;

        PinHandler pinHandler = proc.getPinHandler();

        pinHandler.getPin(Pin.RA0).clearInternally();
        pinHandler.getPin(Pin.RA1).clearInternally();
        pinHandler.getPin(Pin.RA2).clearInternally();
        pinHandler.getPin(Pin.RA3).clearInternally();
        pinHandler.getPin(Pin.RA4).clearInternally();
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
