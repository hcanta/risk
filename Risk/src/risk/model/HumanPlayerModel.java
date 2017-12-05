/**
 * The model package holds the board and the players
 */
package risk.model;

import java.io.Serializable;

import risk.model.playerutils.IPlayer;
import risk.model.playerutils.PlayerModel;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;

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
	 */
	public HumanPlayerModel(String name, PlayerColors color, short playerID) 
	{
		super(name, color, playerID, RiskPlayerType.Human, Strategy.human);
	}

	/**
	 * Duplicates a player object
	 * @param player the duplicate player object
	 */
	public HumanPlayerModel(IPlayer player) {
		super(player.getName(), player.getColor(), player.getPlayerID(), RiskPlayerType.Human, Strategy.human);
		this.setHand(player.getHand());
		for(int i =0; i< player.getTerritoriesOwned().size(); i++)
		{
			this.addTerritory(player.getTerritoriesOwned().get(i));
		}
	}

}
