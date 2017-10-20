package risk.game;

import java.io.File;
import java.io.IOException;

import risk.ui.Game;
import risk.util.map.editor.Utilities;

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
