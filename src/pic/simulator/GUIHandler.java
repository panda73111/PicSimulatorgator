package pic.simulator;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import pic.simulator.gui.PicGUI;

/**
 * @author Lorenzo Toso
 * 
 *         This class is required for a GUI-Element to be repainted. Objects
 *         that implement the interface PicGUI can register themselves and be
 *         notified after every command.
 * 
 */
public class GUIHandler {
	ArrayList<PicGUI> guiObjects;

	public GUIHandler() {
		guiObjects = new ArrayList<>();
	}
	
	
	/**
	 * Repaints all registered gui-elements after a Command has been executed
	 */
	public void repaintGUI() {
		for (PicGUI guiElement : guiObjects) {
			guiElement.repaintGUI();
		}
	}
	

	/**
	 * Registers a gui-element
	 * @param element The element that is supposed to be registered.
	 */
	public void registerGUIElement(PicGUI element) {
		guiObjects.add(element);
	}

	/**
	 * Unregisters a gui-element
	 * @param element The element that is supposed to be unregistered.
	 */
	public void unregisterGUIElement(PicGUI element)
	{
		guiObjects.remove(element);
	}


	public void showError(String title, String error)
	{
		JOptionPane.showMessageDialog(null, title, error, JOptionPane.ERROR_MESSAGE);
	}

}
