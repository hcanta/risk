/**
 * Package contains the views of the game
 */
package risk.views.ui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import risk.game.cards.Card;
import risk.utils.constants.OtherConstants;

/**
 * This class implements the Card Exchange panel it extends JPanel and implements Observer
 * @author hcanta
 * @version 2.1
 */
public class CardPanel extends JPanel {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 2005556110983353872L;

	/**
	 * Constructor of the Card Panel
	 * @param card the card object
	 */
	public CardPanel(Card card) {
		super();
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		JLabel label = new JLabel(card.getTerritory());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setOpaque(true);
		this.add(label, BorderLayout.NORTH);
		JLabel img;
		switch(card.getType())
		{
			case Cavalry:
				img =OtherConstants.cavalry;
				break;
			case Artillery: 
				img =OtherConstants.artillery;
				break;
			case Infantry:
			default:
				img =OtherConstants.infantry;
				break;
		}
		img.setVisible(true);
		img.setOpaque(true);
		this.add(img, BorderLayout.SOUTH);
		this.setOpaque(true);
	}
}
