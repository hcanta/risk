package risk.game.cards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import risk.util.map.Country;



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
	private Country testObject;
	private Country testObject2;

	
	@Before
	public void setUp() throws Exception {
		
		testObject = new Country();
		testCard = new Card("Infantry", testObject);
		typeCard = "Infantry";
		typeCountry = "India";
		checking = new String();
	}

	@Test
	public void testGetName() {
		checking = testObject.getName();
		assertEquals(typeCountry,checking);
	}

	@Test
	public void testGetType() {
		checking = testCard.getType();
		assertEquals(typeCard,checking);
	}

	@Test
	public void testGetCountry() {
		testObject2 = testCard.getCountry();
		assertEquals(testObject2,testObject);
	}

}
