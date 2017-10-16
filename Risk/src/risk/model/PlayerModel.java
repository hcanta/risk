package risk.model;

import java.util.Observable;

import org.apache.log4j.Logger;


/**
 * @author Ayushi Jain
 *
 */

public class PlayerModel extends Observable
{
	public String playerName;
	
	final static Logger logger = Logger.getLogger(PlayerModel.class);
	
	
	/**
	 * Constructor that initializes default values
	 */
	public PlayerModel() {
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

}
