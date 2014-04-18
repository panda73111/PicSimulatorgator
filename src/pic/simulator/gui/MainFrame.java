package pic.simulator.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import pic.simulator.Memorycontrol;
import pic.simulator.PicMemorycontrol;
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
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnReset;
	private JPanel buttonPanel;
	private JTable programmTable;

	public MainFrame(Processor proc) {
		myProcessor = proc;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 10, 2, 10));

		
		
		DefaultTableModel tableModel = new DefaultTableModel(PicMemorycontrol.gpLength/gpTableColCount+1, gpTableColCount);
		gpTable = new JTable(tableModel);

		tableModel = new DefaultTableModel(0, 3);
		sfrTable = new JTable(tableModel);

		tableModel = new DefaultTableModel(5, 2);
		programmTable = new JTable(tableModel);
		
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 0, 0, 10));
		
		btnStart = new JButton("Start");		
		btnStop = new JButton("Stop");		
		btnReset = new JButton("Reset");
		
		initButtonEvents();

		buttonPanel.add(btnStart);
		buttonPanel.add(btnStop);
		buttonPanel.add(btnReset);

		contentPane.add(programmTable);	
		contentPane.add(sfrTable);
		contentPane.add(gpTable);	
		contentPane.add(buttonPanel);
		
		
		initSFRTable();
		
		if(proc!=null)			//This is done to make the GUI designer work
			proc.getGuiHandler().registerGUIElement(this);
	}
	
	private void initButtonEvents()
	{
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myProcessor.stopProgramExecution();
			}
		});
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myProcessor.stopProgramExecution();
			}
		});
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread()
				{
					public void run()
					{
						myProcessor.executeProgram();
					}
				}.start();
			}
		});
	}
	
	public void repaintGUI()
	{
		repaintGpTable();
		repaintSFRTable();
		super.repaint();
	}
	
	private void initSFRTable()
	{
		DefaultTableModel model = (DefaultTableModel)(sfrTable.getModel());
		HashSet<SpecialFunctionRegister> sfr = ((PicMemorycontrol)myProcessor.getMemoryControl()).getSFRSet();
		
		model.setRowCount(sfr.size()+1);
	}
	private void repaintGpTable()
	{
		for(int i=0; i<PicMemorycontrol.gpLength; i++)
		{
			byte byteValue 		= myProcessor.getMemoryControl().getAt(PicMemorycontrol.sfrLength+i);
			gpTable.setValueAt(byteToHex(byteValue), i/gpTableColCount, i%gpTableColCount);
		}
	}
	private void repaintSFRTable()
	{
		
		HashSet<SpecialFunctionRegister> sfr = ((PicMemorycontrol)myProcessor.getMemoryControl()).getSFRSet();
		

		Iterator<SpecialFunctionRegister> i = sfr.iterator();
		int index = 1;
		
		while(i.hasNext())
		{
			SpecialFunctionRegister entry = i.next();

			sfrTable.setValueAt(entry.getName(),  index, 0);
			sfrTable.setValueAt(byteToHex	(entry.getValue()), index, 1);
			sfrTable.setValueAt(byteToBinary(entry.getValue()), index, 2);
			index++;
		}

		sfrTable.setValueAt("work", 								0, 1);
		sfrTable.setValueAt(byteToHex	(myProcessor.workRegister), 0, 1);
		sfrTable.setValueAt(byteToBinary(myProcessor.workRegister), 0, 2);
	}

	private String byteToHex(byte byteValue)
	{
		String hexString = Integer.toHexString(byteValue & 0xFF) + "H";
		while(hexString.length()<3)
		{
			hexString = "0" + hexString;
		}
		return hexString;
	}
	private String byteToBinary(byte byteValue)
	{
		String byteString = Integer.toBinaryString(byteValue & 0xFF) + "b";
		while(byteString.length()<9)
		{
			byteString = "0" + byteString;
		}
		return byteString;
	}
	
	

}
