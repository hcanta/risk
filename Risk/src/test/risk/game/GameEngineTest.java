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

/**
 * 
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
	
	private GameEngine engine;
	@Before
	public void setUp() throws Exception 
	{
		debug = true;
		
		risk.model.RiskBoard.ProperInstance(debug);
		
		this.engine = new GameEngine(null, debug);
		MapUtils.loadFile(new File("Maps/World.map"),debug);
		engine.createBots(4);
		engine.addHumanPlayer("human");
		player = engine.getPlayer(0);
	}

	@Test
	public void testGetNumberOfPlayers()
	{
		
		Assert.assertTrue(engine.getNumberOfPlayers() == 4);
	}
	
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
