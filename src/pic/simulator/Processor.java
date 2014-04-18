package pic.simulator;

import java.io.IOException;

import pic.simulator.interrupts.Interruption;
import pic.simulator.parser.Command;
import pic.simulator.parser.Program;

public class Processor
{

    private Program               picProgram;
    private Memorycontrol         memControl;
	private GUIHandler 			  guiHandler;
    private PinHandler			  pinHandler;
    private InterruptionHandler	  interruptionHandler;
	
	
    private int                   progCounterAddress = SpecialFunctionRegister.PCL;
    public byte                   workRegister       = 0x00;
    private boolean               isInterrupted      = false;
    private boolean				  isRunning			 = false;

    public Processor(String programFileName) throws IOException
    {
        picProgram 	= new Program(programFileName);
        memControl 	= new PicMemorycontrol(this);
        pinHandler 	= new PinHandler();
		guiHandler 	= new GUIHandler();
		interruptionHandler = new InterruptionHandler(this);
    }

    public void executeProgram()
    {
        byte progCounter;

        isRunning = true;

        while (isRunning && (progCounter = memControl.getAt(progCounterAddress)) < picProgram.length())
        {
            if (isInterrupted)
            {
            	Interruption interruption = interruptionHandler.getInterruption();
            }

            Command cmd = fetch(progCounter);
            incrementPCL();
            execute(cmd);

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

    private void incrementPCL()
    {
        memControl.setAt(progCounterAddress, (byte) (memControl.getAt(progCounterAddress) + 1));
    }
    
    public void stopProgramExecution()
    {
    	isRunning = false;
    }
	
	public Memorycontrol getMemoryControl()
	{
		return memControl;
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
