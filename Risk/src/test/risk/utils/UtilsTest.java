/**
 * This Package contains the test-cases and test suite
 * */
package test.risk.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import risk.model.RiskBoard;
import risk.model.maputils.Continent;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.PlayerModel;
import risk.utils.Utils;
import risk.utils.constants.RiskStrings;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;
import risk.utils.constants.RiskIntegers;


/**
 * TestFileFor the Map Utils
 *@author hcanta
 */
public class UtilsTest 
{
	/**
	 * player object
	 */
	private IPlayer owner;

	/**
	 * Connected matrix.
	 */
	private int[][] connected;

	/**
	 * Disconnected matrix.
	 */
	private int[][] disconnected;
	
	/**
	 * Setups the objects and global variables required for the test cases.
	 * @throws Exception Set Up failed
	 *
	 */
	
	@Before
	public void setUp() throws Exception 
	{	
		RiskBoard.ProperInstance(true).clear();
		Utils.loadFile(new File("Maps/World.map"),true);
		connected = new int[][]{{0,1,0},{1,0,1},{0,1,0}};
		disconnected = new int[][]{{0,1,0},{1,0,0},{0,0,0}};
		owner = new PlayerModel("test",PlayerColors.red,(short)1,true,RiskPlayerType.Bot, Strategy.random);
	}
	
	/**
	 * tests the map saving utility of the game.
	 * test case checks the map saving utility of Risk game
	 */
	@Test
	public void testSaveFile() {
		
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		
		try {

			Utils.saveMap("file",true);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertTrue(Utils.loadFile(new File("Maps/file.map"),true));
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
	}

	/**
	 * tests the loading of existing valid map file with .map extension
	 * the test case checking the loading of existing valid map file with .map extension
	 */
	@Test
	public void testLoadFileValid() {
		
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		RiskBoard.ProperInstance(true).clear();
		Utils.loadFile(new File("Maps/Atlantis.map"),true);
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
	}


	/**
	 * the test case testing the load file to be invalid
	 * tests the loading of invalid map file
	 */
	@Test 
	public void testLoadFileInValid() {
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(Utils.loadFile(new File("Maps/invalidAtlantis.map"),true));
		RiskBoard.ProperInstance(true).clear();
		Assert.assertTrue(Utils.loadFile(new File("Maps/Atlantis.map"),true));
	}
	
	/**
	 * to check the loaded file invalid and disconnected graph.
	 */
	@Test 
	public void testLoadFileInValidDisconnected() {
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(Utils.loadFile(new File("Maps/Disconnected.map"),true));

	}

	/**
	 * tests the element traversal in the map file for disconnected graph
	 * to check the disconnected matrix.	
	 */
	@Test 
	public void testDisconnectedMatrix() {
		
		Assert.assertFalse(Utils.performTraversal(disconnected) == 3);
	}
	
	/**
	 * this tests the element traversal in the map file for connected graph
	 */
	@Test 
	public void testConnectedMatrix() {
		
		Assert.assertTrue(Utils.performTraversal(connected) == 3);

	}
	
	/**
	 * Test Save A Territory
	 */
	@Test
	public void testSaveTerritory()
	{
		String[] adj = new String[] {"left", "right","top","bottom"};
		Territory territory = new Territory("hcanta","continent", adj,new Object(),0,0);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.TERRITORY_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e)
        {
        }
		Assert.assertTrue(Utils.saveTerritory(territory,RiskStrings.TERRITORY_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Load a Territory
	 * We create an object Save it and then Load it
	 */
	@Test
	public void testLoadTerritory()
	{
		String[] adj = new String[] {"left", "right","top","bottom"};
		Territory territory = new Territory("hcanta","continent", adj,new Object(),0,0);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.TERRITORY_FILE_TEST;
		territory.setArmyOn(5);
		territory.setOwnerID(owner);
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e)
        {
        }
		Utils.saveTerritory(territory,RiskStrings.TERRITORY_FILE_TEST);
		Assert.assertTrue(Files.exists(Paths.get(str)));
		territory= null;
		Assert.assertNull(territory);
		territory = Utils.loadTerritory(RiskStrings.TERRITORY_FILE_TEST);
		Assert.assertTrue(territory.getTerritoryName().equals("hcanta"));
		Assert.assertTrue(territory.getContinentName().equals("continent"));
		Assert.assertTrue(territory.getOwnerID() == owner.getPlayerID());
		for(int i =0; i< 4; i++)
		{
			Assert.assertTrue(territory.getNeighbours().contains(adj[i]));
		}
	}
	
	/**
	 * Test Save A Continent
	 */
	@Test
	public void testSaveContinent()
	{
		String[] adj = new String[] {"left", "right","top","bottom"};
		Object obj = new Object();
		
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.CONTINENT_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e)
        {
        }
		Continent continent = new Continent("continent", 5, obj);
		continent.addTerritory("hcanta", adj,0,0);
		continent.addTerritory("hcanta1", adj,0,0);
		Assert.assertTrue(Utils.saveContinent(continent,RiskStrings.CONTINENT_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/** Test LoadContinent.
	 * We create a continent and load it
	 * Tests alll the attributes of the continent
	 * Test all the attribute of the territory it contains
	 * */
	@Test
	public void testLoadContinent()
	{
		String[] adj = new String[] {"left", "right","top","bottom"};
		Object obj = new Object();
		
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.CONTINENT_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e)
        {
        }
		Continent continent = new Continent("continent", 5, obj);
		continent.addTerritory("hcanta", adj,0,0);
		continent.addTerritory("hcanta1", adj,0,0);
		Utils.saveContinent(continent,RiskStrings.CONTINENT_FILE_TEST);
		continent= null;
		Assert.assertNull(continent);
		continent = Utils.loadContinent(RiskStrings.CONTINENT_FILE_TEST);
		Assert.assertTrue(continent.getContinentBonus() == 5);
		Assert.assertTrue(continent.getContinentName().equals("continent"));
		Assert.assertTrue(continent.getTerritories().size() == 2);
		
		Territory territory = continent.getTerritory("hcanta");
		Assert.assertTrue(territory.getTerritoryName().equals("hcanta"));
		Assert.assertTrue(territory.getContinentName().equals("continent"));
		Assert.assertTrue(territory.getOwnerID() == RiskIntegers.INITIAL_OWNER);
		for(int i =0; i< 4; i++)
		{
			Assert.assertTrue(territory.getNeighbours().contains(adj[i]));
		}
		
		territory = continent.getTerritory("hcanta1");
		Assert.assertTrue(territory.getTerritoryName().equals("hcanta1"));
		Assert.assertTrue(territory.getContinentName().equals("continent"));
		Assert.assertTrue(territory.getOwnerID() == RiskIntegers.INITIAL_OWNER);
		for(int i =0; i< 4; i++)
		{
			Assert.assertTrue(territory.getNeighbours().contains(adj[i]));
		}
	}
	 
}
