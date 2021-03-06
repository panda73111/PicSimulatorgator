package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.parser.Command;

public class Xorwf extends Command
{
    private static final short argumentCount = 2;
    private static final short cycleCount    = 1;
    private int                cmdNumber;

    private short              arg0, arg1;

    public Xorwf(int cmdNumber, short arg0, short arg1)
    {
        this.cmdNumber = cmdNumber;
        this.arg0 = arg0;
        this.arg1 = arg1;
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
        short val2 = (short) (proc.getMemoryControl().getAt(arg0) & 0xFF);
        short w = (short) (proc.workRegister & 0xFF);
        short res = (short) (w ^ val2);

        if (arg1 == 0)
            proc.workRegister = res;
        else
            proc.getMemoryControl().setAt(arg0, res);

        affectZeroBit(proc, res);

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
        return arg1;
    }
}
