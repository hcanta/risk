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
 * @version 2.0
 */
public class CardTest {
	
	/**
	 * The private member of the TestClass which helps in checking the assertions.
	 */
	
	private Card testCard;
	private String typeCard;
	private Territory testObject;


	
	@Before
	public void setUp() throws Exception {
		
		testObject = new Territory("kolkata","india");
		testCard = new Card(CardType.Infantry, testObject.getTerritoryName());
		typeCard = "Infantry";
		
	}

	@Test
	public void testGetName() {
		assertEquals(testObject.getTerritoryName(),testCard.getTerritory());
	}

	@Test
	public void testGetType() {
		testCard.getType();
		assertEquals(typeCard,testCard.getType().name());
	}


}
