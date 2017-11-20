/**
 * The game package holds the driver and the Game Engine
 */
package risk.game;

import risk.model.RiskBoard;
import risk.views.GameView;

/**
 * The driver holds the main method too launch the game
 * @author hcanta
 *
 */
public class Driver 
{
	/**
	 * Main Method. the Game Engine is initiated there.
	 * @param args (No arguments are needed to lunch the game
	 */
	public static void main(String[] args) 
	{
		RiskBoard.ProperInstance(false);
		GameView gamev = new GameView();RiskBoard.ProperInstance(false).addObserver(gamev);
		@SuppressWarnings("unused")
		GameEngine engine = new GameEngine(gamev, false);
	}
}
