/**
 * This Package contains the testcase and test suite
 * */
package test.risk.utils;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import risk.model.RiskBoard;
import risk.utils.MapUtils;

/**
 * TestFileFor the Map Utils
 *@author hcanta
 *@author Karan
 */
public class MapUtilsTest {

	/**
	 * Connected Matrix;
	 */
	private int[][] connected;

	/**
	 * Disconnected Matrix
	 */
	private int[][] disconnected;
	
	/**
	 * Setups the objects and global variables required for the test cases.
	 * @throws Exception
	 */
	@Before

	/**
	 * the before test case of the connected and disconnected.
	 * @throws Exception
	 */
	public void setUp() throws Exception 
	{

		

		RiskBoard.ProperInstance(true).clear();
		MapUtils.loadFile(new File("Maps/World.map"),true);
		connected = new int[][]{{0,1,0},{1,0,1},{0,1,0}};
		disconnected = new int[][]{{0,1,0},{1,0,0},{0,0,0}};
	}
	
	/**

	 * test case checks the map saving utility of Risk game

	 */
	@Test
	public void testSaveFile() {
		
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		
		try {
			MapUtils.saveMap("file",true);
		} catch (IOException e) {	
			e.printStackTrace();
		}
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertTrue(MapUtils.loadFile(new File("Maps/file.map"),true));
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
	}

	/**

	 * the test case checking the loading of existing valid map file with .map extension

	 */
	@Test
	public void testLoadFileValid() {
		
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
		RiskBoard.ProperInstance(true).clear();
		MapUtils.loadFile(new File("Maps/Atlantis.map"),true);
		Assert.assertFalse(RiskBoard.ProperInstance(true).getContinents().contains("europe"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.ProperInstance(true).validateMap());
	}


	/**
  
	 * the test case testing the load file to be invalid

	 */
	@Test 
	public void testLoadFileInValid() {
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidAtlantis.map"),true));
		RiskBoard.ProperInstance(true).clear();
		Assert.assertTrue(MapUtils.loadFile(new File("Maps/Atlantis.map"),true));
	}
	
	/**

	 * to check the loaded file invalid and disconnected graph.

	 */
	@Test 
	public void testLoadFileInValidDisconnected() {
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/Disconnected.map"),true));

	}

	/**
	 * to check the disconnected matrix.	

	
	 */
	@Test 
	public void testDisconnectedMatrix() {
		
		Assert.assertFalse(MapUtils.performTraversal(disconnected) == 3);
	}
	
	/**

	 * testing the connected matrix

	 */
	@Test 
	public void testConnectedMatrix() {
		
		Assert.assertTrue(MapUtils.performTraversal(connected) == 3);

	}

}
