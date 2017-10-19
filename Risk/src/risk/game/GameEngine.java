/**
 * 
 */
package risk.game;

import java.util.ArrayList;

import risk.model.character.ICharacter;
import risk.model.character.PlayerModel;
import risk.util.RiskEnum.RiskColor;

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
	
	public Stroing randomAssignCountries()
	{
		
	}

		
}
