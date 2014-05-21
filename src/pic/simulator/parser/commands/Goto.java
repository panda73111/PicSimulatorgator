package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Goto extends Command
{
    private static final short argumentCount = 1;
    private static final short cycleCount    = 2;
    private int                cmdNumber;

    private short              arg0;

    public Goto(int cmdNumber, short arg0)
    {
        this.cmdNumber = cmdNumber;
        this.arg0 = arg0;
    }

    public short getArgumentCount()
    {
        return argumentCount;
    }

    public short getCycleCount()
    {
        return cycleCount;
    }

    public int getCmdNumber()
    {
        return cmdNumber;
    }

    @Override
    public void execute(PicProcessor proc)
    {
        proc.getMemoryControl().setAt(SpecialFunctionRegister.PCL, (short) (arg0 & 0xFF));
    }

    @Override
    public String getCmdName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

    @Override
    public short getArg0()
    {
        return arg0;
    }

    @Override
    public short getArg1()
    {
        return 0;
    }
}
