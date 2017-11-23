/**
 * Package contains the views of the game
 */
package risk.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import risk.model.RiskBoard;
import risk.utils.constants.RiskIntegers;
import risk.views.ui.CardExchangePanel;
import risk.views.ui.RiskMenu;
import risk.views.ui.SidePanel;
import risk.views.ui.StatePanel;

/**
 *The Game View Class generates the overall JFrame for the game.
 * @author hcanta
 * @version 2.3
 */
public class GameView implements Observer
{
	/**
	 * The main Frame to be displayed
	 */
	private static JFrame gFrame;
	/**
	 * A label that contains the picture background
	 */
	public final static JLabel backGround = new JLabel(new ImageIcon(
			((new ImageIcon("img/g1.png").getImage().getScaledInstance(RiskIntegers.GAME_WIDTH - RiskIntegers.GAME_OFFSET,
					 RiskIntegers.GAME_HEIGHT - RiskIntegers.GAME_OFFSET -30, java.awt.Image.SCALE_SMOOTH)))));
	
	/**
	 * The top bar Menu
	 */
	private static RiskMenu riskMenu;

	/**
	 * The log/ history menu on the right
	 */
	private static SidePanel historyTextPanel;
	
	/**
	 * The info on the various countries
	 */
	private static SidePanel countryTextPanel;
	/**
	 * Text scroller to be contained within the history panel
	 */
	private static JScrollPane historyTextScroller;
	
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
	    
	    top.setLayout(new GridLayout(2, 0));
	    bottom.setLayout(new GridLayout(2, 1));
	    
	    top.add(riskMenu, BorderLayout.NORTH);
	    top.add(cardPanel, BorderLayout.SOUTH);
	    center.add(backGround);
	    
	    gFrame = new JFrame("Game");
	    gFrame.setIconImage(new ImageIcon(("img/g1.png")).getImage());
	    gFrame.setLayout(new BorderLayout());
	    gFrame.setResizable(false);
	    gFrame.setBackground(Color.white);
	    
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
	 * Returns the History panel of the gameView
	 * @return The history panel of the gameView
	 */
	public SidePanel getHistoryPanel()
	{
		return historyTextPanel;
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
		this.bottom.update(obj, param);
		String stateString = "Current State: "+ ((RiskBoard)obj).getState().name();		
		GameView.historyTextPanel.addMessage(stateString);
		GameView.historyTextPanel.update(obj,param);
		GameView.historyTextScroller.validate();
		GameView.historyTextScroller.repaint();
		
		
		GameView.gFrame.repaint();
		GameView.gFrame.validate();
		
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
}
