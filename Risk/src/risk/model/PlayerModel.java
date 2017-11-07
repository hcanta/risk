/**
 * The model package holds the driver and the Game Engine
 */
package risk.model;

import java.util.ArrayList;

import risk.model.playerutils.IPlayer;
import risk.utils.constants.RiskEnum.PlayerColors;

/**
 * Implementation of the player model
 * @author hcanta
 * @version 2.0
 */
public class PlayerModel implements IPlayer
{

	/**
	 * The Player Name
	 */
	public String playerName;

	/**
	 * The turnID of the Player
	 */
	private short turnID;

	/**
	 * The color of the player
	 */
	private PlayerColors color;
	
	/**
	 * The Number of armies available to be placed
	 */
	private int nbArmiesToBePlaced;
	/**
	 * List of territories owned by the player
	 */
	
	private ArrayList<String>territoriesOwned;
	
	/**
	 * Set to true if we re debugging or not
	 */
	private boolean debug;
	/**
	 * Constructor for the Player Model
	 * @param name the name of the player
	 * @param color the color of the player
	 * @param turnID the turn of the player
	 * @param debug set to true for debugging or testing
	 */
	public PlayerModel(String name, PlayerColors color, short turnID, boolean debug) 
	{
		this.playerName=name;
		this.turnID = turnID;
		this.color = color;
		this.nbArmiesToBePlaced = 0;
		territoriesOwned = new ArrayList<String>();
		this.debug = debug;
	}
	
	/**
	 * Constructor for the Player Model this constructor is used to create computer players
	 * @param color the color of the player
	 * @param turnID the turn of the player
	 * @param debug set to true for debugging or testing
	 */
	public PlayerModel(PlayerColors color, short turnID, boolean debug) 
	{
		 this("Computer Player", color,turnID, debug) ;
	}

	/** 
	 * Returns the color of the player Player Model implementation
	 * @return the color of the character
	 */
	@Override
	public PlayerColors getColor() {
		
		return this.color;
	}

	/**
	 * Returns the name  of the player Player Model implementation
	 * @return the name of the character
	 */
	@Override
	public String getName() {
		
		return this.playerName;
	}

	/**
	 * Returns the Id of the player whose turn it is. Player Model implementation
	 * @return the player Id
	 */
	@Override
	public short getTurnID() {
		
		return this.turnID;
	}

	/**
	 * Increments the number of armies to be placed by the given number Player Model implementation
	 * @param nb number of armies to be placed
	 */
	@Override
	public void updateArmiestoBeplaced(int nb) {
		this.nbArmiesToBePlaced += nb;
		
	}
	/**
	 * Increments the amount of armies to be placed Player Model Implementation
	 */
	@Override
	public void incrementArmies() {
		this.nbArmiesToBePlaced ++;
		
	}
	/**
	 * Decrements the amount of armies to be placed Player Model Implementation
	 */
	@Override
	public void decrementArmies() {
		if(this.nbArmiesToBePlaced > 0)
		{
			this.nbArmiesToBePlaced--;
		}
		
	}

	/**
	 * Set the Number of Armies to be Placed. Player Model Implementation
	 * @param nbArmiesToBePlaced the nbArmiesToBePlaced to be set
	 */
	@Override
	public void setNbArmiesToBePlaced(int nbArmiesToBePlaced) {
		this.nbArmiesToBePlaced = nbArmiesToBePlaced;
		
	}
	/**
	 * Returns the number of armies to be placed. Player Model Implementation
	 * @return the nbArmiesToBePlaced
	 */
	@Override
	public int getNbArmiesToBePlaced() {
		return nbArmiesToBePlaced;
	}

	/**
	 * Add a territory to the list of territories owned. Player Model implementation
	 * @param name the territory to be added
	 */
	@Override
	public void addTerritory(String name) {
		if(!this.territoriesOwned.contains(name))
			this.territoriesOwned.add(name);
		
	}

