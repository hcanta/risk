/**
 * Package contains the views of the game
 */
package risk.views.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import risk.utils.constants.RiskStrings;

/**
 * The Implementation of the Map Selector, it contains the single static function that helps select the proper files
 * @author hcanta
 */
public class MapSelector 
{

	/**
	 * Returns the fileChooser Object of the MapSelector class
	 * @param extension  The extension we re searching for
	 * @return the fileChooser
	 */
	public static JFileChooser getJFileChooser(String extension) 
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setAcceptAllFileFilterUsed(false);

		fileChooser.setDialogTitle(RiskStrings.CONQUEST_FILECHOOSER_DIALOG_TITLE);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("MAP File", extension));
	

		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		return fileChooser;
	}

}
