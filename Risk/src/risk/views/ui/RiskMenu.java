/**
 *  Package contains the various UI components of the risk game
 */
package risk.views.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import risk.utils.constants.RiskIntegers;
import risk.utils.constants.RiskStrings;

/**
 * This class contains a custom JMenu item to be added to the game view, extends JMenuBar
 * @author hcanta
 *
 */
public class RiskMenu  extends JMenuBar implements Serializable
{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 5490475001080749877L;
	
	/**
	 *country off menu item, the action listener need to be added in the class that uses it
	 */
	private final JMenuItem countryOff = new JMenuItem(RiskStrings.TOGGLE_COUNTRY_OFF);
	/**
	 *country on menu item, the action listener need to be added in the class that uses it
	 */
	private final JMenuItem countryOn = new JMenuItem(RiskStrings.TOGGLE_COUNTRY_ON);

	/**
	 *logger off menu item, the action listener need to be added in the class that uses it
	 */
	private final JMenuItem loggerOff = new JMenuItem(RiskStrings.TOGGLE_HISTORY_OFF);
	/**
	 *logger on menu item, the action listener need to be added in the class that uses it
	 */
	private final JMenuItem loggerOn = new JMenuItem(RiskStrings.TOGGLE_HISTORY_ON);
	
	/**
	 *create map menu item, the action listener need to be added in the class that uses it
	 */
	private final JMenuItem menuItemCreateMap = new JMenuItem(RiskStrings.MENU_ITEM_CREATE_MAP);
	
	/**
	 *edit map menu item, the action listener need to be added in the class that uses it
	 */
	private final JMenuItem menuItemEditMap = new JMenuItem(RiskStrings.MENU_ITEM_EDIT_MAP);
	
	/**
	 *exit menu item.
	 */
	private final JMenuItem menuItemExit = new JMenuItem(RiskStrings.MENU_ITEM_EXIT);
	/**
	 *open map and play menu item, the action listener need to be added in the class that uses it
	 */
	private final JMenuItem menuItemOpenMap = new JMenuItem(RiskStrings.MENU_ITEM_OPEN_MAP);
	
	
	
	/**
	 * Jmenu Item to load the Game
	 */
	private final JMenuItem loadGame = new JMenuItem(RiskStrings.LOAD_GAME);
	
	/**
	 * Jmenu Item to Lunch a tournament
	 */
	private final JMenuItem tournament = new JMenuItem(RiskStrings.TOURNAMENT);
	
	/**
	 * Help Menu contains a Read Me and logger info
	 */
	private final JMenu help;
	
	/**
	 * File Menu Jmenu container
	 */
	private JMenu menuFile;
	/**
	 * Constructor of the Risk Menu
	 */
	public RiskMenu() 
	{
		menuFile = new JMenu(RiskStrings.MENU_FILE);
		help = new JMenu(RiskStrings.MENU_HELP);
		
		
		this.add(menuFile);
		this.add(help);
		this.setPreferredSize(new Dimension(RiskIntegers.GAME_WIDTH, RiskIntegers.STATE_PANEL_HEIGHT/4));
		
		menuFile.add(menuItemCreateMap);
		
		
		menuFile.add(menuItemEditMap);
		
		
		menuFile.add(menuItemOpenMap);
		
	
		menuFile.add(tournament);
		
		menuFile.add(loadGame);
		menuFile.add(menuItemExit);
		help.add(loggerOff);
		help.add(countryOff);
		
	    loggerOn.setAccelerator(KeyStroke.getKeyStroke(72, 2));
	    
	    loggerOff.setAccelerator(KeyStroke.getKeyStroke(72, 2));
	    
	    countryOn.setAccelerator(KeyStroke.getKeyStroke(72, 1));
	    
	    countryOff.setAccelerator(KeyStroke.getKeyStroke(72, 1));
	
		
		menuItemExit.addActionListener(new ActionListener()
	    {
		      public void actionPerformed(ActionEvent e)
		      {
		        System.exit(0);
		      }
	    });		
	}
	
	/**
	 * Returns the Jmenu Item country off item
	 * @return the country off item
	 */
	public JMenuItem getcountryOff()
	{
		return this.countryOff;
	}
	
	/**
	 * Returns the Jmenu Item country on item
	 * @return the country on item
	 */
	public JMenuItem getcountryOn()
	{
		return this.countryOn;
	}
	
	/**
	 * Returns the Jmenu Item Logger off item
	 * @return the logger off item
	 */
	public JMenuItem getLoggerOff()
	{
		return this.loggerOff;
	}
	
	/**
	 * Returns the Jmenu Item Logger on item
	 * @return the logger on item
	 */
	public JMenuItem getLoggerOn()
	{
		return this.loggerOn;
	}
	
	/**
	 * Returns the Jmenu Item edit map item
	 * @return the edit map on item
	 */
	public JMenuItem getMenuItemEditMap()
	{
		return this.menuItemEditMap;
	}
	
	/**
	 * Returns the Jmenu Item create map item
	 * @return the create map on item
	 */
	public JMenuItem getMenuItemCreatetMap()
	{
		return this.menuItemCreateMap;
	}
	/**
	 * Returns the Jmenu Item load and Play item
	 * @return the creopenate map on item
	 */
	public JMenuItem getMenuItemOpenetMap()
	{
		return this.menuItemOpenMap;
	}

	/**
	 * Returns the JMenu Item to launch a tournament
	 * @return the tournament the Jmenu Item
	 */
	public JMenuItem getTournament() {
		return this.tournament;
	}

	/**
	 * Returns the JMEnu Item to Load the Game
	 * @return the loadGame  Jmenu Item
	 */
	public JMenuItem getLoadGame() {
		return this.loadGame;
	}

	

	/**
	 * Returns the Help Jmenu
	 * @return The Help Jmenu
	 */
	public JMenu getHelp() {
		
		return  this.help;
	}

	/**
	 * Returns the File Jmenu
	 * @return The File Jmenu
	 */
	public JMenu getFileMenu() {
		
		return  this.menuFile;
	}
}
