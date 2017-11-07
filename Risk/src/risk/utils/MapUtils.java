/**
 *  Package contains the various static, constant and helper method of the game
 */
package risk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import risk.model.maputils.RiskBoard;
import risk.utils.constants.RiskIntegers;

/**
 * This class contains implementation for load and save functions
 * @author hcanta
 */
public class MapUtils 
{
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

}
