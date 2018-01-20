/**
 * This Package contains the testcases for the risk.model package
 */
package test.risk.model;

import java.io.File;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import risk.model.RiskBoard;
import risk.utils.Utils;
/**
 * Test cases for The RiskBoard
 * @version 2.0
 * @author hcanta
 * @author addy
 */
public class RiskBoardTest {

	@Before
	public void setUp() throws Exception 
	{
		RiskBoard.Instance.clear();
		Utils.loadFile(new File("Maps/World.map"));
	}
	/**
	 * This function runs before the test cases.is to test the added continent.
	 */
	@Test
	public void testAddContinent() {
		Assert.assertFalse(RiskBoard.Instance.getContinents().contains("testcontinent"));
		RiskBoard.Instance.addContinent("testcontinent", 3);
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("testcontinent"));
		
	}
	/**
	 * This function runs as the test case 1.it is to test if teh continent is removed.
	 */
	@Test
	public void testRemoveContinent() {		
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("europe"));
		RiskBoard.Instance.removeContinent("europe");
		Assert.assertFalse(RiskBoard.Instance.getContinents().contains("europe"));
				
	}

	/**
	 * this is to test the added territory.
	 */
	@Test
	public void testAddTerritoryStringString() 
	{
		Assert.assertFalse(RiskBoard.Instance.getContinent("europe").containsTerritory("lalaland"));
		RiskBoard.Instance.addTerritory("europe", "lalaland");
		Assert.assertTrue(RiskBoard.Instance.getContinent("europe").containsTerritory("lalaland"));
		
	}

	/**
	 * here the validation of the map is been done.
	 * if the map is proper.
	 */
	@Test
	public void testValidateMap() {
		Assert.assertTrue(RiskBoard.Instance.validateMap());
		RiskBoard.Instance.addContinent("testcontinent", 3);
		Assert.assertFalse(RiskBoard.Instance.validateMap());
		Assert.assertFalse(Utils.loadFile(new File("Maps/invalidAtlantis.map")));
		Assert.assertFalse(Utils.loadFile(new File("Maps/invalidUSA.map")));
		Assert.assertFalse(Utils.loadFile(new File("Maps/invalidWorld.map")));
	}
	/**
	 * this test case checks the removed map.
	 */
	@Test
	public void testRemoveTerritory() {
		Assert.assertTrue(RiskBoard.Instance.getContinent("europe").containsTerritory("iceland"));
		Assert.assertTrue(RiskBoard.Instance.validateMap());
		RiskBoard.Instance.removeTerritory("europe", "iceland");
		Assert.assertFalse(RiskBoard.Instance.getContinent("europe").containsTerritory("lalaland"));
		Assert.assertTrue(RiskBoard.Instance.validateMap());
	}

}
