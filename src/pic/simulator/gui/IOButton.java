package pic.simulator.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class IOButton extends JButton
{
	int ID;
	final Icon checkedImage;
	final Icon uncheckedImage;
	boolean isChecked;
	IOPanel parent;
	
	
	public IOButton(Icon uncheckedImage, Icon checkedImage, int ID, IOPanel panel)
	{
		super(uncheckedImage);
		this.checkedImage	= checkedImage;
		this.uncheckedImage	= uncheckedImage;
		this.ID				= ID;
		this.parent			= panel;
		this.isChecked		= false;
		
		setMargin(new Insets(0,0,0,0));

		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		
		addActionListener(new ClickListener());
	}
	
	class ClickListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			if(isChecked)
				setIcon(uncheckedImage);
			else
				setIcon(checkedImage);
			
			isChecked= !isChecked;
			parent.announceClick((IOButton)arg0.getSource());
		}
	}
	
	public boolean isChecked()
	{
		return isChecked;
	}

}
