/**
 *  Package contains the various static, constant and helper method of the game
 */
package risk.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Stack;

import risk.game.GameEngine;
import risk.game.cards.Card;
import risk.game.cards.Hand;
import risk.model.BotPlayerModel;
import risk.model.HumanPlayerModel;
import risk.model.RiskBoard;
import risk.model.maputils.Continent;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.utils.constants.RiskEnum.GameState;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;
import risk.utils.constants.RiskIntegers;

/**
 * This class contains implementation for load and save functions
 * @author hcanta
 * @version 3.3
 */
public class Utils implements Serializable
{
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 5550477455386115579L;

	/**
	 * Load The Map On the RiskBoard
	 * @param file the file to be loaded in the Risk Board
	 * @return  was the map loaded valid or not
	 */
	public static boolean loadFile(File file) {
		RiskBoard.Instance.clear();
		try
		{
			FileReader fileReader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			RiskBoard.Instance.setBoardName(file.getName());
			
			while ((line = bufferedReader.readLine()) != null) 
			{
				if(line.equals("[Continents]"))
				{
					break;
				}
			}
			
			
			while ((line = bufferedReader.readLine()) != null) 
			{
				if(line.equals("[Territories]"))
				{
					break;
				}
				else if(line.length()>0)
				{
					String[] parts = line.split("=");
					if(parts.length == 2)
					{
						RiskBoard.Instance.addContinent(parts[0].toLowerCase().trim(), Integer.parseInt(parts[1].trim()));
					}
				}
			}
			int xcoord = 0;
			int ycoord = RiskIntegers.GRAPH_CELL_Y_OFFSET;
			while ((line = bufferedReader.readLine()) != null) 
			{
				if(line.length()>0)
				{
					String[] parts = line.split(",");
					if(parts.length >=5)
					{
						String territoryName= parts[0].toLowerCase().trim();
						 xcoord  +=RiskIntegers.GRAPH_CELL_X_OFFSET ;
						 if(xcoord% ( RiskIntegers.GRAPH_CELL_X_OFFSET*(RiskIntegers.CELL_PER_ROWS+ 1)) == 0)
						 {
							 xcoord = RiskIntegers.GRAPH_CELL_X_OFFSET;
							 ycoord += RiskIntegers.GRAPH_CELL_Y_OFFSET;
						 }
						 
						String continentName = parts[3].toLowerCase().trim();
						String [] neighbours =new String[parts.length - 4];
						for(int i =4; i< parts.length; i++)
						{
							neighbours[i-4] = parts[i].toLowerCase().trim();
						}
						RiskBoard.Instance.addTerritory(continentName, territoryName, neighbours,xcoord, ycoord);
					}
					else
					{
						return false;
					}
				}
			}
			bufferedReader.close();
			fileReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return RiskBoard.Instance.validateMap();
		
	}
	
	/**
	 * This Function performs a traversal and returned the number of elements visited
	 * @param adjMatrix The adjacency Matrix on which the traversal is to be performed
	 * @return  The number of element that were traversed
	 */
	public static int performTraversal( int[][] adjMatrix)
	{
		boolean[] isVisited =  new boolean[adjMatrix[0].length];
		for(int i =0; i < isVisited.length; ++i)
			isVisited[i] = false;
		Stack<Integer> stack = new Stack<Integer>(); 
		stack.push(0);
		int count = 0;
		int current;
		while(!stack.isEmpty())
		{
			current = stack.pop();
			if(isVisited[current])
			{
				continue;
			}
			count++;
			isVisited[current] = true;
			
			for(int i =0; i< isVisited.length; i++)
			{
				if(adjMatrix[current][i] == 1 && !isVisited[i])
				{
					stack.push(i);
				}
			}
		}
		
		return count;
	}

	/**
	 * Save the Map to a .map file
	 * @param filename the name of the file where the risk board will be saved
	 * @throws IOException Could not open/write to file
	 */
	public static void saveMap(String filename) throws IOException
	{
		String n_filename = "Maps\\"+filename + ".map";
		FileWriter fw = new FileWriter(n_filename);
		
		fw.write("[Map]\n");
		fw.write("author = team1\n\n");
		fw.write("[Continents]\n");
		fw.write(RiskBoard.Instance.continentsToString());
		fw.write("\n[Territories]\n");
		
		fw.write(RiskBoard.Instance.territoriesToString());
		
		fw.close();
	}

	/**
	 * Search for the index of string given an array
	 * @param str The string that index we search
	 * @param array the array in which the search occurs
	 * @return the index of str in the array
	 */
	public static int getIndexOf(String str, String[] array) 
	{
		for(int i=0; i< array.length; i++)
		{
			if(str.equalsIgnoreCase(array[i]))
			{
				return i;
			}
		}
		return RiskIntegers.ERROR;
	}
	
	/**
	 * Saves the given territory
	 * @param territory The Territory to be saved
	 * @param filePath the file where it must be saved 
	 * @return If the game was saved successfully or not
	 */
	public static boolean saveTerritory(Territory territory, String filePath)
	{
		try 
		{
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(territory);
			out.close();
			fileOut.close();
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Saves the given Card
	 * @param card The Territory to be saved
	 * @param filePath the file where it must be saved 
	 * @return If the card was saved successfully or not
	 */
	public static boolean saveCard(Card card, String filePath)
	{
		try 
		{
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(card);
			out.close();
			fileOut.close();
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Loads a Card from a file
	 * @param relativePath The relative Path to the file
	 * @return the Card object
	 */
	public static Card loadCard(String relativePath)
	{
		Card obj = null;
		try 
		{
			FileInputStream fileIn = new FileInputStream(relativePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj =(Card) in.readObject();
			in.close();
	        fileIn.close();
		} 
		catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * Loads a territory from a file
	 * @param relativePath The relative Path to the file
	 * @return the Territory object
	 */
	public static Territory loadTerritory(String relativePath)
	{
		Territory obj = null;
		try 
		{
			FileInputStream fileIn = new FileInputStream(relativePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj =(Territory) in.readObject();
			in.close();
	        fileIn.close();
		} 
		catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * Returns A color object
	 * @param color The color of the player
	 * @return the corresponding Java awt color
	 */
	public static Color getColor(PlayerColors color) 
	{
		Color nColor = null;
		switch(color)
		{
			case blue:
				nColor = Color.blue;
				break;
			case red:
				nColor = Color.red;
				break;
			case green:
				nColor = Color.green;
				break;
			case gray:
				nColor = Color.GRAY;
				break;
			case pink:
				nColor = Color.PINK;
				break;
			case yellow:
				nColor = Color.YELLOW;
				break;
			default:
				nColor= Color.white;
		}
		return nColor;
	}
	
	
	
	/**
	 * Saves the given continent
	 * @param continent The continent to be saved
	 * @param filePath the file where it must be saved 
	 * @return If the game was saved successfully or not
	 */
	public static boolean saveContinent(Continent continent, String filePath)
	{
		String cont = saveContinentToString(continent);
		if(cont.length() == 0)
			return false;
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString()+"\\"+filePath;
		try 
		{
			FileWriter fw = new FileWriter(path);
			fw.write(cont);
			fw.close();
			return true;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Saves the given board
	 * @param board The board to be saved
	 * @param filePath the file where it must be saved 
	 * @return If the game was saved successfully or not
	 */
	public static boolean saveBoard(RiskBoard board, String filePath)
	{
		String cont = saveBoardToString(board);
		if(cont.length() == 0)
			return false;
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString()+"\\"+filePath;
		try 
		{
			FileWriter fw = new FileWriter(path);
			fw.write(cont);
			fw.close();
			return true;
		} 
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
		return false;
	}
	

	
	/**
	 * creates a string representation of the continent for saving 
	 * @param continent The continent to be saved
	 * @return A string representation of the continent 
	 */
	private static String saveContinentToString(Continent continent)
	{
		StringBuffer str = new StringBuffer();
		
		str.append(dataToString(continent.getContinentName()));
		str.append("\n");
		
		str.append(dataToString(continent.getContinentBonus()));
		str.append("\n");
		
		str.append(dataToString(continent.getOwnerID()));
		str.append("\n");
		
		str.append(dataToString(continent.getTerritories().size()));
		str.append("\n");
		for(int i =0; i< continent.getTerritories().size();i ++)
		{
			str.append(dataToString(continent.getTerritories().get(i)));
			str.append("\n");
			str.append(dataToString(continent.getTerritory(continent.getTerritories().get(i))));
			if( i != continent.getTerritories().size() -1)
				str.append("\n");
		}
		
		return str.toString();
		
	}
	
	/**
	 * creates a string representation of the board for saving 
	 * @param board The risk board to be saved
	 * @return A string representation of the board 
	 */
	private static String saveBoardToString(RiskBoard board)
	{
		StringBuffer str = new StringBuffer();
		str.append(dataToString(board.getBoardName()));
		str.append("\n");
		str.append(dataToString(board.getCurrentPlayer()));
		str.append("\n");
		str.append(dataToString(board.getState()));
		str.append("\n");
		str.append(dataToString(board.getOwnerID()));
		str.append("\n");
		str.append(dataToString(board.getnbCardExchanged()));
		str.append("\n");
		str.append(dataToString(board.getnbRoundsPlayed()));
		str.append("\n");
		str.append(dataToString(board.getContinents().size()));
		str.append("\n");
		
		for(int i =0; i < board.getContinents().size(); i++)
		{
			str.append(saveContinentToString(board.getContinent(board.getContinents().get(i))));
			if( i!= board.getContinents().size() - 1)
			str.append("\n");
		}
		return str.toString();
		
	}
	/**
	 * Loads a Board from a file
	 * @param board the RiskBoard
	 * @param relativePath The relative path where the board is store
	 */
	public static void loadBoard(RiskBoard board, String relativePath)
	{
		board.clear();
		Path currentRelativePath = Paths.get("");
		try
		{
			String path = currentRelativePath.toAbsolutePath().toString()+"\\"+relativePath;
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			loadBoard(board, bufferedReader);
			bufferedReader.close();
			fileReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Loads a Board from a file
	 * @param board the RiskBoard
	 * @param bufferedReader The reader of the file
	 * @return was the board successfully loaded
	 */
	private static boolean loadBoard(RiskBoard board, BufferedReader bufferedReader)
	{
		board.clear();
		String name, currentPlayer;
		GameState state;
		int ownerID, nbContinent, rounds, cards;
		
		try
		{

			String line;
			line = bufferedReader.readLine();
			name = (String)stringToData(line);
			line = bufferedReader.readLine();
			currentPlayer = (String)stringToData(line);
			line = bufferedReader.readLine();
			state = (GameState)stringToData(line);;
			line = bufferedReader.readLine();
			ownerID = (int)stringToData(line);
			line = bufferedReader.readLine();
			cards = (int)stringToData(line);
			line = bufferedReader.readLine();
			rounds = (int)stringToData(line);
			line = bufferedReader.readLine();
			nbContinent = (int)stringToData(line);
			for(int i =0; i< nbContinent; i++)
			{
				Continent cont = loadContinent(bufferedReader);
				board.addContinent(cont);
			}
			board.setBoardName(name);
			board.setCurrentPlayer(currentPlayer);
			board.setOwnerID(ownerID);
			board.setState(state);
			board.setNbRoundsPlayed(rounds);
			board.setNbCardsExchanged(cards);
			return true;
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
			
	}
	/**
	 * Loads a Continent from a file
	 * @param relativePath The relative Path to the file
	 * @return the Continent object
	 */
	public static Continent loadContinent(String relativePath)
	{

		Path currentRelativePath = Paths.get("");
		try
		{
			String path = currentRelativePath.toAbsolutePath().toString()+"\\"+relativePath;
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Continent cont = loadContinent(bufferedReader);
			bufferedReader.close();
			fileReader.close();
			return cont;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Loads a Continent from a file
	 * @param bufferedReader The buffered reader
	 * @return the Continent object
	 */
	private static Continent loadContinent(BufferedReader bufferedReader)
	{
		String continentName;
	
		int ownerID;
		
		int bonus;
		try
		{
			
			String line;
			line = bufferedReader.readLine();
			continentName = (String)stringToData(line);
			line = bufferedReader.readLine();
			bonus = (int)stringToData(line);
			line = bufferedReader.readLine();
			ownerID = (int)stringToData(line);
			line = bufferedReader.readLine();
			int nbTerritories = (int)stringToData(line);
			Continent cont = new Continent(continentName,bonus);
			cont.setOwnerID(ownerID);
			for(int i =0; i< nbTerritories; i++)
			{
				line = bufferedReader.readLine();
				String name = (String)stringToData(line);
				line = bufferedReader.readLine();
				Territory territory = (Territory)stringToData(line);
				cont.addTerritory(name, territory);
			}
			
			return cont;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Convert a serializable object to a string
	 * @param o the object to be converted
	 * @return the string representation of the object
	 */ 
	private static String dataToString(Serializable o)
	{
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 ObjectOutputStream oos;
		try 
		{
			 oos = new ObjectOutputStream(baos);
			 oos.writeObject(o);
			 oos.close();
			 return Base64.getEncoder().encodeToString(baos.toByteArray());
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Convert from String to Object
	 * @param str string info
	 * @return the object
	 */
	private static Object stringToData(String str)
	{
		 byte [] data = Base64.getDecoder().decode( str );
		  try 
		  {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o  = ois.readObject();
			ois.close();
			return o;
		  } 
		  catch (IOException | ClassNotFoundException e) 
		  {
			
			e.printStackTrace();
		  }
		return null;		
	}
	
	/**
	 * creates a string representation of the board for saving 
	 * @param player The risk board to be saved
	 * @return A string representation of the board 
	 */
	private static String savePlayerToString(IPlayer player)
	{
		StringBuffer str = new StringBuffer();
		
		str.append(dataToString(player.getStrategy()));
		str.append("\n");
		str.append(dataToString(player.getType()));
		str.append("\n");
		str.append(saveHandToString(player.getHand()));
		if(player.getHand().size() >= 1)
			str.append("\n");
		str.append(dataToString(player.getName()));
		str.append("\n");
		str.append(dataToString(player.getPlayerID()));
		str.append("\n");
		str.append(dataToString(player.getColor()));
		str.append("\n");
		str.append(dataToString(player.getNbArmiesToBePlaced()));
		str.append("\n");
		str.append(dataToString(player.getTerritoriesOwned()));

		return str.toString();
		
	}
	
	/**
	 * Saves the given continent
	 * @param player The player to be saved
	 * @param filePath the file where it must be saved 
	 * @return If the game was saved successfully or not
	 */
	public static boolean savePlayer(IPlayer player, String filePath)
	{
		String strPlayer = savePlayerToString(player);
		if(strPlayer.length() == 0)
			return false;
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString()+"\\"+filePath;
		try 
		{
			FileWriter fw = new FileWriter(path);
			fw.write(strPlayer);
			fw.close();
			return true;
		} 
		catch (IOException e)
		{	
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Loads a Player object from a file
	 * @param relativePath The relative Path to the file
	 * @return the IPlayer object
	 */
	public static IPlayer loadPlayer(String relativePath)
	{

		Path currentRelativePath = Paths.get("");
		try
		{
			String path = currentRelativePath.toAbsolutePath().toString()+"\\"+relativePath;
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			IPlayer player = loadPlayer(bufferedReader);
			bufferedReader.close();
			fileReader.close();
			return player;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loads a IPlayer from a file
	 * @param bufferedReader The buffered reader
	 * @return the Player object
	 */
	private static IPlayer loadPlayer(BufferedReader bufferedReader)
	{
		try
		{			
			String line;
			line = bufferedReader.readLine();
			Strategy strategy = (Strategy)(stringToData(line));
			line = bufferedReader.readLine();
			RiskPlayerType type = (RiskPlayerType)(stringToData(line));
			
			Hand hand = (Hand)(loadHand(bufferedReader));
			
			line = bufferedReader.readLine();
			String name = (String)(stringToData(line));
			
			line = bufferedReader.readLine();			
			short id = (short)(stringToData(line));
			
			line = bufferedReader.readLine();
			PlayerColors color = (PlayerColors)(stringToData(line));
			
			line = bufferedReader.readLine();
			int nbArmies= (int)(stringToData(line));
			
			line = bufferedReader.readLine();
			@SuppressWarnings("unchecked")
			ArrayList<String> territories = (ArrayList<String>)(stringToData(line));
				
			
			IPlayer player; 
			if(type == RiskPlayerType.Human)
			{
				player = new HumanPlayerModel(name, color, id);
				player.setNbArmiesToBePlaced(nbArmies);
				player.setHand(hand);
				player.setTerritories(territories);
			}
			else
			{
				player = new BotPlayerModel(name, color, id,strategy);
				player.setNbArmiesToBePlaced(nbArmies);
				player.setHand(hand);
				player.setTerritories(territories);
			}
			return player;			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Saves the given continent
	 * @param engine The game engine to be saved
	 * @param fileName the file where it must be saved 
	 * @return If the game was saved successfully or not
	 */
	public static boolean saveGame(GameEngine engine, String fileName)
	{
		StringBuffer info = new StringBuffer();
		info.append(saveBoardToString(engine.getBoard()));
		info.append("\n");
		info.append(saveEngineSpecificAttribute(engine));
		
		if(info.length() == 0)
			return false;
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString()+"\\SavedGames\\"+fileName+".risk";
		
		
		try 
		{
			FileWriter fw = new FileWriter(path);
			fw.write(info.toString());
			fw.close();
			return true;
		} 
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Saves The attribute specific to the Game Engine
	 * @param engine The Game Engine
	 * @return A string representation of the engine Attribute
	 */
	private static String saveEngineSpecificAttribute(GameEngine engine) {
		StringBuffer info = new StringBuffer();
		info.append(dataToString(engine.getCurrentPlayer()));
		info.append("\n");
		info.append(dataToString(engine.getMaxRounds()));
		info.append("\n");
		info.append(dataToString(engine.getPlayerTurnOrder()));
		info.append("\n");
		info.append(dataToString(engine.getNumberOfPlayers()));
		info.append("\n");
		for(int i=0; i< engine.getPlayerTurnOrder().size(); i++)
		{
			String str = savePlayerToString(engine.getPlayer(engine.getPlayerTurnOrder().get(i)));
			if(str == null || str.length() == 0)
				return null;
			info.append(str);
			info.append("\n");
		}
		info.append(dataToString(engine.getHistory()));
		info.append("\n");
		info.append(dataToString(engine.getCountryInfo()));
		return info.toString();
	}
	
	/**
	 * Loads the Game
	 * @param engine the game engine
	 * @param gamePath the path to where the game is saved
	 * @return was the game loaded successfuly
	 */
	public static boolean  loadGame(GameEngine engine, String gamePath)
	{
		Path currentRelativePath = Paths.get("");
		try
		{
			engine.clear();
			String path = currentRelativePath.toAbsolutePath().toString()+"\\SavedGames\\"+gamePath;
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			if(!loadBoard(engine.getBoard(), bufferedReader))
				return false;

			String line; 
			line = bufferedReader.readLine();
			String currentPlayer = (String)stringToData(line);
			engine.setCurrentPlayer(currentPlayer);

			
			line = bufferedReader.readLine();
			int maxRounds = (int)stringToData(line);
			engine.setMaxRounds(maxRounds);
			
			
			line = bufferedReader.readLine();
			@SuppressWarnings("unchecked")
			ArrayList<Integer>playerTurnOrder = (ArrayList<Integer>)stringToData(line);
			engine.setPlayerTurnOrder(playerTurnOrder);

			
			line = bufferedReader.readLine();
			int nbPlayers = (int)stringToData(line);
			for(int i =0; i<nbPlayers; i++)
			{
				
				IPlayer player = (IPlayer)loadPlayer(bufferedReader);
				System.out.println(player.getType().name() +" "+player.getStrategy().name() +" "+player.nbTerritoriesOwned());
				if(player.getType() == RiskPlayerType.Bot)
				{
					player = new BotPlayerModel(player);
				}
				else
				{
					player = new HumanPlayerModel (player);
				}
				player.setRiskBoard(engine.getBoard());
				engine.addPlayer(new Integer(player.getPlayerID()), player);
			}
			line = bufferedReader.readLine();
			String historyInfo =  (String) stringToData(line);
			line = bufferedReader.readLine();
			String countryInfo =  (String) stringToData(line);
			
			engine.setPanelInfo(historyInfo, countryInfo);
			
			bufferedReader.close();
			fileReader.close();
			return true;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * creates a string representation of the Hand for saving 
	 * @param hand The continent to be saved
	 * @return A string representation of the hand 
	 */
	private static String saveHandToString(Hand hand)
	{
		StringBuffer str = new StringBuffer();
		
		
		str.append(dataToString(hand.size()));
		str.append("\n");
		for(int i =0; i< hand.size(); i++)
		{
			str.append(dataToString(hand.getCards().get(i)));
			
			if( i != hand.size() -1)
				str.append("\n");
		}
		return str.toString();
		
	}
	
	/**
	 * Saves the given Hand
	 * @param hand The hand to be saved
	 * @param filePath the file where it must be saved 
	 * @return If the hand was saved successfully or not
	 */
	public static boolean saveHand(Hand hand, String filePath)
	{
		String cont = saveHandToString(hand);
		if(cont.length() == 0)
			return false;
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString()+"\\"+filePath;
		try 
		{
			FileWriter fw = new FileWriter(path);
			fw.write(cont);
			fw.close();
			return true;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Loads a hand from a file
	 * @param relativePath The relative Path to the file
	 * @return the Territory object
	 */
	public static Hand loadHand(String relativePath)
	{
		Hand obj = null;
		Path currentRelativePath = Paths.get("");
		try
		{
			String path = currentRelativePath.toAbsolutePath().toString()+"\\"+relativePath;
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Hand hand = loadHand(bufferedReader);
			bufferedReader.close();
			fileReader.close();
			return hand;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * Loads a hand
	 * @param bufferedReader of the file
	 * @return the Hand 
	 */
	private static Hand loadHand(BufferedReader bufferedReader) {
		int nb;
		try
		{
			Hand hand = new Hand();
			String line;
			line = bufferedReader.readLine();
			nb = (int)stringToData(line);
			for(int i =0; i< nb; i++)
			{
				line = bufferedReader.readLine();
				Card card = (Card)stringToData(line);
				hand.add(card);
				
			}
			return hand;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 *Search for the index of string given an array
	 * @param selectedString The string that index we search
	 * @param values the array in which the search occurs
	 * @return the index of selectedString in the array
	 */
	public static int getIndexOf(String selectedString, Strategy[] values) 
	{

		String [] array = new String[values.length];
		for(int i =0; i < values.length; i++)
		{
			array[i]= values[i].name().trim();
		}
		return getIndexOf(selectedString, array);
	}
}

