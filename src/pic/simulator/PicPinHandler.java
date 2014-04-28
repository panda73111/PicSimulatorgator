package pic.simulator;

import java.util.HashMap;

import pic.simulator.pins.IOPin;
import pic.simulator.pins.MclrPin;
import pic.simulator.pins.Pin;

public class PicPinHandler implements PinHandler
{
    private HashMap<Integer, Pin> pins;

    public PicPinHandler(Processor proc)
    {
        pins = new HashMap<>();
        setupPins(proc);
    }

    public void setupPins(Processor proc)
    {
        pins.put(Pin.RA0, new IOPin("RA0", Pin.RA0));
        pins.put(Pin.RA1, new IOPin("RA1", Pin.RA1));
        pins.put(Pin.RA2, new IOPin("RA2", Pin.RA2));
        pins.put(Pin.RA3, new IOPin("RA3", Pin.RA3));        
        pins.put(Pin.RA4, new IOPin("RA4/T0CKI", Pin.RA4));
        
        pins.put(Pin.RB0, new IOPin("RB0/INT", Pin.RB0));
        pins.put(Pin.RB1, new IOPin("RB1", Pin.RB1));
        pins.put(Pin.RB2, new IOPin("RB2", Pin.RB2));
        pins.put(Pin.RB3, new IOPin("RB3", Pin.RB3));
        pins.put(Pin.RB4, new IOPin("RB4", Pin.RB4));
        pins.put(Pin.RB5, new IOPin("RB5", Pin.RB5));
        pins.put(Pin.RB6, new IOPin("RB6", Pin.RB6));
        pins.put(Pin.RB7, new IOPin("RB7", Pin.RB7));

        pins.put(Pin.MCLR, new MclrPin("MCLR", Pin.MCLR, proc));
        pins.put(Pin.CLKIN, new IOPin("CLKIN", Pin.CLKIN));
        pins.put(Pin.CLKOUT, new IOPin("CLKOUT", Pin.CLKOUT));
    }

    public Pin getPin(int pin)
    {
        return pins.get(pin);
    }

    public int getInternalPinState(int pin)
    {
        return pins.get(pin).getInternalState();
    }

    public int getExternalPinState(int pin)
    {
        return pins.get(pin).getExternalState();
    }

    public void setPinInternally(int pin)
    {
    	Pin p = pins.get(pin);
    	
    	if(p != null)
    		p.clearInternally();
    }

    public void setPinExternally(int pin)
    {
    	Pin p = pins.get(pin);
    	
    	if(p != null)
    		p.setExternally();
    }
    
    public void clearPinExternally(int pin)
    {
    	Pin p = pins.get(pin);
    	
    	if(p != null)
    		p.clearExternally();
    }

    public void setIOPinToInput(int pin)
    {
        ((IOPin) pins.get(pin)).setToInput();
    }

    public void setIOPinToOutput(int pin)
    {
        ((IOPin) pins.get(pin)).setToOutput();
    }
    
    public int getPinCount()
    {
    	return pins.size();
    }
}
