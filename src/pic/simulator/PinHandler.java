package pic.simulator;

import pic.simulator.pins.Pin;

public interface PinHandler {
	
    public void setupPins(PicProcessor proc);

    public Pin getPin(int pin);

    public int getInternalPinState(int pin);
    public int getExternalPinState(int pin);

    public void setPinInternally(int pin);
    public void setPinExternally(int pin);    
    public void clearPinExternally(int pin);

    public void setIOPinToInput(int pin);
    public void setIOPinToOutput(int pin);
    
    public int getPinCount();
}
