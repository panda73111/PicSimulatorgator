package pic.simulator.pins;

public class IOPin extends Pin
{
    private boolean isInput;

    public IOPin(String name, int id)
    {
        super(name, id);
        
        isInput = false;
    }

    public void setToInput()
    {
        isInput = true;
        externalState = Pin.HI_Z;
    }

    public void setToOutput()
    {
        isInput = false;
        externalState = internalState;
    }

    @Override
    public void setInternally()
    {
        internalState = Pin.HIGH;

        if (isInput)
            externalState = Pin.HI_Z;
        else
            externalState = Pin.HIGH;
    }

    @Override
    public void setExternally()
    {
        externalState = Pin.HIGH;

        if (!isInput)
            internalState = Pin.HIGH;
    }

    @Override
    public void clearInternally()
    {
        internalState = Pin.LOW;

        if (isInput)
            externalState = Pin.HI_Z;
        else
            externalState = Pin.LOW;
    }

    @Override
    public void clearExternally()
    {
        externalState = Pin.LOW;

        if (!isInput)
            internalState = Pin.LOW;
    }
}
