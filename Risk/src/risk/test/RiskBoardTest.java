package risk.test;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import risk.util.map.RiskBoard;
import risk.util.map.editor.Utilities;
/**
 * 
 * Test Class for the RiskBoard
 *
 */
public class RiskBoardTest {

	@Before
	public void setUp() throws Exception 
	{
		RiskBoard.Instance.clear();
		Utilities.loadFile(new File("Maps/World.map"));
	}

	@Test
	public void testAddContinent() {
		Assert.assertFalse(RiskBoard.Instance.getContinents().contains("testcontinent"));
		RiskBoard.Instance.addContinent("testcontinent", 3);
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("testcontinent"));
		
	}

	@Test
	public void testRemoveContinent() {		
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("europe"));
		RiskBoard.Instance.removeContinent("europe");
		Assert.assertFalse(RiskBoard.Instance.getContinents().contains("europe"));
				
	}

	@Test
	public void testAddTerritoryStringString() 
	{
		Assert.assertFalse(RiskBoard.Instance.getContinent("europe").containsTerritory("lalaland"));
		RiskBoard.Instance.addTerritory("europe", "lalaland");
		Assert.assertTrue(RiskBoard.Instance.getContinent("europe").containsTerritory("lalaland"));
		
	}

	@Test
	public void testValidateMap() {
		Assert.assertTrue(RiskBoard.Instance.validateMap());
		RiskBoard.Instance.addContinent("testcontinent", 3);
		Assert.assertFalse(RiskBoard.Instance.validateMap());
		Assert.assertFalse(Utilities.loadFile(new File("Maps/invalidAtlantis.map")));
		Assert.assertFalse(Utilities.loadFile(new File("Maps/invalidUSA.map")));
		Assert.assertFalse(Utilities.loadFile(new File("Maps/invalidWorld.map")));
	}

	@Test
	public void testRemoveTerritory() {
		Assert.assertTrue(RiskBoard.Instance.getContinent("europe").containsTerritory("iceland"));
		Assert.assertTrue(RiskBoard.Instance.validateMap());
		RiskBoard.Instance.removeTerritory("europe", "iceland");
		Assert.assertFalse(RiskBoard.Instance.getContinent("europe").containsTerritory("lalaland"));
		Assert.assertTrue(RiskBoard.Instance.validateMap());
	}

}
