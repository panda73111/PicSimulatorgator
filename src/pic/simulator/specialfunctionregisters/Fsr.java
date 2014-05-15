package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Fsr extends SpecialFunctionRegister
{
    private byte value;

	public Fsr()
	{
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
