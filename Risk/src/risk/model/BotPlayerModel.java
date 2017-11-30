/**
 * The model package holds the board and the players
 */
package risk.model;

import java.io.Serializable;

import risk.model.playerutils.IPlayer;
import risk.model.playerutils.PlayerModel;
import risk.model.playerutils.strategy.IStrategy;
import risk.model.playerutils.strategy.StrategyUtils;
import risk.utils.Tuple;
import risk.utils.constants.RiskEnum;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;

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
	 * Creates a duplicate of the player Object
	 * @param player the player Object
	 */
	public BotPlayerModel(IPlayer player) {
		super(player.getName(), player.getColor(), player.getPlayerID(), player.getDebug(), RiskPlayerType.Bot, player.getStrategy());
		this.setHand(player.getHand());
		this.iStrategy =  StrategyUtils.strategyGenerator(strategy, debug, this);
	}

	/**
	 * Perform a reinforcement implemented on BotModel
	 * @return true/ false
	 */
	@Override
	public String reinforce() 
	{
		
		Tuple<String, Integer> reinforceInfo = this.iStrategy.reinforce();
		if( reinforceInfo == null)
		{
			if(this.strategy == Strategy.cheater)
			{
				return "Cheater Reinforce";
			}
			else
			{
				return "Can't Reinforce";
			}
		}
		
		String territory= reinforceInfo.getFirst();
		int army=reinforceInfo.getSecond();
		this.reinforce(territory, army);
		return strategy.name()+" Reinforce "+ territory+" With "+army;	
	}

	/**
	 * Perform a fortification implemented on BotModel
	 * @return true/ false
	 */
	@Override
	public String fortify() 
	{
		Tuple<String, Tuple<String, Integer>> fortifyInfo = this.iStrategy.fortify();
		if( fortifyInfo == null)
		{
			if(this.strategy == Strategy.cheater)
			{
				return "Cheater Fortification";
			}
			else
			{
				return "Can't fortify";
			}
		}
			
		String origin= fortifyInfo.getFirst();
		String destination = fortifyInfo.getSecond().getFirst();
		int armies=fortifyInfo.getSecond().getSecond();
		this.fortify(origin, destination, armies);
		return strategy.name()+" Fortify "+ origin+"->"+armies+"->"+destination;
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
	
	/**
	 * Set The RiskBoard
	 * @param board the new RiskBoard
	 */
	@Override
	public void setRiskBoard(RiskBoard board) {
		this.board  = board;
		this.iStrategy =  StrategyUtils.strategyGenerator(strategy, board.getGraph() == null, this);
	}


}
