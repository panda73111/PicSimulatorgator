package pic.simulator.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel{

    private BufferedImage image;

    
    public ImagePanel(String imagePath) {
       try {                
          image = ImageIO.read(new File(imagePath));
       } catch (IOException ex) {
    	   System.err.println("Fehler beim laden des Bildes");
    	   ex.printStackTrace();
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double scaleFactorX = getWidth()/(double)image.getWidth();
        double scaleFactorY = getHeight()/(double)image.getHeight();
        
        if(image!=null)
        {
        	if(scaleFactorX < scaleFactorY) 
        		g.drawImage(image, 0, 0, getWidth(), (int) (scaleFactorX*image.getHeight()), Color.white, null);
        	else 
        		g.drawImage(image, 0, 0, (int)(scaleFactorY * image.getWidth()), getHeight(), Color.white, null);
        	
        }
    }

}