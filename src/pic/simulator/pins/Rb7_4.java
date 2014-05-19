package pic.simulator.pins;

import pic.simulator.PicProcessor;
import pic.simulator.interrupts.Interruption;

public class Rb7_4 extends IOPin
{
	PicProcessor proc;
	
	public Rb7_4(String name, int id, PicProcessor proc)
	{
		super(name, id);
		this.proc = proc;
	}


	@Override
	public void setExternally()
	{
		boolean wasLow = (externalState == LOW);
		
		super.setExternally();
		
		if(isInput && wasLow)
		{
			proc.getInterruptionHandler().causeInterruption(Interruption.PORTB);
		}
		
	}

	@Override
	public void clearExternally()
	{
		boolean wasHigh = (externalState == HIGH);
		
		super.clearExternally();
		
		if(isInput && wasHigh)
		{
			proc.getInterruptionHandler().causeInterruption(Interruption.PORTB);
		}
	}
}
