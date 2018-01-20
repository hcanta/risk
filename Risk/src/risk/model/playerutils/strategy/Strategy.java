/**
 * 
 */
package risk.model.playerutils.strategy;

import java.util.Random;

import risk.model.RiskBoard;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.utils.Tuple;

/**
 * Implements an abstract strategy class
 * @author hcanta
 *
 */
public abstract class Strategy implements IStrategy {

	/**
	 * The player using the strategy
	 */
	protected IPlayer player;
	
	/**
	 * The current game Board
	 */
	protected RiskBoard board;
	
	/**
	 * Random generator
	 */
	protected Random rand;
	
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 4071212575239080043L;

	/**
	 * Constructor for the Strategy Model
	 * @param player The player using the strategy
	 */
	public Strategy(IPlayer player) 
	{
		this.board = RiskBoard.Instance;
		this.player = player;
		this.rand = new Random();
	}

	/**
	 * Performs a reinforce check
	 * @return  The country to reinforce and the amount of army to reinforce it with
	 */
	@Override
	public Tuple<String, Integer> reinforce() {
		return null;
	}

	/**
	 * Performs a fortify operation
	 * @return a tuple containing the origin and a tuple containing the destination and the amount of army to move
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> fortify() {
		return null;
	}

	/**
	 * Decides which country to attack 
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack (defender)
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> attack() {
		return null;
	}

	/**
	 * Finds the territory with the lowest army
	 * @param territory The current territory loaded from board
	 * @return the index of weakest neighbour territory
	 */
	@Override
	public int getWeakestNeighbour(Territory territory) {
		return 0;
	}

	/**
	 * Finds the territory with the highest army for the respective players
	 * @return the index of strongest territory
	 */
	@Override
	public int getStrongestTerritory() {
		return 0;
	}

}
