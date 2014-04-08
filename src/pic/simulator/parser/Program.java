package pic.simulator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Program
{

	private ArrayList<Command> program;

	public Program() {
		program = new ArrayList<>();
	}

	public Program(String filename) throws IOException {
		this();
		loadProgramFromFile(filename);
	}

	// //////////////////////////////////////////////////////////////////////

	public void loadProgramFromFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		try
		{
			String line;
			while ((line = br.readLine()) != null)
			{
				if (line.startsWith(" ") || line.length() < 9)
					continue;

				short cmdNumber = Short.valueOf(line.substring(0, 4), 16);
				short opcode = Short.valueOf(line.substring(5, 9), 16);

				Command cmd = Command.newInstance(cmdNumber, opcode);
				System.out.printf("line %4d - opcode 0x%04X - %s\n", cmdNumber, opcode, cmd.toString());
				
				program.add(cmd);
			}
		} finally
		{
			br.close();
		}
	}

	// //////////////////////////////////////////////////////////////////////

	public int length() {
		return program.size();
	}

	public Command getCommand(int cmdIndex) {
		if (cmdIndex >= program.size())
			return null;

		return program.get(cmdIndex);
	}
}
