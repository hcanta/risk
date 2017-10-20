package risk.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import risk.game.cards.Card;
import risk.util.map.Territory;



/**
 * This class is a Junit Test Class which checks the method associated with the Card class.
 * @author Mohammad Akif Beg
 * @version 1.0
 */
public class CardTest {
	
	/**
	 * The private member of the TestClass which helps in checking the assertions.
	 */
	
	private Card testCard;
	private String checking;
	private String typeCard;
	private String typeCountry;
	private Territory testObject;


	
	@Before
	public void setUp() throws Exception {
		
		testObject = new Territory("kolkata","india");
		testCard = new Card("Infantry", testObject.getTerritoryName());
		typeCard = "Infantry";
		typeCountry = "kolkata";
		checking = new String();
	}

	@Test
	public void testGetName() {
		checking = testObject.getTerritoryName();
		assertEquals(typeCountry,checking);
	}

	@Test
	public void testGetType() {
		checking = testCard.getType();
		assertEquals(typeCard,checking);
	}

}
