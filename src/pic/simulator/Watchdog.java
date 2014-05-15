package pic.simulator;

import pic.simulator.interrupts.Interruption;

public class Watchdog
{
    public static final int lifetimeMillis = 80;

    private final Processor processor;
    private int             ticks;

    public Watchdog(Processor processor)
    {
        this.processor = processor;
    }

    public void onTick()
    {
        double millisPassed = 0.004d / processor.getFrequency() * ticks;

        if (millisPassed >= lifetimeMillis)
        {
            processor.getInterruptionHandler().causeInterruption(Interruption.WDT);
            ticks = 0;
        }
        else
            ticks++;
    }

    public void reset()
    {
        ticks = 0;
    }
}
