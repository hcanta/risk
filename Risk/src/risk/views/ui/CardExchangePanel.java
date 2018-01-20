/**
 * Package contains the views of the game
 */

package risk.views.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import risk.model.playerutils.IPlayer;
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
	 * Simple Constructor
	 */
	public CardExchangePanel()
	{
		super();
		this.setLayout(new GridLayout(1,5));
		this.setPreferredSize(new Dimension(RiskIntegers.GAME_WIDTH, RiskIntegers.STATE_PANEL_HEIGHT));
		this.setVisible(true);
		
	}
	
	

	/**
	 * The Update method for when the observable element notify them
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		this.setVisible(true);
		this.validate();
		this.repaint();
		
	}

	/**
	 * Populates The Top screen
	 * @param nbRoundsPlayed The Number of rounds
	 * @param cardExchangeCount The number of times Cards were exchanged
	 * @param player the player Object
	 */
	public void populate(int nbRoundsPlayed, int cardExchangeCount, IPlayer player) {
		this.removeAll();
		JLabel rounds = new JLabel(" #of rounds played: " + nbRoundsPlayed);
		
		rounds.setVisible(true);
		rounds.setOpaque(true);
		
		this.add(rounds);
		
		for(int i =0; i< player.getHand().size(); i++)
		{
			this.add(new CardPanel(player.getHand().getCards().get(i)));
		}
		
		JLabel card = new JLabel(" #of cards exchanged: " + cardExchangeCount);
		card.setVisible(true);
		card.setOpaque(true);
		this.add(card);
	}
	
	

}
