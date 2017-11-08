/**
 *  Package contains the various UI components of the risk game
 */
package risk.views.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import risk.utils.constants.RiskStrings;

/**
 * This class contains a custom JMenu item to be added to the game view, extends JMenuBar
 * @author hcanta
 *
 */
public class RiskMenu  extends JMenuBar
{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 5490475001080749877L;

	/**
	 *logger off menu item, the action listener need to be added in the class that uses it
	 */
	public final JMenuItem loggerOff = new JMenuItem(RiskStrings.TOGGLE_HISTORY_OFF);
	/**
	 *logger on menu item, the action listener need to be added in the class that uses it
	 */
	public final JMenuItem loggerOn = new JMenuItem(RiskStrings.TOGGLE_HISTORY_ON);
	
	/**
	 *create map menu item, the action listener need to be added in the class that uses it
	 */
	public final JMenuItem menuItemCreateMap = new JMenuItem(RiskStrings.MENU_ITEM_CREATE_MAP);
	
	/**
	 *edit map menu item, the action listener need to be added in the class that uses it
	 */
	public final JMenuItem menuItemEditMap = new JMenuItem(RiskStrings.MENU_ITEM_EDIT_MAP);
	
	/**
	 *exit menu item.
	 */
	private final JMenuItem menuItemExit = new JMenuItem(RiskStrings.MENU_ITEM_EXIT);
	/**
	 *open map and play menu item, the action listener need to be added in the class that uses it
	 */
	public final JMenuItem menuItemOpenMap = new JMenuItem(RiskStrings.MENU_ITEM_OPEN_MAP);
	
	/**
	 * Help Menu contains a Read Me and logger info
	 */
	public final JMenu help;
	/**
	 * Constructor of the Risk Menu
	 */
	public RiskMenu() 
	{
		JMenu menuFile = new JMenu(RiskStrings.MENU_FILE);
		help = new JMenu(RiskStrings.MENU_HELP);
		
		
		this.add(menuFile);
		this.add(help);
		
		
		menuFile.add(menuItemCreateMap);
		
		
		menuFile.add(menuItemEditMap);
		
		
		menuFile.add(menuItemOpenMap);
		
		
		menuFile.add(menuItemExit);
		
		help.add(loggerOff);
		
		
	    loggerOn.setAccelerator(KeyStroke.getKeyStroke(72, 2));
	    
	    loggerOff.setAccelerator(KeyStroke.getKeyStroke(72, 2));
	
		
		menuItemExit.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		        System.exit(0);
		      }
	    });		
	}

}
