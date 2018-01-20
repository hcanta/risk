/**
 * The Player utils package contains the classes necessary for the creation and work of a player object
 */
package risk.model.playerutils.strategy;

import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.utils.Tuple;

/**
 * Implementation of Cheater the Strategy Model
 * @author hcanta
 * @author addy
 * @version 4.2
 */
public class CheaterStrategyModel extends Strategy {

	/**

	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -7062346114363150158L;


	/**
	 * Constructor for the Strategy Model Class
	 * @param player it is The player using the strategy
	 */
	public CheaterStrategyModel( IPlayer player) 
	{
		super(player);
	}

	/**
	 * Reinforce check is performed.
	 * @return  The country to reinforce and 
	 * the number of armies to reinforce it with
	 * 
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
				board.getTerritory(territory.getTerritoryName()).setArmyOn(armyToBePlaced);
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
				int armyToBePlaced =  board.getTerritory(territory.getTerritoryName()).getArmyOn()*2;
				for(int j =0; j< territory.getNeighbours().size(); j++)
				{
					Territory neighbor = board.getTerritory(territory.getNeighbours().get(j));
					if(territory.getOwnerID() !=neighbor.getOwnerID() )
					{
						territory.setArmyOn(armyToBePlaced);
						break;
					}
				}
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
		return null;
	}
	/**
	 * Finds the territory with the lowest army
	 * @param territory The current territory loaded from board
	 * @return the index of weakest neighbor territory
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
