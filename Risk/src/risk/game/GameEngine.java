/**
 * 
 */
package risk.game;

import java.util.ArrayList;
import java.util.Collections;

import risk.model.ModelException;
import risk.model.character.ICharacter;
import risk.model.character.PlayerModel;
import risk.util.RiskEnum.RiskColor;
import risk.util.map.RiskBoard;

/**
 * @author hcanta
 *
 */
public class GameEngine 
{
	
	private ArrayList<ICharacter> players;
	private short numberOfPlayers;
	
	public static GameEngine Instance = new GameEngine();
	
	public GameEngine ()
	{
		
	}
	
	/**
	 *  Set the number of players
	 * @param numberPl Number of players
	 */
	public void setNumberofPlayers(short numberPl)
	{
		players =  new ArrayList<ICharacter>();
		this.numberOfPlayers = numberPl;
	}
	
	/**
	 * Generates computer player
	 */
	public void createBots()
	{
		for(short i = 1; i< this.numberOfPlayers; i++)
		{			
			players.add(new PlayerModel("Computer "+ i, RiskColor.values()[i], (short)(i+1)));
		}
	}
	
	/**
	 * Ad user/human player
	 * @param name The players name
	 */
	public void addHumanPlayer(String name)
	{
		players.add(new PlayerModel(name, RiskColor.values()[0], (short)(1)));
	}
	
	/**
	 * Give the armies according to the risk rules
	 */
	public void setArmiesforPlayers()
	{
		int nbArmiesToBePlaced = 0;
		
		switch(players.size())
		{
			case 6:
				nbArmiesToBePlaced = 20;
				break;
			case 5:
				nbArmiesToBePlaced = 25;
				break;
			case 4:
				nbArmiesToBePlaced = 30;
				break;
			case 3:
				nbArmiesToBePlaced = 35;
				break;
			case 2:
				nbArmiesToBePlaced = 40;
				break;
		}
		
		for(int i =0; i < players.size(); i++)
		{
			players.get(i).setNbArmiesToBePlaced(nbArmiesToBePlaced);
		}
	}
	
	/**
	 * Randomly assign territories according to the risk rules
	 */
	public void randomAssignTerritories()
	{
		ArrayList<String> countries = RiskBoard.Instance.getTerritories();
		Collections.shuffle(countries);
		
		ArrayList<ICharacter> pl  = new ArrayList<ICharacter>();
		for(int i =0; i< players.size(); i++)
		{
			pl.add(players.get(i));
		}
		
		Collections.shuffle(pl);
		for(int i=0; i< countries.size(); i++)
		{
			int player = i%pl.size();			
			pl.get(player).addTerritory(countries.get(i));
			pl.get(player).decrementArmies();
			RiskBoard.Instance.getTerritory(countries.get(i)).setOwnerID(pl.get(player).getTurnID());
			RiskBoard.Instance.getTerritory(countries.get(i)).setArmyOn(1);
		}

	}
	
	/**
	 * 
	 * @param playername The player name
	 * @param territory1 The origin territory
	 * @param territory2 The destination territory
	 * @param armies The number of armies to be moved
	 * @throws Model Exception  
	 */
	public void fortify(String playername, String territory1, String territory2, int armies) throws ModelException
	{
		for(int i =0; i < players.size(); i++)
		{
			if(players.get(i).getName().equalsIgnoreCase(playername))
			{
				players.get(i).fortify( territory1, territory2,  armies);
				break;
			}
		}
	}
	
	
	/**
	 * 
	 * @param playername The player name
	 * @param territory the territory to reinforce
	 * @throws ModelException one or more of the parameter given is invalid
	 */
	public void reinforce(String playername, String territory) throws ModelException
	{
		for(int i =0; i < players.size(); i++)
		{
			if(players.get(i).getName().equalsIgnoreCase(playername))
			{
				players.get(i).reinforce( territory);
				break;
			}
		}
	}

		
}
