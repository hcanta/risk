/**
 * The risk.model.maputils package contains the class implementing the territory The riskBoard and the continent classes
 */
package risk.model.maputils;

import java.util.ArrayList;
import java.util.HashMap;

import com.mxgraph.view.mxGraph;

import risk.utils.constants.RiskIntegers;

/**
 * This class implements the RiskBoard it contains all the elements of the board. continents and territories
 * @author hcanta
 */
public class RiskBoard 
{
	/**
	 * The graph that will be displayed
	 */
	private mxGraph graph;
	
	/**
	 * The Owner of the Board
	 */
	private int ownerID;
	
	/**
	 * The name of the current Board/map
	 */
	private String boardName;
	/**
	 * HashMap Containing a mapping of name to continent object
	 */
	private HashMap<String, Continent> continents;
	
	/**
	 * Singleton Of the RisBoard Object
	 */
	private static RiskBoard Instance = new RiskBoard(new mxGraph());
	
	/**
	 * Singleton Of the RisBoard Object for testing purposes
	 */
	private static RiskBoard TestInstance = new RiskBoard();
	
	/**
	 * Returns the Proper Riskboard one for play or the one for testing/debugging
	 * @param debug set to true for debugging or testing
	 * @return The proper risboard
	 */
	public static RiskBoard ProperInstance(boolean debug)
	{
		if(debug)
		{
			return TestInstance;
		}
		else
		{
			return Instance;
		}
	}

	/**
	 * Constructor of the Risk Board
	 * @param mxGraph the graph that will be displayed
	 */
	public RiskBoard(mxGraph mxGraph) 
	{
		ownerID = RiskIntegers.INITIAL_OWNER;
		continents = new HashMap<String, Continent>();
		this.graph = mxGraph;
	}
	
	/**
	 * Constructor of the Risk Board mainly for debugging
	 */
	public RiskBoard() 
	{
		this(null);
	}


	/**
	 * Returns the Owner ID Of the Board
	 * @return the ownerID
	 */
	public int getOwnerID() 
	{
		return ownerID;
	}


	/**
	 * Set The OwnerID of the bOARD
	 * @param ownerID the ownerID to set
	 */
	public void setOwnerID(int ownerID) 
	{
		this.ownerID = ownerID;
	}

	
	/**
	 * Verifies if the Continent exists on the board
	 * @param name country or continent the query is about
	 * @return True/False
	 */
	public boolean containsContinent(String name)
	{
		String n_name = name.toLowerCase();
		return continents.containsKey(n_name);
	}
	
	
	/**
	 * Returns The continent Object associated with the name if it exists null otherwise
	 * @param name  Continent searched
	 * @return The continent with the name passed
	 * @throws ModelException the continent is not part of this continent
	 */
	public Continent getContinent(String name)
	{
		String n_name = name.toLowerCase();
		if(continents.containsKey(n_name))
		{
			return continents.get(n_name);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns a copy of the list of continents belonging to the board
	 * @return A list of name of all the continents.
	 */
	public ArrayList<String> getContinents()
	{
		return new ArrayList<String>(this.continents.keySet());
	}
	
	/**
	 * Add a continent to the board based if it doesn t already exist
	 * @param name e name of the continent to be added to the board
	 * @param bonus Bonus of army associated with the continent
	 */
	public void addContinent(String name, int bonus)
	{
		String n_name = name.toLowerCase();
		if(!continents.containsKey(n_name))
		{
			continents.put(n_name, new Continent(n_name,bonus, graph));
		}
	}
	
	/**
	 * Check if the game is Over or Not
	 * @return Is the game done or not;
	 */
	public boolean gameOver()
	{
		return this.ownerID != Integer.MIN_VALUE;
	}
	
	
	/**
	 * Returns the name of the board
	 * @return the boardName
	 */
	public String getBoardName() 
	{
		return boardName;
	}


	/**
	 * Set the name of the board
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
	 * Removes the mentioned continent if it exists
	 * @param continent to be removed from the map
	 */
	public void removeContinent(String continent)
	{
		String n_continent = continent.toLowerCase();
		if(this.continents.containsKey(n_continent))
		{
			continents.get(n_continent).clear();
			this.continents.remove(n_continent);
		}
		
	}
	
	/**
	 * Creates a string representation of the Board
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
	 * Creates a string representation of the Continents and its territories to be saved in a .map file
	 * @return a string representation of the continents to be saved in a .map file
	 */
	public String continentsToString()
	{
		StringBuffer str = new StringBuffer();
		for(String continent : this.continents.keySet())
		{
			str.append(this.continents.get(continent).getContinentName()+"="+this.continents.get(continent).getContinentBonus()+"\n");
		}
		return str.toString();
	}
	/**
	 * Returns a string representation of the territories to be saved in a .map file
	 * @return  a string representation of the territories to be saved in a .map file
	 */
	public String territoriesToString() {
		
		StringBuffer str = new StringBuffer();
		for(String continent : this.continents.keySet())
		{
			str.append(this.continents.get(continent).territoriesToString());
		}
		str.append("\n");
		return str.toString();
	}
	
	/**
	 * Add a territory to the Board
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
	 * Add a territory to the Board
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @param xcoord The x coordinate of the territory
	 * @param ycoord the y coordinate of the territory
	 */
	public void addTerritory(String continentName, String territoryName,int xcoord, int ycoord)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			continents.get(c_name).addTerritory(t_name, xcoord, ycoord);
		}
	}
	
	/**
	 * Add a territory to the board
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @param neighbours the list of neighbors of said territory
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
	 * Add a territory to the board
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @param neighbours the list of neighbors of said territory
	 * @param xcoord The x coordinate of the territory
	 * @param ycoord the y coordinate of the territory 
	 */
	public void addTerritory(String continentName, String territoryName, String[] neighbours,int xcoord, int ycoord)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			continents.get(c_name).addTerritory(t_name, neighbours, xcoord, ycoord);
		}
	}
	
	/**
	 * Returns a List containing the territories present on the board
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

	/**
	 * Returns a territory object that matches the string passed. null if it can t be found
	 * @param territory we re searching for
	 * @return the territory
	 */
	public Territory getTerritory(String territory) 
	{
		for(String continent : this.continents.keySet())
		{
			if(continents.get(continent).getTerritories().contains(territory))
			{
				return continents.get(continent).getTerritory(territory);
			}
		}
		return null;
	}
	

	/**
	 * This method returns the number of territories
	 * @return the number of Territories
	 */
	public int getNbTerritories() 
	{
		return getTerritories().size();
	}
}
