package pic.simulator.specialfunctionregisters;

import java.util.HashMap;

import pic.simulator.PinHandler;
import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.pins.IOPin;
import pic.simulator.pins.Pin;

public class Trisa extends SpecialFunctionRegister
{
    private final HashMap<Integer, IOPin> pins;
    private byte                          value;

    public Trisa(Processor processor)
    {
        this.pins = new HashMap<Integer, IOPin>();

        PinHandler pinHandler = processor.getPinHandler();

        pins.put(Pin.RA0, (IOPin) pinHandler.getPin(Pin.RA0));
        pins.put(Pin.RA1, (IOPin) pinHandler.getPin(Pin.RA0));
        pins.put(Pin.RA2, (IOPin) pinHandler.getPin(Pin.RA0));
        pins.put(Pin.RA3, (IOPin) pinHandler.getPin(Pin.RA0));
        pins.put(Pin.RA4, (IOPin) pinHandler.getPin(Pin.RA0));
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        this.value = (byte) (value & 0x1f);

        if ((value & 0b00001) > 0)
            pins.get(Pin.RA0).setToInput();
        else
            pins.get(Pin.RA0).setToOutput();

        if ((value & 0b00010) > 0)
            pins.get(Pin.RA1).setToInput();
        else
            pins.get(Pin.RA1).setToOutput();

        if ((value & 0b00100) > 0)
            pins.get(Pin.RA2).setToInput();
        else
            pins.get(Pin.RA2).setToOutput();

        if ((value & 0b01000) > 0)
            pins.get(Pin.RA3).setToInput();
        else
            pins.get(Pin.RA3).setToOutput();

        if ((value & 0b10000) > 0)
            pins.get(Pin.RA4).setToInput();
        else
            pins.get(Pin.RA4).setToOutput();
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

        for (IOPin pin : pins.values())
            pin.setToOutput();
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
