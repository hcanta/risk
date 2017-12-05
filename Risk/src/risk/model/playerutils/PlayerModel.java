/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils;

import java.util.ArrayList;

import risk.game.cards.Hand;
import risk.model.RiskBoard;
import risk.model.maputils.Territory;
import risk.utils.Tuple;
import risk.utils.constants.RiskEnum;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;

/**
 * Implementation of the player model
 * @author hcanta
 * @version 3.3
 */
public class PlayerModel implements IPlayer
{
	
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -7159997220981862139L;

	/**
	 * The strategy
	 */
	protected Strategy strategy;

	/**
	 * The board
	 */
	protected  RiskBoard board;
	/**
	 * The type of the player
	 */
	private RiskPlayerType type;
	/**
	 * The hand of the player
	 */
	private Hand hand;
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
	 * Constructor for the Player Model
	 * @param name the name of the player
	 * @param color the color of the player
	 * @param turnID the turn of the player
	 * @param type the type of player
	 * @param strategy the strategy being used
	 */
	public PlayerModel(String name, PlayerColors color, short turnID, RiskPlayerType type, RiskEnum.Strategy strategy) 
	{
		this.hand = new Hand();
		this.playerName=name;
		this.turnID = turnID;
		this.color = color;
		this.nbArmiesToBePlaced = 0;
		territoriesOwned = new ArrayList<String>();
		this.type = type;
		this.board = RiskBoard.Instance;
		this.strategy = strategy;
		
	}
	
	/**
	 * Constructor for the Player Model this constructor is used to create computer players
	 * @param color the color of the player
	 * @param turnID the turn of the player
	 * @param type the type of the player
	 */
	public PlayerModel(PlayerColors color, short turnID, RiskPlayerType type) 
	{
		 this("Computer Player", color,turnID, type,  RiskEnum.Strategy.random) ;
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
	public short getPlayerID() {
		
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
			if(!board.getTerritory(origin).getNeighbours().contains(destination))
			{
				return false;
			}
			int armyOn1 = board.getTerritory(origin).getArmyOn();
			int armyOn2 = board.getTerritory(destination).getArmyOn();
			if( armyOn1 > armies)
			{
				board.getTerritory(origin).setArmyOn(armyOn1 - armies);
				board.getTerritory(destination).setArmyOn(armyOn2 + armies);
				return true;
			}
			
		}
		return false;
		
	}


	/**
	 * Augments that mount of armies on a territory by 1 Player Model Implementation
	 * @param territory The territory to reinforce
	 * @return was the reinforcement successful or not
	 */
	@Override
	public boolean reinforce(String territory) {
		if(this.territoriesOwned.contains(territory.toLowerCase().trim()) )
		{
			int armyOn = board.getTerritory(territory).getArmyOn();
		
			if( this.nbArmiesToBePlaced > 0)
			{
				board.getTerritory(territory).setArmyOn(armyOn + 1);
				this.nbArmiesToBePlaced --;
				return true;
				
			}
		}
		return false;
		
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
			array.add(this.territoriesOwned.get(i)+" "+board.getTerritory(this.territoriesOwned.get(i)).getArmyOn());
		}
		return array;
	}

	

	/**
	 * Augments that mount of armies on a territory by army . Player Model Implementation
	 * @param territory The territory to reinforce
	 * @param army number of armies to reinforce the territory by
	 * @return was the reinforcement successful or not
	 */
	@Override
	public boolean reinforce(String territory, int army) {
		if(this.territoriesOwned.contains(territory.toLowerCase().trim()) )
		{
			int armyOn = board.getTerritory(territory).getArmyOn();
		
			if( this.nbArmiesToBePlaced >= army)
			{
				board.getTerritory(territory).setArmyOn(armyOn + army);
				this.nbArmiesToBePlaced -= army;
				return true;
				
			}
		}
		return false;
		
	}

	/**
	 * Returns the hand of the player, PlayerModel Implementation
	 * @return the hand of the player
	 */
	@Override
	public Hand getHand(){
		
		return this.hand;
	}

	/**
	 * Checks if the player can perform a reinforce attack. Player Model Implementation
	 * @return True /false
	 */
	@Override
	public boolean canReinforce() {
		return this.nbArmiesToBePlaced > 0 && this.territoriesOwned.size() >0;
	}

	/**
	 * checks if the player can perform a fortify attack. Player Model Implementation
	 * return true false
	 */
	@Override
	public boolean canFortify() {
		Territory territory;
		String neighbor;
		if( this.territoriesOwned.size() >1)
		{
			for(int i=0; i< this.territoriesOwned.size(); i++)
			{
				territory = board.getTerritory(territoriesOwned.get(i));
				for(int j =0; j< territory.getNeighbours().size(); j++)
				{
					neighbor = territory.getNeighbours().get(j);
					if(this.territoriesOwned.contains(neighbor))
					{
						if(territory.getArmyOn() > 1 || board.getTerritory(neighbor).getArmyOn() > 1)
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Return the type of the player. Player model implementation
	 * @return type of the player model
	 */
	@Override
	public RiskPlayerType getType() {
		return this.type;
	}

	/**
	 * Perform a reinforcement To be fully implemented on BotModel
	 * @return reinforcement result message
	 */
	@Override
	public String reinforce() 
	{
		return "";
	}

	/**
	 * Perform a fortification To be fully implemented on BotModel
	 * @return fortify result message
	 */
	@Override
	public String fortify() 
	{
		return "";
	}

	
	/**
	 * Perform a check on whether we can attack or not
	 * @return true/ false
	 */
	@Override
	public boolean canAttack() {
		for(int i =0; i < this.getTerritoriesOwned().size(); i++)
		{
			String territory =  this.getTerritoriesOwned().get(i);
			for(int  j =0; j <board.getTerritory(territory).getNeighbours().size(); j++)
			{
				String neighbor = board.getTerritory(territory).getNeighbours().get(j);
				if(board.getTerritory(territory).getArmyOn() > 1 && board.getTerritory(neighbor).getOwnerID() != this.turnID)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Decides which country to attack 
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack and amount of armies to use(defender)
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> attack() 
	{
		return null;
	}

	/**
	 * get The strategyType. Player model implementation
	 * @return the strategy Type
	 */
	@Override
	public Strategy getStrategy() 
	{
		return this.strategy;
	}

	/**
	 * Set The RiskBoard
	 * @param board the new RiskBoard
	 */
	@Override
	public void setRiskBoard(RiskBoard board) {
		this.board  = board;
		
	}



	/**
	 * Set the hand
	 * @param hand the new hand
	 */
	@Override
	public void setHand(Hand hand) {
		this.hand = hand;
		
	}
	/**
	 * Set the territory list
	 * @param territories the new list
	 */
	@Override
	public void setTerritories(ArrayList<String> territories) {
		this.territoriesOwned =  territories;
		
	}

}
