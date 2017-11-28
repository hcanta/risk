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
 * Implementation of the Strategy Model
 * @author Mohammad Akif Beg
 */
public class BenevolentStrategyModel implements IStrategy {

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
	public BenevolentStrategyModel(boolean debug, IPlayer player) 
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
		if(player.canReinforce()){
			int toBePlaced = player.getNbArmiesToBePlaced();
			Territory currentTerritory;
			int weakestTerritory = 0;
			int getNbArmies = board.getTerritory(player.getTerritoriesOwned().get(0)).getArmyOn();
			
			for(int i = 1;i<player.getTerritoriesOwned().size();i++){
				currentTerritory =  board.getTerritory(player.getTerritoriesOwned().get(i));
				
				if (getNbArmies > board.getTerritory(currentTerritory.getTerritoryName()).getArmyOn()) {
					
					getNbArmies = board.getTerritory(currentTerritory.getTerritoryName()).getArmyOn();
					weakestTerritory = i;
				}
			}
			String toReinforce = player.getTerritoriesOwned().get(weakestTerritory);
			
			Tuple<String, Integer> toReturn = new Tuple<String, Integer>(toReinforce, toBePlaced);
			return toReturn;
		}	
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
