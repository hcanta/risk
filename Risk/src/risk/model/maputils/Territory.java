
/**
 * The risk.model.maputils package contains the class implementing the territory The riskBoard and the continent classes
 */
package risk.model.maputils;

import java.util.ArrayList;
import java.util.Arrays;

import risk.model.RiskBoard;
import risk.model.playerutils.IPlayer;
import risk.utils.constants.RiskIntegers;
import risk.views.ui.Vertex;

/**
 * This is the implementation of the Territory country class. 
 * @author hcanta
 * @version 4.1
 */
public class Territory extends BoardComponent 
{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 7319805127272223817L;
	
	/**
	 * The Vertex of the Graph
	 */
	private Vertex vertex;
	
	/**
	 * The name of the territory
	 */
	private String territoryName;
	/**
	 * The name of the continent to which the territory belongs
	 */
	private String continent;
	/**
	 * The list of neighbors to said territory
	 */
	private ArrayList<String> neighbours;
	
	/**
	 * The current owner of the territory
	 */
	private int ownerID;
	
	/**
	 * How many armies are presently on the territory
	 */
	private int armyOn;
	
	/**
	 * X coordinate on the graph
	 */
	private int xCoord;
	/**
	 * Y coordinate on the graph
	 */
	private int yCoord;
	
	
	/**
	 * Constructor for the territory class 
	 * @param name  The name of the territory
	 * @param continent The name of the continent to which the territory belongs
	 * @param neighbours The array of neighbors to said territory

	 * @param xcoord coordinate on the graph
	 * @param ycoord Y coordinate on the graph
	 */
	public Territory(String name, String continent, String[] neighbours, int xcoord, int ycoord) 
	{
		this.territoryName = name;
		this.continent = continent;
		this.neighbours = new ArrayList<String>(Arrays.asList(neighbours));
		this.ownerID = RiskIntegers.INITIAL_OWNER;
		this.armyOn = 1;
		this.setxCoord(xcoord);
		this.setyCoord(ycoord);			
		vertex = new Vertex(this.territoryName);
	}
	
	/**
	 * Constructor for the territory class 
	 * @param name  The name of the territory
	 * @param continent The name of the continent to which the territory belongs
	 * @param xcoord coordinate on the graph
	 * @param ycoord Y coordinate on the graph
	 */
	public Territory(String name, String continent, int xcoord, int ycoord) 
	{
		this(name, continent, new String[0], xcoord, ycoord);
	}

	
	/**
	 * Constructor for the territory class 
	 * @param name  The name of the territory
	 * @param continent The name of the continent to which the territory belongs
	 */
	public Territory(String name, String continent) 
	{
		this(name, continent, new String[0], 0, 0);
	}
	
	/**
	 * Returns the vertex representation on the graph
	 * @return the vertex presentation on the graph
	 */
	public Vertex getVertex() 
	{
		return vertex;
	}
	
	/**
	 * Returns the territory name
	 * @return the territory name
	 */
	public String getTerritoryName() 
	{
		return this.territoryName;
	}

	/**
	 * Returns the continent name to which this territory belongs
	 * @return the continent
	 */
	public String getContinentName() 
	{
		return continent;
	}


	/**
	 * returns the current owner of the territory
	 * @return the ownerID
	 */
	public int getOwnerID() 
	{
		return ownerID;
	}
	
	/**
	 * Set the current owner of the territory
	 * @param owner the owner 
	 */
	public void setOwnerID(IPlayer owner) 
	{
		this.ownerID = owner.getPlayerID();
		vertex.setOwner(owner.getColor());
		
	}
	
