package risk.model;

/**
 * @author Ayushi  Jain
 *
 */

public class MapModel  implements Cloneable 
{
	
	int mapWidth;
	int mapHeight;

	/**
	 * Default constructor
	 */
	public MapModel() {
		// default constructor
	}

	
	/**
	 * this method gets map width
	 * 
	 * @return mapwidth width of the map
	 */
	public int getMapWidth() {
		return mapWidth;
	}
	
	
	/**
	 * this method sets map width
	 * 
	 * @param mapWidth
	 *            width of the map
	 */
	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}
}
