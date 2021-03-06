package pic.simulator;

public class Watchdog
{
    public static final int    timeoutMillis  = 18;   // without prescaling!

    private final PicProcessor proc;
    private int                ticks;
    private double             millisLeft;
    private boolean            stopOnWatchdog = false;

    public Watchdog(PicProcessor processor)
    {
        this.proc = processor;
    }

    public void onTick()
    {
        ticks++;

        double millisPassed = 0.004d / proc.getFrequency() * ticks;
        int millisToPass = timeoutMillis;

        short options = proc.getMemoryControl().getAt(SpecialFunctionRegister.OPTION_REG);
        if ((options & 0b100) != 0)
        {
            // the Prescaler is assigned to the WDT
            int timeFactor = (int) Math.pow(2, (options & 0b111));
            millisToPass *= timeFactor;
        }

        if (millisPassed >= millisToPass)
        {
            millisLeft = 0.0d;
            proc.reset(proc.isSleeping() ? PicProcessor.WDT_IN_SLEEP : PicProcessor.WDT);
            ticks = 0;
            if (stopOnWatchdog)
            {
                proc.stopProgramExecution();
            }
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

    public void enableStopOnWatchdog(boolean doStop)
    {
        stopOnWatchdog = doStop;
    }

    public void resetPrescaler()
    {
        Memorycontrol memCtrl = proc.getMemoryControl();
        short options = memCtrl.getAt(SpecialFunctionRegister.OPTION_REG);
        if ((options & 0b100) != 0)
        {
            // the Prescaler is assigned to the WDT, reset it
            memCtrl.setAt(SpecialFunctionRegister.OPTION_REG, (short) (options & 0xFC));
        }
    }
}
