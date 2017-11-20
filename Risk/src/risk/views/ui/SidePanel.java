/**
 * Package contains the views of the game
 */
package risk.views.ui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;

/**
 * The Side Panel  extends JTextArea Implements Observer It is used to log and display the History of the various
 * State And step taken throughout the game, as well as the info on the various countries
 * @author hcanta
 */
public class SidePanel extends JTextArea implements Observer
{
	
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -2364634371830350680L;

	/**
	* ArrayList of the description of the various events
	*/
	private ArrayList<String> fTextLog;
	

	/**
	* Constructor for the Side Panel
	*/
	public SidePanel() 
	{
		super(10, 25);
		this.setVisible(true);
	    fTextLog = new ArrayList<String>();	
	    this.setEditable(false);
	}
	
	/**
	 * Add a string to the History panel to be displayed
	 * @param msg The message to be added
	 */
	public void addMessage(String msg)
	{
		this.fTextLog.add(msg);
		
	}
	
	/**
	 * Updates the content of the Text to be displayed
	 */
	@Override
	public void update(Observable arg0, Object arg1) 
	{	
		refreshText();
		this.setVisible(true);
		this.validate();
		this.repaint();
	}
		
	/**
	 * Refresh the content of the textArea
	 */
	public void refreshText()
	{
		StringBuilder toPrint = new StringBuilder();
		for (String text : fTextLog)
		{
			toPrint.append(" "+text + "\n");
		}
		this.setText(toPrint.toString());
	}
	
	/**
	 * Clear the message Lists
	 */
	public void clearMessages()
	{
		this.fTextLog.clear();
	}

		
}
