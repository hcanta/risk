package risk.game.cards;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import risk.util.map.Country;

/**
 * This class is a Junit Test Class which checks the method associated with the Deck class.
 * @author Mohammad Akif Beg
 * @version 1.0
 */
public class DeckTest {
	
	/**
	 * The private member of the TestClass which helps in checking the assertions.
	 */
	
	private Deck testObject;
	private Country testCountry;
	private Card testCard;
	private String checking;
	private String otherChecking;
	private ArrayList<Country> temp;
	private ArrayList<Card> cardObject;
	private String typeOfCard;
	
	/**
	 * This is a setUp method which gets implemented before every TestMethod
	 */
	
	@Before
	public void setUp(){
		temp = new ArrayList<Country>();
		cardObject = new ArrayList<Card>();
		temp.add(testCountry);
		testObject = new Deck(temp);
		Country testCountry= temp.remove(0);
		checking = "Infantry";
		cardObject.add(1,testCard);
		otherChecking = testCard.getType();
	}
	
	/**
	 * This method tests the draw() function of the deck class.
	 */
	
	@Test
	public void testDraw() {
		typeOfCard = testObject.draw().toString();
		Assert.assertNotNull(typeOfCard);
		Assert.assertEquals(checking, typeOfCard);
	}
	
	/**
	 * This method tests the draw() function of the deck class.
	 */

	@Test
	public void testAdd() {
		testObject = new Deck(temp);
	    Assert.assertNull(testObject);
	}
	
	/**
	 * This method tests the Shuffle method of the deck class.
	 */

	@Test
	public void testShuffle() {
		testObject = new Deck(temp);
		Assert.assertNotNull(testObject);
		Assert.assertEquals(otherChecking, "Infantry");
	}
}
