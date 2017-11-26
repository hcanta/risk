/**
 * The Player utils package strategy contains the strategy implementation
 */
package risk.model.playerutils.strategy;

/**
 * The Interface of Strategy
 * @author hcanta
 * @version 2.1
 */
public interface IStrategy 
{
	/**
	 * Performs a reinforce operation
	 * @return was the reinforcement successful or not
	 */
	public boolean reinforce();
	
	/**
	 * Performs a fortify operation
	 * @return was the fortify successful or not
	 */
	public boolean fortify();
	
	/**
	 * Decides which country to attack 
	 * @return an array of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack (defender)
	 */
	public String [] attack();

}
