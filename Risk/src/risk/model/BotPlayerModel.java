/**
 * The model package holds the board and the players
 */
package risk.model;

import java.io.Serializable;

import risk.model.playerutils.PlayerModel;
import risk.model.playerutils.strategy.IStrategy;
import risk.model.playerutils.strategy.StrategyUtils;
import risk.utils.Tuple;
import risk.utils.constants.RiskEnum;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;

/**
 * Implementation of the bot model
 * @author hcanta
 */
public class BotPlayerModel extends PlayerModel implements Serializable
{

	/**
	 * The strategy for the Bot
	 */
	private IStrategy iStrategy;
	

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = -3468468163270621039L;

	/**
	 * Constructor for the Human Player Model
	 * @param name the name of the player
	 * @param color the color of the player
	 * @param playerID the turn/player id of the player
	 * @param debug set to true for debugging or testing
	 * @param strategy the strategy the bot is using
	 * */
	public BotPlayerModel(String name, PlayerColors color, short playerID, boolean debug, RiskEnum.Strategy strategy) 
	{
		super(name, color, playerID, debug, RiskPlayerType.Bot, strategy);
		this.iStrategy =  StrategyUtils.strategyGenerator(strategy, debug, this);
		
		
	}
	
	/**
	 * Perform a reinforcement implemented on BotModel
	 * @return true/ false
	 */
	@Override
	public boolean reinforce() 
	{
		Tuple<String, Integer> reinforceInfo = this.iStrategy.reinforce();
		String territory= reinforceInfo.getFirst();
		int army=reinforceInfo.getSecond();
		return this.reinforce(territory, army);
	}

	/**
	 * Perform a fortification implemented on BotModel
	 * @return true/ false
	 */
	@Override
	public boolean fortify() 
	{
		Tuple<String, Tuple<String, Integer>> fortifyInfo = this.iStrategy.fortify();
		String origin= fortifyInfo.getFirst();
		String destination = fortifyInfo.getSecond().getFirst();
		int armies=fortifyInfo.getSecond().getSecond();
		return this.fortify(origin, destination, armies);
	}

	/**
	 * Decides which country to attack . Bot implementation
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack and amount of armies to use(defender)
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> attack() 
	{
		return  iStrategy.attack();
	}

}
