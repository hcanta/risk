
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
import risk.utils.constants.RiskEnum;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;

/**
 * Test cases for The PlayerModel
 * @version 1.0
 * @author hcanta
 * @author Karan
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
	 * Creates the RiskBoard and Player, both human and bots, instances for debugging.
	 * this function runs before every test case.
	 * @throws Exception set up failed
	 */
	@Before
	public void setUp() throws Exception 
	{
		RiskBoard.Instance.clear();
		Utils.loadFile(new File("Maps/World.map"));
		player = new PlayerModel("test",PlayerColors.red,(short)0,RiskPlayerType.Human, RiskEnum.Strategy.human);
		secondPlayer = new PlayerModel("test",PlayerColors.red,(short)1,RiskPlayerType.Bot, RiskEnum.Strategy.random);
	}

	/**
	 * Tests the increment in the number of armies to be placed by the given number Player Model implementation
	 */
	@Test
	public void testUpdateArmiestoBeplaced() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.updateArmiestoBeplaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}

	/**
	 * Increments the amount of armies to be placed Player Model Implementation
	 */
	@Test
	public void testIncrementArmies() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.incrementArmies();
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 1);
	}

	/**
	 * Decrements the amount of armies to be placed Player Model Implementation
	 */
	@Test
	public void testDecrementArmies() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.updateArmiestoBeplaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
		player.decrementArmies();
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 4);
	}

	/**
	 * Set the Number of Armies to be Placed. Player Model Implementation
	 */
	@Test
	public void testSetNbArmiesToBePlaced() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.setNbArmiesToBePlaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}

	/**
	 * Returns the number of armies to be placed. Player Model Implementation
	 */
	@Test
	public void testGetNbArmiesToBePlaced() {
		player.updateArmiestoBeplaced(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}
	
	/**
	 * Tests the addition of territory to the list of territories owned. Player Model implementation
	 */
	@Test
	public void testAddTerritory() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		
	}

	/**
	 * Tests the removal of territory from the list of territories owned. Player Model implementation
	 */
	@Test
	public void testRemoveTerritory() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		player.removeTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
	}

	/**
	 * Tests the fortification for 1 army
	 */
	@Test
	public void testInvalidArmyOneFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		RiskBoard.Instance.getTerritory("alaska").setArmyOn(1);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.Instance.getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta", 1));
	}
	
	/**
	 * Test Invalid Model Army on  fortification
	 */
	@Test
	public void testInvalidArmyAllOriginFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		RiskBoard.Instance.getTerritory("alaska").setArmyOn(2);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.Instance.getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta", 2));
	}
	
	/**
	 * Test Invalid Model Army on  fortification
	 */
	@Test
	public void testInvalidArmyAGreaterOriginFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		RiskBoard.Instance.getTerritory("alaska").setArmyOn(3);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.Instance.getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta",4));
	}
	/**
	 * Test Invalid Model Army on  fortification origin is wrong
	 */
	@Test
	public void testInvalidOriginFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(secondPlayer);
		RiskBoard.Instance.getTerritory("alaska").setArmyOn(3);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alberta").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "alberta",2));
	}
	/**
	 * Test Invalid Model Army on  fortification Destination is wrong
	 */
	@Test
	public void testInvalidDestinationNeighBorButNotOwnedFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		RiskBoard.Instance.getTerritory("alaska").setArmyOn(3);
		
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		
		Assert.assertFalse(player.fortify("alaska", "alberta",1));
	}
	/**
	 * Test Invalid Model Army on  fortification not neighbors
	 */
	@Test
	public void testInvalidDestinationOwnedButNotNeighbOorFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		RiskBoard.Instance.getTerritory("alaska").setArmyOn(3);
		player.addTerritory("ontario");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.Instance.getTerritory("ontario").setOwnerID(player);;
		Assert.assertFalse(player.fortify("alaska", "ontario",2));
	}
	
	/**
	 * Test Valid Model Army on  fortification
	 */
	@Test
	public void testFortify() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		RiskBoard.Instance.getTerritory("alaska").setArmyOn(3);
		player.addTerritory("alberta");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 2);
		RiskBoard.Instance.getTerritory("alberta").setOwnerID(player);;
		RiskBoard.Instance.getTerritory("alberta").setArmyOn(3);
		Assert.assertTrue(player.fortify("alaska", "alberta",1));
		Assert.assertTrue(RiskBoard.Instance.getTerritory("alberta").getArmyOn() == 4);
		Assert.assertTrue(RiskBoard.Instance.getTerritory("alaska").getArmyOn() == 2);
	}

	
	/**
	 * Test Increment army
	 */
	@Test
	public void testIncrementArmiesBy() {
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 0);
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 5);
	}
	
	/**
	 * Test Invalid Reinforce on country
	 */
	@Test
	public void testInvalidArmyReinforceStringInt() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("alaska", 4));
	}
	
	/**
	 * Test Invalid Reinforce on country
	 */
	@Test
	public void testInvalidTerritoryReinforceStringInt() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("peru", 4));
	}
	/**
	 * Test Valid Reinforce on country
	 */
	@Test
	public void testReinforceStringInt() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		Assert.assertTrue(player.reinforce("alaska", 4));
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 1);
		
	}
	/**
	 * Test Invalid Reinforce on country invalid army
	 */
	@Test
	public void testInvalidArmyReinforce() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("alaska"));
	}
	/**
	 * Test Invalid Reinforce on country, invalid territory
	 */
	@Test
	public void testInvalidTerritoryReinforce() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		Assert.assertFalse(player.reinforce("peru"));
	}
	/**
	 * Test Valid Reinforce on country
	 */
	@Test
	public void testReinforceString() {
		Assert.assertTrue(player.getTerritoriesOwned().size() == 0);
		player.addTerritory("alaska");
		player.incrementArmiesBy(5);
		Assert.assertTrue(player.getTerritoriesOwned().size() == 1);
		RiskBoard.Instance.getTerritory("alaska").setOwnerID(player);;
		Assert.assertTrue(player.reinforce("alaska"));
		Assert.assertTrue(player.getNbArmiesToBePlaced() == 4);
		
	}

}

