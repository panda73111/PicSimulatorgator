package pic.simulator;


public class Watchdog
{
    public static final int timeoutMillis = 18; // without prescaling!

    private final Processor proc;
    private int             ticks;
    private double          millisLeft;

    public Watchdog(Processor processor)
    {
        this.proc = processor;
    }

    public void onTick()
    {
        ticks++;

        double millisPassed = 0.004d / proc.getFrequency() * ticks;
        int millisToPass = timeoutMillis;

        byte options = proc.getMemoryControl().getAt(SpecialFunctionRegister.OPTION_REG);
        if ((options & 0b100) != 0)
        {
            // the Prescaler is assigned to the WDT
            int timeFactor = (int) Math.pow(2, (options & 0b111));
            millisToPass *= timeFactor;
        }

        if (millisPassed >= millisToPass)
        {
            millisLeft = 0.0d;
            proc.reset(proc.isSleeping() ? Processor.WDT_IN_SLEEP : Processor.WDT);
            ticks = 0;
            return;
        }

        millisLeft = millisToPass - millisPassed;
    }

    public double getMillisLeft()
    {
        return millisLeft;
    }

    public void reset()
    {
        ticks = 0;
    }
}
