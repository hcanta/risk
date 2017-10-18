package risk.util.map;

import java.util.LinkedList;

public class RiskMap {
	
	public LinkedList<RegionInfo> regions;
	public LinkedList<ContinentInfo> continents;

	public RiskMap() 
	{
		// TODO Auto-generated constructor stub
		this.regions = new LinkedList<RegionInfo>();
		this.continents = new LinkedList<ContinentInfo>();
	}
	
	public RiskMap(LinkedList<RegionInfo> regions, LinkedList<ContinentInfo> continents)
	{
		this.regions = regions;
		this.continents = continents;
	}
	
	/**
	 * add a Region to the map
	 * @param region : Region to be added
	 */
	public void add(RegionInfo region)
	{
		for(RegionInfo r : regions)
			if(r.getId() == region.getId())
			{
				System.err.println("Region cannot be added: id already exists.");
				return;
			}
		regions.add(region);
	}
	
	/**
	 * add a Continent to the map
	 * @param continent : Continent to be added
	 */
	public void add(ContinentInfo continent)
	{
		for(ContinentInfo s : continents)
			if(s.getId() == continent.getId())
			{
				System.err.println("Continent cannot be added: id already exists.");
				return;
			}
		continents.add(continent);
	}
	
	/**
	 * @return : the list of all Regions in this map
	 */
	public LinkedList<RegionInfo> getRegions() {
		return regions;
	}
	
	/**
	 * @return : the list of all Continents in this map
	 */
	public LinkedList<ContinentInfo> getContinents() {
		return continents;
	}
	
	
	/**
	 * @param id : a Region id number
	 * @return : the matching Region object
	 */
	public RegionInfo getRegion(int id)
	{
		for(RegionInfo region : regions)
			if(region.getId() == id)
				return region;
		System.err.println("Could not find region with id " + id);
		return null;
	}
	
	/**
	 * @param id : a Continent id number
	 * @return : the matching Continent object
	 */
	public ContinentInfo getContinent(int id)
	{
		for(ContinentInfo continent : continents)
			if(continent.getId() == id)
				return continent;
		System.err.println("Could not find continent with id " + id);
		return null;
	}
	
	/**
	 * @return : a new Map object exactly the same as this one
	 */
	public RiskMap getMapCopy() {
		RiskMap newMap = new RiskMap();
		for(ContinentInfo sr : continents) //copy continents
		{
			ContinentInfo newContinent = new ContinentInfo(Continents.forId(sr.getId()), sr.getId());
			newMap.add(newContinent);
		}
		for(RegionInfo r : regions) //copy regions
		{
			RegionInfo newRegion = new RegionInfo(Regions.forId(
					r.getId()), r.getId(), newMap.getContinent(r.getContinentData().getId()));
			newMap.add(newRegion);
		}
		for(RegionInfo r : regions) //add neighbors to copied regions
		{
			RegionInfo newRegion = newMap.getRegion(r.getId());
			for(RegionInfo neighbor : r.getNeighbors())
				newRegion.addNeighbor(newMap.getRegion(neighbor.getId()));
		}
		return newMap;
	}
	

}
