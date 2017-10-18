package risk.model.character;

import java.util.Observable;

import org.apache.log4j.Logger;

import risk.util.RiskEnum.RiskColor;


/**
 * @author Ayushi Jain
 *
 */

public class PlayerModel extends Observable implements ICharacter
{
	public String playerName;
	
	final static Logger logger = Logger.getLogger(PlayerModel.class);
	
	
	/**
	 * Constructor that initializes default values
	 */
	public PlayerModel() 
	{
		if (playerName == null) 
		{
			playerName = "Conquest Computer";
		}
	}
	
	
	/**
	 * Constructor that assigns its parameter to class attributes
	 * 
	 * @param new_playerName
	 *            Player name
	 */
	public PlayerModel(String new_playerName) 
	{
		playerName = new_playerName;
		setChanged();
		notifyObservers();
	}


	@Override
	public RiskColor getColor() 
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getName() 
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isTurn() 
	{
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public short getTurnID() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
