package pic.simulator.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Program {

	private ArrayList<Command> program;
	
	public Program()
	{	
		program = new ArrayList<>();
	}
	public Program(String filename) throws IOException
	{
		this();
		loadProgramFromFile(filename);
	}
	
	////////////////////////////////////////////////////////////////////////
	
    public void loadProgramFromFile(String filename) throws IOException
    {
    	BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            String line = br.readLine();
            while(line!=null)
            {
            	Command cmd = Command.newInstance(line);
            	program.add(cmd);
            }
        } finally {
            br.close();
        }
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
