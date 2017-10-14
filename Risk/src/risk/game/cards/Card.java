package risk.game.cards;

import risk.util.map.Country;

/**
 * This class is for the card that is being used in the game.
 * @author Mohammad Akif Beg
 * @version 1.0
 **/
public final class Card {

    private final String type;
    private final Country country;
    /**
     * parameterized constructor
     * @param type String value representing the type of a card
     * @param country Object of country class to associate the card with it.
     */
    public Card( String type, Country country ) {
		this.type = type;
		this.country = country;
    }
    
    /**
     * Getter method of the class 
     * @return String value of the name of the country
     */
	
	public String getName() {
		return country.getName() + ", " + type;
	}
	
	/**
     * Getter method of the class 
     * @return String value of the type of card
     */
	
    public String getType() {
		return type;
    }

    public Country getCountry() {
		return country;
    }
}