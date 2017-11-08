package risk.game.cards;



/**
 * This class is for the card that is being used in the game.
 * @author Mohammad Akif Beg
 * @author hcanta
 * @version 2.0
 **/
public final class Card {

    private final String type;
    private final String country;
    /**
     * parameterized constructor
     * @param type String value representing the type of a card
     * @param country Object of country class to associate the card with it.
     */
    public Card( String type, String country ) {
		this.type = type;
		this.country = country;
    }
    
    /**
     * Getter method of the class 
     * @return String value of the name of the country
     */
	
	public String getNameAndType() {
		return country+ ", " + type;
	}
	
	/**
     * Getter method of the class 
     * @return String value of the type of card
     */
	
    public String getType() {
		return type;
    }

    /**
     * Getter method of the class 
     * @return String object reference
     */
	
    public String getName() {
		return country;
    }
}