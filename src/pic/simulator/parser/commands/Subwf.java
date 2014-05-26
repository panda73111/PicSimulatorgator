package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Subwf extends Command
{
    private static final short argumentCount = 2;
    private static final short cycleCount    = 1;
    private int                cmdNumber;

    private short              arg0, arg1;

    public Subwf(int cmdNumber, short arg0, short arg1)
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
        short w = (short) (proc.workRegister & 0xFF);
        short f = (short) (proc.getMemoryControl().getAt(arg0) & 0xFF);

        short compW = (short) (0xFF & (((~w)+1)));
        short res = (short) (f + compW);

        int resLowerNibble = (w & 0x0F) - (f & 0x0F);

        boolean setC = (res & 0x100) != 0;
        boolean setDC = (resLowerNibble & 0x10) != 0;

        res &= 0xFF;

        if (arg1 == 0)
            proc.workRegister = (short) res;
        else
            proc.getMemoryControl().setAt(arg0, (short) res);

        affectZeroBit(proc, (short) res);

        if(res==0)
        {
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_Z);
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_C);
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_DC);
        }
        else
        {
            proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_Z);
        }
        
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
        return arg1;
    }
}
