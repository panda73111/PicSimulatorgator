package pic.simulator.specialfunctionregisters;

import pic.simulator.SpecialFunctionRegister;

public class Fsr extends SpecialFunctionRegister
{
    private short value;

	public Fsr()
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
		value = 0;
	}

    @Override
    public String getName()
    {
        return getClass().getSimpleName().toLowerCase();
    }

}
