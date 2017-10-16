package risk.util.map.editor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import risk.model.MapModel;


public class FileStorage 
{
	
	/**
	 * this method saves the map file
	 * 
	 * @param new_file
	 *            creates a new file object
	 * @param new_mapModel
	 *            creates a new map model
	 * @return string message
	 */
	public String saveMapFile(File new_file, MapModel new_mapModel) {
		String fileContent = getJsonFromObject(new_mapModel);
		fileContent = (new MiscellaneousHelper()).EncodeBase64(fileContent);
		String filePath = new_file.getPath();
		if (!filePath.endsWith(".map"))
			filePath += ".map";
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(fileContent);
			fileWriter.flush();
			fileWriter.close();
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR : " + e.getMessage();
		}
	}

	/**
	 * this method opens up a map file
	 * 
	 * @param new_file
	 *            new file object
	 * @return mapModel model of the map
	 */
	
	public MapModel openMapFile(File new_file) {

		String fileContent = "";
		try {
			fileContent = new String(Files.readAllBytes(Paths.get(new_file.getPath())));
			System.out.println("File content is: "+fileContent);
//			fileContent = (new MiscellaneousHelper()).DecodeBase64(fileContent);
//			System.out.println("File content after decoding is: "+fileContent);
			
			return (MapModel) getObjectFromJson(fileContent, MapModel.class);
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * this method gets json from object
	 * 
	 * @param new_object
	 *            new object
	 * @return json converts gson to json and returns it
	 */
	public String getJsonFromObject(Object new_object) {
		Gson gson = new Gson();
		return gson.toJson(new_object);
	}

	/**
	 * this methods gets object from a json
	 * 
	 * @param new_jsonString
	 *            json string object
	 * @param new_class
	 *            new clas
	 * @return object object from json
	 */
	
	public Object getObjectFromJson(String new_jsonString, Class<?> new_class) {
		
		
		Gson gson = new Gson();		
		return gson.fromJson(new_jsonString, new_class);
		
	}

}
