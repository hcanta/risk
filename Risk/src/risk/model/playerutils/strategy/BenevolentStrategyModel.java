/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import java.util.ArrayList;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.utils.Tuple;

/**
 * Implementation of the Strategy Model
 * @author hcanta
 * @author Mohammad Akif Beg
 * @version 4.1
 */
public class BenevolentStrategyModel extends Strategy {


	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 735869297957870033L;
	/**
	 * Constructor for the Strategy Model
	 * @param player The player using the strategy
	 */
	public BenevolentStrategyModel(IPlayer player) 
	{
		super(player);
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
			int weakestTerritory = 0;

			int getNbArmies = Integer.MAX_VALUE;
			
			for(int i = 1;i<player.getTerritoriesOwned().size();i++){
				Territory currentTerritory = board.getTerritory(player.getTerritoriesOwned().get(i));
				
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
		if(player.canFortify())
		{
			int weakestTerritory = Integer.MIN_VALUE;

			int getNbArmies = board.getTerritory(player.getTerritoriesOwned().get(0)).getArmyOn();
			
			for(int i = 1;i<player.getTerritoriesOwned().size();i++){
				Territory currentTerritory = board.getTerritory(player.getTerritoriesOwned().get(i));
				
				if (getNbArmies > board.getTerritory(currentTerritory.getTerritoryName()).getArmyOn() && currentTerritory.canBeFortified()) {
					
					getNbArmies = board.getTerritory(currentTerritory.getTerritoryName()).getArmyOn();
					weakestTerritory = i;
				}
			}
			if(weakestTerritory == Integer.MIN_VALUE)
				return null;
			ArrayList<Tuple<String,String>> fortifiable = new ArrayList<Tuple<String,String>>();
			Territory territory = board.getTerritory(player.getTerritoriesOwned().get(weakestTerritory));
			for(int j = 0; j < territory.getNeighbours().size(); j++)
			{
				Territory t = board.getTerritory(territory.getNeighbours().get(j));
				if(t.canFortify(territory.getTerritoryName()))
				{
					fortifiable.add(new Tuple<String,String>( territory.getNeighbours().get(j),territory.getTerritoryName()));
				}
			}
		
			int index = rand.nextInt(fortifiable.size());
			String origin = fortifiable.get(index).getFirst();
			String destination = fortifiable.get(index).getSecond();
			int armyToMove = board.getTerritory(destination).getArmyOn() - 1;
			Tuple<String, Tuple<String,Integer>> toReturn = 
					new  Tuple<String, Tuple<String,Integer>>(origin, new Tuple<String,Integer>(destination, armyToMove));
			return toReturn;
			
		}
		return null;
	}

	/**
	 * Decides which country to attack
	 * And In the Benevolent Strategy it will remain abstract 
	 * @return a tuple of size 2, where the first element is the origin (attacker) and  the second is the destination of the attack (defender)
	 */
	@Override
	public Tuple<String, Tuple<String, Integer>> attack() 
	{
		return null;
	}

	/**
	 * Finds the territory with the lowest army
	 * @param territory The current territory loaded from board
	 * @return the index of weakest neighbour territory
	 */
	@Override
	public int getWeakestNeighbour(Territory territory) {
		
		return 0;
	}
	/**
	 * Finds the territory with the highest army for the respective players
	 * @return the index of strongest territory
	 */
	@Override
	public int getStrongestTerritory() {
		
		return 0;
	}
}
