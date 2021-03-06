package pic.simulator.pins;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.interrupts.Interruption;
import pic.simulator.specialfunctionregisters.Optionreg;

public class Rb0 extends IOPin
{
	PicProcessor proc;
	
	public Rb0(String name, int id, PicProcessor proc)
	{
		super(name, id);
		this.proc = proc;
	}


	@Override
	public void setExternally()
	{
		boolean wasLow = (externalState == LOW);
		
		super.setExternally();
		
		//if (isInput)
    		if(wasLow && intOnRisingEdge())
    		{
    			proc.getInterruptionHandler().causeInterruption(Interruption.RB0);
    		}
	}

	@Override
	public void clearExternally()
	{
		boolean wasHigh = (externalState == HIGH);
		
		super.clearExternally();
		
		//if (isInput)
    		if(wasHigh && !intOnRisingEdge())
    		{
    			proc.getInterruptionHandler().causeInterruption(Interruption.RB0);
    		}
	}
	
	private boolean intOnRisingEdge()
	{
		return proc.getMemoryControl().getBitAt(SpecialFunctionRegister.OPTION_REG, Optionreg.INTEDG);
	}
}
