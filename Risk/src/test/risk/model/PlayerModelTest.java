/**
 * This Package contains the testcases for the risk.model package
 */
package test.risk.model;



import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import risk.model.RiskBoard;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.PlayerModel;
import risk.utils.Utils;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;
/**
 * Test cases for The PlayerModel
 * @version 1.0
 * @author hcanta
 * @author addy
 */
public class PlayerModelTest {
	/**
	 * player object
	 */
	private IPlayer player;
	
	/**
	 * A second player of object
	 */
	private IPlayer secondPlayer;
	/**
	 * Are we debugging
	 */
	private boolean debug;
	@Before
	public void setUp() throws Exception 
	{
		debug =true;
		RiskBoard.ProperInstance(debug).clear();
		Utils.loadFile(new File("Maps/World.map"),debug);
		player = new PlayerModel("test",PlayerColors.red,(short)0,debug,RiskPlayerType.Human,Strategy.human);
		secondPlayer = new PlayerModel("test",PlayerColors.red,(short)1,debug,RiskPlayerType.Bot, Strategy.random);
	}

	

	@Test
	public void testUpdateArmiestoBeplaced() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.updateArmiestoBeplaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}

	@Test
	public void testIncrementArmies() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.incrementArmies();
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 1);
	}

	@Test
	public void testDecrementArmies() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.updateArmiestoBeplaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
		player.decrementArmies();
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 4);
	}

	@Test
	public void testSetNbArmiesToBePlaced() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.setNbArmiesToBePlaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}

	@Test
	public void testGetNbArmiesToBePlaced() {
		player.updateArmiestoBeplaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}

	@Test
	public void testAddTerritory() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		
	}

	@Test
	public void testRemoveTerritory() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		player.removeTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
	}

	@Test
	public void testInvalidArmyOneFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(1);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.ProperInstance(debug).getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta", 1));
	}
	
	@Test
	public void testInvalidArmyAllOriginFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(2);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.ProperInstance(debug).getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta", 2));
	}
	
	@Test
	public void testInvalidArmyAGreaterOriginFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(3);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.ProperInstance(debug).getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta",4));
	}
	
	@Test
	public void testInvalidOriginFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(secondPlayer);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(3);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta",2));
	}
	
	@Test
	public void testInvalidDestinationNeighBorButNotOwnedFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(3);
		
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		
		Assert.assertFalse(player.fortify("alaska", "alberta",1));
	}
	
	@Test
	public void testInvalidDestinationOwnedButNotNeighbOorFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(3);
		player.addTerritory("ontario");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.ProperInstance(debug).getTerritory("ontario").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "ontario",2));
	}
	
	@Test
	public void testFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setArmyOn(3);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.ProperInstance(debug).getTerritory("alberta").setOwnerID(player);;
		RiskBoard.ProperInstance(debug).getTerritory("alberta").setArmyOn(3);
		Assert.assertTrue(player.fortify("alaska", "alberta",1));
		Assert.assertTrue(RiskBoard.ProperInstance(debug).getTerritory("alberta").getArmyOn() == 4);
		Assert.assertTrue(RiskBoard.ProperInstance(debug).getTerritory("alaska").getArmyOn() == 2);
	}

	

	@Test
	public void testIncrementArmiesBy() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}

	@Test
	public void testInvalidArmyReinforceStringInt() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("alaska", 4));
	}
	
	@Test
	public void testInvalidTerritoryReinforceStringInt() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("peru", 4));
	}
	@Test
	public void testReinforceStringInt() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		Assert.assertTrue(player.reinforce("alaska", 4));
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 1);
		
	}
	
	@Test
	public void testInvalidArmyReinforce() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("alaska"));
	}
	
	@Test
	public void testInvalidTerritoryReinforce() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("peru"));
	}
	@Test
	public void testReinforceString() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.ProperInstance(debug).getTerritory("alaska").setOwnerID(player);;
		Assert.assertTrue(player.reinforce("alaska"));
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 4);
		
	}

}
