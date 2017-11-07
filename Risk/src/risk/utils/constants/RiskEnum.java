/**
 *  Package contains the various static , final and constants for the game
 */
package risk.utils.constants;

/**
 * All the enums necessary for the games
 * @author hcanta
 *
 */
public class RiskEnum 
{
	/**
	 * The Game State  enum
	 * @author hcanta
	 */
	public static enum GameState
	{
		IDLE, STARTUP, REINFORCE, ATTACK, FORTIFY
	}
	
	/**
	 * The colors that can be assigned to a player 
	 * @author hcanta
	 */
	public static enum PlayerColors
	{
		red, yellow, brown, green,  pink, purple
	}
}
