/**
 * This Package contains the testcase for the test.risk.game folder
 */
package test.risk.game;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import risk.game.GameEngine;
import risk.model.RiskBoard;
import risk.model.playerutils.IPlayer;
import risk.utils.MapUtils;
import risk.views.GameView;

/**
 * TestFileFor the game engine
 *@author hcanta
 */
public class GameEngineTest {

	/**
	 * player object
	 */
	private IPlayer player;
	
	/**
	 * Are we debugging
	 */
	private boolean debug;
	
	/**
	 * GameEngine object
	 */
	private GameEngine engine;
	
	/**
	 * Creates the RiskBoard and GameEngine instances for debugging.
	 * this function runs before every test case.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		debug = false;
		
		risk.model.RiskBoard.ProperInstance(debug);
		GameView gamev = new GameView();risk.model.RiskBoard.ProperInstance(debug).addObserver(gamev);
		gamev.getFrame().setVisible(debug);
		this.engine = new GameEngine(gamev, debug);
		MapUtils.loadFile(new File("Maps/World.map"),debug);
		engine.createBots(4);
		engine.addHumanPlayer("human");
		player = engine.getPlayer(0);
	}

	/**
	 * Tests the number of players currently playing.
	 */
	@Test
	public void testGetNumberOfPlayers()
	{
		
		Assert.assertTrue(engine.getNumberOfPlayers() == 4);
	}
	
	/**
	 * tests the number of armies according to risk rules
	 */
	@Test
	public void testGetNumberOfArmies()
	{
		
		engine.createBots(2);
		engine.addHumanPlayer("human");
		Assert.assertTrue(engine.setArmiesforPlayers() == 40);
		
		engine.createBots(3);
		engine.addHumanPlayer("human");
		Assert.assertTrue(engine.setArmiesforPlayers() == 35);
		
		engine.createBots(4);
		engine.addHumanPlayer("human");
		Assert.assertTrue(engine.setArmiesforPlayers() == 30);
		
		engine.createBots(5);
		engine.addHumanPlayer("human");
		Assert.assertTrue(engine.setArmiesforPlayers() == 25);
		
		engine.createBots(6);
		engine.addHumanPlayer("human");
		Assert.assertTrue(engine.setArmiesforPlayers() == 20);
	}
	
	/**
	 * testing the random assignation of armies and territories.
	 */
	@Test
	public void testRandomAssign() {
		
		risk.model.RiskBoard.ProperInstance(debug).clear();
		MapUtils.loadFile(new File("Maps/World.map"),debug);
		
		engine.createBots(6);
		engine.addHumanPlayer("human");
		engine.setArmiesforPlayers();
		
		Assert.assertTrue(engine.getPlayer(0).getNbArmiesToBePlaced() == 20);
		engine.randomAssignTerritories();

		Assert.assertTrue(engine.getPlayer(0).getNbArmiesToBePlaced() == (20 - engine.getPlayer(0).nbTerritoriesOwned()));
	}
	
	/**
	 * Checks if the player with the owner ID integer can Attack
	 */
	@Test
	public void testcanAttack()
	{
		engine.setArmiesforPlayers();
		engine.randomAssignTerritories();
		player.addTerritory("alaska");	
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(0);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(5);
		Assert.assertTrue(engine.canAttack(new Integer(0)));
	}
}
