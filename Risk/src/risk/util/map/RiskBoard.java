package risk.util.map;

import java.util.HashMap;

import risk.model.ModelException;

public class RiskBoard 
{
	private int ownerID;
	private String boardName;
	private HashMap<String, Continent> continents;

	public RiskBoard() {

		ownerID = Integer.MIN_VALUE;
	}


	/**
	 * @return the ownerID
	 */
	public int getOwnerID() 
	{
		return ownerID;
	}


	/**
	 * @param ownerID the ownerID to set
	 */
	public void setOwnerID(int ownerID) 
	{
		this.ownerID = ownerID;
	}

	
	/**
	 * 
	 * @param name country or continent the query is about
	 * @return True/False
	 */
	public boolean containsContinent(String name)
	{
		String n_name = name.toLowerCase();
		return continents.containsKey(n_name);
	}
	
	
	/**
	 * 
	 * @param name  Continent searched
	 * @return The continent with the name passed
	 * @throws ModelException the continent is not part of this continent
	 */
	public Continent getContinent(String name) throws ModelException
	{
		String n_name = name.toLowerCase();
		if(continents.containsKey(n_name))
		{
			return continents.get(n_name);
		}
		else
		{
			throw new ModelException("The continent is not part of this continent");
		}
	}
	
	/**
	 * 
	 * @param name e name of the continent to be added to the board
	 * @param bonus Bonus of army associated with the continent
	 */
	public void addContinent(String name, int bonus)
	{
		String n_name = name.toLowerCase();
		if(!continents.containsKey(n_name))
		{
			continents.put(n_name, new Continent(n_name,bonus));
		}
	}
	
	/**
	 * 
	 * @return Is the game done or not;
	 */
	public boolean gameOver()
	{
		checkOwnerStatus();
		return this.ownerID != Integer.MIN_VALUE;
	}
	
	/**
	 *  Update the OwnerID if necessary
	 */
	public void checkOwnerStatus()
	{
		int id = continents.get(continents.keySet().toArray()[0]).getOwnerID();
		boolean update = true;
		for(String value: continents.keySet())
		{
			if (continents.get(value).getOwnerID()!= id)
			{
				update = false;
				break;
			}
		}
		
		if(update)
		{
			this.ownerID = id;
		}
	}


	/**
	 * @return the boardName
	 */
	public String getBoardName() 
	{
		return boardName;
	}


	/**
	 * @param boardName the boardName to set
	 */
	public void setBoardName(String boardName) 
	{
		this.boardName = boardName;
	}
	
	/**
	 * Empties the list of territories belonging to that 
	 */
	public void clear()
	{
		for(String value: continents.keySet())
		{
			continents.get(value).clear();
		}
		continents.clear();
	}
	
	/**
	 * 
	 * @param continent to be removed to the adjacency list.
	 */
	public void removeTerritory(String continent)
	{
		String n_continent = continent.toLowerCase();
		if(this.continents.containsKey(n_continent))
		{
			continents.get(n_continent).clear();
			this.continents.remove(n_continent);
		}
		
	}

}
