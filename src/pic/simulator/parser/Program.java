package pic.simulator.parser;

import java.util.ArrayList;

public class Program {

	private ArrayList<Command> program;
	
	public Program()
	{	
		program = new ArrayList<>();
	}
	public Program(String filename)
	{
		this();
		loadProgramFromFile(filename);
	}
	
	////////////////////////////////////////////////////////////////////////
	
    public void loadProgramFromFile(String filename)
    {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}
    
    ////////////////////////////////////////////////////////////////////////
    
    public int length() {
    	return program.size();
    }
    public Command getCommand(int cmdIndex)
    {
    	if(cmdIndex >= program.size())
    		return null;
    	
    	return program.get(cmdIndex);
    }
}
