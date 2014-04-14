package pic.simulator.specialfunctionregisters;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;

public class Status extends SpecialFunctionRegister
{
	Processor processor;
	byte value;

	public Status(Processor processor)
	{
		this.processor = processor;
		reset();
	}
	
	@Override
	public void setValue(byte value) {
		this.value = value;
	}

	@Override
	public byte getValue() {
		return value;
	}

	@Override
	public void reset() {
		value = 0;
	}

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
