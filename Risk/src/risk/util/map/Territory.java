package risk.util.map;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * 
 * @author hcanta
 * This is the implementation of the Territory/ country class. 
 */
public class Territory 
{
	private String territoryName;
	private String continent;
	private ArrayList<String> neighbours;
	private int ownerID;
	private int armyOn;
	
	public Territory(String name, String continent, int ownerID) 
	{

		this.territoryName = name;
		this.continent = continent;
		this.neighbours = new ArrayList<String>();
		this.ownerID = ownerID;
		this.armyOn = 1;
	}

	public Territory(String name, String continent) 
	{

		this.territoryName = name;
		this.continent = continent;
		this.neighbours = new ArrayList<String>();
		this.ownerID = Integer.MIN_VALUE;
		this.armyOn = 1;
	}

	public Territory(String name, String continent, String[] neighbours) {
		this.territoryName = name;
		this.continent = continent;
		this.neighbours = new ArrayList<String>(Arrays.asList(neighbours));
		this.ownerID = Integer.MIN_VALUE;
		this.armyOn = 1;
	}

	/**
	 * @return the territory name
	 */
	public String getTerritoryName() 
	{
		return this.territoryName;
	}

	/**
	 * @return the continent
	 */
	public String getContinentName() 
	{
		return continent;
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
	 * @param neighbour new neighbour to be added to the adjacency list.
	 */
	public void addNeighbours(String neighbour)
	{
		String n_neighbour = neighbour.toLowerCase();
		if(!this.neighbours.contains(n_neighbour))
		{
			this.neighbours.add(n_neighbour);
		}
	}
	
	
	public ArrayList<String> getNeighbours()
	{
		//We use this instead of clone because the method clone misbehave at times.
		return new ArrayList<String>(this.neighbours);
	}

	/**
	 * get the amount of army on the territory
	 * @return the armyOn
	 */
	public int getArmyOn() {
		return armyOn;
	}

	/**
	 * set the amount of army on the territory
	 * @param armyOn the armyOn to set
	 */
	public void setArmyOn(int armyOn) 
	{
		this.armyOn = armyOn;
	}
	
	/**
	 * Clears the list of Neighbours
	 */
	public void clear()
	{
		this.neighbours.clear();
	}
	
	/**
	 * 
	 * @param neighbour to be removed to the adjacency list.
	 */
	public void removeNeighbours(String neighbour)
	{
		String n_neighbour = neighbour.toLowerCase();
		if(this.neighbours.contains(n_neighbour))
		{
			this.neighbours.remove(n_neighbour);
		}
		
	}
	
	/**
	 * @return A string representation of the Territory
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append(this.territoryName+ ": ");
		for(String neighbour : this.neighbours)
		{
			str.append(neighbour + ". ");
		}
		
		str.append("\n");
		
		return str.toString();
	}


}
