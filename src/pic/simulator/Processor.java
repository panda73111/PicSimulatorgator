package pic.simulator;

import java.io.IOException;
import java.util.HashMap;

import pic.simulator.parser.*;

public class Processor
{
    public byte                   workRegister       = 0x00;

    private int                   progCounterAddress = SpecialFunctionRegister.PCL;
    private Program               picProgram;
    private Memorycontrol         memControl;
    private HashMap<Integer, Pin> pins;
    private boolean               isInterrupted      = false;

    public Processor(String programFileName) throws IOException
    {
        picProgram = new Program(programFileName);
        memControl = new Memorycontrol(this, 0xFF, (short) 2);
        pins = new HashMap<Integer, Pin>();

        setupPins();
    }

    private void setupPins()
    {
        pins.put(Pin.OSC1, new Pin("OSC1/CLKIN"));
        pins.put(Pin.OSC2, new Pin("OSC2/CLKOUTOSC2/CLKOUT"));
        pins.put(Pin.MCLR, new Pin("MCLR"));
        pins.put(Pin.RA0, new IOPin("RA0"));
        pins.put(Pin.RA1, new IOPin("RA1"));
        pins.put(Pin.RA2, new IOPin("RA2"));
        pins.put(Pin.RA3, new IOPin("RA3"));
        pins.put(Pin.RA4, new IOPin("RA4/T0CKI"));
        pins.put(Pin.RB0, new IOPin("RB0/INT"));
        pins.put(Pin.RB1, new IOPin("RB1"));
        pins.put(Pin.RB2, new IOPin("RB2"));
        pins.put(Pin.RB3, new IOPin("RB3"));
        pins.put(Pin.RB4, new IOPin("RB4"));
        pins.put(Pin.RB5, new IOPin("RB5"));
        pins.put(Pin.RB6, new IOPin("RB6"));
        pins.put(Pin.RB7, new IOPin("RB7"));
    }

    public void executeProgram()
    {
        byte progCounter;

        boolean doTimeMeasurement = true;
        long startTime = System.currentTimeMillis();
        long executionDuration = 5000;

        int instructionCounter = 0;

        while ((progCounter = memControl.getAt(progCounterAddress)) < picProgram.length())
        {
            if (doTimeMeasurement && System.currentTimeMillis() - startTime > executionDuration)
                break;

            instructionCounter++;

            if (isInterrupted)
            {
                // TODO
            }

            Command cmd = fetch(progCounter);
            incrementPCL();
            execute(cmd);

            // System.out.println("---Executed " + cmd.toString() + "---");

            // TODO repaint

            // memControl.printMemory();
        }
        System.out.println(instructionCounter + " instructions in " + executionDuration / 1000 + " second.");
        System.out.println("Ca " + instructionCounter / (executionDuration / 1000) + " instructions per second");
    }

    private Command fetch(int cmdIndex)
    {
        return picProgram.getCommand(cmdIndex);
    }

    private void execute(Command cmd)
    {
        cmd.execute(this);
    }

    private void incrementPCL()
    {
        memControl.setAt(progCounterAddress, (byte) (memControl.getAt(progCounterAddress) + 1));
    }

    public byte getAtAddress(int address)
    {
        return memControl.getAt(address);
    }

    public void setAtAddress(int address, byte value)
    {
        memControl.setAt(address, value);
    }

    public boolean getBitAtAddress(int address, short bit)
    {
        return (memControl.getAt(address) & (1 << bit)) != 0;
    }

    public void setBitAtAddress(int address, short bit)
    {
        byte val = (byte) (memControl.getAt(address) | (1 << bit));
        memControl.setAt(address, val);
    }

    public void clearBitAtAddress(int address, short bit)
    {
        byte val = getAtAddress(address);
        val &= 0xFF - (1 << bit);
        memControl.setAt(address, val);
    }

    public void pushStack(int value)
    {
        memControl.push(value);
    }

    public int popStack()
    {
        return memControl.pop();
    }

    public void setStatusBit(short bit)
    {
        setBitAtAddress(SpecialFunctionRegister.STATUS, bit);
    }

    public void clearStatusBit(short bit)
    {
        clearBitAtAddress(SpecialFunctionRegister.STATUS, bit);
    }
}
