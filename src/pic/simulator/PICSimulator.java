/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pic.simulator;

import java.awt.EventQueue;
import java.io.IOException;

import pic.simulator.gui.MainFrame;


/**
 *
 * @author hudini
 */
public class PICSimulator 
{
	/*
	 * Argument is filename
	 */
    public static void main(String[] args) throws IOException 
    {
    	if(args.length != 1)
    	{
    		System.out.println("---Wrong number of arguments---");
    		System.exit(1);
    	}
    	
    	/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try
				{
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});*/
    	
    	Processor processor = new Processor(args[0]);
    	processor.executeProgram();
    	System.out.println("---Execution terminated---");
    }
    
}
