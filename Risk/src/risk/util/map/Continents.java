package risk.util.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public enum Continents 
{
	North_America(1),
	South_America(2),
	Europe(3),
	Africa(4),	
	Asia(5),
	Australia(6);
	
	public static final int TOTAL_ID = 6;
	private static Map<Integer, Continents> ContinentFlag = null;
	private static Map<Integer, Continents> ContinentId = null;
	public final int id;
	public final int continentFlag;
	private Set<Regions> regions = null;
	
	/**
	 * Must be 1-based!
	 */
	

	private Continents(int id) 
	{
		this.id = id;
		this.continentFlag = 1 << (id-1);
	}
	
	
	public Set<Regions> getRegions() 
	{
		if (regions == null) 
		{
			synchronized(this) 
			{
				if (regions == null) 
				{
					Set<Regions> regions = new HashSet<Regions>();
					for (Regions regionName : Regions.values()) 
					{
						if (regionName.continent == this) 
						{
							regions.add(regionName);
						}
					}
					this.regions = regions;
				}
			}
		}
		return regions;
	}

	
	public static Continents forId(int id) 
	{
		if (ContinentId == null) {
			ContinentId = new HashMap<Integer, Continents>();
			for (Continents continent : Continents.values()) {
				ContinentId.put(continent.id, continent);
			}
		}
		return ContinentId.get(id);
	}
	
	
	
	public static Continents fromFlag(int continentFlag) {
		if (ContinentFlag == null) {
			ContinentFlag = new HashMap<Integer, Continents>();
			for (Continents continent : Continents.values()) {
				ContinentFlag.put(continent.continentFlag, continent);
			}
		}
		return ContinentFlag.get(continentFlag);
	}
}
