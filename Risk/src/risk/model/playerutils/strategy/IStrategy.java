/**
 * The Player utils package strategy contains the strategy implementation
 */
package risk.model.playerutils.strategy;

import java.io.Serializable;

import risk.model.maputils.Territory;
import risk.utils.Tuple;

/**
 * The Interface of Strategy
 * @author hcanta
 * @version 2.2
 */
public interface IStrategy extends Serializable
{
	/**
	 * Performs a reinforce check
	 * @return  The country to reinforce and the amount of army to reinforce it with
	 */
	public Tuple<String,Integer> reinforce();
	
	/**
	 * Performs a fortify operation
	 * @return a tuple containing the origin and a tuple containing the destination and the amount of army to move
	 */
	public Tuple<String,Tuple<String,Integer>> fortify();
	
	/**
	 * Decides which country to attack 
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack (defender)
	 */
	public Tuple<String,Tuple<String,Integer>> attack();

	/**
	 * Finds the territory with the lowest army
	 * @param territory The current territory loaded from board
	 * @return the index of weakest neighbour territory
	 */
	public int getWeakestNeighbour(Territory territory);
	/**
	 * Finds the territory with the highest army for the respective players
	 * @return the index of strongest territory
	 */
	public int getStrongestTerritory();

}
