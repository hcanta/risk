/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import risk.model.playerutils.IPlayer;
import risk.utils.constants.RiskEnum;

/**
 * Some Utils functions
 * @author hcanta
 * @version 2.1
 */
public class StrategyUtils 
{
	/**
	 * Generates the strategy object
	 * @param strategy The strategy we wish to have
	 * @param player The player using the strategy
	 * @return The strategy generated
	 */
	public static IStrategy strategyGenerator(RiskEnum.Strategy strategy, IPlayer player)
	{
		IStrategy strat;
		switch(strategy)
		{
			case aggressive:
				strat = new AggresiveStrategyModel( player);
				break;
			case cheater:
				strat = new CheaterStrategyModel( player);
				break;
			case benevolent:
				strat = new BenevolentStrategyModel (player);
				break;
			case random:
			default:
				strat = new RandomStrategyModel( player);
				break;
		}
		return strat;
	}

}
