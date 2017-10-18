package risk.util.map;

import java.util.LinkedList;



public class ContinentInfo {
	
	private Continents continent;
	private int id;
	private LinkedList<RegionInfo> subRegions;

	
	
	public ContinentInfo(Continents continent, int id)
	{
		this.continent = continent;
		this.id = id;
		subRegions = new LinkedList<RegionInfo>();
	}
	
	public void addSubRegion(RegionInfo subRegion)
	{
		if(!subRegions.contains(subRegion))
			subRegions.add(subRegion);
	}

	/**
	 * @return A list with the Regions that are part of this SuperRegion
	 */
	public LinkedList<RegionInfo> getSubRegions() {
		return subRegions;
	}

	public Continents getContinent() {
		return continent;
	}
	
	/**
	 * @return The id of this SuperRegion
	 */
	public int getId() {
		return id;
	}
}
