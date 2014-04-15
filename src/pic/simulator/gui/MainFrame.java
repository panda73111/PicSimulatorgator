package pic.simulator.gui;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import pic.simulator.Memorycontrol;
import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;

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
		setBounds(100, 100, 450, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 2, 0, 0));

		DefaultTableModel tableModel = new DefaultTableModel(Memorycontrol.gpLength/gpTableColCount+1, gpTableColCount);
		gpTable = new JTable(tableModel);

		tableModel = new DefaultTableModel(0, 3);
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
		HashSet<SpecialFunctionRegister> sfr = myProcessor.getMemoryControl().getSFRSet();
		Iterator<SpecialFunctionRegister> i = sfr.iterator();
		int index = 0;
		
		while(i.hasNext())
		{
			SpecialFunctionRegister entry = i.next();

			sfrTable.setValueAt(entry.getName(), index, 0);
			index++;
		}
	}
	private void repaintGpTable()
	{
		for(int i=0; i<Memorycontrol.gpLength; i++)
		{
			byte byteValue 		= myProcessor.getAtAddress(Memorycontrol.sfrLength+i);
			gpTable.setValueAt(byteToHex(byteValue), i/gpTableColCount, i%gpTableColCount);
		}
	}
	private void repaintSFRTable()
	{
		
		HashSet<SpecialFunctionRegister> sfr = myProcessor.getMemoryControl().getSFRSet();
		

		Iterator<SpecialFunctionRegister> i = sfr.iterator();
		int index = 0;
		
		while(i.hasNext())
		{
			SpecialFunctionRegister entry = i.next();

			sfrTable.setValueAt(byteToHex	(entry.getValue()), index, 1);
			sfrTable.setValueAt(byteToBinary(entry.getValue()), index, 2);
			index++;
		}
	}

	private String byteToHex(byte byteValue)
	{
		return Integer.toHexString(byteValue & 0xFF) + "H ";
	}
	private String byteToBinary(byte byteValue)
	{
		return Integer.toBinaryString(byteValue & 0xFF) + "b";
	}
	
	

}
