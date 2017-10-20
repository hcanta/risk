package risk.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import risk.util.map.RiskBoard;
import risk.util.map.editor.Utilities;

public class UtilitiesTest {

	@Before
	public void setUp() throws Exception 
	{
		RiskBoard.Instance.clear();
		Utilities.loadFile(new File("Maps/World.map"));
	}
	
	@Test
	public void testSaveFile()
	{
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("europe"));
		Assert.assertFalse(RiskBoard.Instance.getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.Instance.validateMap());
		try {
			Utilities.saveMap("file");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		RiskBoard.Instance.clear();
		Assert.assertTrue(Utilities.loadFile(new File("Maps/file.map")));
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("europe"));
	}

	@Test
	public void testLoadFileValid() {
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("europe"));
		Assert.assertFalse(RiskBoard.Instance.getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.Instance.validateMap());
		RiskBoard.Instance.clear();
		Utilities.loadFile(new File("Maps/Atlantis.map"));
		Assert.assertFalse(RiskBoard.Instance.getContinents().contains("europe"));
		Assert.assertTrue(RiskBoard.Instance.getContinents().contains("kala"));
		Assert.assertTrue(RiskBoard.Instance.validateMap());
	}
	
	@Test 
	public void testLoadFileInValid() {
		RiskBoard.Instance.clear();
		Assert.assertFalse(Utilities.loadFile(new File("Maps/invalidAtlantis.map")));
		RiskBoard.Instance.clear();
		Assert.assertTrue(Utilities.loadFile(new File("Maps/Atlantis.map")));
	}

}
