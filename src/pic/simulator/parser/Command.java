package pic.simulator.parser;

import javax.annotation.processing.Processor;

import pic.simulator.parser.commands.*;

public abstract class Command
{
	public static Command newInstance(short opcode) {
		short arg0 = (short) (opcode & 0x7f);
		short arg1 = (short) ((opcode & 0x80) >> 7);

		// first group of commands: byte-oriented file register operations
		switch ((opcode & 0x3f_00) >> 8)
		{
		case 0b00_0111:

			return new Addwf(arg0, arg1);
		case 0b00_0101:
			return new Andwf(arg0, arg1);
		case 0b00_0001:
			if (arg1 == 1)
				return new Clrf(arg0);
			else
				return new Clrw();
		case 0b00_1001:
			return new Comf(arg0, arg1);
		case 0b00_0011:
			return new Decf(arg0, arg1);
		case 0b00_1011:
			return new Decfsz(arg0, arg1);
		case 0b00_1010:
			return new Incf(arg0, arg1);
		case 0b00_1111:
			return new Incfsz(arg0, arg1);
		case 0b00_0100:
			return new Iorwf(arg0, arg1);
		case 0b00_1000:
			return new Movf(arg0, arg1);
		case 0b00_0000:
			if (arg1 == 1)
				return new Movwf(arg0);
			else
				return new Nop();
		case 0b00_1101:
			return new Rlf(arg0, arg1);
		case 0b00_1100:
			return new Rrf(arg0, arg1);
		case 0b00_0010:
			return new Subwf(arg0, arg1);
		case 0b00_1110:
			return new Swapf(arg0, arg1);
		case 0b00_0110:
			return new Xorwf(arg0, arg1);
		default:
			arg1 = (short) ((opcode & 0x0380) >> 7);

			// second group of commands: bit-oriented file register operations
			switch ((opcode & 0x3c_00) >> 10)
			{
			case 0b01_00:
				return new Bcf(arg0, arg1);
			case 0b01_01:
				return new Bsf(arg0, arg1);
			case 0b01_10:
				return new Btfsc(arg0, arg1);
			case 0b01_11:
				return new Btfss(arg0, arg1);
			}

			arg0 = (short) (opcode & 0xff);

			// third group of commands: literal and control operations
			if ((opcode & 0x3e_00) >> 9 == 0b11_111)
				return new Addlw(arg0);
			else if ((opcode & 0x3f_00) >> 8 == 0b11_1001)
				return new Andlw(arg0);
			else if ((opcode & 0x38_00) >> 11 == 0b10_0)
				return new Call((short) (opcode & 0x07_ff));
			else if ((opcode & 0x3f_ff) == 0b00_0000_0110_0100)
				return new Clrwdt();
			else if ((opcode & 0x38_00) >> 11 == 0b10_1)
				return new Goto((short) (opcode & 0x07_ff));
			else if ((opcode & 0x3f_00) >> 8 == 0b11_1000)
				return new Iorlw(arg0);
			else if ((opcode & 0x3c_00) >> 10 == 0b11_00)
				return new Movlw(arg0);
			else if ((opcode & 0x3f_ff) == 0b00_0000_0000_1001)
				return new Retfie();
			else if ((opcode & 0x3c_00) >> 10 == 0b11_01)
				return new Retlw(arg0);
			else if ((opcode & 0x3f_ff) == 0b00_0000_0000_1000)
				return new Return();
			else if ((opcode & 0x3f_ff) == 0b00_0000_0110_0011)
				return new Sleep();
			else if ((opcode & 0x3e_00) >> 9 == 0b11_110)
				return new Sublw(arg0);
			else if ((opcode & 0x3f_00) >> 8 == 0b11_1010)
				return new Xorlw(arg0);
		}
		return null;
	}

	public abstract short getArgumentCount();

	public abstract short getCycleCount();

	public abstract void execute(Processor proc);
}
