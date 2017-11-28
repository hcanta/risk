/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import java.util.ArrayList;

import risk.model.RiskBoard;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.strategy.IStrategy;
import risk.utils.Tuple;

/**
 * Implementation of Cheater the Strategy Model
 * @author hcanta
 * @author addy
 */
public class CheaterStrategyModel implements IStrategy {

	/**
	 * The Player using this strategy i.
	 */
	@SuppressWarnings("unused")
	private IPlayer player;
	/**
	 * The game board currently active
	 */
	@SuppressWarnings("unused")
	private RiskBoard board;
	/**
	 * Constructor for the Strategy Model Class
	 * @param debug the current board been used
	 * @param player it is The player using the strategy
	 */
	public CheaterStrategyModel(boolean debug, IPlayer player) 
	{
		this.board = RiskBoard.ProperInstance(debug);
		this.player = player;
	}

	/**
	 * Reinforce check is performed.
	 * @return  The country to reinforce and the number of armies to reinforce it with
	 */
	@Override
	public Tuple<String, Integer> reinforce() 
	{
		if(player.canReinforce())
		{
			
			Territory  territory;
			for(int i =0; i< player.getTerritoriesOwned().size(); i++)
			{
				territory = board.getTerritory(player.getTerritoriesOwned().get(i));
				int armyToBePlaced =  board.getTerritory(territory.getTerritoryName()).getArmyOn()*2;
				
				
				/**
				 * each territory needs the army to be
				 * doubled and 
				 * placed
				 * 
				 */
			}
			
			
			
			
			
		
		}
		return null;
	}

	/**
	 * Fortify operation performed
	 * @return a tuple containing a origin and the tuple containing the destination and the amount of army to move
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> fortify() 
	{
		if(player.canFortify())
		{
			
			Territory  territory;
			for(int i =0; i< player.getTerritoriesOwned().size(); i++)
			{
				territory = board.getTerritory(player.getTerritoriesOwned().get(i));
				
				/**
				 * check which territory is neighbor with other players
				 * and then double it.
				 * 
				 */
				
				int armyToBePlaced =  board.getTerritory(territory.getTerritoryName()).getArmyOn()*2;
				
			}
			
			
			
			
			
		}
			
			
			
		return null;
	}

	/**
	 * The attack is planned and which country to attack on 
	 * @return a tuple of size 2, where the first element is the (attacker) origin  and  the second is the (defender) destination of the attack 
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> attack() 
	{
		
		
		/**
		 * if the attack is from cheater player 
		 * it automatically wins the territories neighboring it.
		 * 
		 * 
		 */
		
		
		if(player.canAttack())
		{
			return null;
			
		}
		
		return null;
	}

}
