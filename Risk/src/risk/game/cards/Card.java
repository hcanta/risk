/**
 * The game cards package holds the implementation of the cards object
 */
package risk.game.cards;

import java.io.Serializable;

import risk.utils.constants.RiskEnum.CardType;

/**
 * This class is for the card that is being used in the game.
 * @author hcanta
 * @author Akif
 * @version 3.3
 **/
public final class Card implements Serializable
{

	/**
	 * Generated Version Serial UID
	 */
	private static final long serialVersionUID = 8007690005633224392L;
	/**
	 * The Card Type
	 */
    private  CardType type;
    /**
     * The territory that was captured that yielded this card
     */
    private  String territory;
    /**
     * Constructor for the Card class
     * parameterized constructor
     * @param type String value representing the type of a card
     * @param territory Object of territory class to associate the card with it.
     */
    public Card( CardType type, String territory ) {
		this.type = type;
		this.territory = territory;
    }
    
    /**
     * Getter method of the class 
     * @return String value of the name of the territory
     */
	public String getNameAndType() {
		return territory+ ", " + type.name();
	}
	
	/**
     * Getter method of the class 
     * @return String value of the type of card
     */
    public CardType getType() {
		return type;
    }

    /**
     * Returns the territory of the card
     * @return String territory of the card
     */
    public String getTerritory() {
		return territory;
    }
}