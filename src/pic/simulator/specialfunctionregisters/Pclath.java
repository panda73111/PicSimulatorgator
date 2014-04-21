package pic.simulator.specialfunctionregisters;

import pic.simulator.PicMemorycontrol;
import pic.simulator.SpecialFunctionRegister;

public class Pclath extends SpecialFunctionRegister
{
    private final PicMemorycontrol memCtrl;
    private Pcl                    pcl;

    public Pclath(PicMemorycontrol memCtrl)
    {
        this.memCtrl = memCtrl;
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        short oldPclVal = pcl.get13BitValue();
        short newPclVal = (short) ((value << 8) | (oldPclVal & 0xFF));
        pcl.set13BitValue(newPclVal);
    }

    @Override
    public byte getValue()
    {
        // not readable
        return 0;
    }

    @Override
    public void reset()
    {
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

    @Override
    public void onMemInitFinished()
    {
        pcl = (Pcl) memCtrl.getSFR(SpecialFunctionRegister.PCL);
    }

}
