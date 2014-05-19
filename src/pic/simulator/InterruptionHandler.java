package pic.simulator;

import java.util.PriorityQueue;

import pic.simulator.interrupts.Interruption;

public class InterruptionHandler
{
    final PicProcessor                   myProcessor;
    final PriorityQueue<Interruption> interruptionQueue;

    public InterruptionHandler(PicProcessor proc)
    {
        myProcessor = proc;
        interruptionQueue = new PriorityQueue<>();
    }

    public boolean hasInterruption()
    {
        return !interruptionQueue.isEmpty();
    }

    public Interruption getInterruption()
    {
        return interruptionQueue.poll();
    }

    public void causeInterruption(int interruptionID)
    {
        Interruption interrupt = new Interruption(myProcessor, interruptionID);
        interruptionQueue.add(interrupt);
    }

    public void reset()
    {
        interruptionQueue.clear();
    }
}
