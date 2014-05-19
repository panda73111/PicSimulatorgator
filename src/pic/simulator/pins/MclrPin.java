package pic.simulator.pins;

import pic.simulator.PicProcessor;

public class MclrPin extends Pin
{
    private final PicProcessor processor;
    
    public MclrPin(String name, int id, PicProcessor processor)
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
        processor.reset(processor.isSleeping() ? PicProcessor.MCLR_IN_SLEEP : PicProcessor.MCLR);
    }

}
