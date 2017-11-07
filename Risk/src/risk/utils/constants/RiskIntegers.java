/**
 *  Package contains the various static , final and constants for the game
 */
package risk.utils.constants;

/**
 * This class contains all the constants final integers
 * @author hcanta
 */
public class RiskIntegers 
{
	/**
	* Final width size of the playable game screen.
	*/
	public final static int GAME_WIDTH = 1000;
	  
	/**
	* Final height size of the playable game screen.
	*/
	public final static int GAME_HEIGHT = 940;
	  
	/**
	* The difference for the Map Display Area
	*/
	public final static int GAME_OFFSET = 400;
	  
	/**
	* the height of the state panel
	*/
	public final static int STATE_PANEL_HEIGHT = 80;
	  
	/**
	* Width of the player phase label at the bottom of the screen
	*/
	public final static int PLAYER_PHASE_WIDTH = 290;
	  
	/**
	 * Height of the player phase label at the bottom of the screen
	 */
	public final static int PLAYER_PHASE_HEIGHT = 20;
	  
	/**
	 * The initial Owner ID when a country or Board or territory is created
	 */
	public static final int INITIAL_OWNER = Integer.MIN_VALUE;
	
	/**
	 * The size of a cell on the graph displayed on the X axis
	 */
	public static final int GRAPH_CELL_DIMENTION_X = 80;
	
	/**
	 * The size of a cell on the graph displayed on the Y axis
	 */
	public static final int GRAPH_CELL_DIMENTION_Y = 15;
	
	/**
	 * x Offset Of center Of Cells
	 */
	public static final int GRAPH_CELL_X_OFFSET = 125;
	
	/**
	 * y Offset Of center Of Cells
	 */
	public static final int GRAPH_CELL_Y_OFFSET = 50;

	/**
	 * Number Of Cells per Rows on the graph to be displayed
	 */
	public static final int CELL_PER_ROWS = 4;

}
