package pic.simulator.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class IOButton extends JButton
{
	int ID;
	private Icon checkedImage;
	private Icon uncheckedImage;
	boolean isChecked;
	private IOPanel parent;
	
	
	public IOButton(String imageName, int ID, IOPanel panel)
	{
		super();
		this.ID				= ID;
		this.parent			= panel;
		this.isChecked		= false;
		
		loadImages(imageName);
		
		setMargin(new Insets(0,0,0,0));

		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		
		addActionListener(new ClickListener());
		update();
	}
	private void loadImages(String imageName)
	{
		uncheckedImage 	= new ImageIcon(IOPanel.imagePath + IOPanel.lowPrefix  + imageName + IOPanel.imageExtension);
		checkedImage 	= new ImageIcon(IOPanel.imagePath + IOPanel.highPrefix + imageName + IOPanel.imageExtension);
	}
	
	
	class ClickListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) {
			isChecked= !isChecked;
			update();
			parent.announceClick((IOButton)arg0.getSource());
		}
	}
	
	public void update()
	{
		if(isChecked)
			setIcon(checkedImage);
		else
			setIcon(uncheckedImage);
	}

}
