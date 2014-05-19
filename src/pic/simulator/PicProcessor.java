package pic.simulator;

import java.io.IOException;
import java.util.HashSet;

import pic.simulator.interrupts.Interruption;
import pic.simulator.parser.Command;
import pic.simulator.parser.Program;
import pic.simulator.pins.Pin;
import pic.simulator.specialfunctionregisters.Intcon;
import pic.simulator.specialfunctionregisters.Optionreg;
import pic.simulator.specialfunctionregisters.Pcl;
import pic.simulator.specialfunctionregisters.Status;
import pic.simulator.specialfunctionregisters.Tmr0;
import pic.simulator.specialfunctionregisters.Trisa;
import pic.simulator.specialfunctionregisters.Trisb;

public class PicProcessor implements Processor
{
    // reset causes
    public static final int     POWER_ON      = 0;
    public static final int     MCLR          = 1;
    public static final int     MCLR_IN_SLEEP = 2;
    public static final int     WDT           = 3;
    public static final int     WDT_IN_SLEEP  = 4;

    private Program             picProgram;
    private Memorycontrol       memControl;
    private GUIHandler          guiHandler;
    private PinHandler          pinHandler;
    private InterruptionHandler interruptionHandler;
    private Watchdog            watchdog;

    private HashSet<Integer>    breakPointSet;

    private long                cycleCount    = 0;
    public long                 cmdDelay      = 0;
    public byte                 workRegister  = 0x00;
    private boolean             isRunning     = false;
    private boolean             isSleeping    = false;
    private double              frequency     = 4.0f; // in MHz
    private boolean             wdtEnabled;
    private Pcl                 pcl;
    private Tmr0                timer0;
    private int                 cntPinPrevState;

    public PicProcessor()
    {
        breakPointSet = new HashSet<Integer>();

        pinHandler = new PicPinHandler(this);
        interruptionHandler = new InterruptionHandler(this);
        memControl = new PicMemorycontrol(this);
        watchdog = new Watchdog(this);

        // Reset(POWER_ON); already done by MainFrame

        guiHandler = new GUIHandler();
        wdtEnabled = true; 
    }

    public PicProcessor(String programFileName) throws IOException
    {
        this();
        picProgram = new Program(programFileName);
    }
    public PicProcessor(Program p)
    {
        this();
        picProgram = p;
    }

    public void executeProgram()
    {
        if (picProgram == null)
        {
            System.err.println("Kein Programm geladen.");
            return;
        }

        isRunning = true;
        watchdog.reset();

        while (isRunning && pcl.get13BitValue() < picProgram.length())
        {
            executeNextCommand();
        }
        isRunning = false;
    }

    public void setWdtState(boolean enabled)
    {
        wdtEnabled = enabled;
    }

    public boolean isWdtEnabled()
    {
        return wdtEnabled;
    }

    public double getFrequency()
    {
        return frequency;
    }

    public void setFrequency(double frequency)
    {
        if (frequency <= 0)
            throw new IllegalArgumentException("frequency");

        this.frequency = frequency;
    }

    private void timerTick()
    {
        int cntPinCurState;
        timer0.onTick();
        if (wdtEnabled)
            watchdog.onTick();
        cntPinCurState = pinHandler.getExternalPinState(Pin.RA4);
        if (cntPinCurState != cntPinPrevState)
        {
            timer0.onPinChange(cntPinCurState);
            cntPinPrevState = cntPinCurState;
        }
    }

