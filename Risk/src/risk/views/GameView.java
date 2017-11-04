/**
 * Package contains the views of the game
 */
package risk.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import risk.utils.constants.RiskIntegers;
import risk.views.ui.HistoryPanel;
import risk.views.ui.RiskMenu;
import risk.views.ui.StatePanel;

/**
 *The Game View Class generates the overall JFrame for the game.
 * @author hcanta
 */
public class GameView 
{
	private static JFrame gFrame;
	final JLabel backGround = new JLabel(new ImageIcon(
			((new ImageIcon("img/g1.png").getImage().getScaledInstance(RiskIntegers.GAME_WIDTH - RiskIntegers.GAME_OFFSET,
					 RiskIntegers.GAME_HEIGHT - RiskIntegers.GAME_OFFSET -30, java.awt.Image.SCALE_SMOOTH)))));
	
	private static RiskMenu riskMenu;

	private HistoryPanel textPanel;
	private JScrollPane textScroller;
	private JPanel top;
	private JPanel center;
	private StatePanel bottom;
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
	 * Display the Frame and launch the Game
	 */
	public void launch() 
	{
		// TODO Auto-generated method stub
		
	}
}
