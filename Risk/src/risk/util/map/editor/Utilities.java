package risk.util.map.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import risk.util.map.MapExcpetion;
import risk.util.map.RiskBoard;

public class Utilities {

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
