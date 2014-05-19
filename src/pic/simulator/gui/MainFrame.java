package pic.simulator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import pic.simulator.PicMemorycontrol;
import pic.simulator.PicProcessor;
import pic.simulator.SpecialFunctionRegister;
import pic.simulator.parser.Program;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PicGUI
{
    private static final int    gpTableColCount = 8;
    private static final String SLEEP_ON = "PIC schläft...";
    private static final String SLEEP_OFF = "PIC schläft nicht.";
    
    
    Thread                      processorThread;
    private PicProcessor           myProcessor;

    private JPanel              mainPanel;
    private JTable              gpTable;
    private JTable              sfrTable;

    private JButton             btnReset;
    private JButton             btnOpenProgram;
    private JPanel              buttonPanel;

    private JPanel              programmPanel;
    private JPanel              debugButtonPanel;
    private JTable              programmTable;
    private JScrollPane         scrollPane;

    private JButton             btnStop;
    private JButton             btnStep;
    private JButton             btnStart;
    private JButton             btnHelp;

    private JPanel              contentPanel;

    private JLabel              stackLabel;
    private JPanel              stackPanel;
    private JTable              stackTable;

    private JPanel              runtimePanel;
    private JLabel              runtimeLabel;
    private JLabel              cycleLabel;
    private JFormattedTextField quarzTextField;
    private JButton             btnApply;

    private JPanel				watchdogPanel;
    private JLabel				isSleepingText;
    private JLabel				watchdogText;
    private JCheckBox			cbEnableWatchdog;
    private JCheckBox			cbStopOnWatchdog;
    
    private JPanel				rightPanel;
    
    private IOPanel             ioPanel;

    public MainFrame(PicProcessor proc)
    {
        myProcessor = proc;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 600);

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
        btnHelp = new JButton("Hilfe");
        btnOpenProgram = new JButton("Öffnen");

        buttonPanel.add(btnOpenProgram);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnHelp);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        initButtonEvents();
        initGPTable();
        initSFRTable();
        initProgram();
        initStack();
        initRuntimeCounter();

        if (proc != null) // This is done to make the GUI designer work
            proc.getGuiHandler().registerGUIElement(this);

        resetProcessor();
    }

    private void configureContentPanel()
    {
        contentPanel.setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();


        ioPanel = new IOPanel(myProcessor.getPinHandler());
        ioPanel.setPreferredSize(new Dimension(150, 175));
        ioPanel.setMaximumSize(new Dimension(150, 175));
        upperPanel.add(ioPanel);
        
        JPanel gpPanel = new JPanel();
        gpPanel.add(new JLabel("General purpose registers"));

        gpTable = new JTable(new DefaultTableModel(PicMemorycontrol.gpLength / gpTableColCount + 1, gpTableColCount));
        gpPanel.add(gpTable);
        gpTable.setPreferredSize(new Dimension(200, 200));
        gpTable.setMaximumSize(new Dimension(200, 200));
        gpPanel.setPreferredSize(new Dimension(200, 200));
        gpPanel.setMaximumSize(new Dimension(200, 200));
        upperPanel.add(gpPanel);


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


        rightPanel= new JPanel(new GridLayout(2, 1, 0,5));
        
        runtimePanel = new JPanel();
        runtimePanel.setLayout(new GridLayout(2, 3, 5, 5));
        runtimePanel.setBorder(BorderFactory.createLineBorder(Color.black));

        quarzTextField = new JFormattedTextField(new DecimalFormat("####.########"));
        quarzTextField.setHorizontalAlignment(JTextField.RIGHT);
        btnApply = new JButton("Übernehmen");

        runtimeLabel = new JLabel();
        cycleLabel = new JLabel();

        runtimePanel.add(runtimeLabel);
        runtimePanel.add(quarzTextField);
        runtimePanel.add(new JLabel("MHz"));
        runtimePanel.add(cycleLabel);
        runtimePanel.add(btnApply);
        rightPanel.add(runtimePanel);


        watchdogPanel = new JPanel(new GridLayout(4, 1));
        isSleepingText = new JLabel(SLEEP_OFF);
        watchdogText = new JLabel("Zeit bis Watchdog-Reset: 0ms");
        
        cbEnableWatchdog = new JCheckBox("Watchdog aktivieren");
        cbEnableWatchdog.setSelected(true);
        cbEnableWatchdog.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				cbStopOnWatchdog.setEnabled(cbEnableWatchdog.isSelected());
				watchdogText.setEnabled(cbEnableWatchdog.isSelected());
				myProcessor.setWdtState(cbEnableWatchdog.isSelected());
			}
		});
        cbStopOnWatchdog = new JCheckBox("Stop on Watchdog");
        
        cbStopOnWatchdog.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				myProcessor.getWatchdog().enableStopOnWatchdog(cbStopOnWatchdog.isSelected());
			}
		});
        watchdogPanel.add(isSleepingText);
        watchdogPanel.add(watchdogText);
        watchdogPanel.add(cbEnableWatchdog);
        watchdogPanel.add(cbStopOnWatchdog);
        rightPanel.add(watchdogPanel);
        
        
        upperPanel.add(rightPanel);
        
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

    private void resetProcessor()
    {
        myProcessor.stopProgramExecution();
        processorThread = null;
        myProcessor.reset(PicProcessor.POWER_ON);
        repaintGUI();
    }

    private void initButtonEvents()
    {
        btnReset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                resetProcessor();
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
                        resetProcessor();
                    }
                    catch (IOException e)
                    {
                        JOptionPane.showMessageDialog(null, "Fehler beim einlesen der Datei.", "Dateifehler",
                                JOptionPane.OK_OPTION);
                        e.printStackTrace();
                    }
                }
            }
        });
        btnApply.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                repaintRuntimeCounter();
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
        repaintRuntimeCounter();
        repaintWDPanel();
        super.repaint();
    }

    private void initRuntimeCounter()
    {
        quarzTextField.setValue(4);
        repaintRuntimeCounter();
    }

    private void repaintRuntimeCounter()
    {
        double freq = myProcessor.getFrequency();
        try
        {
            freq = new Double(quarzTextField.getText());
        }
        catch (Exception e)
        {
        }
        cycleLabel.setText(" " + myProcessor.getCycleCount() + " Zyklen");
        runtimeLabel.setText(" " + (freq / 4.0d) * myProcessor.getCycleCount() + " \u00B5s");

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

                    if (oldVal.endsWith("B"))
                    {
                        target.setValueAt(oldVal.substring(0, oldVal.length() - 1), row, 0);
                        myProcessor.removeBreakPoint(row);
                    }
                    else
                    {
                        target.setValueAt(oldVal + "B", row, 0);
                        myProcessor.addBreakPoint(row);
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
    private void repaintWDPanel()
    {
    	if(myProcessor.isSleeping())
    	{
    		isSleepingText.setText(SLEEP_ON);
    	}
    	else
    	{
    		isSleepingText.setText(SLEEP_OFF);
    	}
    	if(myProcessor.isWdtEnabled())
    	{
    		watchdogText.setText("Zeit bis Watchdog-Reset: " + myProcessor.getWatchdog().getMillisLeft() + "ms");
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

}
