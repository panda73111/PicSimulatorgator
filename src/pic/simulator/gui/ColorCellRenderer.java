package pic.simulator.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class ColorCellRenderer implements TableCellRenderer {

	public static int colorIndex = 0;
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		JLabel label = new JLabel((String) value);
		label.setOpaque(true);
		
		if(row==colorIndex)
			label.setBackground(Color.red);
			
		return label;
	}
}