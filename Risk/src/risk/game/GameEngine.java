/**
 * 
 */
package risk.game;

import java.util.ArrayList;
import java.util.Collections;

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
	public GameEngine ()
	{
		
	}

	public void setNumberofPlayers(short numberPl)
	{
		players =  new ArrayList<ICharacter>();
		this.numberOfPlayers = numberPl;
	}
	
	public void createBots()
	{
		for(short i = 1; i< this.numberOfPlayers; i++)
		{			
			players.add(new PlayerModel("Computer "+ i, RiskColor.values()[i], (short)(i+1)));
		}
	}
	
	public void addHumanPlayer(String name)
	{
		players.add(new PlayerModel(name, RiskColor.values()[0], (short)(1)));
	}
	
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
			RiskBoard.Instance.getTerritory(countries.get(i)).setOwnerID(pl.get(player).getTurnID());
		}

	}

		
}
