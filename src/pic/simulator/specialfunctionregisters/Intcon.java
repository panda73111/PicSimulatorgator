package pic.simulator.specialfunctionregisters;

import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;

public class Intcon extends SpecialFunctionRegister
{
	
	public static final short GENERAL_INTERRUPT_ENABLE = 7;
	public static final short EEPROM_ENABLE = 6;
	public static final short TIMER0_ENABLE = 5;
	public static final short RB0_ENABLE = 4;
	public static final short PORTB_ENABLE = 3;
	
    private byte value;

	public Intcon(Processor processor)
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
