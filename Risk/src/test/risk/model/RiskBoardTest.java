/**
 * This Package contains the testcases for the risk.model package
 */
package test.risk.model;

import java.io.File;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import risk.model.RiskBoard;
import risk.utils.MapUtils;
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
		RiskBoard.ProperInstance(true).clear();
		MapUtils.loadFile(new File("Maps/World.map"),true);
	}
	/**
	 * This function runs before the test cases.is to test the added continent.
	 */
	@Test
	public void testAddContinent() {
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("testcontinent"));
		RiskBoard.ProperInstance(true).addContinent("testcontinent", 3);
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("testcontinent"));
		
	}
	/**
	 * This function runs as the test case 1.it is to test if teh continent is removed.
	 */
	@Test
	public void testRemoveContinent() {		
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		RiskBoard.ProperInstance(true).removeContinent("europe");
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
				
	}

	/**
	 * this is to test the added territory.
	 */
	@Test
	public void testAddTerritoryStringString() 
	{
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("lalaland"));
		RiskBoard.ProperInstance(true).addTerritory("europe", "lalaland");
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("lalaland"));
		
	}

	/**
	 * here the validation of the map is been done.
	 * if the map is proper.
	 */
	@Test
	public void testValidateMap() {
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		RiskBoard.ProperInstance(true).addContinent("testcontinent", 3);
		Assert.assertFalse(RiskBoard.ProperInstance(true).validateMap());
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidAtlantis.map"),true));
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidUSA.map"),true));
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidWorld.map"),true));
	}
	/**
	 * this test case checks the removed map.
	 */
	@Test
	public void testRemoveTerritory() {
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("iceland"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		RiskBoard.ProperInstance(true).removeTerritory("europe", "iceland");
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("lalaland"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
	}

}
