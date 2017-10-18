package risk.util.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public enum Regions 
{
	// NORTH AMERICA
		Alaska("Alaska", 1, Continents.North_America, true, 2, 4, 30), 
		Northwest_Territory("Northwest Terr.", 2, Continents.North_America, false, 3, 4, 5),
		Greenland("Greenland", 3, Continents.North_America, true, 5, 6, 14),
		Alberta("Alberta", 4, Continents.North_America, false, 5, 7),
		Ontario("Ontario", 5, Continents.North_America, false, 6, 7, 8), 
		Quebec("Quebec", 6, Continents.North_America, false, 8), 
		Western_United_States("Western US", 7, Continents.North_America, false, 8, 9), 
		Eastern_United_States("Eastern US", 8, Continents.North_America, false, 9), 
		Central_America("Central America", 9, Continents.North_America, true, 10),
		
		// SOUTH AMERICA
		Venezuela("Venezuela", 10, Continents.South_America, true, 11, 12), 
		Peru("Peru", 11, Continents.South_America, false, 12, 13), 
		Brazil("Brazil", 12, Continents.South_America, true, 13, 21), 
		Argentina("Argentina", 13, Continents.South_America, false),
		
		// EUROPE
		Iceland("Iceland", 14, Continents.Europe, true, 15, 16), 
		Great_Britain("Great Britain", 15, Continents.Europe, false, 16, 18 ,19), 
		Scandinavia("Scandinavia", 16, Continents.Europe, false, 17), 
		Ukraine("Ukraine", 17, Continents.Europe, true, 19, 20, 27, 32, 36), 
		Western_Europe("West.Eur", 18, Continents.Europe, true, 19, 20, 21), 
		Northern_Europe("North.Eur", 19, Continents.Europe, false, 20), 
		Southern_Europe("South.Eur", 20, Continents.Europe, true, 21, 22, 36),
		
		// AFRIKA
		North_Africa("North Africa", 21, Continents.Africa, true, 22, 23, 24), 
		Egypt("Egypt", 22, Continents.Africa, true, 23, 36), 
		East_Africa("East Africa", 23, Continents.Africa, true, 24, 25, 26, 36), 
		Congo("Congo", 24, Continents.Africa, false, 25), 
		South_Africa("South Africa", 25, Continents.Africa, false, 26), 
		Madagascar("Madagascar", 26, Continents.Africa, false),
		
		// ASIA
		Ural("Ural", 27, Continents.Asia, true, 28, 32, 33), 
		Siberia("Siberia", 28, Continents.Asia, false, 29, 31, 33, 34), 
		Yakutsk("Yakutsk", 29, Continents.Asia, false, 30, 31), 
		Kamchatka("Kamchatka", 30, Continents.Asia, true, 31, 34, 35), 
		Irkutsk("Irkutsk", 31, Continents.Asia, false, 34), 
		Kazakhstan("Kazakhstan", 32, Continents.Asia, true, 33, 36, 37), 
		China("China", 33, Continents.Asia, false, 34, 37, 38), 
		Mongolia("Mongolia", 34, Continents.Asia, false, 35), 
		Japan("Japan", 35, Continents.Asia, false), 
		Middle_East("Middle East", 36, Continents.Asia, true, 37), 
		India("India", 37, Continents.Asia, false, 38), 
		Siam("Siam", 38, Continents.Asia, true, 39), 
		
		// AUSTRALIA
		Indonesia("Indonesia", 39, Continents.Australia, true, 40, 41), 
		New_Guinea("New Guinea", 40, Continents.Australia, false, 41, 42), 
		Western_Australia("West. Australia", 41, Continents.Australia, false, 42), 
		Eastern_Australia("East. Australia", 42, Continents.Australia, false);
		
		public static final int LAST_ID = 42;
				
		/**
		 * Must be 1-based!
		 */
		
		public final int id;
		public final Continents continent;
		public final String mapName;
		/**
		 * Whether this region makes the border for the continent.
		 */
		public final boolean continentBorder;
		
		/**
		 * Region flag.
		 */
		public final long regionFlag;
		
		/**
		 * DO NOT USE, contains only "forward" neighbours. Use {@link #getNeighbours()} to obtain ALL neighbours.
		 * Used for {@link GameMap} initialization only.
		 */
		private final int[] forwardNeighbourIds;	
		/**
		 * DO NOT USE, contains only "forward" neighbours. Use {@link #getNeighbours()} to obtain ALL neighbours.
		 * Used for {@link GameMap} initialization only.
		 */
		private List<Regions> forwardNeighbours = null;
		
		/**
		 * List of all neighbour regions.
		 */
		private List<Regions> allNeighbours = null;	
		
		private Regions(String mapName, int id, Continents superRegion, boolean continentBorder, int... forwardNeighbourIds) {		
			this.mapName = mapName;
			this.id = id;
			this.continent = superRegion;
			this.continentBorder = continentBorder;
			this.regionFlag = ((long)1) << (id-1);
			this.forwardNeighbourIds = forwardNeighbourIds;
		}
		
		/**
		 * All neighbour {@link Region}s.
		 * @return
		 */
		public List<Regions> getNeighbours() {
			if (allNeighbours == null) {
				synchronized(this) {
					if (allNeighbours == null) {
						// FIND MY NEIGHBOUR
						List<Regions> neighbours = new ArrayList<Regions>();
						for (int i = 0; i < forwardNeighbourIds.length; ++i) {
							for (Regions region : Regions.values()) {
								if (region.id == forwardNeighbourIds[i]) {
									neighbours.add(region);
									break;
								}
							}
						}
						
						// FIND ME IN NEIGHBOURS OF OTHERS
						for (Regions region : Regions.values()) {
							for (int id : region.forwardNeighbourIds) {
								if (id == this.id) {
									neighbours.add(region);
									break;
								}
							}
						}
						
						// SET THE LIST
						this.allNeighbours = neighbours;
					}
				}	
			}
			return allNeighbours;
		}
		
		/**
		 * USED ONLY FOR THE MAP INITIALIZATION ... for the full list of neighbours use {@link #getNeighbours()}.
		 * @return
		 */
		public List<Regions> getForwardNeighbours() {
			if (forwardNeighbours == null) {
				synchronized(this) {
					if (forwardNeighbours == null) {
						// FIND MY FORWARD NEIGHBOURS
						List<Regions> neighbours = new ArrayList<Regions>();
						for (int i = 0; i < forwardNeighbourIds.length; ++i) {
							for (Regions region : Regions.values()) {
								if (region.id == forwardNeighbourIds[i]) {
									neighbours.add(region);
									break;
								}
							}
						}
						
						this.forwardNeighbours = neighbours;
					}
				}	
			}
			return forwardNeighbours;
		}
		
		private static Map<Integer, Regions> id2Region = null;
		
		public static Regions forId(int id) {
			if (id2Region == null) {
				id2Region = new HashMap<Integer, Regions>();
				for (Regions region : Regions.values()) {
					id2Region.put(region.id, region);
				}
			}
			return id2Region.get(id);
		}
		
		private static Map<Long, Regions> flagToRegion = null;
			
		public static Regions fromFlag(long regionFlag) {
			if (flagToRegion == null) {
				flagToRegion = new HashMap<Long, Regions>();
				for (Regions region : Regions.values()) {
					flagToRegion.put(region.regionFlag, region);
				}
			}
			return flagToRegion.get(regionFlag);
		}
	
}
