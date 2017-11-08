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
import risk.views.ui.HistoryPanel;
import risk.views.ui.RiskMenu;
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
	private static HistoryPanel textPanel;
	/**
	 * Text scroller to be contained within the history panel
	 */
	private static JScrollPane textScroller;
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
	 * Pannel to the East of the frame
	 */
	private JPanel east;
	
	
	/**
	* Constructor for the GameView object
	*/
	public GameView()
	{
		
		riskMenu = new RiskMenu();
		historyActionListener();
		
	    top = new JPanel();
	    center = new JPanel();
	    bottom = new StatePanel();
	    east = new JPanel();
	    
	    bottom.setVisible(true);
	    center.setBackground(Color.lightGray);
	    center.setVisible(true);
	    east.setVisible(true);
	    east.setLayout(new GridLayout(1, 1));
	    center.setLayout(new GridLayout(0, 1));
	    
	    top.setLayout(new GridLayout(0, 1));
	    bottom.setLayout(new GridLayout(2, 1));;
	    
	    top.add(riskMenu);
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
	    
	    textPanel = new HistoryPanel();
	    
	    textScroller = new JScrollPane(textPanel);
	    textScroller.setVisible(true);
	    
	    textScroller.setBackground(Color.WHITE);

	    east.add(textScroller);
	  
	    gFrame.getContentPane().add(east, BorderLayout.EAST);
	    
	    gFrame.repaint();
	    gFrame.validate();
	}
	
	/**
	* This Method add the action Listener for the Logger Buttons in the risk menu object created within the constructor
	*/
	private void historyActionListener()
	{
		GameView.riskMenu.loggerOff.addActionListener(new ActionListener()
	    {

			public void actionPerformed(ActionEvent e)
			{
				east.setVisible(false);
				GameView.riskMenu.help.add(GameView.riskMenu.loggerOn);
				GameView.riskMenu.help.remove(GameView.riskMenu.loggerOff);
				GameView.gFrame.repaint();
				GameView.gFrame.validate();
			}
		});
		
		GameView.riskMenu.loggerOn.addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				east.setVisible(true);
				GameView.riskMenu.help.remove(GameView.riskMenu.loggerOn);
				GameView.riskMenu.help.add(GameView.riskMenu.loggerOff);
				GameView.gFrame.repaint();
				GameView.gFrame.validate();
			}  
		});	
	}
	

	/**
	 * Returns the History panel of the gameView
	 * @return The history panel of the gameView
	 */
	public HistoryPanel getHistoryPanel()
	{
		return textPanel;
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
		if(!GameView.textPanel.getLastMessage().equals(stateString))
			GameView.textPanel.addMessage(stateString);
		GameView.textPanel.update();
		GameView.textScroller.validate();
		GameView.textScroller.repaint();
		
		
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
