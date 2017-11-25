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
import risk.utils.Utils;
/**
 * 
 * TestFileFor the Map Utils
 *@author hcanta
 */
public class UtilsTest {

	/**
	 * Connected Matrix;
	 */
	private int[][] connected;
	/**
	 * Disconnected Matrix
	 */
	private int[][] disconnected;
	@Before
	public void setUp() throws Exception 
	{
		RiskBoard.ProperInstance(true).clear();
		Utils.loadFile(new File("Maps/World.map"),true);
		connected = new int[][]{{0,1,0},{1,0,1},{0,1,0}};
		disconnected = new int[][]{{0,1,0},{1,0,0},{0,0,0}};
	}
	
	@Test
	public void testSaveFile()
	{
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
	
	@Test 
	public void testLoadFileInValid() {
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(Utils.loadFile(new File("Maps/invalidAtlantis.map"),true));
		RiskBoard.ProperInstance(true).clear();
		Assert.assertTrue(Utils.loadFile(new File("Maps/Atlantis.map"),true));
	}
	
	@Test 
	public void testLoadFileInValidDisconnected() {
		RiskBoard.ProperInstance(true).clear();
		Assert.assertFalse(Utils.loadFile(new File("Maps/Disconnected.map"),true));

	}
	
	@Test 
	public void testDisconnectedMatrix() {
		
		Assert.assertFalse(Utils.performTraversal(disconnected) == 3);
	}
	
	@Test 
	public void testConnectedMatrix() {
		
		Assert.assertTrue(Utils.performTraversal(connected) == 3);

	}

}
