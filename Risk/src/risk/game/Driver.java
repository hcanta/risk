package risk.game;

import risk.ui.Game;

/**
 * @author akif
 * Contains the main method that will run the game
 *
 */
public class Driver {

	/**
	 * Main Method of the class that creates the Game Instance and starts the
	 * game.
	 * 
	 * @param new_args
	 *            contains the supplied command-line arguments as an array of
	 *            String objects
	 */
	public static void main(String new_args[]) 
	{
		Game.getInstance().start();
	}

}
