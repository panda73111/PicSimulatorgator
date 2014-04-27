package pic.simulator;

import java.io.IOException;

import pic.simulator.interrupts.Interruption;
import pic.simulator.parser.Command;
import pic.simulator.parser.Program;
import pic.simulator.pins.Pin;
import pic.simulator.specialfunctionregisters.Pcl;
import pic.simulator.specialfunctionregisters.Tmr0;

public class Processor
{
    // reset causes
    public static final int     POWER_ON           = 0;
    public static final int     MCLR               = 1;
    public static final int     MCLR_IN_SLEEP      = 2;
    public static final int     WDT                = 3;
    public static final int     WDT_IN_SLEEP       = 4;

    private Program             picProgram;
    private Memorycontrol       memControl;
    private GUIHandler          guiHandler;
    private PinHandler          pinHandler;
    private InterruptionHandler interruptionHandler;

    private int                 progCounterAddress = SpecialFunctionRegister.PCL;
    public byte                 workRegister       = 0x00;
    private boolean             isInterrupted      = false;
    private boolean             isRunning          = false;

    private Pcl                 pcl;

    private Tmr0                timer0;
    private int                 cntPinPrevState;

    public Processor(String programFileName) throws IOException
    {
        Reset(POWER_ON);

        picProgram = new Program(programFileName);
    }

    public void executeProgram()
    {
        byte progCounter;
        int cntPinCurState;

        isRunning = true;

        while (isRunning && (progCounter = memControl.getAt(progCounterAddress)) < picProgram.length())
        {
            if (isInterrupted)
            {
                Interruption interruption = interruptionHandler.getInterruption();
            }

            Command cmd = fetch(progCounter);
            pcl.increment();
            execute(cmd);

            timer0.onTick();
            cntPinCurState = pinHandler.getExternalPinState(Pin.RA4);
            if (cntPinCurState != cntPinPrevState)
            {
                timer0.onPinChange(cntPinCurState);
                cntPinPrevState = cntPinCurState;
            }

            System.out.println("---Executed " + cmd.toString() + "---");

            guiHandler.repaintGUI();
        }
        isRunning = false;
    }

    private Command fetch(int cmdIndex)
    {
        return picProgram.getCommand(cmdIndex);
    }

    private void execute(Command cmd)
    {
        cmd.execute(this);
    }

    public void stopProgramExecution()
    {
        isRunning = false;
    }

    public void Reset(int cause)
    {
        switch (cause)
        {
            case POWER_ON:
                // initialize everything
                pinHandler = new PinHandler();
                
                PicMemorycontrol picMemCtrl = new PicMemorycontrol(this);
                memControl = picMemCtrl;
                
                guiHandler = new GUIHandler();
                
                interruptionHandler = new InterruptionHandler(this);
                
                pcl = (Pcl) picMemCtrl.getSFR(SpecialFunctionRegister.PCL);
                timer0 = (Tmr0) picMemCtrl.getSFR(SpecialFunctionRegister.TMR0);
                
                cntPinPrevState = pinHandler.getExternalPinState(Pin.RA4);
                break;
            case MCLR:
                break;
            case MCLR_IN_SLEEP:
                break;
            case WDT:
                break;
            case WDT_IN_SLEEP:
                break;
        }
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
}
