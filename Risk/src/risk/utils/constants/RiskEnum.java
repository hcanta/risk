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
		red, yellow, gray, green,  pink, orange
	}
	
	/**
	 * The Different type Card Possible
	 * @author hcanta
	 */
	public static enum CardType
	{
		Infantry, Calvary, Artillery
	}
	
	/**
	 * The different events that can cause a change and a notifications to the observers
	 * @author hcanta
	 */
	public static enum RiskEvent
	{
		StateChange, GeneralUpdate, HistoryUpdate, CountryUpdate, CardTrade
	}
	
	/**
	 * Player Type
	 * @author hcanta
	 */
	public static enum RiskPlayerType
	{
		Human, Bot
	}
	
	/**
	 * The different type of strategy
	 * @author hcanta
	 *
	 */
	public static enum Strategy
	{
		aggressive, benevolent, random, cheater, human
	}
	
	
}
