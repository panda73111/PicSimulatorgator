package pic.simulator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import pic.simulator.PicMemorycontrol;
import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Program;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PicGUI {
	private static final int gpTableColCount = 8;

	private JPanel mainPanel;
	private JTable gpTable;
	private JTable sfrTable;
	private Processor myProcessor;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnReset;
	private JPanel buttonPanel;
	private JTable programmTable;
	private JPanel contentPanel;
	private IOPanel ioPanel;
	
	private JScrollPane scrollPane;

	public MainFrame(Processor proc) {
		myProcessor = proc;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		
		
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0, 0));
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        contentPanel = new JPanel();
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		configureContentPanel();		
		
		buttonPanel = new JPanel();
				buttonPanel.setLayout(new GridLayout(1, 3, 0, 10));

				btnStart = new JButton("Start");
				btnStop = new JButton("Stop");
				btnReset = new JButton("Reset");

				buttonPanel.add(btnStart);
				buttonPanel.add(btnStop);
				buttonPanel.add(btnReset);
		
				mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		
		initButtonEvents();
		initGPTable();
		initSFRTable();
		initProgram();

		if (proc != null) // This is done to make the GUI designer work
			proc.getGuiHandler().registerGUIElement(this);
	}

	private void configureContentPanel()
	{
		contentPanel.setLayout(new BorderLayout());
		
		JPanel upperPanel = new JPanel();
		
		JPanel gpPanel = new JPanel();
		gpPanel.add(new JLabel("General purpose registers"));
		
		gpTable = new JTable(new DefaultTableModel(PicMemorycontrol.gpLength / gpTableColCount + 1, gpTableColCount));
		gpPanel.add(gpTable);
		gpTable.setPreferredSize(new Dimension(200, 200));
		gpTable.setMaximumSize(new Dimension(200, 200));
		gpPanel.setPreferredSize(new Dimension(200, 200));
		gpPanel.setMaximumSize(new Dimension(200, 200));
		upperPanel.add(gpPanel);
		
		ioPanel = new IOPanel(myProcessor.getPinHandler());
		ioPanel.setPreferredSize(new Dimension(150, 175));
		ioPanel.setMaximumSize(new Dimension(150, 175));
		upperPanel.add(ioPanel);
		
		sfrTable = new JTable(new DefaultTableModel(8, 3));
		upperPanel.add(sfrTable);
		
		contentPanel.add(upperPanel, BorderLayout.CENTER);
		
		programmTable = new JTable(new DefaultTableModel(10, 2));
		scrollPane = new JScrollPane(programmTable);
		scrollPane.setPreferredSize(new Dimension(contentPanel.getWidth(), 200));
		scrollPane.setMaximumSize(new Dimension(contentPanel.getWidth(), 200));
		
		contentPanel.add(scrollPane, BorderLayout.SOUTH);
	}
	
	
	private void initButtonEvents() {
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myProcessor.stopProgramExecution();
				myProcessor.Reset(Processor.POWER_ON);
				repaintGUI();
			}
		});
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myProcessor.stopProgramExecution();
			}
		});
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() {
					public void run() {
						myProcessor.executeProgram();
					}
				}.start();
			}
		});
	}

	public void repaintGUI() {
		repaintGpTable();
		repaintSFRTable();
		repaintProgram();
		repaintIO();
		super.repaint();
	}

	
	private void initGPTable()
	{
		gpTable.setEnabled(false);
	}
	
	private void initSFRTable() {
		DefaultTableModel model = (DefaultTableModel) (sfrTable.getModel());
		HashSet<SpecialFunctionRegister> sfr = ((PicMemorycontrol) myProcessor
				.getMemoryControl()).getSFRSet();

		model.setRowCount(sfr.size() + 1);
		sfrTable.setEnabled(false);
	}

	private void initProgram() {
		Program prog = myProcessor.getProgram();

		DefaultTableModel model = (DefaultTableModel) (programmTable.getModel());
		model.setColumnCount(2);
		model.setRowCount(prog.length());
		model.setColumnIdentifiers(new String[]{"Linenumber", "Command"});

		for (int i = 0; i < prog.length(); i++) {
			programmTable.setValueAt(i, i, 0);
			programmTable.setValueAt(prog.getCommand(i).toString(), i, 1);
		}

		programmTable.getColumnModel().getColumn(0).setPreferredWidth(2);
		programmTable.setEnabled(false);


	}

	private void repaintGpTable() {
		for (int i = 0; i < PicMemorycontrol.gpLength; i++) {
			byte byteValue = myProcessor.getMemoryControl().getAt(PicMemorycontrol.gpBegin + i);
			gpTable.setValueAt(byteToHex(byteValue), i / gpTableColCount, i
					% gpTableColCount);
		}
	}

	private void repaintSFRTable() {

		HashSet<SpecialFunctionRegister> sfr = ((PicMemorycontrol) myProcessor
				.getMemoryControl()).getSFRSet();

		Iterator<SpecialFunctionRegister> i = sfr.iterator();
		int index = 1;

		while (i.hasNext()) {
			SpecialFunctionRegister entry = i.next();

			sfrTable.setValueAt(entry.getName(), index, 0);
			sfrTable.setValueAt(byteToHex(entry.getValue()), index, 1);
			sfrTable.setValueAt(byteToBinary(entry.getValue()), index, 2);
			index++;
		}

		sfrTable.setValueAt("work", 0, 1);
		sfrTable.setValueAt(byteToHex(myProcessor.workRegister), 0, 1);
		sfrTable.setValueAt(byteToBinary(myProcessor.workRegister), 0, 2);
	}

	private void repaintProgram() 
	{
		int pcl = myProcessor.getMemoryControl().getAt(SpecialFunctionRegister.PCL);
		programmTable.clearSelection();
		programmTable.addRowSelectionInterval(pcl, pcl);
	}
	private void repaintIO()
	{
		ioPanel.repaint(myProcessor);
	}
	
	
	private String byteToHex(byte byteValue) {
		String hexString = Integer.toHexString(byteValue & 0xFF) + "H";
		while (hexString.length() < 3) {
			hexString = "0" + hexString;
		}
		return hexString;
	}

	private String byteToBinary(byte byteValue) {
		String byteString = Integer.toBinaryString(byteValue & 0xFF) + "b";
		while (byteString.length() < 9) {
			byteString = "0" + byteString;
		}
		return byteString;
	}

}
