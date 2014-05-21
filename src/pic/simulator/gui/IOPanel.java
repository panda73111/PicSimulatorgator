package pic.simulator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import pic.simulator.PicProcessor;
import pic.simulator.PinHandler;
import pic.simulator.Processor;
import pic.simulator.pins.Pin;

@SuppressWarnings("serial")
public class IOPanel extends JPanel
{
    private Image       bgImage;
    private JPanel      buttonPanel;
    private IOButton[]  ioButtons      = new IOButton[16];
    private Processor   proc;

    static final String imagePath      = "PinImages\\";
    static final String imageExtension = ".png";

    static final String lowPrefix      = "L";
    static final String highPrefix     = "H";

    static final String bgImageName    = "bg";
    static final int    btCount        = 16;
    static final int    rlPinCount     = 18;

    public IOPanel(Processor processor)
    {
        this.proc = processor;

        bgImage = Toolkit.getDefaultToolkit().createImage(imagePath + bgImageName + imageExtension);

        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        add(Box.createRigidArea(new Dimension(getWidth(), 10)));

        buttonPanel = new JPanel(new GridLayout(8, 2, -4, -5));
        buttonPanel.setOpaque(false);

        initButtons();
        for (IOButton i : ioButtons)
            buttonPanel.add(i);

        add(buttonPanel);
    }

    private void initButtons()
    {
        ioButtons[1] = createButton("RA1", Pin.RA1);
        ioButtons[3] = createButton("RA0", Pin.RA0);
        ioButtons[5] = createButton("CLKIN", Pin.CLKIN);
        ioButtons[7] = createButton("CLKOUT", Pin.CLKOUT);
        ioButtons[9] = createButton("RB7", Pin.RB7);
        ioButtons[11] = createButton("RB6", Pin.RB6);
        ioButtons[13] = createButton("RB5", Pin.RB5);
        ioButtons[15] = createButton("RB4", Pin.RB4);

        ioButtons[0] = createButton("RA2", Pin.RA2);
        ioButtons[2] = createButton("RA3", Pin.RA3);
        ioButtons[4] = createButton("RA4", Pin.RA4);
        ioButtons[6] = createButton("MCLR", Pin.MCLR);
        ioButtons[8] = createButton("RB0", Pin.RB0);
        ioButtons[10] = createButton("RB1", Pin.RB1);
        ioButtons[12] = createButton("RB2", Pin.RB2);
        ioButtons[14] = createButton("RB3", Pin.RB3);
    }

    private IOButton createButton(String fileName, int id)
    {
        IOButton b = new IOButton(fileName, id, proc);

        if (id > rlPinCount / 2)
            b.setHorizontalAlignment(JButton.LEFT);
        else
            b.setHorizontalAlignment(JButton.RIGHT);
        return b;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(bgImage, (getWidth() - bgImage.getWidth(null)) / 2, (getHeight() - bgImage.getHeight(null)) / 2,
                null);
    }

    public Image getImage()
    {
        return bgImage;
    }

    public void repaint(PicProcessor proc)
    {
        PinHandler p = proc.getPinHandler();
        for (int i = 0; i < p.getPinCount(); i++)
        {
            ioButtons[i].update();
        }
    }
}