	/**
	 * Removes a territory from the list of territories owned. Player Model Implementation
	 * @param name the territory to be removed
	 */
	@Override
	public void removeTerritory(String name) {
		if(this.territoriesOwned.contains(name))
			this.territoriesOwned.remove(name);
		
	}

	/**
	 * Fortify the destination by moving armies from the origin. Player Model implementation
	 * @param origin The origin territory
	 * @param destination The destination territory
	 * @param armies The number of armies to be moved
	 * @return was the fortification successful or not
	 */
	@Override
	public boolean fortify(String origin, String destination, int armies) {
		if(this.territoriesOwned.contains(origin.toLowerCase().trim()) && 
				this.territoriesOwned.contains(destination.toLowerCase().trim()))
		{
			int armyOn1 = RiskBoard.ProperInstance(debug).getTerritory(origin).getArmyOn();
			int armyOn2 = RiskBoard.ProperInstance(debug).getTerritory(destination).getArmyOn();
			if( armyOn1 > armies)
			{
				RiskBoard.ProperInstance(debug).getTerritory(origin).setArmyOn(armyOn1 - armies);
				RiskBoard.ProperInstance(debug).getTerritory(destination).setArmyOn(armyOn2 + armies);
				return true;
			}
			
		}
		return false;
		
	}


	/**
	 * Augments that mount of armies on a territory by 1 Player Model Implementation
	 * @param territory The territory to reinforce
	 */
	@Override
	public void reinforce(String territory) {
		if(this.territoriesOwned.contains(territory.toLowerCase().trim()) )
		{
			int armyOn = RiskBoard.ProperInstance(debug).getTerritory(territory).getArmyOn();
		
			if( this.nbArmiesToBePlaced > 0)
			{
				RiskBoard.ProperInstance(debug).getTerritory(territory).setArmyOn(armyOn + 1);
				this.nbArmiesToBePlaced --;
				
			}
		}
		
	}

	/**
	 * Increments the amount of armies to be placed by the given amount Player Model Implementation
	 * @param continentBonus the amount of bonus armies if the continent is conquered
	 */
	@Override
	public void incrementArmiesBy(int continentBonus) 
	{
		this.nbArmiesToBePlaced+=continentBonus;	
	}

	/**
	 *  Returns the number of territories owned. Player Model Implementation
	 * @return  the number of territories belonging to the player
	 */
	@Override
	public int nbTerritoriesOwned() 
	{
		return this.territoriesOwned.size();
	}

	/**
	 * Returns a list of the territories owned. Player Model Implementation
	 * @return a list of territories owned
	 */
	@Override
	public ArrayList<String> getTerritoriesOwned() 
	{
		return new ArrayList<String>(this.territoriesOwned);
	}

	/**
	 *  Returns list of territories owned with  how many armies are on it. Player Model Implementation
	 * @return a list of territories owned with the amount of armies present on it
	 */
	@Override
	public ArrayList<String> getTerritoriesOwnedWithArmies() 
	{
		
		ArrayList<String> array = new ArrayList<String>();
		for(int i = 0; i< this.territoriesOwned.size(); i++)
		{
			array.add(this.territoriesOwned.get(i)+" "+RiskBoard.ProperInstance(debug).getTerritory(this.territoriesOwned.get(i)).getArmyOn());
		}
		return array;
	}

	/**
	 * Exchanges cards own by a player for armies to be added Player Model Implementation
	 */
	@Override
	public void exchangeCards() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Augments that mount of armies on a territory by army . Player Model Implementation
	 * @param territory The territory to reinforce
	 * @param army number of armies to reinforce the territory by
	 */
	@Override
	public void reinforce(String territory, int army) {
		if(this.territoriesOwned.contains(territory.toLowerCase().trim()) )
		{
			int armyOn = RiskBoard.ProperInstance(debug).getTerritory(territory).getArmyOn();
		
			if( this.nbArmiesToBePlaced >= army)
			{
				RiskBoard.ProperInstance(debug).getTerritory(territory).setArmyOn(armyOn + 1);
				this.nbArmiesToBePlaced -= army;
				
			}
		}
		
	}

}
