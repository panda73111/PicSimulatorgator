package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;

import pic.simulator.parser.Command;

public class Movlw extends Command
{
    private static final short argumentCount = 1;
    private static final short cycleCount    = 1;
    private int                cmdNumber;

    private short              arg0;

    public Movlw(int cmdNumber, short arg0)
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
        proc.workRegister = (short) (arg0 & 0xFF);
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
