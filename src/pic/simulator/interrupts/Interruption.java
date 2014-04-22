package pic.simulator.interrupts;

public class Interruption implements Comparable<Interruption>
{
    public static final int T0CKI    = 0;

    private int             priority = 0;
    private int             cause;

    public Interruption(int cause)
    {
        this.cause = cause;
    }

    public int getCause()
    {
        return cause;
    }

    public int getInterruptionCode()
    {
        return 0;
    }

    public int getPriority()
    {
        return priority;
    }

    @Override
    public int compareTo(Interruption arg0)
    {

        if (priority < arg0.getPriority())
            return -1;
        else if (priority > arg0.getPriority())
            return 1;
        return 0;
    }
}
