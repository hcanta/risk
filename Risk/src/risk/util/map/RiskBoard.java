package risk.util.map;

import java.util.ArrayList;
import java.util.HashMap;

import risk.model.ModelException;

public class RiskBoard 
{
	private int ownerID;
	private String boardName;
	private HashMap<String, Continent> continents;
	
	public static RiskBoard Instance = new RiskBoard();

	public RiskBoard() {

		ownerID = Integer.MIN_VALUE;
		continents = new HashMap<String, Continent>();
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
			continents.get(value).checkOwnerStatus();
			
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
		this.ownerID = Integer.MIN_VALUE;
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
	
	/**
	 * @return a string representation of the Board
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append("\nBoard: " + this.boardName+ "\n ");
		for(String continent : this.continents.keySet())
		{
			str.append(this.continents.get(continent).toString());
		}
		str.append("\n");
		
		return str.toString();
	}
	
	/**
	 * 
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 */
	public void addTerritory(String continentName, String territoryName)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			continents.get(c_name).addTerritory(t_name);
		}
	}
	
	/**
	 * 
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @param neighbours the list of neighbours of said territory
	 */
	public void addTerritory(String continentName, String territoryName, String[] neighbours)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			continents.get(c_name).addTerritory(t_name, neighbours);
		}
	}
	
	/**
	 * 
	 * @return all the territories name on the board
	 */
	public ArrayList<String>getTerritories()
	{
		ArrayList<String> countries = new ArrayList<String>();
		for(String continent : this.continents.keySet())
		{
			countries.addAll(this.continents.get(continent).getTerritories());
		}
		
		return countries;
		
	}
}
