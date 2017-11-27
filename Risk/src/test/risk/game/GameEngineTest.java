
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
import risk.utils.Utils;
import risk.utils.constants.RiskEnum;

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
	/**
	 * The Game Engine in uses
	 */
	private GameEngine engine;
	/**
	 * Creates the RiskBoard and GameEngine instances for debugging.
	 * this function runs before every test case.
	 * @throws Exception set up failed
	 */
	@Before
	public void setUp() throws Exception 
	{
		debug = true;
		
		risk.model.RiskBoard.ProperInstance(debug);
		
		this.engine = new GameEngine(null, debug);
		Utils.loadFile(new File("Maps/World.map"),debug);
		
		engine.addHumanPlayer("human", RiskEnum.PlayerColors.red);
		player = engine.getPlayer(engine.testFirstPlayer());
		engine.createBots(4, false, RiskEnum.PlayerColors.red);
		engine.generateTurnOrder();
		
		
	}
	/**
	 * tests the map saving utility of Risk game
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
		engine.addHumanPlayer("human", RiskEnum.PlayerColors.red);
		engine.createBots(2, false, RiskEnum.PlayerColors.red);
		
		Assert.assertTrue(engine.setArmiesforPlayers() == 40);
		
		engine.addHumanPlayer("human", RiskEnum.PlayerColors.red);
		engine.createBots(3, false, RiskEnum.PlayerColors.red);
		
		Assert.assertTrue(engine.setArmiesforPlayers() == 35);
		
		engine.addHumanPlayer("human", RiskEnum.PlayerColors.red);
		engine.createBots(4, false, RiskEnum.PlayerColors.red);
		
		Assert.assertTrue(engine.setArmiesforPlayers() == 30);
		engine.addHumanPlayer("human", RiskEnum.PlayerColors.red);
		engine.createBots(5, false, RiskEnum.PlayerColors.red);
		
		Assert.assertTrue(engine.setArmiesforPlayers() == 25);
		engine.addHumanPlayer("human", RiskEnum.PlayerColors.red);
		engine.createBots(6, false, RiskEnum.PlayerColors.red);
		
		Assert.assertTrue(engine.setArmiesforPlayers() == 20);
	}
	
	/**
	 * testing the random assignation of armies and territories.
	 */
	@Test
	public void testRandomAssign() {
		risk.model.RiskBoard.ProperInstance(debug).clear();
		Utils.loadFile(new File("Maps/World.map"),debug);
		
		engine.addHumanPlayer("human", RiskEnum.PlayerColors.red);
		Integer id =  new Integer (engine.testFirstPlayer());
		engine.createBots(6, false, RiskEnum.PlayerColors.red);		
		engine.generateTurnOrder();
		engine.setArmiesforPlayers();
		
		Assert.assertTrue(engine.getPlayer(id).getNbArmiesToBePlaced() == 20);
		engine.randomAssignTerritories();

		Assert.assertTrue(engine.getPlayer(id).getNbArmiesToBePlaced() == (20 - engine.getPlayer(id).nbTerritoriesOwned()));
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
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(5);
		Assert.assertTrue(player.canAttack());
	}
}