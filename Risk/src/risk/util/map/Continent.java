package risk.util.map;

import java.util.ArrayList;
import java.util.HashMap;

import risk.model.ModelException;

/**
 * 
 * @author hcanta
 * This is the implementation of the continent class.
 */
public class Continent 
{
	
	private String continentName;
	private HashMap<String, Territory> territories;
	private int ownerID;
	private int continentBonus;

	public Continent(String name, int continentBonus) 
	{
		this.continentBonus = continentBonus;
		this.continentName = name;
		this.ownerID = Integer.MIN_VALUE;
		territories = new HashMap<String, Territory>();
	}
	
	/**
	 * 
	 * @param name country or territory the query is about
	 * @return True/False
	 */
	public boolean containsTerritory(String name)
	{
		String n_name = name.toLowerCase();
		return territories.containsKey(n_name);
	}
	
	
	/**
	 * 
	 * @param name  Territory searched
	 * @return The territory with the name passed
	 * @throws ModelException the territory is not part of this continent
	 */
	public Territory getTerritory(String name) throws ModelException
	{
		String n_name = name.toLowerCase();
		if(territories.containsKey(n_name))
		{
			return territories.get(n_name);
		}
		else
		{
			throw new ModelException("The territory is not part of this continent");
		}
	}
	
	/**
	 * 
	 * @param name The name of the territory to be added to the continent
	 * @param ownerID the ownerID /Player ID of the Owner
	 */
	public void addTerritory(String name, int ownerID)
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, new Territory(n_name,this.continentName, ownerID));
		}
	}
	
	/**
	 * 
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
	 * @return the name
	 */
	public String getContinentName() 
	{
		return continentName;
	}

	/**
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
	public boolean hasASingleOwner()
	{
		return this.ownerID != Integer.MIN_VALUE;
	}
	
	/**
	 *  Update the OwnerID if necessary
	 */
	public void checkOwnerStatus()
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
	 * 
	 * @param territory to be removed to the adjacency list.
	 */
	public void removeTerritory(String territory)
	{
		String n_territory = territory.toLowerCase();
		if(this.territories.containsKey(n_territory))
		{
			territories.get(n_territory).clear();
			this.territories.remove(n_territory);
		}
		
	}
	
	
	/**
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
	 * 
	 * @return  a string representation of the territories to be saved in a .map file
	 */
	public String territoriesToString() {
		StringBuffer str = new StringBuffer();
		
		for(String territory : this.territories.keySet())
		{
			str.append(this.territories.get(territory).territoriesToString());
		}
		str.append("\n");
		
		return str.toString();
	}
	
	/**
	 * 
	 * @param name The name of the territory to be added to the continent
	 * @param neighbours the neighbous to said territory
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
	 * 
	 * @return the list of territories belonging to that continent
	 */
	public ArrayList<String> getTerritories() {
		
		return new ArrayList<String>(territories.keySet());
	}

	public boolean validateContinent() {
		
		boolean valid = true;
		if(!(this.territories.keySet().size()>=1))
			return false;
		for(String territory : this.territories.keySet())
		{
			valid = this.territories.get(territory).validateTerritory();
			if(!valid)
				return false;
		}
		return valid;
	}

	

}
