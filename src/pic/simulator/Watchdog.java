package pic.simulator;

import java.util.Timer;
import java.util.TimerTask;

public class Watchdog
{
    private final Processor processor;
    private final Timer     timer;
    private final TimerTask task;

    public Watchdog(Processor processor)
    {
        this.processor = processor;
        this.timer = new Timer();
        this.task = new TimerTask()
        {
            @Override
            public void run()
            {

            }
        };
    }

    public void start(long delay)
    {
        timer.schedule(task, delay);
    }
}
