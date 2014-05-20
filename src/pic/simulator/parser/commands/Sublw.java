package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Sublw extends Command
{
    private static final short argumentCount = 1;
    private static final short cycleCount    = 1;
    private int                cmdNumber;

    private short              arg0;

    public Sublw(int cmdNumber, short arg0)
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
        short w = proc.workRegister;
        short res = (short) (w - arg0);

        boolean setDC = (((proc.workRegister & 0x0F) + (arg0 & 0x0F)) & 0x10) != 0;
        boolean setC = (res & 0x1FF) != 0;

        proc.workRegister = (short) (0xFF & res);

        affectZeroBit(proc, (short) res);

        if (setDC)
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_DC);
        else
            proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_DC);

        if (setC)
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_C);
        else
            proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_C);
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
