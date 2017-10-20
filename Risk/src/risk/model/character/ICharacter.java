package risk.model.character;

import java.util.ArrayList;

import risk.util.RiskEnum.RiskColor;
/**
 * 
 * The player Model and later on Robot model will implement this
 *
 */
public interface ICharacter 
{
	/**
	 * 
	 * @return the color of the character
	 */
	public RiskColor getColor();
	
	/**
	 * 
	 * @return the name of the character
	 */
	public String getName();
	
	/**
	 * Is it the players turn
	 * @return True/False
	 */
	public boolean isTurn();
	
	
	/**
	 * 
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
	 * @param nbArmiesToBePlaced the nbArmiesToBePlaced to be set
	 */
	void setNbArmiesToBePlaced(int nbArmiesToBePlaced);
	
	/**
	 * @return the nbArmiesToBePlaced
	 */
	int getNbArmiesToBePlaced();
	
	/**
	 * @param name the territory to be added
	 */
	void addTerritory(String name);
	
	/**
	 * @param name the territory to be removed
	 */
	void removeTerritory(String name);
	
	/**
	* @param territory1 The origin territory
	 * @param territory2 The destination territory
	 * @param armies The number of armies to be moved
	 */
	public void fortify(String territory1, String territory2, int armies);
	
	
	/**
	 * 
	 * @param territory The territory to reinforce
	 */
	public void reinforce(String territory);

	/**
	 * Increments the amount of armies to be placed by the given amount
	 * @param continentBonus the amount of bonus armies if the continent is conquered
	 */
	public void incrementArmiesBy(int continentBonus);
	
	/**
	 * 
	 * @return  the number of territories belonging to the player
	 */
	public int nbTerritoriesOwned();

	/**
	 * 
	 * @return a list of territories owned
	 */
	public ArrayList<String> getTerritoriesOwned();
	
	/**
	 * 
	 * @return a list of territories owned with the amount of armies present on it
	 */
	public ArrayList<String> getTerritoriesOwnedWithArmies();
}
