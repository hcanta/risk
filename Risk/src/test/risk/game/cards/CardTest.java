/**
 * This package Tests The risk Card class
 */
package test.risk.game.cards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import risk.game.cards.Card;
import risk.model.maputils.Territory;
import risk.utils.constants.RiskEnum.CardType;

/**
 * This class is a Junit Test Class which checks the method associated with the Card class.
 * @author hcanta
 * @author Mohammad Akif Beg
 * @author Karan
 * @author addy
 * @version 3.0
 */
public class CardTest {
	
	/**
	 * The private member of the TestClass which helps in checking the assertions.
	 */
	
	private Card testCard;
	private String typeCard;
	private Territory testObject;


	/**
	 * Initializes the Territory class and Card class objects before every test case
	 * @throws Exception Set Up failed
	 */
	@Before
	public void setUp() throws Exception {
		
		testObject = new Territory("kolkata","india");
		testCard = new Card(CardType.Infantry, testObject.getTerritoryName());
		typeCard = "Infantry";
		
	}

	/**
	 * Tests the card associated with the territory
	 */
	@Test
	public void testGetName() {
		
		assertEquals(testObject.getTerritoryName(),testCard.getTerritory());
	}

	/**
	 * Tests the card type
	 */
	@Test
	public void testGetType() {
		
		testCard.getType();
		assertEquals(typeCard,testCard.getType().name());
	}


}
