package pic.simulator;

import java.io.IOException;

import pic.simulator.parser.Command;
import pic.simulator.parser.Program;

public interface Processor extends Runnable
{
    public void loadProgram(String filename) throws IOException;
    public void executeProgram();
    public void stopProgramExecution();

    Command fetch(int cmdIndex);
    void execute(Command cmd);

    public void reset(int cause);
    
    public void sleep();
    public void wakeup();

    public void setWdtState(boolean enabled);
    public boolean isWdtEnabled();

    public double getFrequency();
    public void setFrequency(double frequency);

    public long getCycleCount();

    public boolean isSleeping();
    public boolean isRunning();

    public Memorycontrol getMemoryControl();
    public PinHandler getPinHandler();
    public GUIHandler getGuiHandler();
    public Program getProgram();
    public InterruptionHandler getInterruptionHandler();
    public Watchdog getWatchdog();
}
