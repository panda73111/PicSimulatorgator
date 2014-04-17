package pic.simulator;

import java.util.HashMap;

import pic.simulator.pins.IOPin;
import pic.simulator.pins.Pin;

public class PinHandler {

    private HashMap<Integer, Pin> pins;
    
	public PinHandler()
	{
		pins = new HashMap<>();
		setupPins();
	}
    private void setupPins()
    {
        pins.put(Pin.OSC1, new Pin("OSC1/CLKIN"));
        pins.put(Pin.OSC2, new Pin("OSC2/CLKOUTOSC2/CLKOUT"));
        pins.put(Pin.MCLR, new Pin("MCLR"));
        pins.put(Pin.RA0, new IOPin("RA0"));
        pins.put(Pin.RA1, new IOPin("RA1"));
        pins.put(Pin.RA2, new IOPin("RA2"));
        pins.put(Pin.RA3, new IOPin("RA3"));
        pins.put(Pin.RA4, new IOPin("RA4/T0CKI"));
        pins.put(Pin.RB0, new IOPin("RB0/INT"));
        pins.put(Pin.RB1, new IOPin("RB1"));
        pins.put(Pin.RB2, new IOPin("RB2"));
        pins.put(Pin.RB3, new IOPin("RB3"));
        pins.put(Pin.RB4, new IOPin("RB4"));
        pins.put(Pin.RB5, new IOPin("RB5"));
        pins.put(Pin.RB6, new IOPin("RB6"));
        pins.put(Pin.RB7, new IOPin("RB7"));
    }

}
