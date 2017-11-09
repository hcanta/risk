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
 */
public class RiskBoardTest {

	@Before
	public void setUp() throws Exception 
	{
		RiskBoard.ProperInstance(true).clear();
		MapUtils.loadFile(new File("Maps/World.map"),true);
	}

	@Test
	public void testAddContinent() {
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("testcontinent"));
		RiskBoard.ProperInstance(true).addContinent("testcontinent", 3);
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("testcontinent"));
		
	}

	@Test
	public void testRemoveContinent() {		
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		RiskBoard.ProperInstance(true).removeContinent("europe");
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
				
	}

	@Test
	public void testAddTerritoryStringString() 
	{
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("lalaland"));
		RiskBoard.ProperInstance(true).addTerritory("europe", "lalaland");
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("lalaland"));
		
	}

	@Test
	public void testValidateMap() {
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		RiskBoard.ProperInstance(true).addContinent("testcontinent", 3);
		Assert.assertFalse(RiskBoard.ProperInstance(true).validateMap());
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidAtlantis.map"),true));
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidUSA.map"),true));
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidWorld.map"),true));
	}

	@Test
	public void testRemoveTerritory() {
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("iceland"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		RiskBoard.ProperInstance(true).removeTerritory("europe", "iceland");
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinent("europe").containsTerritory("lalaland"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
	}

}
