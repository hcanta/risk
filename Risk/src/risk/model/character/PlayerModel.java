package risk.model.character;

import java.util.ArrayList;
import java.util.Observable;

import org.apache.log4j.Logger;

import risk.model.ModelException;
import risk.util.RiskEnum.RiskColor;
import risk.util.map.RiskBoard;


/**
 * @author Ayushi Jain
 * @author hcanta
 *
 */

public class PlayerModel extends Observable implements ICharacter
{
	public String playerName;

	private short turnID;

	private RiskColor color;
	
	private int nbArmiesToBePlaced;
	
	private ArrayList<String>territoriesOwned;
	
	final static Logger logger = Logger.getLogger(PlayerModel.class);
	
	
	/**
	 * Constructor that initializes default values
	 */
	public PlayerModel() 
	{
		if (playerName == null) 
		{
			playerName = "Conquest Computer";
		}
	}
	
	
	/**
	 * Constructor that assigns its parameter to class attributes
	 * 
	 * @param new_playerName
	 *            Player name
	 */
	public PlayerModel(String new_playerName) 
	{
		playerName = new_playerName;
		setChanged();
		notifyObservers();
	}
	
	public PlayerModel(String name, RiskColor color, short turnID) 
	{
		this.playerName=name;
		this.turnID = turnID;
		this.color = color;
		territoriesOwned = new ArrayList<String>();
	}


	@Override
	public RiskColor getColor() 
	{
		
		return this.color;
	}

	@Override
	public String getName() 
	{
		
		return this.playerName;
	}


	@Override
	public boolean isTurn() 
	{
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public short getTurnID() 
	{
		
		return this.turnID;
	}


	/**
	 * @return the nbArmiesToBePlaced
	 */
	@Override
	public int getNbArmiesToBePlaced() {
		return nbArmiesToBePlaced;
	}


	/**
	 * @param nbArmiesToBePlaced the nbArmiesToBePlaced to set
	 */
	@Override
	public void setNbArmiesToBePlaced(int nbArmiesToBePlaced) {
		this.nbArmiesToBePlaced = nbArmiesToBePlaced;
	}
	
	/**
	 * Decrements the amount of armies
	 */
	@Override
	public void decrementArmies()
	{
		if(this.nbArmiesToBePlaced > 0)
		{
			this.nbArmiesToBePlaced--;
		}
	}
	
	/**
	 * Increments the amount of armies to be placed
	 */
	@Override
	public void incrementArmies()
	{
		this.nbArmiesToBePlaced ++;
	}
	
	/**
	 *  Increments the number of armies to be placed by the given number;
	 * @param nb
	 */
	@Override
	public void updateArmiestoBeplaced(int nb)
	{
		this.nbArmiesToBePlaced += nb;
	}
	
	/**
	 * @param name the territory to be added
	 */
	@Override
	public void addTerritory(String name)
	{
		if(!this.territoriesOwned.contains(name))
			this.territoriesOwned.add(name);
	}
	
	/**
	 * @param name the territory to be removed
	 */
	@Override
	public void removeTerritory(String name)
	{
		if(this.territoriesOwned.contains(name))
			this.territoriesOwned.remove(name);
	}
	/**
	 * @param territory1 The origin territory
	 * @param territory2 The destination territory
	 * @param armies The number of armies to be moved
	 * @throws Model Exception one or more of the parameter given is invalid
	 */
	@Override
	public void fortify(String territory1, String territory2, int armies) throws ModelException
	{
		if(this.territoriesOwned.contains(territory1.toLowerCase().trim()) && 
				this.territoriesOwned.contains(territory2.toLowerCase().trim()))
		{
			int armyOn1 = RiskBoard.Instance.getTerritory(territory1).getArmyOn();
			int armyOn2 = RiskBoard.Instance.getTerritory(territory2).getArmyOn();
			if( armyOn1 > armies)
			{
				RiskBoard.Instance.getTerritory(territory1).setArmyOn(armyOn1 - armies);
				RiskBoard.Instance.getTerritory(territory2).setArmyOn(armyOn2 + armies);
			}
			else
			{
				throw new ModelException("The amount of armies to be moved exceed the amount of amries present");
			}
	
		}
		else
		{
			throw new ModelException("One or more of the territory is not owned by the player");
		}
		
	}


	@Override
	public void reinforce(String territory) 
	{
		if(this.territoriesOwned.contains(territory.toLowerCase().trim()) )
		{
			int armyOn = RiskBoard.Instance.getTerritory(territory).getArmyOn();
		
			if( this.nbArmiesToBePlaced > 0)
			{
				RiskBoard.Instance.getTerritory(territory).setArmyOn(armyOn + 1);
				this.nbArmiesToBePlaced --;
				
			}
			else
			{
				throw new ModelException("There is no more armies to be placed");
			}
	
		}
		else
		{
			throw new ModelException("One or more of the territory is not owned by the player");
		}
		
	}


	@Override
	public void incrementArmiesBy(int continentBonus) 
	{
		this.nbArmiesToBePlaced+=continentBonus;
		
	}

	
}
