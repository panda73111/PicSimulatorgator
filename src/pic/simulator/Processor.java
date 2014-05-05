package pic.simulator;

import java.io.IOException;

import pic.simulator.interrupts.Interruption;
import pic.simulator.parser.Command;
import pic.simulator.parser.Program;
import pic.simulator.pins.Pin;
import pic.simulator.specialfunctionregisters.*;

public class Processor
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

    public byte                 workRegister  = 0x00;
    private boolean             isInterrupted = false;
    private boolean             isRunning     = false;
    private boolean             isSleeping    = false;

    private Pcl                 pcl;

    private Tmr0                timer0;
    private int                 cntPinPrevState;
    private short               configurationWord;

    public Processor()
    {
        pinHandler = new PicPinHandler(this);
        memControl = new PicMemorycontrol(this);
        
    	Reset(POWER_ON);

        guiHandler = new GUIHandler();
    }
    
    public Processor(String programFileName) throws IOException
    {
    	this();
        picProgram = new Program(programFileName);
    }

    public void executeProgram()
    {
    	if(picProgram == null)
    	{
    		System.err.println("Kein Porgramm geladen.");
    		return;	
    	}
    	
        int cntPinCurState;

        isRunning = true;

        while (isRunning && pcl.get13BitValue() < picProgram.length())
        {
            if (isInterrupted)
            {
                Interruption interruption = interruptionHandler.getInterruption();
            }

            Command cmd = fetch(pcl.get13BitValue());
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
            try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

    public void Reset(int cause)
    {
        PicMemorycontrol picMemCtrl = (PicMemorycontrol) memControl;
        Status statusReg = (Status) picMemCtrl.getSFR(SpecialFunctionRegister.STATUS);
        Intcon intconReg = (Intcon) picMemCtrl.getSFR(SpecialFunctionRegister.INTCON);
        Optionreg optionReg = (Optionreg) picMemCtrl.getSFR(SpecialFunctionRegister.OPTION_REG);
        Trisa trisaReg = (Trisa) picMemCtrl.getSFR(SpecialFunctionRegister.TRISA);
        Trisb trisbReg = (Trisb) picMemCtrl.getSFR(SpecialFunctionRegister.TRISB);

        switch (cause)
        {
            case POWER_ON:
                // initialize everything

                workRegister = 0;

                picMemCtrl.reset();
                memControl = picMemCtrl;
                memControl.clearGP();
                
                interruptionHandler = new InterruptionHandler(this);

                pcl = (Pcl) picMemCtrl.getSFR(SpecialFunctionRegister.PCL);
                timer0 = (Tmr0) picMemCtrl.getSFR(SpecialFunctionRegister.TMR0);

                cntPinPrevState = pinHandler.getExternalPinState(Pin.RA4);
                break;
            case MCLR:
                statusReg.setValue((byte) (statusReg.getValue() & 0x7));
                pcl.set13BitValue((short) 0);
                intconReg.reset();
                optionReg.reset();
                trisaReg.reset();
                break;
            case MCLR_IN_SLEEP:
                statusReg.setValue((byte) (statusReg.getValue() & 0x7));
                statusReg.clearBit(3);
                statusReg.setBit(4);
                pcl.set13BitValue((short) 0);
                intconReg.reset();
                optionReg.reset();
                trisaReg.reset();
                break;
            case WDT:
                statusReg.setValue((byte) (statusReg.getValue() & 0x7));
                statusReg.setBit(3);
                statusReg.clearBit(4);
                pcl.set13BitValue((short) 0);
                intconReg.reset();
                optionReg.reset();
                trisaReg.reset();
                break;
            case WDT_IN_SLEEP:
                statusReg.clearBit(3);
                statusReg.clearBit(4);
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