    public void executeNextCommand()
    {
        if (pcl.get13BitValue() < picProgram.length())
        {
            if (interruptionHandler.hasInterruption())
            {
                Interruption interruption = interruptionHandler.getInterruption();
                interruption.executeInterruption();
                return;
            }

            if (isSleeping)
            {
                // let the watchdog wakeup the processor
                if (wdtEnabled)
                    watchdog.onTick();
                try
                {
                    Thread.sleep(cmdDelay);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return;
            }

            Command cmd = fetch(pcl.get13BitValue());

            if (cmd == null) // case: Breakpoint
                return;

            pcl.increment();
            execute(cmd);

            timerTick();

            // System.out.println("---Executed " + cmd.toString() + "---");

            guiHandler.repaintGUI();
            try
            {
                Thread.sleep(cmdDelay);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Command fetch(int cmdIndex)
    {
        if (breakPointSet.contains(cmdIndex))
        {
            stopProgramExecution();
            return null;
        }
        return picProgram.getCommand(cmdIndex);
    }

    public void execute(Command cmd)
    {
        cmd.execute(this);
        cycleCount += cmd.getCycleCount();
    }

    public void loadProgram(String filename) throws IOException
    {
        picProgram = new Program(filename);
    }

    public void stopProgramExecution()
    {
        isRunning = false;
    }

    public boolean isSleeping()
    {
        return isSleeping;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void reset(int cause)
    {
        PicMemorycontrol picMemCtrl = (PicMemorycontrol) memControl;
        Status statusReg = (Status) picMemCtrl.getSFR(SpecialFunctionRegister.STATUS);
        Intcon intconReg = (Intcon) picMemCtrl.getSFR(SpecialFunctionRegister.INTCON);
        Optionreg optionReg = (Optionreg) picMemCtrl.getSFR(SpecialFunctionRegister.OPTION_REG);
        Trisa trisaReg = (Trisa) picMemCtrl.getSFR(SpecialFunctionRegister.TRISA);
        Trisb trisbReg = (Trisb) picMemCtrl.getSFR(SpecialFunctionRegister.TRISB);
        Pcl pcl = (Pcl)picMemCtrl.getSFR(SpecialFunctionRegister.PCL);
        
        switch (cause)
        {
            case POWER_ON:
                // initialize everything

                cycleCount = 0;
                workRegister = 0;

                picMemCtrl.reset();
                memControl.clearGP();
                interruptionHandler.reset();
                watchdog.reset();

                this.pcl = (Pcl) picMemCtrl.getSFR(SpecialFunctionRegister.PCL);
                this.timer0 = (Tmr0) picMemCtrl.getSFR(SpecialFunctionRegister.TMR0);

                cntPinPrevState = pinHandler.getExternalPinState(Pin.RA4);
                break;
            case MCLR:
                statusReg.setValue((byte) (statusReg.getValue() & 0x7));
                pcl.set13BitValue((short) 0);
                intconReg.reset();
                optionReg.reset();
                trisaReg.reset();
                trisbReg.reset();
                break;
            case MCLR_IN_SLEEP:
                statusReg.setValue((byte) (statusReg.getValue() & 0x7));
                statusReg.clearBit(3);
                statusReg.setBit(4);
                pcl.set13BitValue((short) 0);
                intconReg.reset();
                optionReg.reset();
                trisaReg.reset();
                trisbReg.reset();
                break;
            case WDT:
                statusReg.setValue((byte) (statusReg.getValue() & 0x7));
                statusReg.setBit(3);
                statusReg.clearBit(4);
                pcl.set13BitValue((short) 0);
                intconReg.reset();
                optionReg.reset();
                trisaReg.reset();
                trisbReg.reset();
                break;
            case WDT_IN_SLEEP:
                statusReg.clearBit(3);
                statusReg.clearBit(4);
                wakeup();
                break;
        }
    }

    public void sleep()
    {
        isSleeping = true;
        watchdog.reset();
    }

    public void wakeup()
    {
        isSleeping = false;
    }

    public long getCycleCount()
    {
        return cycleCount;
    }

    public Memorycontrol getMemoryControl()
    {
        return memControl;
    }

    public PinHandler getPinHandler()
    {
        return pinHandler;
    }

    public GUIHandler getGuiHandler()
    {
        return guiHandler;
    }

    public Program getProgram()
    {
        return picProgram;
    }

    public InterruptionHandler getInterruptionHandler()
    {
        return interruptionHandler;
    }

    public Watchdog getWatchdog()
    {
        return watchdog;
    }

    public void addBreakPoint(int address)
    {
        breakPointSet.add(address);
    }

    public void removeBreakPoint(int address)
    {
        breakPointSet.remove(address);
    }

    @Override
    public void run()
    {
        executeProgram();
    }
}
