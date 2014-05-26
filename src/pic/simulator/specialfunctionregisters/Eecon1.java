package pic.simulator.specialfunctionregisters;

import pic.simulator.PicMemorycontrol;
import pic.simulator.SpecialFunctionRegister;

public class Eecon1 extends SpecialFunctionRegister {
	private final PicMemorycontrol memCtrl;
	private short value;
	private short internalValue;

	public Eecon1(PicMemorycontrol memCtrl) {
		this.memCtrl = memCtrl;
		reset();
	}

	@Override
	public void setValue(short value) {
		this.value = (short) (value & 0b11100);
		this.internalValue = value;

		// check for read attempt
		if ((value & 0b1) != 0) {
			// RD bit set
			short adr = memCtrl.getSFR(SpecialFunctionRegister.EEADR)
					.getValue();
			short data = memCtrl.getEepromByte(adr);
			memCtrl.getSFR(SpecialFunctionRegister.EEDATA).setValue(data);
		}

		memCtrl.tryEepromWrite();
	}

	public short getInternalValue() {
		return internalValue;
	}

	public void onWriteError() {
		// set WRERR bit
		value |= 0b1000;
		internalValue |= 0b1000;
	}

	@Override
	public short getValue() {
		return (short) (value & 0b11100);
	}

	@Override
	public void reset() {
		value = 0;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName().toLowerCase();
	}
}
