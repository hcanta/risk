/**
 * Package contains the views of the game
 */

package risk.views.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import risk.utils.constants.RiskIntegers;

/**
 * This class implements the Card Exchange panel it extends JPanel and implements Observer
 * @author hcanta
 * @version 2.1
 */
public class CardExchangePanel extends JPanel implements Observer, Serializable
{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 3498591350211981708L;
	
	/**
	 * The Number of times Card Exchange Happened 
	 */
	private int nbCardExhangeRounds;
	
	/**
	 * Simple Constructor
	 */
	public CardExchangePanel()
	{
		super();
		this.setLayout(new GridLayout(2,1));
		this.setPreferredSize(new Dimension(RiskIntegers.GAME_WIDTH, RiskIntegers.STATE_PANEL_HEIGHT));
		this.setVisible(true);
		this.nbCardExhangeRounds = 0;
	}
	
	/**
	 * Increments the Number of card Exhange rounds By 1
	 */
	public void incrementCardExchangeRounds()
	{
		this.nbCardExhangeRounds ++;
	}
	/**
	 * Returns the number of nbCard Rounds. The number of card exchange is this number times 3
	 * @return The number of card Exchange rounds
	 */
	public int getNbCardExchangeRounds()
	{
		return this.nbCardExhangeRounds;
	}

	/**
	 * The Update method for when the observable element notify them
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	

}
