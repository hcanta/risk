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
<<<<<<< HEAD
	/**
	 * teh before test case of the connected and disconnected.
	 * @throws Exception
	 */
	public void setUp() throws Exception 
	{
=======
	public void setUp() throws Exception {
		
>>>>>>> e6b14fa01f3d31e97a92c565e82463529789c5c5
		RiskBoard.ProperInstance(true).clear();
		MapUtils.loadFile(new File("Maps/World.map"),true);
		connected = new int[][]{{0,1,0},{1,0,1},{0,1,0}};
		disconnected = new int[][]{{0,1,0},{1,0,0},{0,0,0}};
	}
	
	/**
<<<<<<< HEAD
	 * test case checks the saved file.
=======
	 * tests the map saving utility of Risk game
>>>>>>> e6b14fa01f3d31e97a92c565e82463529789c5c5
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
<<<<<<< HEAD
	 * the test case checking the loaded file.
=======
	 * tests the loading of existing valid map file with .map extension
>>>>>>> e6b14fa01f3d31e97a92c565e82463529789c5c5
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
<<<<<<< HEAD

	/**
	 * the test case testing the load file to be invalid
=======
	
	/**
	 * tests the loading of invalid map file
>>>>>>> e6b14fa01f3d31e97a92c565e82463529789c5c5
	 */
	@Test 
	public void testLoadFileInValid() {
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/invalidAtlantis.map"),true));
		RiskBoard.ProperInstance(true).clear();
		Assert.assertTrue(MapUtils.loadFile(new File("Maps/Atlantis.map"),true));
	}
	
	/**
<<<<<<< HEAD
	 * to check the loaded file invalid and disconnected.
=======
	 * tests the loading of map which is a disconnected graph.
>>>>>>> e6b14fa01f3d31e97a92c565e82463529789c5c5
	 */
	@Test 
	public void testLoadFileInValidDisconnected() {
		
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(MapUtils.loadFile(new File("Maps/Disconnected.map"),true));

	}
<<<<<<< HEAD
	/**
	 * to check the disconnected matrix.	
=======
	
	/**
	 * tests the element traversal in the map file for disconnected graph
>>>>>>> e6b14fa01f3d31e97a92c565e82463529789c5c5
	 */
	@Test 
	public void testDisconnectedMatrix() {
		
		Assert.assertFalse(MapUtils.performTraversal(disconnected) == 3);
	}
	
	/**
<<<<<<< HEAD
	 * testing the connected matrix
=======
	 * tests the element traversal in the map file for connected graph
>>>>>>> e6b14fa01f3d31e97a92c565e82463529789c5c5
	 */
	@Test 
	public void testConnectedMatrix() {
		
		Assert.assertTrue(MapUtils.performTraversal(connected) == 3);

	}

}
