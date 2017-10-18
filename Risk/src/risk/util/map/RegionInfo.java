package risk.util.map;

import java.util.LinkedList;


public class RegionInfo 
{
	private Regions region;
	private int id;
	private LinkedList<RegionInfo> neighbors;
	private ContinentInfo continent;
	
	
	public RegionInfo(Regions region, int id, ContinentInfo superRegion)
	{
		this.region = region;
		this.id = id;
		this.continent = superRegion;
		this.neighbors = new LinkedList<RegionInfo>();
		if (superRegion != null) {
			superRegion.addSubRegion(this);
		}
	}
	
	public RegionInfo(Regions region, int id, ContinentInfo superRegion, String playerName, int armies)
	{
		this.region = region;
		this.id = id;
		this.continent = superRegion;
		this.neighbors = new LinkedList<RegionInfo>();
		
		superRegion.addSubRegion(this);
	}
	
	public void addNeighbor(RegionInfo neighbor)
	{
		if(!neighbors.contains(neighbor))
		{
			neighbors.add(neighbor);
			neighbor.addNeighbor(this);
		}
	}
	
	/**
	 * @param region a Region object
	 * @return True if this Region is a neighbor of given Region, false otherwise
	 */
	public boolean isNeighbor(RegionInfo region)
	{
		if(neighbors.contains(region))
			return true;
		return false;
	}
	
	/**
	 * @return The id of this Region
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return A list of this Region's neighboring Regions
	 */
	public LinkedList<RegionInfo> getNeighbors() {
		return neighbors;
	}

	/**
	 * @return The SuperRegion this Region is part of
	 */
	public ContinentInfo getContinentData() {
		return continent;
	}
	
	public Regions getRegion() {
		return region;
	}

	public Continents getContinent() {
		return region.continent;
	}
	
	

}
