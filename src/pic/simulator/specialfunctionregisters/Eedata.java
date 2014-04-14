package pic.simulator.specialfunctionregisters;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;

public class Eedata extends SpecialFunctionRegister
{
	Processor processor;
	byte value;
	
	public Eedata(Processor processor)
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
		this.value = 0;
	}

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
