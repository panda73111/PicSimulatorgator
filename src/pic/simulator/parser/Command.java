package pic.simulator.parser;

import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.commands.*;

public abstract class Command
{
    public static Command newInstance(int cmdNumber, short opcode)
    {
        short arg0 = (short) (opcode & 0x7f);
        short arg1 = (short) ((opcode & 0x80) >> 7);

        // first group of commands: byte-oriented file register operations
        switch ((opcode & 0x3f_00) >> 8)
        {
            case 0b00_0111:
                return new Addwf(cmdNumber, arg0, arg1);
            case 0b00_0101:
                return new Andwf(cmdNumber, arg0, arg1);
            case 0b00_0001:
                if (arg1 == 1)
                    return new Clrf(cmdNumber, arg0);
                return new Clrw(cmdNumber);
            case 0b00_1001:
                return new Comf(cmdNumber, arg0, arg1);
            case 0b00_0011:
                return new Decf(cmdNumber, arg0, arg1);
            case 0b00_1011:
                return new Decfsz(cmdNumber, arg0, arg1);
            case 0b00_1010:
                return new Incf(cmdNumber, arg0, arg1);
            case 0b00_1111:
                return new Incfsz(cmdNumber, arg0, arg1);
            case 0b00_0100:
                return new Iorwf(cmdNumber, arg0, arg1);
            case 0b00_1000:
                return new Movf(cmdNumber, arg0, arg1);
            case 0b00_0000:
                if (arg1 == 1)
                    return new Movwf(cmdNumber, arg0);
                if ((arg0 & 0x1F) == 0b00000)
                    return new Nop(cmdNumber);
                if ((opcode & 0x3f_ff) == 0b00_0000_0000_1001)
                    return new Retfie(cmdNumber);
                if ((opcode & 0x3f_ff) == 0b00_0000_0000_1000)
                    return new Return(cmdNumber);
                if ((opcode & 0x3f_ff) == 0b00_0000_0110_0011)
                    return new Sleep(cmdNumber);
                if ((opcode & 0x3f_ff) == 0b00_0000_0110_0100)
                    return new Clrwdt(cmdNumber);
            case 0b00_1101:
                return new Rlf(cmdNumber, arg0, arg1);
            case 0b00_1100:
                return new Rrf(cmdNumber, arg0, arg1);
            case 0b00_0010:
                return new Subwf(cmdNumber, arg0, arg1);
            case 0b00_1110:
                return new Swapf(cmdNumber, arg0, arg1);
            case 0b00_0110:
                return new Xorwf(cmdNumber, arg0, arg1);
            default:
                arg1 = (short) ((opcode & 0x0380) >> 7);

                // second group of commands: bit-oriented file register
                // operations
                switch ((opcode & 0x3c_00) >> 10)
                {
                    case 0b01_00:
                        return new Bcf(cmdNumber, arg0, arg1);
                    case 0b01_01:
                        return new Bsf(cmdNumber, arg0, arg1);
                    case 0b01_10:
                        return new Btfsc(cmdNumber, arg0, arg1);
                    case 0b01_11:
                        return new Btfss(cmdNumber, arg0, arg1);
                }

                arg0 = (short) (opcode & 0xff);

                // third group of commands: literal and control operations
                if ((opcode & 0x3e_00) >> 9 == 0b11_111)
                    return new Addlw(cmdNumber, arg0);
                if ((opcode & 0x3f_00) >> 8 == 0b11_1001)
                    return new Andlw(cmdNumber, arg0);
                if ((opcode & 0x38_00) >> 11 == 0b10_0)
                    return new Call(cmdNumber, (short) (opcode & 0x07_ff));
                if ((opcode & 0x38_00) >> 11 == 0b10_1)
                    return new Goto(cmdNumber, (short) (opcode & 0x07_ff));
                if ((opcode & 0x3f_00) >> 8 == 0b11_1000)
                    return new Iorlw(cmdNumber, arg0);
                if ((opcode & 0x3c_00) >> 10 == 0b11_00)
                    return new Movlw(cmdNumber, arg0);
                if ((opcode & 0x3c_00) >> 10 == 0b11_01)
                    return new Retlw(cmdNumber, arg0);
                if ((opcode & 0x3e_00) >> 9 == 0b11_110)
                    return new Sublw(cmdNumber, arg0);
                if ((opcode & 0x3f_00) >> 8 == 0b11_1010)
                    return new Xorlw(cmdNumber, arg0);
        }
        return null;
    }

    public void affectZeroBit(PicProcessor proc, short result)
    {
        if (result == 0)
            proc.getMemoryControl().setStatusBit(SpecialFunctionRegister.STATUS_Z);
        else
            proc.getMemoryControl().clearStatusBit(SpecialFunctionRegister.STATUS_Z);
    }

    public abstract short getArgumentCount();

    public abstract short getCycleCount();

    public abstract String getCmdName();

    public abstract short getArg0();

    public abstract short getArg1();

    public abstract void execute(PicProcessor processor);

    @Override
    public String toString()
    {
        switch (getArgumentCount())
        {
            case 2:
                return String.format("%s 0x%04X, 0x%04X", getCmdName(), getArg0(), getArg1());
            case 1:
                return String.format("%s 0x%04X", getCmdName(), getArg0());
            default:
                return String.format("%s", getCmdName());
        }
    }
}
