package pic.simulator;

import java.util.PriorityQueue;

import pic.simulator.interrupts.Interruption;

public class InterruptionHandler 
{
	final Processor myProcessor;
	final PriorityQueue<Interruption> interruptionQueue;
	
	public InterruptionHandler(Processor proc)
	{
		myProcessor = proc;
		interruptionQueue = new PriorityQueue<>();
	}
	
	public Interruption getInterruption()
	{
		return interruptionQueue.poll();
	}

}
