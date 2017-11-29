/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import risk.model.playerutils.IPlayer;
import risk.utils.constants.RiskEnum;

/**
 * Some Utils functions
 * @author hcanta
 * 
 *
 */
public class StrategyUtils 
{
	/**
	 * Generates the strategy object
	 * @param strategy The strategy we wish to have
	 * @param debug The board being used
	 * @param player The player using the strategy
	 * @return The strategy generated
	 */
	public static IStrategy strategyGenerator(RiskEnum.Strategy strategy, boolean debug, IPlayer player)
	{
		IStrategy strat;
		switch(strategy)
		{
			case aggressive:
				strat = new AggresiveStrategyModel(debug, player);
			case cheater:
				strat = new CheaterStrategyModel(debug, player);
				break;
			case benevolent:
				strat = new BenevolentStrategyModel(debug, player);
				break;
			case random:
			default:
				strat = new RandomStrategyModel(debug, player);
				break;
				
		}
		return strat;
	}

}
