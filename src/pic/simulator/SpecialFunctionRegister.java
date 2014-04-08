package pic.simulator;

public class SpecialFunctionRegister
{
	// Bank0
	static final int INDF0 	= 0x00;
	static final int TMR0 	= 0x01;
	static final int PCL0 	= 0x02;
	static final int STATUS0= 0x03;
	static final int FSR0 	= 0x04;
	static final int PORTA	= 0x05;
	static final int PORTB 	= 0x06;
	static final int NONE0 	= 0x07;
	static final int EEDATA = 0x08;
	static final int EEADR	= 0x09;
	static final int PCLATH	= 0x0A;
	static final int INTCON	= 0x0B;

	// Bank1	
	static final int INDF1 	= 0x80;
	static final int OPTION_REG	= 0x81;
	static final int PCL1 	= 0x82;
	static final int STATUS1= 0x83;
	static final int FSR1 	= 0x84;
	static final int TRISA	= 0x85;
	static final int TRISB 	= 0x86;
	static final int NONE1 	= 0x87;
	static final int EECON1 = 0x88;
	static final int EECON2	= 0x89;
	
	final Memorycontrol memControl;
	
	public SpecialFunctionRegister(Memorycontrol memControl)
	{
		this.memControl = memControl;
		initSFR(memControl);
	}
	
	void initSFR(Memorycontrol memControl)
	{
		memControl.setAt(INDF0, 	(byte) 0b00000000);
		memControl.setAt(TMR0, 		(byte) 0b00000000);
		memControl.setAt(PCL0, 		(byte) 0b00000000);
		memControl.setAt(STATUS0,	(byte) 0b00011000);
		memControl.setAt(FSR0, 		(byte) 0b00000000);
		memControl.setAt(PORTA, 	(byte) 0b00000000);
		memControl.setAt(PORTB, 	(byte) 0b00000000);
		memControl.setAt(NONE0, 	(byte) 0b00000000);
		memControl.setAt(EEDATA, 	(byte) 0b00000000);
		memControl.setAt(EEADR, 	(byte) 0b00000000);
		memControl.setAt(PCLATH, 	(byte) 0b00000000);
		memControl.setAt(INTCON, 	(byte) 0b00000000);
		
		memControl.setAt(INDF1, 	(byte) 0b00000000);
		memControl.setAt(OPTION_REG,(byte) 0b11111111);
		memControl.setAt(PCL1, 		(byte) 0b00000000);
		memControl.setAt(STATUS1, 	(byte) 0b00011000);
		memControl.setAt(FSR1, 		(byte) 0b00000000);
		memControl.setAt(TRISA, 	(byte) 0b00011111);
		memControl.setAt(TRISB, 	(byte) 0b11111111);
		memControl.setAt(NONE1, 	(byte) 0b00000000);
		memControl.setAt(EECON1, 	(byte) 0b00000000);
		memControl.setAt(EECON2, 	(byte) 0b00000000);
	}
	
	public byte getAt(int address)
	{
		switch(address)
		{
		
		case PORTA:
		case PCLATH:
		case TRISA:
		case EECON1:
			byte val = memControl.getFromMemoryAt(address);
			return (byte) (val & 0b00011111);
			
		case INDF0:
		case INDF1:
		case NONE0:
		case NONE1:
		case EECON2:
			return 0;
		
		
		case TMR0:
			//TODO
		default:
			return memControl.getFromMemoryAt(address);
		}
	}
	
}
