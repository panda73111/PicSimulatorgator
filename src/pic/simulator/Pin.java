package pic.simulator;

public class Pin
{
    public static final int OSC1 = 0;
    public static final int OSC2 = 1;
    public static final int MCLR = 2;
    public static final int RA0 = 3;
    public static final int RA1 = 4;
    public static final int RA2 = 5;
    public static final int RA3 = 6;
    public static final int RA4 = 7;
    public static final int RB0 = 8;
    public static final int RB1 = 9;
    public static final int RB2 = 10;
    public static final int RB3 = 11;
    public static final int RB4 = 12;
    public static final int RB5 = 13;
    public static final int RB6 = 14;
    public static final int RB7 = 15;
    
    private final String name;
    
    public Pin(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
}
