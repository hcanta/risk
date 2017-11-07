/**
 * * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils;

import java.util.ArrayList;

import risk.utils.constants.RiskEnum.PlayerColors;

/**
 * This interface contains the function that a player Human or Not must implement
 * @author hcanta
 * @version 1
 */
public interface IPlayer 
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
	public short getTurnID();
	
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
	 */
	public void fortify(String origin, String destination, int armies);
	
	
	/**
	 * Augments that mount of armies on a territory by 1 
	 * @param territory The territory to reinforce
	 */
	public void reinforce(String territory);
	
	/**
	 * Augments that mount of armies on a territory by army 
	 * @param territory The territory to reinforce
	 * @param army nb of armies to reinforce the territory by
	 */
	public void reinforce(String territory, int army);

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
	 * Exchanges cards own by a player for armies to be added
	 */
	public void exchangeCards();

}
