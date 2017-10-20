package risk.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

import risk.game.GameEngine;
import risk.model.MapModel;
import risk.util.map.RiskBoard;
import risk.util.map.editor.Utilities;
import risk.util.RiskEnum.E_MapEditorMode;

public class JMenuBarComponent 
{
	public static final String MENU_FILE = "FILE";
	public static final String MENU_ITEM_CREATE_MAP = "CREATE MAP";
	public static final String MENU_ITEM_EDIT_MAP = "EDIT MAP";
	public static final String MENU_ITEM_OPEN_MAP = "OPEN MAP AND PLAY";
	public static final String MENU_ITEM_EXIT = "EXIT GAME";
	public static final String MSG_CLOSING_GAME_APPLICATION = "Closing game application.";
	public static final String MSG_MENU_SELECTED = "Breadcrumbs Menu Selected: %s > %s.";
	public static final String TITLE_MAP_EDITOR = "MAP EDITOR";
	public static final int CHILD_POPUP_WINDOW_WIDTH = (int) 1000 - 100;
	public static final int CHILD_POPUP_WINDOW_HEIGHT = (int) (1000 / 12 * 9) - 100;
	
	String returnMsg = "";
	BufferedReader in;
	String playerName;
	String mapFilePath;
	String[] dataArray, dataFileInfoArray;
	File fileGameLoad;
	
		final static Logger logger = Logger.getLogger(JMenuBarComponent.class);

		
		public JMenuBar getGameJMenuBar( JFrame new_jframe) 
		{
			final JLabel backGround = new JLabel(new ImageIcon(
					((new ImageIcon("images/Conquest_Game1.png").getImage().getScaledInstance(new_jframe.getSize().width,
							(int) ((int) new_jframe.getSize().height - 30), java.awt.Image.SCALE_SMOOTH)))));
			new_jframe.add(backGround);

			JMenuBar menuBar = new JMenuBar();
			JMenu menuFile = new JMenu(MENU_FILE);
			menuBar.add(menuFile);
			
			final JMenuItem menuItemCreateMap = new JMenuItem(MENU_ITEM_CREATE_MAP);
			menuFile.add(menuItemCreateMap);
			
			final JMenuItem menuItemEditMap = new JMenuItem(MENU_ITEM_EDIT_MAP);
			menuFile.add(menuItemEditMap);
			
			final JMenuItem menuItemOpenMap = new JMenuItem(MENU_ITEM_OPEN_MAP);
			menuFile.add(menuItemOpenMap);
			
			final JMenuItem menuItemExit = new JMenuItem(MENU_ITEM_EXIT);
			menuFile.add(menuItemExit);
			
			
			/**
			 * This class handle the menu Item action on click action
			 * 
			 * @author Ayushi
			 * @author hcanta
			 * 
			 */
			class menuItemAction implements ActionListener 
			{

				@Override
				public void actionPerformed(ActionEvent e) 
				{
					boolean isGamePlay = false;
					JFileChooser fileChooser = new JFileChooser();
					File file;
	
					if (e.getSource().equals(menuItemOpenMap))
					{
						System.out.println("OPENING A MAP in .map format");
						isGamePlay = true;
						fileChooser = new JFileChooserComponent().getJFileChooser
								(JFileChooserComponent.E_JFileChooserMode.MapLoad);
						int result = fileChooser.showOpenDialog(new_jframe);
						
						if (result == JFileChooser.APPROVE_OPTION) 
						{
							file = fileChooser.getSelectedFile();
							System.out.println("file name is: "+file);
							if (!isGamePlay) 
							{
								file = new File(getMapFilePath());
							}
							// Check file exist or not
							if (!file.exists() && !file.isDirectory()) 
							{
								JOptionPane.showMessageDialog(null,
										"ERROR: Unable to find .Map file in the system. \n Please paste a new .map file at the required location."
												+ file.getAbsolutePath());
							} 
							else 
							{
								new_jframe.remove(backGround);
								new_jframe.setBackground(Color.WHITE);
								Utilities.loadFile(file);
										
								System.out.println(RiskBoard.Instance.toString());
								GameEngine.Instance.startup();
							
							}
							
						}
						
					}
					
					else if (e.getSource().equals(menuItemExit)) 
					{
						logger.info(String.format(MSG_MENU_SELECTED, MENU_FILE, MENU_ITEM_EXIT));
						logger.info(MSG_CLOSING_GAME_APPLICATION);
						System.exit(0);
					}
					
					else if (e.getSource().equals(menuItemCreateMap))
					{
						GameEngine.Instance.createMap();
					}
					else if (e.getSource().equals(menuItemEditMap))
					{
						System.out.println("OPENING A MAP in .map format");
						isGamePlay = true;
						fileChooser = new JFileChooserComponent().getJFileChooser
								(JFileChooserComponent.E_JFileChooserMode.MapLoad);
						int result = fileChooser.showOpenDialog(new_jframe);
						
						if (result == JFileChooser.APPROVE_OPTION) 
						{
							file = fileChooser.getSelectedFile();
							System.out.println("file name is: "+file);
							if (!isGamePlay) 
							{
								file = new File(getMapFilePath());
							}
							// Check file exist or not
							if (!file.exists() && !file.isDirectory()) 
							{
								JOptionPane.showMessageDialog(null,
										"ERROR: Unable to find .Map file in the system. \n Please paste a new .map file at the required location."
												+ file.getAbsolutePath());
							} 
							else 
							{
								new_jframe.remove(backGround);
								new_jframe.setBackground(Color.WHITE);
								Utilities.loadFile(file);
										
								System.out.println(RiskBoard.Instance.toString());
								
								GameEngine.Instance.editMap();
							}
						}

					}
				}
			}
							
			menuItemCreateMap.addActionListener(new menuItemAction());
			menuItemOpenMap.addActionListener(new menuItemAction());
			menuItemExit.addActionListener(new menuItemAction());
			menuItemEditMap.addActionListener(new menuItemAction());
			return menuBar;
		}
	
		
		/**
		 * This method selects the map path from data that was read from load()
		 * 
		 * @return map path string
		 */
		public String getMapFilePath() 
		{

			try 
			{
				in = new BufferedReader(new FileReader(fileGameLoad.getAbsoluteFile()));

				String temp = in.readLine();

				dataArray = temp.split(";");

				dataFileInfoArray = dataArray[0].split(",");
				playerName = dataFileInfoArray[0];
				mapFilePath = dataFileInfoArray[1];
				returnMsg = mapFilePath;

			} 
			catch (Exception e)
			{
				e.getMessage();
			}
			return returnMsg;
		}

}
