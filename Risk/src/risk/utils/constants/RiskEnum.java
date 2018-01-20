/**
 *  Package contains the various static , final and constants for the game
 */
package risk.utils.constants;

import java.io.Serializable;

/**
 * All the enums necessary for the games
 * @author hcanta
 * @version 3.3
 */
public class RiskEnum implements Serializable
{
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 3630922664686386671L;

	/**
	 * The Game State  enum
	 * @author hcanta
	 */
	public static enum GameState
	{
		IDLE, STARTUP, REINFORCE, ATTACK, FORTIFY, NEXT_PLAYER
	}
	
	/**
	 * The colors that can be assigned to a player 
	 * @author hcanta
	 */
	public static enum PlayerColors
	{
		red, yellow, gray, green,  pink, blue
	}
	
	/**
	 * The Different type Card Possible
	 * @author hcanta
	 */
	public static enum CardType
	{
		Infantry, Cavalry, Artillery
	}
	
	/**
	 * The different events that can cause a change and a notifications to the observers
	 * @author hcanta
	 */
	public static enum RiskEvent
	{
		StateChange, GeneralUpdate, HistoryUpdate, CountryUpdate, CardTrade, GraphUpdate
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