	/**
	 * Add a neighbor to the current territory list of neighbors
	 * @param neighbour new neighbor to be added to the adjacency list.
	 * @return was the neighbor properly added
	 */
	public boolean addNeighbours(String neighbour)
	{
		String n_neighbour = neighbour.toLowerCase();
		if(!this.neighbours.contains(n_neighbour))
		{
			this.neighbours.add(n_neighbour);
			vertex.setToolTipText(neighbours.toString());
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a copy of the list of neighbors of the current territory
	 * @return a copy of the list of neighbors
	 */
	public ArrayList<String> getNeighbours()
	{ 
		return new ArrayList<String>(this.neighbours);
	}

	/**
	 * get the amount of army on the current territory
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
		this.armyOn = armyOn < 0 ? 0 : armyOn;
	}
	
	
	/**
	 * Removes a neighbors from the current territory adjacency list if it exists
	 * @param neighbour to be removed to the adjacency list.
	 * @return was the neighbor removed
	 */
	public boolean removeNeighbours(String neighbour)
	{
		String n_neighbour = neighbour.toLowerCase();
		if(this.neighbours.contains(n_neighbour))
		{
			this.neighbours.remove(n_neighbour);
			return true;
		}
		return false;		
	}
	
	/**
	 * Creates a string representation of the territory
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
	
	/**
	 * Creates a string representation of the territory to be saved in a .map file
	 * @return  a string representation of the territories to be saved in a .map file
	 */
	public String territoriesToString() 
	{
		StringBuffer str = new StringBuffer();
		str.append(this.territoryName+",0,0,"+this.continent);
		for(String neighbour : this.neighbours)
		{
			str.append(","+neighbour);
		}
		str.append("\n");
		
		return str.toString();
	}

	/**
	 * The X coordinate of the territory
	 * @return the xCoord
	 */
	public int getxCoord() {
		return xCoord;
	}

	/**
	 * Set the X coordinate of the territory to be displayed on a graph
	 * @param xCoord the xCoord to set
	 */
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 *  The Y coordinate of the territory
	 * @return the yCoord
	 */
	public int getyCoord() {
		return yCoord;
	}

	/**
	 * Set the Y coordinate of the territory to be displayed on a graph
	 * @param yCoord the yCoord to set
	 */
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}
	
	/**
	 * Clears the list of Neighbors
	 */
	public void clear()
	{
		for(int i =0; i< this.neighbours.size(); i ++)
		{
			try
			{
				RiskBoard.Instance.getTerritory(this.neighbours.get(i)).removeNeighbours(this.territoryName);		
			}
			catch(Exception e){}
		}
		this.neighbours.clear();
	}
	
	/**
	 * This method checks if we re dealing with a valid territory or not
	 * @return True/False Is the territory valid or not
	 */
	public boolean validateTerritory() {
		if(this.neighbours.size()< 1)
			return false;
		for(int i = 0; i< this.neighbours.size(); i++)
		{
			if(RiskBoard.Instance.getTerritory(this.neighbours.get(i)) == null)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the basic info of the territory
	 * @return basic info of the territory Owner(0) info (1)
	 */
	public String[] basicInfo()
	{
		return new String[]{""+this.ownerID, " Name: "+this.territoryName +"\n Armies On: "+ this.armyOn+"\n Continent: "+this.continent };
	}
	
	/**
	 * check if the territory can be fortified from the current territory assuming there s
	 * enough armies to be moved
	 * @param territory The territory to check if it can be fortified
	 * @return True False
	 */
	public boolean canFortify(String territory)
	{
		Territory neighbor = RiskBoard.Instance.getTerritory(territory);
		return this.neighbours.contains(territory) && this.armyOn > 1&& this.ownerID == neighbor.getOwnerID();			
	}
	
	/**
	 * Checks If the territory can attack
	 * @param territory The territory to check to be attacked
	 * @return True False
	 */
	public boolean canAttack(String territory)
	{
		Territory neighbor = RiskBoard.Instance.getTerritory(territory);
		return this.neighbours.contains(territory) && this.armyOn > 1 && this.ownerID != neighbor.getOwnerID();		
	}
	
	/**
	 * Returns the Number of dice rolls if you re an attacker
	 * @return nb of dices rolls
	 */
	public int potentialNbOfDiceRollAttack()
	{
		int nbOfdiceRollAttacker = 0;
		if(this.getArmyOn()> 4)
		{
			nbOfdiceRollAttacker = 3;
		}
		else if(this.getArmyOn() == 3)
		{
			nbOfdiceRollAttacker = 2;
		}
		else if(this.getArmyOn() == 2)
		{
			nbOfdiceRollAttacker = 1;
		}
		return nbOfdiceRollAttacker;
	}
	
	/**
	 * Returns the Number of dice rolls if you re an defender
	 * @return nb of dices rolls
	 */
	public int potentialNbOfDiceRollDefender()
	{
		int nbOfdiceRollAttacker = 1;
		if(this.getArmyOn()>= 2)
		{
			nbOfdiceRollAttacker = 2;
		}

		return nbOfdiceRollAttacker;
	}

	/**
	 * Checks if the territory can be fortified
	 * @return the territory can be fortified
	 */
	public boolean canBeFortified() {
		
		for(int i =0; i< this.neighbours.size(); i++)
		{
			String territory = this.neighbours.get(i);
			Territory neighbor = RiskBoard.Instance.getTerritory(territory);
			if(neighbor.canFortify(this.territoryName))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the Territory can attack
	 * @return Can the territory attack
	 */
	public boolean canAttack() {
		if(this.armyOn > 1)
		{
			for(int i =0; i< this.neighbours.size(); i++)
			{
				String territory = this.neighbours.get(i);
				Territory neighbor = RiskBoard.Instance.getTerritory(territory);
				if(neighbor.ownerID != this.ownerID)
				{
					return true;
				}
			}
		}
		return false;
	}
	
}

