/**
 *  The risk.model.maputils package contains the class implementing the territory The riskBoard and the continent classes
 */
package risk.model.maputils;

import java.util.ArrayList;
import java.util.HashMap;

import risk.utils.Utils;
import risk.utils.constants.RiskIntegers;

/**
 * This is the implementation of the Continent class, a continent continent contains territory.
 * @author hcanta
 * @version 4.1`
 */
public class Continent extends BoardComponent
{
	
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -2804669608728063119L;

	/**
	 * The name of the continent
	 */
	private String continentName;
	/**
	 * HashMap Containing a mapping of name to Territory object
	 */
	private HashMap<String, Territory> territories;
	/**
	 * The current owner of the Continent
	 */
	private int ownerID;
	/**
	 * The continent bonus
	 */
	private int continentBonus;
	
	/**
	 * Constructor of the continent.
	 * @param name the name of the continent
	 * @param continentBonus The associated bonus with the continent
	 */
	public Continent(String name, int continentBonus) 
	{
		this.continentBonus = continentBonus;
		this.continentName = name;
		this.ownerID = RiskIntegers.INITIAL_OWNER;
		territories = new HashMap<String, Territory>();
	}
	
	
	
	/**
	 * Verifies if the territory belongs to the continent
	 * @param name country or territory the query is about
	 * @return True/False
	 */
	public boolean containsTerritory(String name)
	{
		String n_name = name.toLowerCase();
		return territories.containsKey(n_name);
	}
	
	
	/**
	 * Returns a territory object or null if said territory is not part of the continent
	 * @param name  Territory searched
	 * @return The territory with the name passed or null if said territory can t be found
	 */
	public Territory getTerritory(String name) 
	{
		String n_name = name.toLowerCase();
		if(territories.containsKey(n_name))
		{
			return territories.get(n_name);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Add a territory to the Continent
	 * @param name The name of the territory to be added to the continent
	 * @param xCoord the x coordinate of the territory on the graph
	 * @param yCoord the y coordinate of the territory on the graph
	 */
	public void addTerritory(String name, int xCoord, int yCoord)
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, new Territory(n_name,this.continentName,xCoord,yCoord));
		}
	}
	
	/**
	 * Add a territory to the Continent
	 * @param name  The name of the territory to be added to the continent
	 * @return was the territory added
	 */
	public boolean addTerritory(String name)
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, new Territory(n_name,this.continentName, 0, 0));
			return true;
		}
		return false;
	}
	
	/**
	 * Add a territory to the continent
	 * @param name The name of the territory to be added to the continent
	 * @param neighbours the neighbors to said territory
	 */
	public void addTerritory(String name, String[] neighbours) 
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, new Territory(n_name,this.continentName, neighbours, 0, 0));
		}
		
	}
	
	/**
	 * Add a territory to the continent
	 * @param name The name of the territory to be added to the continent
	 * @param neighbours the neighbors to said territory
	 * @param xCoord the x coordinate of the territory on the graph
	 * @param yCoord the y coordinate of the territory on the graph
	 */
	public void addTerritory(String name, String[] neighbours,int xCoord, int yCoord)
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, new Territory(n_name,this.continentName, neighbours,xCoord,yCoord));
		}
		
	}

	/**
	 * Returns the current owner of the continent
	 * @return the ownerID
	 */
	public int getOwnerID() 
	{
		checkOwnerStatus();
		return ownerID;
	}

	/**
	 * Set the current owner of the continent
	 * @param ownerID the ownerID to set
	 */
	public void setOwnerID(int ownerID) 
	{
		this.ownerID = ownerID;
	}

	/**
	 * Return the name of the continent
	 * @return the name of the continent
	 */
	public String getContinentName() 
	{
		return continentName;
	}

	/**
	 * Get the Bonus associated with the continent
	 * @return the continentBonus
	 */
	public int getContinentBonus() 
	{
		return continentBonus;
	}
	
	/**
	 * Is the continent owned by a player
	 * @return True/False
	 */
	public boolean isOwned()
	{
		return this.ownerID != RiskIntegers.INITIAL_OWNER;
	}
	
	/**
	 * Creates a string representation of the Continent
	 * @return a string representation of the continent
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append("\nContinent: " + this.continentName+ "\n\n");
		for(String territory : this.territories.keySet())
		{
			str.append(this.territories.get(territory).toString());
		}
		str.append("\n");
		
		return str.toString();
	}
	
	/**
	 * Creates a string representation of the Continent and its territories to be saved in a .map file
	 * @return  a string representation of the territories to be saved in a .map file
	 */
	public String territoriesToString() 
	{
		StringBuffer str = new StringBuffer();
		
		for(String territory : this.territories.keySet())
		{
			str.append(this.territories.get(territory).territoriesToString());
		}
		str.append("\n");
		
		return str.toString();
	}
	
	
	
	/**
	 * Returns a copy of the list of continents belonging to that continent
	 * @return the list of territories belonging to that continent
	 */
	public ArrayList<String> getTerritories() 
	{
		return new ArrayList<String>(territories.keySet());
	}
	
	/**
	 * Empties the list of territories belonging to that 
	 */
	public void clear()
	{
		for(String value: territories.keySet())
		{
			territories.get(value).clear();
		}
		territories.clear();
	}
	
	/**
	 * Removes a territory from the continent if it exists
	 * @param territory to be removed to the adjacency list.
	 * @return was the territory removed or not
	 */
	public boolean removeTerritory(String territory)
	{
		String n_territory = territory.toLowerCase();
		if(this.territories.containsKey(n_territory))
		{
			territories.get(n_territory).clear();
			this.territories.remove(n_territory);
			return true;
		}
		return false;
	}
	
	/**
	 * This method checks if we re dealing with a valid continent or not
	 * @return True/False Is the continent valid or not
	 */
	public boolean validateContinent() 
	{
		boolean valid = true;
		if(!(this.territories.keySet().size()>=1))
			return false;
		
		for(String territory : this.territories.keySet())
		{
			valid = this.territories.get(territory).validateTerritory();
			if(!valid)
			{
				return false;
			}
		}
		int[][] matrix = new int [territories.keySet().size()][territories.keySet().size()];
		for(int i =0; i<territories.keySet().size(); i++ )
		{
			for(int j =0; j<territories.keySet().size(); j++ )
			{
				if(i!=j)
				{
					String neighbour = (String) territories.keySet().toArray()[j];
					if(territories.get(territories.keySet().toArray()[i]).getNeighbours().contains(neighbour))
					{
						matrix[i][j] = 1;
					}
				}
			}
		}
		valid = Utils.performTraversal(matrix)==territories.keySet().size();
		return valid;
	}
	
	/**
	 *  Update the OwnerID if necessary
	 */
	private void checkOwnerStatus()
	{
		int id = territories.get(territories.keySet().toArray()[0]).getOwnerID();
		boolean update = true;
		for(String value: territories.keySet())
		{
			if (territories.get(value).getOwnerID()!= id)
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
	 * Adds a territory to the continent
	 * @param name The name of the territory
	 * @param territory the territory
	 */
	public void addTerritory(String name, Territory territory) 
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, territory);
		}
		
	}
}
