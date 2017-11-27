/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import risk.model.RiskBoard;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.strategy.IStrategy;
import risk.utils.Tuple;

/**
 * Implementation of Cheater the Strategy Model
 * @author hcanta
 */
public class CheaterStrategyModel implements IStrategy {

	/**
	 * The player using the strategy
	 */
	@SuppressWarnings("unused")
	private IPlayer player;
	/**
	 * The current game Board
	 */
	@SuppressWarnings("unused")
	private RiskBoard board;
	/**
	 * Constructor for the Strategy Model
	 * @param debug The current board in use
	 * @param player The player using the strategy
	 */
	public CheaterStrategyModel(boolean debug, IPlayer player) 
	{
		this.board = RiskBoard.ProperInstance(debug);
		this.player = player;
	}

	/**
	 * Performs a reinforce check
	 * @return  The country to reinforce and the amount of army to reinforce it with
	 */
	@Override
	public Tuple<String, Integer> reinforce() 
	{
		return null;
	}

	/**
	 * Performs a fortify operation
	 * @return a tuple containing the origin and a tuple containing the destination and the amount of army to move
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> fortify() 
	{
		return null;
	}

	/**
	 * Decides which country to attack 
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack (defender)
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> attack() 
	{
		return null;
	}

}
