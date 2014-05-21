package pic.simulator.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import pic.simulator.Processor;
import pic.simulator.pins.Pin;

@SuppressWarnings("serial")
public class IOButton extends JButton
{
    int               id;
    private Icon      checkedImage;
    private Icon      uncheckedImage;
    private Processor proc;

    public IOButton(String imageName, int id, Processor processor)
    {
        super();
        this.id = id;
        this.proc = processor;

        loadImages(imageName);

        setMargin(new Insets(0, 0, 0, 0));

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        addActionListener(new ClickListener());
        update();
    }

    private void loadImages(String imageName)
    {
        uncheckedImage = new ImageIcon(IOPanel.imagePath + IOPanel.lowPrefix + imageName + IOPanel.imageExtension);
        checkedImage = new ImageIcon(IOPanel.imagePath + IOPanel.highPrefix + imageName + IOPanel.imageExtension);
    }

    class ClickListener implements ActionListener
    {
        public void actionPerformed(ActionEvent arg0)
        {
            if (isChecked())
            {
                proc.getPinHandler().getPin(id).clearExternally();
                update(true);
            }
            else
            {
                proc.getPinHandler().getPin(id).setExternally();
                update(false);
            }
        }
    }

    public void update()
    {
        update(isChecked());
    }

    private void update(boolean isChecked)
    {
        if (isChecked())
            setIcon(checkedImage);
        else
            setIcon(uncheckedImage);
    }

    private boolean isChecked()
    {
        return proc.getPinHandler().getExternalPinState(id) != Pin.LOW;
    }
}
