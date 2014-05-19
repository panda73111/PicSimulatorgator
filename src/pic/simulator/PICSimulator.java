package pic.simulator;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pic.simulator.gui.MainFrame;


/**
 *
 * @author Herr Doktor Strunz
 */
public class PICSimulator 
{
	/*
	 * Argument is filename
	 */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException 
    {
    	if(args.length > 1)
    	{
    		System.err.println("---Wrong number of arguments---");
    		System.exit(1);
    	}
    	
    	UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	
    	final PicProcessor processor;
    	if(args.length==1)
    		processor = new PicProcessor(args[0]);
    	else
    		processor = new PicProcessor();
    		
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try
				{
					MainFrame frame = new MainFrame(processor);
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
    	
    	//processor.executeProgram();
    	//System.out.println("---Execution terminated---");
    }
    
}
