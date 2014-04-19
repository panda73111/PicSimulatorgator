package pic.simulator.pins;

public abstract class Pin
{
    public static final int OSC1 = 0;
    public static final int OSC2 = 1;
    public static final int MCLR = 2;
    public static final int RA0  = 3;
    public static final int RA1  = 4;
    public static final int RA2  = 5;
    public static final int RA3  = 6;
    public static final int RA4  = 7;
    public static final int RB0  = 8;
    public static final int RB1  = 9;
    public static final int RB2  = 10;
    public static final int RB3  = 11;
    public static final int RB4  = 12;
    public static final int RB5  = 13;
    public static final int RB6  = 14;
    public static final int RB7  = 15;
    
    public static final int LOW  = 0;
    public static final int HIGH = 1;
    public static final int HI_Z = 2;

    protected final String  name;
    protected final int     id;
    protected int           externalState;
    protected int           internalState;

    public Pin(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    public int getInternalState()
    {
        return internalState;
    }

    public int getExternalState()
    {
        return externalState;
    }

    public abstract void setInternally();

    public abstract void setExternally();

    public abstract void clearInternally();

    public abstract void clearExternally();

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
}
