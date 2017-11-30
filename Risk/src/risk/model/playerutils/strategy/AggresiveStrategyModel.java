
/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import risk.model.RiskBoard;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.strategy.IStrategy;
import risk.utils.Tuple;

/**
 * Implementation of the  AggressiveStrategy Model
 * @author hcanta
 * @author Karanbir Singh
 */
public class AggresiveStrategyModel implements IStrategy , Serializable
{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 5028528489240863763L;

	/**
	 * The player using the strategy
	 */
	private IPlayer player;
	
	/**
	 * The current game Board
	 */
	private RiskBoard board;
	
	/**
	 * Random generator
	 */
	private Random rand;
	
	/**
	 * Constructor for the Strategy Model
	 * @param debug The current board in use
	 * @param player The player using the strategy
	 */
	public AggresiveStrategyModel(boolean debug, IPlayer player) 
	{
		this.board = RiskBoard.ProperInstance(debug);
		this.player = player;
		this.rand = new Random();
	}
	
	/**
	 * Finds the territory with the highest army for the respective players
	 * @return the index of strongest territory
	 */
	public int getStrongestTerritory() {
		
		Territory territory;
		int getArmies=0;
		int strongestTerritory=0;
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
	 * Finds the territory with the lowest army
	 * @param territory The current territory loaded from board
	 * @return the index of weakest neighbour territory
	 */
	public int getWeakestNeighbour(Territory territory) {

		int weakestNeighbour=1000;
		for(int j = 0; j < territory.getNeighbours().size(); j++)
		{
			if (territory.canAttack(territory.getNeighbours().get(j))) {
				
				if (weakestNeighbour > board.getTerritory(territory.getNeighbours().get(j)).getArmyOn()) {
					
					weakestNeighbour = board.getTerritory(territory.getNeighbours().get(j)).getArmyOn();
				}
			}
		}
		
		return weakestNeighbour;
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
			String toReinforce = player.getTerritoriesOwned().get(getStrongestTerritory());
			
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
			
			ArrayList<Tuple<String,String>> fortifiable = new ArrayList<Tuple<String,String>>();
			Territory  territory = board.getTerritory(player.getTerritoriesOwned().get(getStrongestTerritory()));
			
			for(int j = 0; j < territory.getNeighbours().size(); j++)
			{
				Territory t = board.getTerritory(territory.getNeighbours().get(j));
				if(t.canFortify(territory.getTerritoryName()))
				{
					fortifiable.add(new Tuple<String,String>( territory.getNeighbours().get(j),territory.getTerritoryName()));
				}
			}
		
			int index = rand.nextInt(fortifiable.size());
			String origin = fortifiable.get(index).getSecond();
			String destination = fortifiable.get(index).getFirst();
			int armyToMove = board.getTerritory(destination).getArmyOn() - 1;
			Tuple<String, Tuple<String,Integer>> toReturn = 
					new  Tuple<String, Tuple<String,Integer>>(origin, new Tuple<String,Integer>(destination, armyToMove));
			return toReturn;
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
			
			Territory  territory;
			territory = board.getTerritory(player.getTerritoriesOwned().get(getStrongestTerritory()));
				
			if(territory.canAttack(territory.getNeighbours().get(getWeakestNeighbour(territory)))) {

				Tuple<String, Tuple<String,Integer>> toReturn = 
						new  Tuple<String, Tuple<String,Integer>>(territory.getTerritoryName(), new Tuple<String,Integer>(territory.getNeighbours().get(getWeakestNeighbour(territory)), territory.getArmyOn()));
				return toReturn;
			}			
		}
		
		return null;
	}

}

