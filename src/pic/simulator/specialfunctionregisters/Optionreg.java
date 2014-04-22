package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Optionreg extends SpecialFunctionRegister
{
    private byte value;

	public Optionreg()
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
		value = (byte) 0xFF;
	}

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
