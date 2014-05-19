package pic.simulator.specialfunctionregisters;

import java.util.HashMap;

import pic.simulator.PinHandler;
import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.pins.IOPin;
import pic.simulator.pins.Pin;

public class Trisb extends SpecialFunctionRegister
{
    private final HashMap<Integer, IOPin> pins;
    private byte                          value;

    public Trisb(PicProcessor processor)
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
            pins.get(Pin.RB0).setToInput();
        else
            pins.get(Pin.RB0).setToOutput();

        if ((value & 0b00000010) > 0)
            pins.get(Pin.RB1).setToInput();
        else
            pins.get(Pin.RB1).setToOutput();

        if ((value & 0b00000100) > 0)
            pins.get(Pin.RB2).setToInput();
        else
            pins.get(Pin.RB2).setToOutput();

        if ((value & 0b00001000) > 0)
            pins.get(Pin.RB3).setToInput();
        else
            pins.get(Pin.RB3).setToOutput();

        if ((value & 0b00010000) > 0)
            pins.get(Pin.RB4).setToInput();
        else
            pins.get(Pin.RB4).setToOutput();

        if ((value & 0b00100000) > 0)
            pins.get(Pin.RB5).setToInput();
        else
            pins.get(Pin.RB5).setToOutput();

        if ((value & 0b01000000) > 0)
            pins.get(Pin.RB6).setToInput();
        else
            pins.get(Pin.RB6).setToOutput();

        if ((value & 0b10000000) > 0)
            pins.get(Pin.RB7).setToInput();
        else
            pins.get(Pin.RB7).setToOutput();
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
