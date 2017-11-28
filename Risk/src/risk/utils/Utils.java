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
import java.util.Base64;
import java.util.Stack;

import risk.model.RiskBoard;
import risk.model.maputils.Continent;
import risk.model.maputils.Territory;
import risk.utils.constants.RiskEnum.PlayerColors;
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
	 * @param debug are we in debug mode or not
	 * @return  was the map loaded valid or not
	 */
	public static boolean loadFile(File file, boolean debug) {
		RiskBoard.ProperInstance(debug).clear();
		try
		{
			FileReader fileReader = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			RiskBoard.ProperInstance(debug).setBoardName(file.getName());
			
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
						RiskBoard.ProperInstance(debug).addContinent(parts[0].toLowerCase().trim(), Integer.parseInt(parts[1].trim()));
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
						RiskBoard.ProperInstance(debug).addTerritory(continentName, territoryName, neighbours,xcoord, ycoord);
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
		return RiskBoard.ProperInstance(debug).validateMap();
		
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
	 * @param debug set to true for debugging or testing
	 * @throws IOException Could not open/write to file
	 */
	public static void saveMap(String filename, boolean debug) throws IOException
	{
		String n_filename = "Maps\\"+filename + ".map";
		FileWriter fw = new FileWriter(n_filename);
		
		fw.write("[Map]\n");
		fw.write("author = team1\n\n");
		fw.write("[Continents]\n");
		fw.write(RiskBoard.ProperInstance(debug).continentsToString());
		fw.write("\n[Territories]\n");
		
		fw.write(RiskBoard.ProperInstance(debug).territoriesToString());
		
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
	 * @param continent The Territory to be saved
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
	 * creates a string representation of the continent for saving 
	 * @param continent The Territory to be saved
	 * @return A string representation of the continent 
	 */
	public static String saveContinentToString(Continent continent)
	{
		StringBuffer str = new StringBuffer();
		
		str.append(dataToString(continent.getContinentName()));
		str.append("\n");
		
		str.append(dataToString(continent.getContinentBonus()));
		str.append("\n");
		
		str.append(dataToString(continent.getGraph()));
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
			str.append("\n");
		}
		return str.toString();
		
	}
	
	/**
	 * Loads a Continent from a file
	 * @param relativePath The relative Path to the file
	 * @return the Continent object
	 */
	public static Continent loadContinent(String relativePath)
	{
		String continentName;
		Object graph;
		int ownerID;
		boolean gra;
		
		int bonus;
		Path currentRelativePath = Paths.get("");
		try
		{
			String path = currentRelativePath.toAbsolutePath().toString()+"\\"+relativePath;
			FileReader fileReader = new FileReader(new File(path));
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			line = bufferedReader.readLine();
			continentName = (String)stringToData(line);
			line = bufferedReader.readLine();
			bonus = (int)stringToData(line);
			line = bufferedReader.readLine();
			gra = (boolean) stringToData(line);
			graph = gra ?new Object () : null;
			line = bufferedReader.readLine();
			ownerID = (int)stringToData(line);
			line = bufferedReader.readLine();
			int nbTerritories = (int)stringToData(line);
			Continent cont = new Continent(continentName,bonus,graph);
			cont.setOwnerID(ownerID);
			for(int i =0; i< nbTerritories; i++)
			{
				line = bufferedReader.readLine();
				String name = (String)stringToData(line);
				line = bufferedReader.readLine();
				Territory territory = (Territory)stringToData(line);
				cont.addTerritory(name, territory);
			}
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
			 oos = new ObjectOutputStream( baos );
			 oos.writeObject( o );
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

}

