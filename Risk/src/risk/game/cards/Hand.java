/**
 * The game cards package holds the implementation of the cards object
 */
package risk.game.cards;


import java.io.Serializable;
import java.util.ArrayList;
/**
 * This class is for the card that is being used in the game.
 * @author hcanta
 * @author Akif
 * @version 3.3
 **/
public class Hand implements Serializable
{

	/**
	 * Generated Version UID
	 */
	private static final long serialVersionUID = 44005035593304612L;


	
	/**
	 * The cards that the player has
	 */
	private ArrayList<Card> hand;

	/**
	 * No argument constructor. Instantiate the hand.
	 **/
	public Hand() {
	
		hand = new ArrayList<Card>();
	}
	
	/**
	 * Adds the card to the hand of the player 
	 * @param card The card to be added
	 **/
	public void add(Card card) {
	
		hand.add(card);
	}
	/**
	 *  Removes the cards at the given indices from the hand This assumes that the index are given in ascending number
	 * @param index1 Index of the first card to be removed
	 * @param index2 Index of the second card to be removed
	 * @param index3 Index of the third card to be removed
	 */
	public void removeCardsFromHand(int index1, int index2, int index3) {
	
		if (canTurnInCards()) {
			hand.remove(index3);
			hand.remove(index2);
			hand.remove(index1);
		
		} else {
			System.out.println("You must trade in three cards of the same type or one of each type.");
		}
	}
	
	/**
	 * returns true if the player can turn in cards
	 * @return True/False can the card be turned in
	 **/
	public boolean canTurnInCards() {
	
		
		
		return (hand.size() >= 3);
	}

	/**
	 * Returns true if the player must turn in cards
	 * @return true/false must the card be turned in 
	 **/
	public boolean mustTurnInCards() {
	
		return hand.size() >= 5;
	}

	/**
	 * Returns the hand
	 * @return the ArrayList of cards (the hand)
	 **/
	public ArrayList<Card> getCards() {
		return hand;
	}
	
	/**
	 * Creates a string representation of the hand
	 * @return a string representation of the hand
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append("");
		for(int i =0;  i< this.hand.size(); i++)
		{
			str.append(""+i +" "+this.hand.get(i).getNameAndType()+"\n");
		}
			
		return str.toString();
		
	}

	/**
	 * Returns the size of the hand
	 * @return size of the hand
	 */
	public int size() {
		return this.hand.size();
	}

	
}
