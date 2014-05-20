package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Optionreg extends SpecialFunctionRegister
{
	public static final short INTEDG = 6;
	
    private short value;

	public Optionreg()
	{
		reset();
	}
	
	@Override
	public void setValue(short value) {
		this.value = value;
	}

	@Override
	public short getValue() {
		return value;
	}

	@Override
	public void reset() {
		value = 0xFF;
	}

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
