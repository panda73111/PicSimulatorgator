package pic.simulator.interrupts;

import pic.simulator.PicMemorycontrol;
import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.specialfunctionregisters.Intcon;
import pic.simulator.specialfunctionregisters.Pcl;

public class Interruption {
	public static final int TIMER0 = 0;
	public static final int RB0 = 1;
	public static final int PORTB = 2;
	public static final int EEPROM = 3;

	private int cause;
	private Processor proc;

	public Interruption(Processor proc, int cause) {
		this.proc = proc;
		this.cause = cause;
	}

	public int getCause() {
		return cause;
	}

	public void executeInterruption() {
		if (!isEnabled())
			return;

		PicMemorycontrol mem = (PicMemorycontrol) proc.getMemoryControl();
		mem.setBitAt(SpecialFunctionRegister.INTCON,
				Intcon.GENERAL_INTERRUPT_ENABLE);
		mem.pushStack(proc.getMemoryControl()
				.getAt(SpecialFunctionRegister.PCL));
		Pcl pcl = (Pcl) mem.getSFR(SpecialFunctionRegister.PCL);
		pcl.set13BitValue((short) 0x0004);
	}

	public boolean isEnabled() {
		if (!proc.getMemoryControl().getBitAt(SpecialFunctionRegister.INTCON,
				Intcon.GENERAL_INTERRUPT_ENABLE))
			return false;

		short enableBit = 0;
		switch (cause) {
		case TIMER0:
			enableBit = Intcon.TIMER0_ENABLE;
			break;
		case RB0:
			enableBit = Intcon.RB0_ENABLE;
			break;
		case PORTB:
			enableBit = Intcon.PORTB_ENABLE;
			break;
		case EEPROM:
			enableBit = Intcon.EEPROM_ENABLE;
			break;
		}
		return (!proc.getMemoryControl().getBitAt(
				SpecialFunctionRegister.INTCON, enableBit));
	}
}
