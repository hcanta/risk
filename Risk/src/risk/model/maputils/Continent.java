/**
 *  The risk.model.maputils package contains the class implementing the territory The riskBoard and the continent classes
 */
package risk.model.maputils;

import java.util.ArrayList;
import java.util.HashMap;

import com.mxgraph.view.mxGraph;

import risk.utils.constants.RiskIntegers;

/**
 * This is the implementation of the Continent class, a continent continent contains territory.
 * @author hcanta
 */
public class Continent 
{
	/**
	 * The graph that will be displayed
	 */
	private mxGraph graph;
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
	 * @param graph the graph that will be displayed
	 */
	public Continent(String name, int continentBonus,  mxGraph graph) 
	{
		this.continentBonus = continentBonus;
		this.continentName = name;
		this.ownerID = RiskIntegers.INITIAL_OWNER;
		territories = new HashMap<String, Territory>();
		this.graph = graph;
	}
	
	/**
	 * Constructor of the continent.
	 * @param name the name of the continent
	 * @param continentBonus The associated bonus with the continent
	 */
	public Continent(String name, int continentBonus) 
	{
		this(name, continentBonus, null);
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
			territories.put(n_name, new Territory(n_name,this.continentName,this.graph,xCoord,yCoord));
		}
	}
	
	/**
	 * Add a territory to the Continent
	 * @param name  The name of the territory to be added to the continent
	 */
	public void addTerritory(String name)
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, new Territory(n_name,this.continentName));
		}
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
			territories.put(n_name, new Territory(n_name,this.continentName, neighbours));
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
			territories.put(n_name, new Territory(n_name,this.continentName, neighbours,this.graph,xCoord,yCoord));
		}
		
	}

	/**
	 * Returns the current owner of the continent
	 * @return the ownerID
	 */
	public int getOwnerID() 
	{
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


}
