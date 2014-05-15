package pic.simulator.specialfunctionregisters;

import pic.simulator.PicMemorycontrol;
import pic.simulator.SpecialFunctionRegister;

public class Eecon2 extends SpecialFunctionRegister
{
    private final PicMemorycontrol memCtrl;
    private byte                   value;
    private boolean                writeAllowed;

    public Eecon2(PicMemorycontrol memCtrl)
    {
        this.memCtrl = memCtrl;
        reset();
    }

    public boolean isWriteAllowed()
    {
        return writeAllowed;
    }

    @Override
    public void setValue(byte value)
    {
        Eecon1 eecon1 = (Eecon1) memCtrl.getSFR(SpecialFunctionRegister.EECON1);
        // write sequence has to be 0x55, 0xAA
        writeAllowed = false;
        if (this.value == 0x55)
        {
            if (value == 0xAA)
                writeAllowed = true;
            else
                eecon1.onWriteError();
        }
        else if (value != 0x55)
            eecon1.onWriteError();
        this.value = value;
    }

    @Override
    public byte getValue()
    {
        return 0;
    }

    @Override
    public void reset()
    {
        value = 0;
        writeAllowed = false;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
