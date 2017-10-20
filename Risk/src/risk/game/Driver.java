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
		
		try {
			Utilities.loadFile(new File("D:\\OneDrive\\Soen6441\\Maps\\Atlantis.map"));
			Utilities.saveMap("test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Game.getInstance().start();
	}

}
