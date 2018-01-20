
/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import java.util.ArrayList;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.utils.Tuple;

/**
 * Implementation of the Random Strategy Model
 * @author hcanta
 * @version 4.1
 */
public class RandomStrategyModel extends Strategy {

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 5927190171534944442L;
	/**
	
	/**
	 * Constructor for the Strategy Model
	 * @param player The player using the strategy
	 */
	public RandomStrategyModel(IPlayer player) 
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
		if(player.canReinforce())
		{
			int toBePlaced = player.getNbArmiesToBePlaced();
			int index = rand.nextInt(player.getTerritoriesOwned().size());
			String toReinforce = player.getTerritoriesOwned().get(index);
			
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
			ArrayList<Tuple<String,String>> fortifiable = new ArrayList<Tuple<String,String>>();
			Territory  territory;
			for(int i =0; i< player.getTerritoriesOwned().size(); i++)
			{
				territory = board.getTerritory(player.getTerritoriesOwned().get(i));
				for(int j = 0; j < territory.getNeighbours().size(); j++)
				{
					if(territory.canFortify(territory.getNeighbours().get(j)))
					{
						fortifiable.add(new Tuple<String,String>(territory.getTerritoryName(), territory.getNeighbours().get(j)));
					}
				}
			}
			if(fortifiable.size() == 0)
				return null;
			
			
			int index = rand.nextInt(fortifiable.size());
			String origin = fortifiable.get(index).getFirst();
			String destination = fortifiable.get(index).getSecond();
			int armyToMove = rand.nextInt(board.getTerritory(origin).getArmyOn());
			armyToMove = armyToMove == 0 ? 1 : armyToMove;
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
		if(player.canAttack())
		{
			ArrayList<Tuple<String,String>> attack = new ArrayList<Tuple<String,String>>();
			Territory  territory;
			for(int i =0; i< player.getTerritoriesOwned().size(); i++)
			{
				territory = board.getTerritory(player.getTerritoriesOwned().get(i));
				for(int j = 0; j < territory.getNeighbours().size(); j++)
				{
					if(territory.canAttack(territory.getNeighbours().get(j)))
					{
						attack.add(new Tuple<String,String>(territory.getTerritoryName(), territory.getNeighbours().get(j)));
					}
				}
			}
			
			
			int index = rand.nextInt(attack.size());
			String origin = attack.get(index).getFirst();
			String destination = attack.get(index).getSecond();
			int armyToMove = rand.nextInt(board.getTerritory(origin).getArmyOn());
			Tuple<String, Tuple<String,Integer>> toReturn = 
					new  Tuple<String, Tuple<String,Integer>>(origin, new Tuple<String,Integer>(destination, armyToMove));
			return toReturn;
		}
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

