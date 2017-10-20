package risk.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import risk.game.GameEngine;
import risk.util.map.RiskBoard;
import risk.util.map.editor.Utilities;

/**
 * 
 * TestFileFor the game engine
 *
 */
public class GameEngineTest {

	@Before
	public void setUp() throws Exception 
	{
		RiskBoard.Instance.clear();
		Utilities.loadFile(new File("Maps/World.map"));
		GameEngine.Instance.setNumberofPlayers((short)4);
		GameEngine.Instance.createBots();
		GameEngine.Instance.addHumanPlayer("human");
	}

	@Test
	public void testGetNumberOfPlayers()
	{
		Assert.assertTrue(GameEngine.Instance.getNumberOfPlayers() == (short)4);
	}
	
	@Test
	public void testGetNumberOfArmies()
	{
		GameEngine.Instance.setNumberofPlayers((short)2);
		GameEngine.Instance.createBots();
		GameEngine.Instance.addHumanPlayer("human");
		Assert.assertTrue(GameEngine.Instance.setArmiesforPlayers() == 40);
		GameEngine.Instance.setNumberofPlayers((short)3);
		GameEngine.Instance.createBots();
		GameEngine.Instance.addHumanPlayer("human");
		Assert.assertTrue(GameEngine.Instance.setArmiesforPlayers() == 35);
		GameEngine.Instance.setNumberofPlayers((short)4);
		GameEngine.Instance.createBots();
		GameEngine.Instance.addHumanPlayer("human");
		Assert.assertTrue(GameEngine.Instance.setArmiesforPlayers() == 30);
		GameEngine.Instance.setNumberofPlayers((short)5);
		GameEngine.Instance.createBots();
		GameEngine.Instance.addHumanPlayer("human");
		Assert.assertTrue(GameEngine.Instance.setArmiesforPlayers() == 25);
		GameEngine.Instance.setNumberofPlayers((short)6);
		GameEngine.Instance.createBots();
		GameEngine.Instance.addHumanPlayer("human");
		Assert.assertTrue(GameEngine.Instance.setArmiesforPlayers() == 20);
	}
	

	@Test
	public void testFortify() {
		fail("Not yet implemented");
	}

	@Test
	public void testReinforce() {
		fail("Not yet implemented");
	}

}
