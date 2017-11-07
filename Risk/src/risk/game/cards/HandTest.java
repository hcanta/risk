package risk.game.cards;

import junit.framework.TestCase;
import risk.util.map.Country;

public class HandTest extends TestCase {
	
	Card card, card1, card2, card3, card4, card5, card6 ;
	Hand first;
	private Country TestObject;
	
	protected void setUp() throws Exception {
		first = new Hand();
		card = new Card("Infantry",TestObject);
		card1 = new Card("Cavalry",TestObject);
		card2 = new Card("Artillery",TestObject);
		card3 = new Card("Cavalry",TestObject);
		card4 = new Card("Cavalry",TestObject);
		card5 = new Card("Artillery",TestObject);
		card6 = new Card("Infantry",TestObject);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAdd() {
		assertNotNull(first);
		first.add(card);
		first.add(card1);
		first.add(card2);
		first.add(card3);
		first.add(card4);
		first.add(card5);
		first.add(card6);
	}
	
public void testCanTurnInCards() {
		
		boolean temp = first.canTurnInCards(1,2,3);
		assertFalse(temp);
		
	}
public void testRemoveCardsFromHand() {
	boolean temp = first.getCards().remove(card);
	assertFalse(temp);
	boolean temp2 = first.getCards().remove(card);
	assertFalse(temp2);
}

	
	public void testMustTurnInCards() {
		boolean temp = first.mustTurnInCards();
		assertFalse(temp);			
	}
}
