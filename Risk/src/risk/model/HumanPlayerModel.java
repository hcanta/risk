/**
 * The model package holds the board and the players
 */
package risk.model;

import java.io.Serializable;

import risk.model.playerutils.PlayerModel;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;

/**
 * Implementation of the Human Player Model
 * @author hcanta
 */
public class HumanPlayerModel extends PlayerModel implements Serializable 
{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 8346633213834901551L;

	/**
	 * Constructor for the Human Player Model
	 * @param name the name of the player
	 * @param color the color of the player
	 * @param playerID the turn/player id of the player
	 * @param debug set to true for debugging or testing
	 */
	public HumanPlayerModel(String name, PlayerColors color, short playerID, boolean debug) 
	{
		super(name, color, playerID, debug, RiskPlayerType.Human);
	}

}
