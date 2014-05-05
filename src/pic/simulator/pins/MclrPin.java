package pic.simulator.pins;

import pic.simulator.Processor;

public class MclrPin extends Pin
{
    private final Processor processor;
    
    public MclrPin(String name, int id, Processor processor)
    {
        super(name, id);
        
        this.processor = processor;
        this.externalState = Pin.HIGH;
    }

    @Override
    public void setInternally()
    {
    }

    @Override
    public void setExternally()
    {
        externalState = Pin.HIGH;
    }

    @Override
    public void clearInternally()
    {
    }

    @Override
    public void clearExternally()
    {
        externalState = Pin.LOW;
        processor.Reset(processor.isSleeping() ? Processor.MCLR_IN_SLEEP : Processor.MCLR);
    }

}
