/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pic.simulator;

import java.io.IOException;

import pic.simulator.parser.*;

public class Processor
{

	private int 			progCounter;
	private Program 		picProgram;
	private Memorycontrol 	memControl;
	
	private boolean 		isInterrupted;

	public Processor(String programFileName) throws IOException
	{
		picProgram = new Program(programFileName);
		memControl = new Memorycontrol(0xFF, (short)2);
	}
	
	
	public void executeProgram() {
		while (progCounter < picProgram.length())
		{
			if (isInterrupted)
			{
				// TODO
			}
			Command cmd = fetch(progCounter);
			execute(cmd);
			progCounter++;
			// TODO repaint
		}
	}

	private Command fetch(int cmdIndex) {
		return picProgram.getCommand(cmdIndex);
	}

	private void execute(Command cmd) {
	}
}
