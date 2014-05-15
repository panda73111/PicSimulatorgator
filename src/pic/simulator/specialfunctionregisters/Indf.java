package pic.simulator.specialfunctionregisters;

import pic.simulator.PicMemorycontrol;
import pic.simulator.SpecialFunctionRegister;

public class Indf extends SpecialFunctionRegister
{
    private final PicMemorycontrol memCtrl;

    public Indf(PicMemorycontrol memCtrl)
    {
        this.memCtrl = memCtrl;
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        byte targetAddr = memCtrl.getAt(INDF);
        memCtrl.setAt(targetAddr, value);
    }

    @Override
    public byte getValue()
    {
        byte targetAddr = memCtrl.getAt(INDF);
        return memCtrl.getAt(targetAddr);
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

}
