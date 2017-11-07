/**
 * Package contains the views of the game
 */
package risk.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import risk.model.maputils.RiskBoard;
import risk.utils.MapUtils;
import risk.utils.constants.RiskIntegers;
import risk.utils.constants.RiskStrings;
import risk.views.ui.GraphDisplayPanel;
import risk.views.ui.HistoryPanel;
import risk.views.ui.MapSelector;
import risk.views.ui.RiskMenu;
import risk.views.ui.StatePanel;

/**
 *The Game View Class generates the overall JFrame for the game.
 * @author hcanta
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
	final static JLabel backGround = new JLabel(new ImageIcon(
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
		loadAndPlayActionListener();
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
	 * This method add the action listener for the load and Play option from the riskMenu
	 */
	private void loadAndPlayActionListener()
	{
		GameView.riskMenu.menuItemOpenMap.addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser =  MapSelector.getJFileChooser();
				int result = fileChooser.showOpenDialog(GameView.gFrame);
				
				if (result == JFileChooser.APPROVE_OPTION) 
				{
					GameView.textPanel.addMessage("Attempting To Load Map");
					RiskBoard.ProperInstance(false).update();
					
					File file = fileChooser.getSelectedFile();
					if (!file.exists() && !file.isDirectory()) 
					{
						JOptionPane.showMessageDialog(null,
								RiskStrings.INVALID_FILE_LOCATION
										+ file.getAbsolutePath());
						
						GameView.textPanel.addMessage(RiskStrings.INVALID_FILE_LOCATION);
						RiskBoard.ProperInstance(false).update();
					}
					else
					{
						GameView.textPanel.addMessage("file name is: " + file.getName());
						RiskBoard.ProperInstance(false).update();
						if(MapUtils.loadFile(file, false))
						{
							GameView.textPanel.addMessage("The Map File was properly loaded.");
							RiskBoard.ProperInstance(false).update();
							
							GameView.center.remove(backGround);
							GameView.center.add((new GraphDisplayPanel(RiskBoard.ProperInstance(false).getGraph()).getContentPane()));
							GameView.gFrame.repaint();
							GameView.gFrame.validate();
						}
						else
						{
							GameView.textPanel.addMessage("The Map File was invalid.");
							GameView.center.removeAll();
							GameView.center.add(backGround);
							RiskBoard.ProperInstance(false).update();
							RiskBoard.ProperInstance(false).clear();
						}
						
					}
				}
			}  
		});	
			
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		this.bottom.update(arg0, arg1);
		GameView.textPanel.update();
		GameView.textScroller.validate();
		GameView.textScroller.repaint();
		;
		
		GameView.gFrame.repaint();
		GameView.gFrame.validate();
		
	}
}
