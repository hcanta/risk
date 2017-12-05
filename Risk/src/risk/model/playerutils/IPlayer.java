/**
 * * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils;

import java.io.Serializable;
import java.util.ArrayList;

import risk.game.cards.Hand;
import risk.model.RiskBoard;
import risk.utils.Tuple;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;

/**
 * This interface contains the function that a player Human or Not must implement
 * @author hcanta
 * @version 1.2
 */
public interface IPlayer extends Serializable
{
	/**
	 * Returns the color of the player
	 * @return the color of the character
	 */
	public PlayerColors getColor();
	
	/**
	 * Returns the name  of the player
	 * @return the name of the character
	 */
	public String getName();
	
	/**
	 * Returns the Id of the player whose turn it is
	 * @return the player Id
	 */
	public short getPlayerID();
	
	/**
	 *  Increments the number of armies to be placed by the given number;
	 * @param nb number of armies to be placed
	 */
	void updateArmiestoBeplaced(int nb);
	
	/**
	 * Increments the amount of armies to be placed
	 */
	void incrementArmies();
	
	/**
	 * Decrements the amount of armies
	 */
	void decrementArmies();
	
	/**
	 * Set the Number of Armies to be Placed
	 * @param nbArmiesToBePlaced the nbArmiesToBePlaced to be set
	 */
	void setNbArmiesToBePlaced(int nbArmiesToBePlaced);
	
	/**
	 * Returns the number of armies to be placed
	 * @return the nbArmiesToBePlaced
	 */
	int getNbArmiesToBePlaced();
	
	/**
	 * Add a territory to the list of territories owned
	 * @param name the territory to be added
	 */
	void addTerritory(String name);
	
	/**
	 * Removes a territory from the list of territories owned
	 * @param name the territory to be removed
	 */
	void removeTerritory(String name);
	
	/**
	 * Fortify the destination by moving armies from the origin
	 * @param origin The origin territory
	 * @param destination The destination territory
	 * @param armies The number of armies to be moved
	 * @return was the fortification successful or not
	 */
	public boolean fortify(String origin, String destination, int armies);
	
	
	/**
	 * Augments that mount of armies on a territory by 1 
	 * @param territory The territory to reinforce
	 * @return was the reinforcement successful or not
	 */
	public boolean reinforce(String territory);
	
	/**
	 * Augments that mount of armies on a territory by army 
	 * @param territory The territory to reinforce
	 * @param army nb of armies to reinforce the territory by
	 * @return was the reinforcement successful or not
	 */
	public boolean reinforce(String territory, int army);

	/**
	 * Increments the amount of armies to be placed by the given amount
	 * @param continentBonus the amount of bonus armies if the continent is conquered
	 */
	public void incrementArmiesBy(int continentBonus);
	
	/**
	 *  Returns the number of territories owned
	 * @return  the number of territories belonging to the player
	 */
	public int nbTerritoriesOwned();

	/**
	 * Returns a list of the territories owned
	 * @return a list of territories owned
	 */
	public ArrayList<String> getTerritoriesOwned();
	
	/**
	 *  Returns list of territories owned with  how many armies are on it.
	 * @return a list of territories owned with the amount of armies present on it
	 */
	public ArrayList<String> getTerritoriesOwnedWithArmies();
	
	
	/**
	 * Returns the Hand of the player
	 * @return the Hand of the player
	 */
	public Hand getHand();
	/**
	 * Checks if the player can perform a reinforce attack
	 * @return True /false
	 */
	public boolean canReinforce();
	
	/**
	 * Checks if the player can perform a fortify attack
	 * @return True /false
	 */
	public boolean canFortify();
	
	/**
	 * Return the type of the player
	 * @return type of the player model
	 */
	public RiskPlayerType getType();
	
	/**
	 * Perform a reinforcement
	 * @return reinforcement result message
	 */
	public String reinforce();
	
	/**
	 * Perform a fortification
	 * @return fortify result message
	 */
	public String fortify();
	
	
	/**
	 * check if an attack can be made 
	 * @return true/ false
	 */
	public boolean canAttack();
	
	/**
	 * Decides which country to attack 
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack and amount of armies to use(defender)
	 */
	public Tuple<String,Tuple<String,Integer>> attack();
	
	/**
	 * get The strategyType
	 * @return the strategy Type
	 */
	public Strategy getStrategy();

	/**
	 * Set The RiskBoard
	 * @param board the new RiskBoard
	 */
	public void setRiskBoard(RiskBoard board);
	

	
	/**
	 * Sets the hand
	 * @param hand The hand to be set
	 */
	public void setHand(Hand hand);

	/**
	 * Set the territory list
	 * @param territories the new list
	 */
	public void setTerritories(ArrayList<String> territories);

}
