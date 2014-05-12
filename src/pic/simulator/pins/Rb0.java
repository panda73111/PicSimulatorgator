package pic.simulator.pins;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.interrupts.Interruption;
import pic.simulator.specialfunctionregisters.Optionreg;

public class Rb0 extends IOPin
{
	Processor proc;
	
	public Rb0(String name, int id, Processor proc)
	{
		super(name, id);
		this.proc = proc;
	}


	@Override
	public void setExternally()
	{
		boolean wasLow = (externalState == LOW);
		
		super.setExternally();
		
		if(isInput && wasLow && intOnRisingEdge())
		{
			proc.getInterruptionHandler().causeInterruption(Interruption.RB0);
		}
		
	}

	@Override
	public void clearExternally()
	{
		boolean wasHigh = (externalState == HIGH);
		
		super.clearExternally();
		
		if(isInput && wasHigh && !intOnRisingEdge())
		{
			proc.getInterruptionHandler().causeInterruption(Interruption.RB0);
		}
	}
	
	private boolean intOnRisingEdge()
	{
		return proc.getMemoryControl().getBitAt(SpecialFunctionRegister.OPTION_REG, Optionreg.INTEDG);
	}
}
