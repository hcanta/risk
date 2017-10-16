package risk.game.cards;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

import risk.util.map.Country;

import java.util.Collections;

/**
 * This class creates the Deck of cards as per the number of countries in the map. It also configures which 
 * type of card it is among the three types.
 * @author Mohammad Akif Beg
 * @version 1.0
 **/

public class Deck {

		private int i;
		
		private String input;
		private String name;
		
		private String[] typesArray;
		
		private ArrayList<Card> deck;
		private ArrayList<Country> countries;

		private Card drawCard;

		/**
		* This method creates all cards as per the number of countries selected in the map, one for each territory. 
		* Each card has either a type of Infantry, Cavalry, or Artillery. Ensure that the number of
		* Infantry, Cavalry, and Artillery are the same
		**/
		
		public Deck (ArrayList<Country> countries) {		
			
			Collections.shuffle(countries);
			
			//String Array of CardTypes
			typesArray = new String[]{ "Infantry", "Cavalry", "Artillery" };
			
			deck = new ArrayList<Card>();
			
			for (i = 0; i < countries.size(); i++) {
			// Inserting a card into the Deck
			// Assuming country size is 42 right now.
				deck.add(new Card(typesArray[i / 14], countries.get(i)));
				System.out.println("Added new card to deck: " + deck.get(i).getName());
			}
			Collections.shuffle(deck);
		}
		

		/**
		* Withdraws a card from the deck and return it
		**/
		public Card draw() {
		
			drawCard = deck.get(0);
			deck.remove(0);
			
			return drawCard;
		}

		/**
		* Add a card to the deck
		* @param card an object of the Card class
		**/
		public void add(Card card) {
		
			deck.add(card);
		}

		/**
		* Shuffle the deck of cards so that it becomes random.
		**/
		public void shuffle() {
		
			Collections.shuffle(deck);
		}
}