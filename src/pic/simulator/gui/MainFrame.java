package pic.simulator.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import pic.simulator.Memorycontrol;
import pic.simulator.Processor;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PicGUI
{
	private static final int gpTableColCount = 8;
	
	private JPanel contentPane;
	private JTable gpTable;
	private JTable sfrTable;
	private Processor myProcessor;
	

	public MainFrame(Processor proc) {
		myProcessor = proc;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 2, 0, 0));

		DefaultTableModel tableModel = new DefaultTableModel(Memorycontrol.gpLength/gpTableColCount+1, gpTableColCount);
		gpTable = new JTable(tableModel);

		tableModel = new DefaultTableModel(Memorycontrol.sfrLength, 3);
		sfrTable = new JTable(tableModel);

		contentPane.add(gpTable);
		contentPane.add(sfrTable);
		
		initSFRTable();
		
		if(proc!=null)			//This is done to make the GUI designer work
			proc.registerGUIElement(this);
	}
	public void repaintGUI()
	{
		repaintGpTable();
		repaintSFRTable();
		super.repaint();
	}
	
	private void initSFRTable()
	{
		
	}
	private void repaintGpTable()
	{
		for(int i=0; i<Memorycontrol.gpLength; i++)
		{
			byte byteValue 		= myProcessor.getAtAddress(Memorycontrol.sfrLength+i);
			String stringValue 	= Integer.toHexString(byteValue & 0xFF) + "H ";
			gpTable.setValueAt(stringValue, i/gpTableColCount, i%gpTableColCount);
		}
	}
	private void repaintSFRTable()
	{
	}
	
	

}
