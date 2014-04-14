package pic.simulator.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame
{

	private JPanel contentPane;
	private JTable table;
	private JTextField txtFilename;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		table = new JTable();
		contentPane.add(table);
		
		JList list = new JList();
		contentPane.add(list);
		
		txtFilename = new JTextField();
		txtFilename.setText("Filename");
		contentPane.add(txtFilename);
		txtFilename.setColumns(10);
	}

}
