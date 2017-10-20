package risk.util.map.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import risk.util.map.MapExcpetion;
import risk.util.map.RiskBoard;

public class Utilities 
{
	
	/**
	 * 
	 * @param filename the name of the file where the risk board will be saved
	 * @throws IOException
	 */
	public static void saveMap(String filename) throws IOException
	{
		String n_filename = filename + ".map";
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
	 * 
	 * @param file the file to be loaded in the Risk Board
	 * @throws MapExcpetion
	 */
	public static void loadFile(File file) throws MapExcpetion
	{
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
			while ((line = bufferedReader.readLine()) != null) 
			{
				if(line.length()>0)
				{
					String[] parts = line.split(",");
					if(parts.length >=5)
					{
						String territoryName= parts[0].toLowerCase().trim();
						String continentName = parts[3].toLowerCase().trim();
						String [] neighbours =new String[parts.length - 4];
						for(int i =4; i< parts.length; i++)
						{
							neighbours[i-4] = parts[i].toLowerCase().trim();
						}
						RiskBoard.Instance.addTerritory(continentName, territoryName, neighbours);
					}
					else
					{
						throw new MapExcpetion("A country must have at least one neighbour");
					}
				}
			}
			bufferedReader.close();
			fileReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
