package pic.simulator.specialfunctionregisters;

import pic.simulator.PicMemorycontrol;
import pic.simulator.SpecialFunctionRegister;

public class Eecon1 extends SpecialFunctionRegister
{
    private final PicMemorycontrol memCtrl;
    private Eecon2                 eecon2;
    private byte                   value;

    public Eecon1(PicMemorycontrol memCtrl)
    {
        this.memCtrl = memCtrl;
        reset();
    }

    @Override
    public void setValue(byte value)
    {
        this.value = (byte) (value & 0b11100);

        // check for read attempt
        if ((value & 0b1) != 0)
        {
            // RD bit set
            byte adr = memCtrl.getAt(SpecialFunctionRegister.EEADR);
            byte data = memCtrl.getEepromByte(adr);
            memCtrl.setAt(SpecialFunctionRegister.EEDATA, data);
        }

        // check for write attempt
        if ((value & 0b110) == 0b110)
        {
            // WR and WREN bits set
            if (eecon2.isWriteAllowed())
            {
                // clear WRERR bit
                this.value &= ~0b1000;
                // set WR bit, writing is in progress
                this.value |= 0b10;

                byte adr = memCtrl.getAt(SpecialFunctionRegister.EEADR);
                byte data = memCtrl.getAt(SpecialFunctionRegister.EEDATA);
                memCtrl.setEepromByte(adr, data);

                // write successful, clear WR bits
                // TODO: timer to delay the write process
                this.value &= ~0b10;
                // TODO: EEPROM write interrupt
            }
            else
            {
                onWriteError();
            }
        }
    }

    public void onWriteError()
    {
        // set WRERR bit
        value |= 0b1000;
    }

    @Override
    public byte getValue()
    {
        return (byte) (value & 0b11100);
    }

    @Override
    public void reset()
    {
        value = 0;
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

    @Override
    public void onMemInitFinished()
    {
        eecon2 = (Eecon2) memCtrl.getSFR(SpecialFunctionRegister.EECON2);
    }
}
