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

import risk.game.cards.Card;
import risk.game.cards.Hand;
import risk.model.BotPlayerModel;
import risk.model.HumanPlayerModel;
import risk.model.RiskBoard;
import risk.model.maputils.Continent;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.PlayerModel;
import risk.utils.Utils;
import risk.utils.constants.RiskStrings;
import risk.utils.constants.RiskEnum.CardType;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;
import risk.utils.constants.RiskIntegers;


/**
 * TestFileFor the Map Utils
 *@author hcanta
 *@version 4.1
 */
public class UtilsTest 
{
	/**
	 * The RiskBoard being used for testing
	 */
	private RiskBoard board;
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
		board = RiskBoard.Instance;
		board.clear();
		Utils.loadFile(new File("Maps/World.map"));
		connected = new int[][]{{0,1,0},{1,0,1},{0,1,0}};
		disconnected = new int[][]{{0,1,0},{1,0,0},{0,0,0}};
		owner = new PlayerModel("test",PlayerColors.red,(short)1,RiskPlayerType.Bot, Strategy.aggressive);
		owner.addTerritory("territory");
	}
	
	/**
	 * tests the map saving utility of the game.
	 * test case checks the map saving utility of Risk game
	 */
	@Test
	public void testSaveFile() {
		
		Assert.assertTrue(board.getContinents().contains("europe"));
		Assert.assertFalse(board.getContinents().contains("kala"));
		Assert.assertTrue(board.validateMap());
		
		try {

			Utils.saveMap("file");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		board.clear();
		Assert.assertTrue(Utils.loadFile(new File("Maps/file.map")));
		Assert.assertTrue(board.getContinents().contains("europe"));
	}

	/**
	 * tests the loading of existing valid map file with .map extension
	 * the test case checking the loading of existing valid map file with .map extension
	 */
	@Test
	public void testLoadFileValid() {
		
		Assert.assertTrue(board.getContinents().contains("europe"));
		Assert.assertFalse(board.getContinents().contains("kala"));
		Assert.assertTrue(board.validateMap());
		board.clear();
		Utils.loadFile(new File("Maps/Atlantis.map"));
		Assert.assertFalse(board.getContinents().contains("europe"));
		Assert.assertTrue(board.getContinents().contains("kala"));
		Assert.assertTrue(board.validateMap());
	}


	/**
	 * the test case testing the load file to be invalid
	 * tests the loading of invalid map file
	 */
	@Test 
	public void testLoadFileInValid() {
		
		board.clear();
		Assert.assertFalse(Utils.loadFile(new File("Maps/invalidAtlantis.map")));
		board.clear();
		Assert.assertTrue(Utils.loadFile(new File("Maps/Atlantis.map")));
	}
	
	/**
	 * to check the loaded file invalid and disconnected graph.
	 */
	@Test 
	public void testLoadFileInValidDisconnected() {
		
		board.clear();
		Assert.assertFalse(Utils.loadFile(new File("Maps/Disconnected.map")));

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
		Territory territory = new Territory("hcanta","continent", adj,0,0);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.TERRITORY_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}
		
		Assert.assertTrue(Utils.saveTerritory(territory,RiskStrings.TERRITORY_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Save A Card
	 */
	@Test
	public void testSaveCard()
	{
		Card card = new Card(CardType.Cavalry, "Canada");
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.CARD_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}
		
		Assert.assertTrue(Utils.saveCard(card,RiskStrings.CARD_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Save and Load A Card
	 */
	@Test
	public void testSaveLoadCard()
	{
		Card card = new Card(CardType.Cavalry, "Canada");
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.CARD_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}
		
		Assert.assertTrue(Utils.saveCard(card,RiskStrings.CARD_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
		card= null;
		Assert.assertNull(card);
		card = Utils.loadCard(RiskStrings.CARD_FILE_TEST);
		Assert.assertTrue(card.getType() == CardType.Cavalry);
		Assert.assertTrue(card.getTerritory().equals("Canada"));
		
	}
	
	/**
	 * Test Load a Territory
	 * We create an object Save it and then Load it
	 */
	@Test
	public void testLoadTerritory()
	{
		String[] adj = new String[] {"left", "right","top","bottom"};
		Territory territory = new Territory("hcanta","continent", adj,0,0);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.TERRITORY_FILE_TEST;
		territory.setArmyOn(5);
		territory.setOwnerID(owner);
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}
		
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
		
		
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.CONTINENT_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}
		
		Continent continent = new Continent("continent", 5);
		continent.addTerritory("hcanta", adj,0,0);
		continent.addTerritory("hcanta1", adj,0,0);
		Assert.assertTrue(Utils.saveContinent(continent,RiskStrings.CONTINENT_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Save A Board
	 */
	@Test
	public void testSaveBoard()
	{
		board.clear();
		Utils.loadFile(new File("Maps/Demo.map"));
		board.setNbCardsExchanged(5);
		board.setNbRoundsPlayed(3);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.BOARD_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.saveBoard(board,RiskStrings.BOARD_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Load Continent
	 * Test The board
	 */
	@Test
	public void testLoadBoard()
	{
		board.clear();
		Utils.loadFile(new File("Maps/Demo.map"));
		Assert.assertTrue(board.getnbCardExchanged() == 0);
		Assert.assertTrue(board.getnbRoundsPlayed() == 0);
		Assert.assertTrue(board.getContinents().size() ==2);
		board.clear();
		Assert.assertTrue(board.getContinents().size() == 0);
		Utils.loadBoard(board, RiskStrings.BOARD_FILE_TEST);
		Assert.assertTrue(board.getContinents().size() == 2);
		for(int i =0; i< board.getNbTerritories(); i++)
		{
			Territory territory = board.getTerritory(board.getTerritories().get(i));
			Assert.assertTrue(territory.getNeighbours().size() == 38);
		}
		Assert.assertTrue(board.getnbCardExchanged() == 5);
		Assert.assertTrue(board.getnbRoundsPlayed() == 3);
			
	}
	/** Test LoadContinent.
	 * We create a continent and load it
	 * Tests all the attributes of the continent
	 * Test all the attribute of the territory it contains
	 * */
	@Test
	public void testLoadContinent()
	{
		String[] adj = new String[] {"left", "right","top","bottom"};
		
		
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.CONTINENT_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e)
        {
        }
		Continent continent = new Continent("continent", 5);
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
	
	/**
	 * Test Save A  player
	 */
	@Test
	public void testSavePlayerModel()
	{

		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.PLAYER_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.savePlayer(owner,RiskStrings.PLAYER_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Save A  Human player
	 */
	@Test
	public void testSaveHumanPlayerModel()
	{

		owner = new HumanPlayerModel("test",PlayerColors.red,(short)1);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.HUMAN_PLAYER_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.savePlayer(owner,RiskStrings.HUMAN_PLAYER_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Save A  Bot player
	 */
	@Test
	public void testSaveBotPlayerModel()
	{

		owner = new BotPlayerModel("test",PlayerColors.red,(short)1, Strategy.random);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.BOT_PLAYER_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.savePlayer(owner,RiskStrings.BOT_PLAYER_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	 
	/**
	 * Test Save and Load A  player
	 */
	@Test
	public void testSaveLoadPlayerModel()
	{

		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.PLAYER_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.savePlayer(owner,RiskStrings.PLAYER_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
		owner = null;
		Assert.assertNull(owner);
		owner = (PlayerModel) Utils.loadPlayer(RiskStrings.PLAYER_FILE_TEST);
		Assert.assertTrue(owner.getName().equals("test"));
		Assert.assertTrue(owner.getColor() == PlayerColors.red);
		Assert.assertTrue(owner.getType() == RiskPlayerType.Bot);
		Assert.assertTrue(owner.getStrategy()== Strategy.aggressive);
		Assert.assertTrue(owner.getPlayerID() == 1);
		Assert.assertTrue(owner.getTerritoriesOwned().size() == 1);
	}
	
	/**
	 * Test Save and Load A  Human player
	 */
	@Test
	public void testSaveAndLoadHumanPlayerModel()
	{

		owner = new HumanPlayerModel("human",PlayerColors.green,(short)2);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.HUMAN_PLAYER_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.savePlayer(owner,RiskStrings.HUMAN_PLAYER_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
		owner = null;
		Assert.assertNull(owner);
		owner = (HumanPlayerModel) Utils.loadPlayer(RiskStrings.HUMAN_PLAYER_FILE_TEST);
		Assert.assertTrue(owner.getName().equals("human"));
		Assert.assertTrue(owner.getColor() == PlayerColors.green);
		Assert.assertTrue(owner.getType() == RiskPlayerType.Human);
		Assert.assertTrue(owner.getStrategy()== Strategy.human);
		Assert.assertTrue(owner.getPlayerID() == 2);
	}
	
	/**
	 * Test Save and loadA  Bot player
	 */
	@Test
	public void testSaveAndLoadBotPlayerModel()
	{

		owner = new BotPlayerModel("bot",PlayerColors.blue,(short)3, Strategy.random);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.BOT_PLAYER_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.savePlayer(owner,RiskStrings.BOT_PLAYER_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
		owner = null;
		Assert.assertNull(owner);
		owner = (BotPlayerModel) Utils.loadPlayer(RiskStrings.BOT_PLAYER_FILE_TEST);
		Assert.assertTrue(owner.getName().equals("bot"));
		Assert.assertTrue(owner.getColor() == PlayerColors.blue);
		Assert.assertTrue(owner.getType() == RiskPlayerType.Bot);
		Assert.assertTrue(owner.getStrategy()== Strategy.random);
		Assert.assertTrue(owner.getPlayerID() == 3);
		Assert.assertTrue(owner.getHand().size() == 0);
	}
	
	/**
	 * Test Save A Hand
	 */
	@Test
	public void testSaveHand()
	{
		Card card1 = new Card(CardType.Cavalry, "Canada");
		Card card2 = new Card(CardType.Infantry, "providence");
		Hand hand = new Hand();
		hand.add(card1);
		hand.add(card2);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.HAND_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}
		
		Assert.assertTrue(Utils.saveHand(hand,RiskStrings.HAND_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
	}
	
	/**
	 * Test Save and Load A Hand
	 */
	@Test
	public void testSaveLoadHand()
	{
		Card card1 = new Card(CardType.Cavalry, "Canada");
		Card card2 = new Card(CardType.Infantry, "providence");
		Hand hand = new Hand();
		hand.add(card1);
		hand.add(card2);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.HAND_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}
		
		Assert.assertTrue(Utils.saveHand(hand,RiskStrings.HAND_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
		hand= null;
		Assert.assertNull(hand);
		hand = Utils.loadHand(RiskStrings.HAND_FILE_TEST);
		Assert.assertTrue(hand.size() == 2);
		
		Assert.assertTrue(hand.getCards().get(0).getType() == CardType.Cavalry);
		Assert.assertTrue(hand.getCards().get(0).getTerritory().equals("Canada"));
		
		Assert.assertTrue(hand.getCards().get(1).getType() == CardType.Infantry);
		Assert.assertTrue(hand.getCards().get(1).getTerritory().equals("providence"));
		
	}
	
	/**
	 * Test Save and loadA  Bot player With Hand
	 */
	@Test
	public void testSaveAndLoadBotPlayerModelWithHand()
	{

		owner = new BotPlayerModel("bot",PlayerColors.blue,(short)3, Strategy.random);
		Card card1 = new Card(CardType.Cavalry, "Canada");
		Card card2 = new Card(CardType.Infantry, "providence");
		Hand hand = new Hand();
		hand.add(card1);
		hand.add(card2);
		owner.setHand(hand);
		Path currentRelativePath = Paths.get("");
		String str = currentRelativePath.toAbsolutePath().toString()+"\\"+RiskStrings.BOT_HAND_PLAYER_FILE_TEST;
		try
		{
            Files.deleteIfExists(Paths.get(str));
        }
        catch(IOException e){}

		Assert.assertTrue(Utils.savePlayer(owner,RiskStrings.BOT_HAND_PLAYER_FILE_TEST));
		Assert.assertTrue(Files.exists(Paths.get(str)));
		owner = null;
		hand = null;
		Assert.assertNull(hand);
		Assert.assertNull(owner);
		owner = (BotPlayerModel) Utils.loadPlayer(RiskStrings.BOT_HAND_PLAYER_FILE_TEST);
		Assert.assertTrue(owner.getName().equals("bot"));
		Assert.assertTrue(owner.getColor() == PlayerColors.blue);
		Assert.assertTrue(owner.getType() == RiskPlayerType.Bot);
		Assert.assertTrue(owner.getStrategy()== Strategy.random);
		Assert.assertTrue(owner.getPlayerID() == 3);
		hand = owner.getHand();
		Assert.assertTrue(hand.getCards().get(0).getType() == CardType.Cavalry);
		Assert.assertTrue(hand.getCards().get(0).getTerritory().equals("Canada"));
		
		Assert.assertTrue(hand.getCards().get(1).getType() == CardType.Infantry);
		Assert.assertTrue(hand.getCards().get(1).getTerritory().equals("providence"));
	}
}
