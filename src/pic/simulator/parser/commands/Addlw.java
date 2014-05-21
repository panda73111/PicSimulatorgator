package pic.simulator.parser.commands;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;

public class Addlw extends Command
{
    private static final short argumentCount = 1;
    private static final short cycleCount    = 1;
    private int                cmdNumber;

    private short              arg0;

    public Addlw(int cmdNumber, short arg0)
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
        
        short newValue = (short) (w + val2);

        boolean setDC = (((proc.workRegister & 0x0F) + (arg0 & 0x0F)) & 0x10) != 0;
        boolean setC = (newValue & 0x100) != 0;

        proc.workRegister = (short) (0xFF & newValue);

        if (setDC)
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_DC);
        else
            proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_DC);

        if (setC)
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_C);
        else
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_C);

        affectZeroBit(proc, newValue);
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
