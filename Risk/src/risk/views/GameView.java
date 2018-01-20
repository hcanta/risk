/**
 * Package contains the views of the game
 */
package risk.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import risk.model.RiskBoard;
import risk.utils.constants.OtherConstants;
import risk.utils.constants.RiskEnum;
import risk.utils.constants.RiskIntegers;
import risk.utils.constants.RiskStrings;
import risk.views.ui.CardExchangePanel;
import risk.views.ui.RiskGraph;
import risk.views.ui.RiskMenu;
import risk.views.ui.SidePanel;
import risk.views.ui.StatePanel;

/**
 *The Game View Class generates the overall JFrame for the game.
 * @author hcanta
 * @version 2.3
 */
public class GameView implements Observer, Serializable
{
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -6643458269966204806L;
	/**
	 * The main Frame to be displayed
	 */
	private static JFrame gFrame;
	
	
	/**
	 * The top bar Menu
	 */
	private static RiskMenu riskMenu;

	/**
	 * The log/ history menu on the right
	 */
	private static SidePanel historyTextPanel;
	
	/**
	 * Text scroller to be contained within the history panel
	 */
	private static JScrollPane historyTextScroller;
	
	/**
	 * The info on the various countries
	 */
	private static SidePanel countryTextPanel;
	/**
	 * Text scroller that contains the graph
	 */
	private JScrollPane graphScroller = null;
	
	/**
	 * The Risk Graph
	 */
	private RiskGraph graph = null;
	
	/**
	 * Text scroller to be contained within the country panel
	 */
	private static JScrollPane countryTextScroller;
	/**
	 * The panel of the top of the frame
	 */
	private JPanel top;
	/**
	 * Centered panel of the frame where the graphs will be displayed
	 */
	private static JPanel center;
	/**
	 * Bottom panel of the frame that contains the state panel
	 */
	private StatePanel bottom;
	
	/**
	 * Panel to the East of the frame
	 */
	private JPanel east;
	
	
	/**
	 * Panel to the west of the frame
	 */
	private JPanel west;
	
	/**
	 * The Card Exchange Panel
	 */
	private CardExchangePanel cardPanel;
	
	/**
	* Constructor for the GameView object
	*/
	public GameView()
	{
		
		riskMenu = new RiskMenu();
		historyActionListener();
		countryActionListener();
	    top = new JPanel();
	    center = new JPanel();
	    bottom = new StatePanel();
	    east = new JPanel();
	    west = new JPanel();
	    cardPanel = new CardExchangePanel();
	    
	    bottom.setVisible(true);
	    center.setBackground(Color.lightGray);
	    center.setVisible(true);
	    east.setVisible(true);
	    east.setLayout(new GridLayout(1, 1));
	    west.setVisible(true);
	    west.setLayout(new GridLayout(1, 1));
	    center.setLayout(new GridLayout(0, 1));
	    
	    top.setLayout(new BorderLayout());
	    bottom.setLayout(new GridLayout(2, 1));
	    
	    top.add(riskMenu, BorderLayout.NORTH);
	    top.add(cardPanel, BorderLayout.SOUTH);
	    center.add(OtherConstants.backGround);
	    
	    gFrame = new JFrame(RiskStrings.RISK);
	    gFrame.setIconImage(new ImageIcon(("img/g1.png")).getImage());
	    gFrame.setLayout(new BorderLayout());
	    gFrame.setResizable(false);
	    gFrame.setBackground(Color.white);
	    gFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    gFrame.setVisible(true);
	    gFrame.setSize(RiskIntegers.GAME_WIDTH, RiskIntegers.GAME_HEIGHT);
	    gFrame.getContentPane().add(top,BorderLayout.NORTH );
	    gFrame.getContentPane().add(bottom, BorderLayout.SOUTH);
	    gFrame.getContentPane().add(center, BorderLayout.CENTER);
	    gFrame.setLocationRelativeTo(null);
	    
	    historyTextPanel = new SidePanel();
	    countryTextPanel = new SidePanel();
	    
	    historyTextScroller = new JScrollPane(historyTextPanel);
	    historyTextScroller.setVisible(true);
	    historyTextScroller.setBackground(Color.WHITE);
	    
	    
	    countryTextScroller = new JScrollPane(countryTextPanel);
	    countryTextScroller.setVisible(true);
	    countryTextScroller.setBackground(Color.WHITE);

	    east.add(historyTextScroller);
	    west.add(countryTextScroller);
	    gFrame.getContentPane().add(east, BorderLayout.EAST);
	    gFrame.getContentPane().add(west, BorderLayout.WEST);
	    gFrame.repaint();
	    gFrame.validate();
	}
	
