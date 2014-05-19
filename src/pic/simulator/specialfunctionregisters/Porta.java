package pic.simulator.specialfunctionregisters;

import java.util.HashMap;

import pic.simulator.PinHandler;
import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.pins.IOPin;
import pic.simulator.pins.Pin;

public class Porta extends SpecialFunctionRegister
{
    private final HashMap<Integer, IOPin> pins;
    private byte                          value;

    public Porta(PicProcessor processor)
    {
        this.pins = new HashMap<Integer, IOPin>();

        PinHandler pinHandler = processor.getPinHandler();

        pins.put(Pin.RA0, (IOPin) pinHandler.getPin(Pin.RA0));
        pins.put(Pin.RA1, (IOPin) pinHandler.getPin(Pin.RA1));
        pins.put(Pin.RA2, (IOPin) pinHandler.getPin(Pin.RA2));
        pins.put(Pin.RA3, (IOPin) pinHandler.getPin(Pin.RA3));
        pins.put(Pin.RA4, (IOPin) pinHandler.getPin(Pin.RA4));
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        this.value = (byte) (value & 0x1f);

        if ((value & 0b00001) > 0)
            pins.get(Pin.RA0).setInternally();
        else
            pins.get(Pin.RA0).clearInternally();

        if ((value & 0b00010) > 0)
            pins.get(Pin.RA1).setInternally();
        else
            pins.get(Pin.RA1).clearInternally();

        if ((value & 0b00100) > 0)
            pins.get(Pin.RA2).setInternally();
        else
            pins.get(Pin.RA2).clearInternally();

        if ((value & 0b01000) > 0)
            pins.get(Pin.RA3).setInternally();
        else
            pins.get(Pin.RA3).clearInternally();

        if ((value & 0b10000) > 0)
            pins.get(Pin.RA4).setInternally();
        else
            pins.get(Pin.RA4).clearInternally();
    }

    @Override
    public byte getValue()
    {
        value = 0;
        
        if (pins.get(Pin.RA4).getInternalState() != Pin.LOW)
            value |= 0b10000;
        
        if (pins.get(Pin.RA3).getInternalState() != Pin.LOW)
            value |= 0b1000;
        
        if (pins.get(Pin.RA2).getInternalState() != Pin.LOW)
            value |= 0b100;
        
        if (pins.get(Pin.RA1).getInternalState() != Pin.LOW)
            value |= 0b10;
        
        if (pins.get(Pin.RA0).getInternalState() != Pin.LOW)
            value |= 0b1;
        
        return  value;
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
