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
	 * The territory with the highest army for the respective player.
	 */
	private int strongestTerritory;
	
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
	 * Finds the territory with the highest army for the respective players
	 * @return the index of strongest territory
	 */
	public int getStrongestTerritory() {
		
		Territory territory;
		int getArmies=0;
		for (int i = 0; i < player.getTerritoriesOwned().size(); i++) {
			
			territory = board.getTerritory(player.getTerritoriesOwned().get(i));
			
			if (getArmies < board.getTerritory(territory.getTerritoryName()).getArmyOn()) {
		
				getArmies = board.getTerritory(territory.getTerritoryName()).getArmyOn();
				strongestTerritory = i;
			}			
		}
		
		return strongestTerritory;
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
			int getArmies=0;
/*			for (int i = 0; i < player.getTerritoriesOwned().size(); i++) {
				
				territory = board.getTerritory(player.getTerritoriesOwned().get(i));
				
				if (getArmies < board.getTerritory(territory.getTerritoryName()).getArmyOn()) {
			
					getArmies = board.getTerritory(territory.getTerritoryName()).getArmyOn();
					strongestTerritory = i;
				}
				
			}*/
			
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
		/*if (player.canAttack()) {
			
			ArrayList<Tuple<String,String>> attack = new ArrayList<Tuple<String,String>>();
			Territory  territory;
			int getArmies=0;
			for(int i =0; i< player.getTerritoriesOwned().size(); i++)
			{
				territory = board.getTerritory(player.getTerritoriesOwned().get(i));
				
				if (getArmies < board.getTerritory(territory.getTerritoryName()).getArmyOn()) {
					
					getArmies = board.getTerritory(territory.getTerritoryName()).getArmyOn();
					strongestTerritory = i;
				}
			}
			
			for(int j = 0; j < territory.getNeighbours().size(); j++)
			{
				if(territory.canAttack(territory.getNeighbours().get(j)))
				{
					attack.add(new Tuple<String,String>(territory.getTerritoryName(), territory.getNeighbours().get(j)));
				}
			}
			
			int index = rand.nextInt(attack.size());
			String origin = attack.get(index).getFirst();
			String destination = attack.get(index).getSecond();
			int armyToMove = rand.nextInt(board.getTerritory(origin).getArmyOn());
			
			Tuple<String, Tuple<String,Integer>> toReturn = 
					new  Tuple<String, Tuple<String,Integer>>(origin, new Tuple<String,Integer>(destination, armyToMove));
			return toReturn;
		}*/
		
		return null;
	}

}
