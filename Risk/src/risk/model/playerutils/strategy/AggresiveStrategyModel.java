/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import risk.model.RiskBoard;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.strategy.IStrategy;
import risk.utils.Tuple;

/**
 * Implementation of the  AggressiveStrategy Model
 * @author hcanta
 * @author Karan
 */
public class AggresiveStrategyModel implements IStrategy {

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
	public AggresiveStrategyModel(boolean debug, IPlayer player) 
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
		if (player.canReinforce()) {
			
			int toBePlaced = player.getNbArmiesToBePlaced();
			Territory territory;
			int strongestTerritory=0;
			int getArmies=0;
			for (int i = 0; i < player.getTerritoriesOwned().size(); i++) {
				
				territory = board.getTerritory(player.getTerritoriesOwned().get(i));
				//territoryName = territory.getTerritoryName();
				//getArmies = board.getTerritory(territoryName).getArmyOn();
				
				if (getArmies < board.getTerritory(territory.getTerritoryName()).getArmyOn()) {
			
					getArmies = board.getTerritory(territory.getTerritoryName()).getArmyOn();
					strongestTerritory = i;
				}
				
			}
			
			String toReinforce = player.getTerritoriesOwned().get(strongestTerritory);
			
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
		if (player.canFortify()) {
			
		}
		
		return null;
	}

	/**
	 * Decides which country to attack 
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack (defender)
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> attack() 
	{
		if (player.canAttack()) {
			
		}
		
		return null;
	}

}
