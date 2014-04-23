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
        picProgram = new Program(programFileName);
        pinHandler = new PinHandler();

        PicMemorycontrol picMemCtrl = new PicMemorycontrol(this);

        memControl = picMemCtrl;
        guiHandler = new GUIHandler();
        interruptionHandler = new InterruptionHandler(this);

        pcl = (Pcl) picMemCtrl.getSFR(SpecialFunctionRegister.PCL);

        timer0 = (Tmr0) picMemCtrl.getSFR(SpecialFunctionRegister.TMR0);
        cntPinPrevState = pinHandler.getExternalPinState(Pin.RA4);
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
