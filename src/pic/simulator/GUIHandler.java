package pic.simulator;

import java.util.ArrayList;

import pic.simulator.gui.PicGUI;

public class GUIHandler 
{
	ArrayList<PicGUI> guiObjects;
	
	public GUIHandler()
	{
		guiObjects = new ArrayList<>();
	}
	
	public void repaintGUI()
	{
		for(PicGUI guiElement : guiObjects)
		{
			guiElement.repaintGUI();
		}
	}
	public void registerGUIElement(PicGUI element)
	{
		guiObjects.add(element);
	}

}
