package pic.simulator.specialfunctionregisters;

import java.util.HashMap;

import pic.simulator.PinHandler;
import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.pins.IOPin;
import pic.simulator.pins.Pin;

public class Portb extends SpecialFunctionRegister
{
    private final HashMap<Integer, IOPin> pins;
    private byte                          value;

    public Portb(PicProcessor processor)
    {
        this.pins = new HashMap<Integer, IOPin>();

        PinHandler pinHandler = processor.getPinHandler();

        pins.put(Pin.RB0, (IOPin) pinHandler.getPin(Pin.RB0));
        pins.put(Pin.RB1, (IOPin) pinHandler.getPin(Pin.RB1));
        pins.put(Pin.RB2, (IOPin) pinHandler.getPin(Pin.RB2));
        pins.put(Pin.RB3, (IOPin) pinHandler.getPin(Pin.RB3));
        pins.put(Pin.RB4, (IOPin) pinHandler.getPin(Pin.RB4));
        pins.put(Pin.RB5, (IOPin) pinHandler.getPin(Pin.RB5));
        pins.put(Pin.RB6, (IOPin) pinHandler.getPin(Pin.RB6));
        pins.put(Pin.RB7, (IOPin) pinHandler.getPin(Pin.RB7));
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        this.value = (byte) (value & 0x1f);

        if ((value & 0b00000001) > 0)
            pins.get(Pin.RB0).setInternally();
        else
            pins.get(Pin.RB0).clearInternally();

        if ((value & 0b00000010) > 0)
            pins.get(Pin.RB1).setInternally();
        else
            pins.get(Pin.RB1).clearInternally();

        if ((value & 0b00000100) > 0)
            pins.get(Pin.RB2).setInternally();
        else
            pins.get(Pin.RB2).clearInternally();

        if ((value & 0b00001000) > 0)
            pins.get(Pin.RB3).setInternally();
        else
            pins.get(Pin.RB3).clearInternally();

        if ((value & 0b00010000) > 0)
            pins.get(Pin.RB4).setInternally();
        else
            pins.get(Pin.RB4).clearInternally();

        if ((value & 0b0010000) > 0)
            pins.get(Pin.RB5).setInternally();
        else
            pins.get(Pin.RB5).clearInternally();

        if ((value & 0b0100000) > 0)
            pins.get(Pin.RB6).setInternally();
        else
            pins.get(Pin.RB6).clearInternally();

        if ((value & 0b1000000) > 0)
            pins.get(Pin.RB7).setInternally();
        else
            pins.get(Pin.RB7).clearInternally();
    }

    @Override
    public byte getValue()
    {
        value = 0;
        
        if (pins.get(Pin.RB7).getInternalState() != Pin.LOW)
            value |= 0b10000000;
        
        if (pins.get(Pin.RB6).getInternalState() != Pin.LOW)
            value |= 0b1000000;
        
        if (pins.get(Pin.RB5).getInternalState() != Pin.LOW)
            value |= 0b100000;
        
        if (pins.get(Pin.RB4).getInternalState() != Pin.LOW)
            value |= 0b10000;
        
        if (pins.get(Pin.RB3).getInternalState() != Pin.LOW)
            value |= 0b1000;
        
        if (pins.get(Pin.RB2).getInternalState() != Pin.LOW)
            value |= 0b100;
        
        if (pins.get(Pin.RB1).getInternalState() != Pin.LOW)
            value |= 0b10;
        
        if (pins.get(Pin.RB0).getInternalState() != Pin.LOW)
            value |= 0b1;
        
        return value;
    }

    @Override
    public void reset()
    {
        value = 0;

        for (IOPin pin : pins.values())
            pin.clearInternally();
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
