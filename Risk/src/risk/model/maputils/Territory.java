/**
 * The risk.model.maputils package contains the class implementing the territory The riskBoard and the continent classes
 */
package risk.model.maputils;

import java.util.ArrayList;
import java.util.Arrays;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import risk.model.RiskBoard;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskIntegers;

/**
 * This is the implementation of the Territory country class. 
 * @author hcanta
 */
public class Territory {

	/**
	 * The graph that will be displayed
	 */
	private mxGraph graph;
	/**
	 * Vertex representation on the graph
	 */
	private Object vertex = null;
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
	 * @param graph the graph that will be displayed
	 * @param xcoord coordinate on the graph
	 * @param ycoord Y coordinate on the graph
	 */
	public Territory(String name, String continent, String[] neighbours, mxGraph graph, int xcoord, int ycoord) 
	{
		this.territoryName = name;
		this.continent = continent;
		this.neighbours = new ArrayList<String>(Arrays.asList(neighbours));
		this.ownerID = RiskIntegers.INITIAL_OWNER;
		this.armyOn = 1;
		this.graph = graph;
		this.setxCoord(xcoord);
		this.setyCoord(ycoord);
		
		if(graph!= null)
		{
			Object parent = graph.getDefaultParent();
			vertex = graph.insertVertex(parent, null, name, this.xCoord, this.yCoord, RiskIntegers.GRAPH_CELL_DIMENTION_X,
					RiskIntegers.GRAPH_CELL_DIMENTION_Y);
		}
	}
	
	/**
	 * Constructor for the territory class 
	 * @param name  The name of the territory
	 * @param continent The name of the continent to which the territory belongs
	 * @param graph the graph that will be displayed
	 * @param xcoord coordinate on the graph
	 * @param ycoord Y coordinate on the graph
	 */
	public Territory(String name, String continent,  mxGraph graph, int xcoord, int ycoord) 
	{
		this(name, continent, new String[0], graph, xcoord, ycoord);
	}
	
	/**
	 * Constructor for the territory class 
	 * @param name  The name of the territory
	 * @param continent The name of the continent to which the territory belongs
	 * @param neighbours The array of neighbors to said territory
	 */
	public Territory(String name, String continent, String[] neighbours) 
	{
		this(name, continent, neighbours, null, 0, 0);
	}
	
	/**
	 * Constructor for the territory class 
	 * @param name  The name of the territory
	 * @param continent The name of the continent to which the territory belongs
	 */
	public Territory(String name, String continent) 
	{
		this(name, continent, new String[0], null, 0, 0);
	}
	
	/**
	 * Returns the vertex representation on the graph
	 * @return the vertex presentation on the graph
	 */
	public Object getVertex() 
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
	 * @param ownerID the ownerID to set
	 */
	public void setOwnerID(int ownerID) 
	{
		this.ownerID = ownerID;
		if(graph!= null)
		{
			graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, PlayerColors.values()[ownerID].name(), new Object[]{vertex}); 
		}
	}
	
	/**
	 * Add a neighbor to the current territory list of neighbors
	 * @param neighbour new neighbor to be added to the adjacency list.
	 */
	public void addNeighbours(String neighbour)
	{
		String n_neighbour = neighbour.toLowerCase();
		if(!this.neighbours.contains(n_neighbour))
		{
			this.neighbours.add(n_neighbour);
		}
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
			RiskBoard.ProperInstance(graph == null).getTerritory(this.neighbours.get(i)).removeNeighbours(this.territoryName);
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
		
		for(String neighbour : this.neighbours)
		{
			if(RiskBoard.ProperInstance(graph == null).getTerritory(neighbour) == null)
				return false;
			if(!RiskBoard.ProperInstance(graph == null).getTerritory(neighbour).getNeighbours().contains(this.territoryName))
				return false;
		}
		return true;
	}
}
