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
	 * @param strategy the strategy the bot is using
	 * */
	public BotPlayerModel(String name, PlayerColors color, short playerID, RiskEnum.Strategy strategy) 
	{
		super(name, color, playerID, RiskPlayerType.Bot, strategy);
		this.iStrategy =  StrategyUtils.strategyGenerator(strategy, this);

	}
	/**
	 * Creates a duplicate of the player Object
	 * @param player the player Object
	 */
	public BotPlayerModel(IPlayer player) {
		super(player.getName(), player.getColor(), player.getPlayerID(), RiskPlayerType.Bot, player.getStrategy());
		this.setHand(player.getHand());
		for(int i =0; i< player.getTerritoriesOwned().size(); i++)
		{
			this.addTerritory(player.getTerritoriesOwned().get(i));
		}
		this.iStrategy =  StrategyUtils.strategyGenerator(strategy,  this);
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
				return this.strategy.name()+ " Can't Reinforce";
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
				return this.strategy.name()+" ";
			}
		}
			
		String origin= fortifyInfo.getFirst();
		String destination = fortifyInfo.getSecond().getFirst();
		int armies=fortifyInfo.getSecond().getSecond() ;
		this.fortify(origin, destination, armies);
		return strategy.name()+" Fortify "+ origin+"->"+((armies == 0) ? 1 : armies) +"->"+destination;
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
		this.iStrategy =  StrategyUtils.strategyGenerator(strategy, this);
	}


}
