package pic.simulator;

import pic.simulator.interrupts.Interruption;

public class Watchdog
{
    public static final int     timeoutMillis = 80;   // without prescaling!

    private final Processor     proc;
    private final Memorycontrol memCtrl;
    private int                 ticks;
    private int                 prescalerSkippedTicks;

    public Watchdog(Processor processor)
    {
        this.proc = processor;
        memCtrl = processor.getMemoryControl();
    }

    public void onTick()
    {
        double millisPassed = 0.004d / proc.getFrequency() * ticks;
        int millisToPass = timeoutMillis;
        
        byte options = memCtrl.getAt(SpecialFunctionRegister.OPTION_REG);
        if ((options & 0b100) != 0)
        {
            // the Prescaler is assigned to the WDT
            int timeFactor = (int) Math.pow(2, (options & 0b111));
            millisToPass = timeoutMillis * timeFactor;
        }

        if (millisPassed >= millisToPass)
        {
            proc.getInterruptionHandler().causeInterruption(Interruption.WDT);
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