	/**
	* This Method add the action Listener for the Logger Buttons in the risk menu object created within the constructor
	*/
	private void historyActionListener()
	{
		GameView.riskMenu.getLoggerOff().addActionListener(new ActionListener()
	    {

			public void actionPerformed(ActionEvent e)
			{
				east.setVisible(false);
				GameView.riskMenu.getHelp().add(GameView.riskMenu.getLoggerOn());
				GameView.riskMenu.getHelp().remove(GameView.riskMenu.getLoggerOff());
				GameView.gFrame.repaint();
				GameView.gFrame.validate();
			}
		});
		
		GameView.riskMenu.getLoggerOn().addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				east.setVisible(true);
				GameView.riskMenu.getHelp().remove(GameView.riskMenu.getLoggerOn());
				GameView.riskMenu.getHelp().add(GameView.riskMenu.getLoggerOff());
				GameView.gFrame.repaint();
				GameView.gFrame.validate();
			}  
		});	
	}
	
	/**
	* This Method add the action Listener for the country Buttons in the risk menu object created within the constructor
	*/
	private void countryActionListener()
	{
		GameView.riskMenu.getcountryOff().addActionListener(new ActionListener()
	    {

			public void actionPerformed(ActionEvent e)
			{
				west.setVisible(false);
				GameView.riskMenu.getHelp().add(GameView.riskMenu.getcountryOn());
				GameView.riskMenu.getHelp().remove(GameView.riskMenu.getcountryOff());
				GameView.gFrame.repaint();
				GameView.gFrame.validate();
			}
		});
		
		GameView.riskMenu.getcountryOn().addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				west.setVisible(true);
				GameView.riskMenu.getHelp().remove(GameView.riskMenu.getcountryOn());
				GameView.riskMenu.getHelp().add(GameView.riskMenu.getcountryOff());
				GameView.gFrame.repaint();
				GameView.gFrame.validate();
			}  
		});	
	}

	
	
	/**
	 * Returns the country panel of the gameView
	 * @return The country panel of the gameView
	 */
	public SidePanel getCountryPanel()
	{
		return countryTextPanel;
	}
	
	/**
	 * Returns the RiskMenu of the Game View
	 * @return The risk Menu of the GameView
	 */
	public RiskMenu getRiskMenu()
	{
		return riskMenu;
	}
	
	/**
	 * This method is called whenever the observed object is changed. An application calls an Observable object's notifyObservers method to have all the object's observers notified of the change.
	 * @param obj The object Changed
	 * @param param a parameter for possible observers
	 */
	@Override
	public void update(Observable obj, Object param) {

		switch((RiskEnum.RiskEvent)param)
		{
			case HistoryUpdate:
				 historyUpdate(obj, param);
				break;
			case StateChange:
				bottom.update(obj, param);
				break;
				
			case CountryUpdate:
				 countryUpdate(obj, param);
				 if(graph != null)
				 {
					 graph.update(obj, param);
				 }
			case CardTrade:
				this.getCardPanel().update(obj, param);
				break;
			case GraphUpdate:
				this.addGraph(((RiskBoard)obj));
				break;
			default:
			case GeneralUpdate:
				historyUpdate(obj, param);
				bottom.update(obj, param);
				 countryUpdate(obj, param);
				 if(graph != null)
				 {
					 graph.update(obj, param);
				 }
				break;
		}
		
		GameView.gFrame.repaint();
		GameView.gFrame.validate();
		
	}
	/**
	 * This method Update the history
	 * @param obj The object Changed
	 * @param param a parameter for possible observers
	 */
	private void historyUpdate(Observable obj,Object param)
	{
		GameView.historyTextPanel.update(obj, param);
		GameView.historyTextScroller.validate();
		GameView.historyTextScroller.repaint();
		
	}
	/**
	 * This method Update the country
	 * @param obj The object Changed
	 * @param param the risk event
	 */
	private void countryUpdate(Observable obj, Object param)
	{
		GameView.countryTextPanel.update(obj, param);
		GameView.countryTextScroller.validate();
		GameView.countryTextScroller.repaint();
		
	}

	/**
	 * Returns the JFrame of the Game View
	 * @return The Jframe
	 */
	public JFrame getFrame() {
	
		return GameView.gFrame;
	}

	/**
	 * Returns the center panel of the game view
	 * @return The center panel of the game view
	 */
	public JPanel getCenter() {
		return center;
	}

	/**
	 * Adds the Graph to the Game View
	 * @param board The risk Board
	 */
	public void addGraph(RiskBoard board) {
		this.getCenter().removeAll();
		this.getCenter().repaint();
		this.getCenter().validate();
		graph = new RiskGraph(board);
		this.graphScroller = new JScrollPane(graph);
		graphScroller.setVisible(true);
		center.add(graphScroller);
		this.getCenter().repaint();
		this.getCenter().validate();
		
	}

	/**
	 * Cleans The Center of the board
	 */
	public void cleanCenter() {

		this.getCenter().removeAll();
		this.getCenter().repaint();
		this.getCenter().validate();
		this.getCenter().add(OtherConstants.backGround);
		this.getCenter().repaint();
		this.getCenter().validate();
		if(graphScroller!=null)
			graphScroller.removeAll();
		if(graph!=null)
			graph.removeAll();
		graph = null;
		graphScroller = null;
		
		
	}
	
	/**
	 * The Card Panel
	 * @return the card panel
	 */
	public CardExchangePanel getCardPanel()
	{
		return this.cardPanel;
	}
}
