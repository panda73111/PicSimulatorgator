package pic.simulator;

public abstract class SpecialFunctionRegister
{
    // Bank0
    public static final int   INDF0      = 0x00;
    public static final int   TMR0       = 0x01;
    public static final int   PCL0       = 0x02;
    public static final int   STATUS0    = 0x03;
    public static final int   FSR0       = 0x04;
    public static final int   PORTA      = 0x05;
    public static final int   PORTB      = 0x06;
    public static final int   NONE0      = 0x07;
    public static final int   EEDATA     = 0x08;
    public static final int   EEADR      = 0x09;
    public static final int   PCLATH0    = 0x0A;
    public static final int   INTCON0    = 0x0B;

    // Bank1
    public static final int   INDF1      = 0x80;
    public static final int   OPTION_REG = 0x81;
    public static final int   PCL1       = 0x82;
    public static final int   STATUS1    = 0x83;
    public static final int   FSR1       = 0x84;
    public static final int   TRISA      = 0x85;
    public static final int   TRISB      = 0x86;
    public static final int   NONE1      = 0x87;
    public static final int   EECON1     = 0x88;
    public static final int   EECON2     = 0x89;
    public static final int   PCLATH1    = 0x8A;
    public static final int   INTCON1    = 0x8B;

    // Nicer access
    public static final int   PCL        = PCL0;
    public static final int   PCLATH     = PCLATH0;
    public static final int   STATUS     = STATUS0;
    public static final int   INTCON     = INTCON0;
    public static final int   INDF       = INDF0;
    public static final int   FSR        = FSR0;

    public static final short STATUS_C   = 0;
    public static final short STATUS_DC  = 1;
    public static final short STATUS_Z   = 2;
    public static final short STATUS_PD  = 3;
    public static final short STATUS_TO  = 4;
    public static final short STATUS_RP0 = 5;

    public abstract void setValue(short val);

    public abstract short getValue();

    public abstract void reset();

    public abstract String getName();
}
