package risk.util.map;

import java.util.HashMap;

import risk.model.ModelException;

/**
 * 
 * @author hcanta
 * This is the implementation of the continent class.
 */
public class Continent 
{
	
	private String name;
	private HashMap<String, Territory> territories;
	private int ownerID;
	private int continentBonus;

	public Continent(String name, int continentBonus) 
	{
		this.continentBonus = continentBonus;
		this.name = name;
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
			territories.put(n_name, new Territory(n_name,this.name, ownerID));
		}
	}
	
	/**
	 * 
	 * @param name name The name of the territory to be added to the continent
	 */
	public void addTerritory(String name)
	{
		String n_name = name.toLowerCase();
		if(!territories.containsKey(n_name))
		{
			territories.put(n_name, new Territory(n_name,this.name));
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
	public String getName() 
	{
		return name;
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

}
