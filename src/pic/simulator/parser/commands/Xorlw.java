package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.parser.Command;

public class Xorlw extends Command
{
    private static final short argumentCount = 1;
    private static final short cycleCount    = 1;
    private int                cmdNumber;

    private short              arg0;

    public Xorlw(int cmdNumber, short arg0)
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
        short val2 = (short) (arg0 & 0xFF);
        short w = (short) (proc.workRegister & 0xFF);
        short res = (short) (w ^ val2);

        proc.workRegister = res;

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
        return 0;
    }
}
