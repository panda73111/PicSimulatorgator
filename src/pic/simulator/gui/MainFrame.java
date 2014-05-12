package pic.simulator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import pic.simulator.PicMemorycontrol;
import pic.simulator.Processor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Program;
import pic.simulator.specialfunctionregisters.Pcl;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PicGUI
{
	private static final int gpTableColCount = 8;

	Thread processorThread;
	private Processor myProcessor;
	private HashMap<Short, Boolean> breakPointMap;
	
	private JPanel mainPanel;
	private JTable gpTable;
	private JTable sfrTable;

	private JButton btnReset;
	private JButton btnOpenProgram;
	private JPanel buttonPanel;

	private JPanel programmPanel;
	private JPanel debugButtonPanel;
	private JTable programmTable;
	private JScrollPane scrollPane;

	private JButton btnStop;
	private JButton btnStep;
	private JButton btnStart;

	private JPanel contentPanel;

	private JLabel stackLabel;
	private JPanel stackPanel;
	private JTable stackTable;

	private IOPanel ioPanel;

	public MainFrame(Processor proc)
	{
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

		btnReset = new JButton("Reset");
		btnOpenProgram = new JButton("Öffnen");

		buttonPanel.add(btnOpenProgram);
		buttonPanel.add(btnReset);

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		initButtonEvents();
		initGPTable();
		initSFRTable();
		initProgram();
		initStack();
		
		breakPointMap = new HashMap<Short, Boolean>();

		if (proc != null) // This is done to make the GUI designer work
			proc.getGuiHandler().registerGUIElement(this);

		btnReset.getActionListeners()[0].actionPerformed(null);
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

		stackPanel = new JPanel();
		stackLabel = new JLabel("Stack");
		stackTable = new JTable(new DefaultTableModel(PicMemorycontrol.maxStackSize, 1));
		stackPanel.add(stackLabel);
		stackPanel.add(stackTable);
		stackPanel.setPreferredSize(new Dimension(100, 200));
		stackPanel.setMaximumSize(new Dimension(100, 200));
		upperPanel.add(stackPanel);

		contentPanel.add(upperPanel, BorderLayout.CENTER);

		programmPanel = new JPanel();
		programmPanel.setLayout(new BorderLayout());

		debugButtonPanel = new JPanel();
		debugButtonPanel.setLayout(new GridLayout(3, 1));

		btnStart = new JButton("Start");
		btnStop = new JButton("Stop");
		btnStep = new JButton("Schritt");

		debugButtonPanel.add(btnStart);
		debugButtonPanel.add(btnStop);
		debugButtonPanel.add(btnStep);

		programmTable = new JTable(new DefaultTableModel(10, 2));
		scrollPane = new JScrollPane(programmTable);
		programmPanel.setPreferredSize(new Dimension(contentPanel.getWidth(), 200));
		programmPanel.setMinimumSize(new Dimension(contentPanel.getWidth(), 200));

		programmPanel.add(debugButtonPanel, BorderLayout.EAST);
		programmPanel.add(scrollPane, BorderLayout.CENTER);
		contentPanel.add(programmPanel, BorderLayout.SOUTH);
	}

	private void initButtonEvents()
	{
		btnReset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				myProcessor.stopProgramExecution();
				processorThread = null;
				myProcessor.Reset(Processor.POWER_ON);
				repaintGUI();
			}
		});
		btnStop.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				myProcessor.stopProgramExecution();
				processorThread = null;
			}
		});
		btnStart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				processorThread = new Thread(myProcessor);
				processorThread.start();
			}
		});
		btnStep.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				myProcessor.executeNextCommand();
			}
		});
		final MainFrame frame = this;
		btnOpenProgram.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				FileDialog filePicker = new FileDialog(frame);
				filePicker.setFile("*.lst");
				filePicker.setVisible(true);

				String filename = filePicker.getDirectory() + filePicker.getFile();

				if (filePicker.getFile() != null)
				{
					try
					{
						myProcessor.loadProgram(filename);
						initProgram();
					} catch (IOException e)
					{
						JOptionPane.showMessageDialog(null, "Fehler beim einlesen der Datei.", "Dateifehler", JOptionPane.OK_OPTION);
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void repaintGUI()
	{
		repaintGpTable();
		repaintSFRTable();
		repaintProgram();
		repaintIO();
		repaintStack();
		handleBreakPoint();
		super.repaint();
	}

	private void initGPTable()
	{
		gpTable.setEnabled(false);
	}

	private void initSFRTable()
	{
		DefaultTableModel model = (DefaultTableModel) (sfrTable.getModel());
		HashSet<SpecialFunctionRegister> sfr = ((PicMemorycontrol) myProcessor.getMemoryControl()).getSFRSet();

		model.setRowCount(sfr.size() + 1);
		sfrTable.setEnabled(false);
	}

	private void initProgram()
	{
		DefaultTableModel model = (DefaultTableModel) (programmTable.getModel());
		model.setColumnCount(2);
		model.setColumnIdentifiers(new String[] { "Linenumber", "Command" });
		programmTable.getColumnModel().getColumn(0).setWidth(50);

		programmTable.setEnabled(false);

		Program prog = myProcessor.getProgram();
		if (prog == null)
			return;

		model.setRowCount(prog.length());

		for (int i = 0; i < prog.length(); i++)
		{
			programmTable.setValueAt(new Integer(i).toString(), i, 0);
			programmTable.setValueAt(prog.getCommand(i).toString(), i, 1);
		}

		programmTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					JTable target = (JTable) e.getSource();	
					int row = target.rowAtPoint(e.getPoint());

					String oldVal = (String) target.getValueAt(row, 0);
					
					if(oldVal.endsWith("B"))
					{
						target.setValueAt(oldVal.substring(0, oldVal.length()-1), row, 0);
						breakPointMap.put((short) row, false);
					}
					else
					{
						target.setValueAt(oldVal + "B", row, 0);
						breakPointMap.put((short) row, true);
					}
				}
			}
		});

	}

	private void initStack()
	{
		stackTable.setDefaultRenderer(Object.class, new ColorCellRenderer());

		for (int i = 0; i < stackTable.getRowCount(); i++)
		{
			stackTable.setValueAt("", i, 0);
		}
	}

	private void repaintGpTable()
	{
		for (int i = 0; i < PicMemorycontrol.gpLength; i++)
		{
			byte byteValue = myProcessor.getMemoryControl().getAt(PicMemorycontrol.gpBegin + i);
			gpTable.setValueAt(byteToHex(byteValue), i / gpTableColCount, i % gpTableColCount);
		}
	}

	private void repaintSFRTable()
	{

		HashSet<SpecialFunctionRegister> sfr = ((PicMemorycontrol) myProcessor.getMemoryControl()).getSFRSet();

		SpecialFunctionRegister sfrArray[] = new SpecialFunctionRegister[sfr.size()];
		sfr.toArray(sfrArray);
		Arrays.sort(sfrArray, new Comparator<SpecialFunctionRegister>()
		{
			@Override
			public int compare(SpecialFunctionRegister arg0, SpecialFunctionRegister arg1)
			{
				return arg0.toString().compareTo(arg1.toString());
			}
		});

		for (int i = 0; i < sfrArray.length; i++)
		{
			int index = i + 1;
			SpecialFunctionRegister entry = sfrArray[i];

			sfrTable.setValueAt(entry.getName(), index, 0);
			sfrTable.setValueAt(byteToHex(entry.getValue()), index, 1);
			sfrTable.setValueAt(byteToBinary(entry.getValue()), index, 2);
		}

		sfrTable.setValueAt("work", 0, 0);
		sfrTable.setValueAt(byteToHex(myProcessor.workRegister), 0, 1);
		sfrTable.setValueAt(byteToBinary(myProcessor.workRegister), 0, 2);
	}

	private void repaintProgram()
	{
		int pcl = myProcessor.getMemoryControl().getAt(SpecialFunctionRegister.PCL);
		programmTable.clearSelection();
		programmTable.addRowSelectionInterval(pcl, pcl);

		Rectangle rect = programmTable.getCellRect(pcl, 0, true);
		programmTable.scrollRectToVisible(rect);
	}

	private void repaintIO()
	{
		ioPanel.repaint(myProcessor);
	}

	private void repaintStack()
	{
		Stack<Integer> s = ((PicMemorycontrol) myProcessor.getMemoryControl()).getStack();

		ColorCellRenderer.colorIndex = s.size();

		for (int i = 0; i < s.size() && i < PicMemorycontrol.maxStackSize; i++)
		{
			stackTable.setValueAt(new Integer(s.get(i)).toString(), i, 0);
		}

	}

	private String byteToHex(byte byteValue)
	{
		String hexString = Integer.toHexString(byteValue & 0xFF) + "H";
		while (hexString.length() < 3)
		{
			hexString = "0" + hexString;
		}
		return hexString;
	}

	private String byteToBinary(byte byteValue)
	{
		String byteString = Integer.toBinaryString(byteValue & 0xFF) + "b";
		while (byteString.length() < 9)
		{
			byteString = "0" + byteString;
		}
		return byteString;
	}
	
	private void handleBreakPoint()
	{
		PicMemorycontrol picMemCtrl = (PicMemorycontrol) myProcessor.getMemoryControl();
        Pcl pcl = (Pcl) picMemCtrl.getSFR(SpecialFunctionRegister.PCL);
        
        short row = pcl.get13BitValue();
        
        Boolean bool = breakPointMap.get(row);
        if(bool != null && breakPointMap.get(row))
        	myProcessor.stopProgramExecution();
	}

}
