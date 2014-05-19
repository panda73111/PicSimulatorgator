package pic.simulator.parser.commands;

import pic.simulator.PicMemorycontrol;
import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Command;
import pic.simulator.specialfunctionregisters.Pcl;

public class Call extends Command
{
    private static final short argumentCount = 1;
    private static final short cycleCount    = 2;
    private int                cmdNumber;

    private short              arg0;

    public Call(int cmdNumber, short arg0)
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
        PicMemorycontrol memCtrl = (PicMemorycontrol) proc.getMemoryControl();
        Pcl pcl = (Pcl) memCtrl.getSFR(SpecialFunctionRegister.PCL);

        short oldPclVal = pcl.get13BitValue();
        proc.getMemoryControl().pushStack(oldPclVal);

        short newPclVal = (short) ((oldPclVal & 0x1800) | (arg0 & 0x07FF));
        pcl.set13BitValue(newPclVal);
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
