package risk.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class JFileChooserComponent 
{

	private JFileChooser fileChooser;
	
	
	/**
	 * The Method gives you JFileChooser Save or Open mode according to provided
	 * input.
	 * 
	 * @param new_fileChooserMode
	 *            the mode of a file open and on the base of this argument it
	 *            behave accordingly.
	 * @return a file
	 */
	
	public JFileChooser getJFileChooser(E_JFileChooserMode new_fileChooserMode) 
	{
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setAcceptAllFileFilterUsed(false);

		if (new_fileChooserMode == E_JFileChooserMode.MapLoad) 
		{
			System.out.println("Inside JFileChooserComponent class");
			fileChooser.setDialogTitle("Conquest Game .MAP File");
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("MAP File", "map"));
		} 
		else 
		{
			System.out.println("To be implemented later");
		}

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		return fileChooser;
	}

	/**
	 * @return the fileChooser
	 */
	public JFileChooser getFileChooser() {
		return fileChooser;
	}
	
	
	public enum E_JFileChooserMode 
	{
		MapOpen, MapSave, MapPlay, MapLoad, GameSave
	}
}
