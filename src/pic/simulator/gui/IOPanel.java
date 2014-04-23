package pic.simulator.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import pic.simulator.PinHandler;

@SuppressWarnings("serial")
public class IOPanel extends JPanel
{
	Image bgImage;
	JPanel buttonPanel;
	IOButton[] ioButtons = new IOButton[16];
	PinHandler ioHandler;
	
	static final String bgImagePath = "PinImages\\bg.png";
	static final String[] uncheckedButtonIconPaths = new String[]{
		"PinImages\\L11.png", "PinImages\\L12.png", 
		"PinImages\\L21.png", "PinImages\\L22.png",
		"PinImages\\L31.png", "PinImages\\L32.png",
		"PinImages\\L41.png", "PinImages\\L42.png",
		"PinImages\\L51.png", "PinImages\\L52.png",
		"PinImages\\L61.png", "PinImages\\L62.png",
		"PinImages\\L71.png", "PinImages\\L72.png",
		"PinImages\\L81.png", "PinImages\\L82.png"};
	static final String[] checkedButtonIconPaths = new String[]{
		"PinImages\\S11.png", "PinImages\\S12.png", 
		"PinImages\\S21.png", "PinImages\\S22.png",
		"PinImages\\S31.png", "PinImages\\S32.png",
		"PinImages\\S41.png", "PinImages\\S42.png",
		"PinImages\\S51.png", "PinImages\\S52.png",
		"PinImages\\S61.png", "PinImages\\S62.png",
		"PinImages\\S71.png", "PinImages\\S72.png",
		"PinImages\\S81.png", "PinImages\\S82.png"};
	
	static ImageIcon[] uncheckedButtonIcons = new ImageIcon[uncheckedButtonIconPaths.length];
	static ImageIcon[] checkedButtonIcons = new ImageIcon[checkedButtonIconPaths.length];
	
	public IOPanel(PinHandler ioHandler)
	{
		this.ioHandler = ioHandler;
		
		loadImages();
		
		setBackground(Color.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(Box.createRigidArea(new Dimension(getWidth(), 10)));
		
		buttonPanel = new JPanel(new GridLayout(8, 2, -4, 0));
		buttonPanel.setOpaque(false);
		
		for(int i = 0; i <ioButtons.length; i++)
		{
			ioButtons[i] = new IOButton(uncheckedButtonIcons[i], checkedButtonIcons[i], i, this);
			
			buttonPanel.add(ioButtons[i]);
			
			if(i%2==0)
				ioButtons[i].setHorizontalAlignment(JButton.RIGHT);
			else
				ioButtons[i].setHorizontalAlignment(JButton.LEFT);
		}		
		add(buttonPanel);
	}

    private void loadImages() {
		bgImage = Toolkit.getDefaultToolkit().createImage(bgImagePath);
		
		for(int i=0; i < uncheckedButtonIconPaths.length; i++)
		{
			uncheckedButtonIcons[i] = new ImageIcon(uncheckedButtonIconPaths[i]);
			checkedButtonIcons[i] = new ImageIcon(checkedButtonIconPaths[i]);
		}
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, (getWidth()-bgImage.getWidth(null))/2, (getHeight()-bgImage.getHeight(null))/2, null);
	}
    
    public Image getImage()
    {
    	return bgImage;
    }
    public void announceClick(IOButton ioButton)
    {
    	if(ioButton.isChecked)
    		ioHandler.setPinExternally(ioButton.ID);
    	else
    		ioHandler.clearPinExternally(ioButton.ID);
    }
}
