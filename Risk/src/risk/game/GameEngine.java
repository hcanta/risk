
/**
 * The game package holds the driver and the Game Engine
 */
package risk.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import risk.game.cards.Card;
import risk.model.BotPlayerModel;
import risk.model.HumanPlayerModel;
import risk.model.RiskBoard;
import risk.model.maputils.Continent;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.utils.Tuple;
import risk.utils.Utils;
import risk.utils.constants.RiskEnum;
import risk.utils.constants.RiskEnum.CardType;
import risk.utils.constants.RiskEnum.GameState;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskEvent;
import risk.utils.constants.RiskEnum.RiskPlayerType;
import risk.utils.constants.RiskEnum.Strategy;
import risk.utils.constants.RiskIntegers;
import risk.utils.constants.RiskStrings;
import risk.views.GameView;
import risk.views.ui.MapSelector;

/**
 * The Implementation of the Game Engine
 * @author hcanta
 * @version 4.1
 */
public class GameEngine implements Serializable 
{
	/**
	 * max number of rounds
	 */
	private int maxRounds;

	/**
	 * The current Player Name
	 */
	private String currentPlayer = RiskStrings.RISK_SYSTEM;
	
	/**
	 * Generated Serialization UID
	 */
	private static final long serialVersionUID = 2572722777333878769L;
	/**
	 * Random Generator for the class
	 */
	private Random rand;

	
	/**
	 * Creates a mapping of player IDs to IPlayer Object
	 */
	private HashMap<Integer, IPlayer> players;
	
	/**
	 * Contains the order in which the players will play
	 */
	private ArrayList<Integer> playerTurnOrder;
	
	/**
	 * The board on which the game is played
	 */
	private RiskBoard board;
	/**
	 * The Game View
	 */
	private transient GameView gamev;
	

	/**
	 *Constructor Of the Game Engine
	 *@param gamev The game View
	 */
	public GameEngine(GameView gamev) 
	{
		this.players = new HashMap<Integer, IPlayer>();
		rand = new Random();

		this.gamev = gamev;
		board = RiskBoard.Instance;
		if(gamev !=null)
		{
			addMenuItemActionListener();
		}
		this.board.resetRoundPlayed();
		maxRounds = (Integer.MAX_VALUE);
	}
	
	/**
	 * Simple Constructor of the GameEngine Without a game View
	 */
	public GameEngine()
	{
		this(null);
	}
	
	private void territoryInfo()
	{

		board.getTerritoryInfo().setLength(0); 
		String territory;
		for(int i =0; i< board.getTerritories().size(); i++)
		{
			territory = board.getTerritories().get(i);
			String[] info = board.getTerritory(territory).basicInfo();
			board.getTerritoryInfo().append(info[1]);
			board.getTerritoryInfo().append("\n");
			board.getTerritoryInfo().append("Owner: "+players.get(Integer.parseInt(info[0])).getName()+"\n\n");
		}
		board.update(RiskEvent.CountryUpdate);
	}
	
	/**
	 * Adds The action Listener to the menu items of the Game View
	 */
	private void addMenuItemActionListener()
	{
		//Adding Create Map ActionListener
		this.gamev.getRiskMenu().getMenuItemCreatetMap().addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				board.resetRoundPlayed();
				
				addToHistoryPanel("\n"+RiskStrings.INITIATE_CREATE_MAP);
				Thread thread = new Thread(new Runnable() {
			         @Override
			         public void run() {
			             createMap();
			         }
				});
				thread.start();
			}
		});
		
		// Adding Edit Map Action Listener
		this.gamev.getRiskMenu().getMenuItemEditMap().addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
				{
					board.resetRoundPlayed();
					
					addToHistoryPanel("\n"+RiskStrings.INITIATE_EDIT_MAP);
					Thread thread = new Thread(new Runnable() {
				         @Override
				         public void run() {
				             editMap();
				         }
					});
					thread.start();
			
				}
		});
		//Adding Load and Play Map Action Listener
		this.gamev.getRiskMenu().getMenuItemOpenetMap().addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
				{
					board.resetRoundPlayed();
					addToHistoryPanel("\n"+RiskStrings.INITIATE_LOAD_PLAY);
					
					if(loadMapHelper(true))
					{
						
						setState(GameState.STARTUP);
						Thread thread = new Thread(new Runnable() {
					         @Override
					         public void run() {
					             if(deploy())
					             {
					            	 setState(GameState.NEXT_PLAYER);
					            	 play();
					             }
					         }
						});
						thread.start();
					}
				}
		});
		
		this.gamev.getRiskMenu().getLoadGame().addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				addToHistoryPanel(RiskStrings.ATTEMPT_LOAD_GAME);
				if(loadGame())
				{
					Thread thread = new Thread(new Runnable() {
				         @Override
				         public void run() 
				         {	 
			            	 play(true,false);
				         }
					});
					thread.start();
				}
				
			}
		});
		
		this.gamev.getRiskMenu().getTournament().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				board.resetRoundPlayed();
				addToHistoryPanel("\n"+RiskStrings.INITIATE_LOAD_PLAY);
				setState(GameState.STARTUP);
				Thread thread = new Thread(new Runnable() 
				{
			         @Override
			         public void run() 
			         {
			            tournament();
			         }
				});
				thread.start();
			}
		});
	}
	
	/**
	 * Loads the Game
	 * @return was the game loaded properly
	 */
	private boolean loadGame()
	{
		JFileChooser fileChooser =  MapSelector.getJFileChooser("risk");
		int result = fileChooser.showOpenDialog(this.gamev.getFrame());
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			this.addToHistoryPanel("Attempting To Load Game");
			
			
			File file = fileChooser.getSelectedFile();
			if (!file.exists() && !file.isDirectory()) 
			{
				JOptionPane.showMessageDialog(null,
						RiskStrings.INVALID_FILE_LOCATION
								+ file.getAbsolutePath());
				
				this.addToHistoryPanel(RiskStrings.INVALID_FILE_LOCATION);
				
				
			}
			else
			{
				this.addToHistoryPanel("file name is: " + file.getName());
				
				if(Utils.loadGame(this, file.getName()))
				{
					this.addToHistoryPanel("The Game File was properly loaded.");	
					board.update(RiskEvent.GraphUpdate);
					return true;
				}
				else
				{
					this.addToHistoryPanel("The Map File was invalid.");
					board.clear();
					this.gamev.cleanCenter();
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds a message to the history panel
	 * @param message The message To be Added To the history panel
	 */
	private void addToHistoryPanel(String message)
	{
		board.addMessage(message);
		board.update(RiskEvent.HistoryUpdate);
	}
	/**
	 * generate the turn order
	 */
	public void generateTurnOrder()
	{
		playerTurnOrder = new ArrayList<Integer>();
		for(int i=0; i< players.keySet().size(); ++i)
		{
			playerTurnOrder.add(new Integer(players.get(players.keySet().toArray()[i]).getPlayerID()));
		}
	}
	/**
	 * Creates a Map from scratch with user input
	 */
	private void createMap() 
	{
		board.clear();
		board.setBoardName(RiskStrings.CREATED_MAP);
		
		String str = "";
		String input;
		int nbContinent = 0;
		
		// Create the continents
		addToHistoryPanel(RiskStrings.CREATE_CONTINENT);
		while(nbContinent < 2)
		{
			
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),RiskStrings.CREATE_MAP_CONTINENT_QUERY, 
					RiskStrings.MENU_ITEM_CREATE_MAP, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return;
			try
			{
				nbContinent = Integer.parseInt(input);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(gamev.getFrame(),
					   RiskStrings.PLEASE_NUMBER,
					   RiskStrings.MENU_ITEM_CREATE_MAP,
					    JOptionPane.WARNING_MESSAGE);
			}        
		}
		
		// Name The continents and set their bonus
		addToHistoryPanel(RiskStrings.CREATE_CONTINENT_BONUS);
		for(int i =0; i<nbContinent; i++)
		{
			boolean addCont = addContinent (RiskStrings.MENU_ITEM_CREATE_MAP, " "+ (i+1)+" ");

			if(!addCont)
			{
				return;
			}
		}
		
		JOptionPane.showMessageDialog(gamev.getFrame(),
				RiskStrings.CONTINENT_CREATED+"\n\n"+board.continentsToString());
		
		// Adding countries
		this.addToHistoryPanel(RiskStrings.CREATE_TERRITORIES);
		int nbCountries;
		for(int i =0; i<nbContinent;i++)
		{
			
			nbCountries = 0;
			while(nbCountries <= 0)
			{
				
				input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.TERRITORY_NUMBER_QUERY + board.getContinents().get(i),
						 RiskStrings.MENU_ITEM_CREATE_MAP+" - "+RiskStrings.CONTINENT+": " + board.getContinents().get(i), 
						 JOptionPane.PLAIN_MESSAGE, null, null, "");
				if(input == null)
					return;
				try
				{
					nbCountries = Integer.parseInt(input);
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(gamev.getFrame(),
						   RiskStrings.PLEASE_NUMBER,
						   RiskStrings.MENU_ITEM_CREATE_MAP,
						    JOptionPane.WARNING_MESSAGE);
				}   
			}
			
			String country= "";
			for(int j =0; j< nbCountries; j++)
			{
				country= "";
				while(country.length()==0)
				{
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.PLEASE_NAME + (j+1) +RiskStrings.TERRITORY,
							 RiskStrings.MENU_ITEM_CREATE_MAP, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						return;
					country = input;
					country = country.toLowerCase().trim();
					
				}
				
				int nbNeighbours =0;
				while(nbNeighbours <=0)
				{
					
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.TERRITORY_NEIGHBOUR_NUMBER_QUERY,
							 RiskStrings.MENU_ITEM_CREATE_MAP+" - "+RiskStrings.TERRITORY+": " + country, 
							 JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						return;
					try
					{
						nbNeighbours = Integer.parseInt(input);
					}
					catch(Exception e)
					{
						JOptionPane.showMessageDialog(gamev.getFrame(),
							   RiskStrings.PLEASE_NUMBER,
							   RiskStrings.MENU_ITEM_CREATE_MAP,
							    JOptionPane.WARNING_MESSAGE);
					}   
				}
				
				str = "";

				board.addTerritory(board.getContinents().get(i), country);
				for(int k =0; k <nbNeighbours; k++)
				{
					while(str.length()==0)
					{
						input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.TERRITORY_NEIGHBOUR_NAME_QUERY,
								 RiskStrings.MENU_ITEM_CREATE_MAP+" - "+RiskStrings.TERRITORY+": " + country, 
								 JOptionPane.PLAIN_MESSAGE, null, null, "");
						if(input == null)
							return;
						str = input;
						str = input.toLowerCase().trim();
						
						
					}
					board.getTerritory(country).addNeighbours(str);
					str= "";
				}
				
			}
		}
		
		this.validateAndSave(RiskStrings.MENU_ITEM_CREATE_MAP);
	}
	/**
	 * Returns a file object for the tournament
	 * @return the file object
	 */
	private File tournamentLoader()
	{
		JFileChooser fileChooser =  MapSelector.getJFileChooser("map");
		int result = fileChooser.showOpenDialog(this.gamev.getFrame());
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			this.addToHistoryPanel("Attempting To Load Map");
			
			
			File file = fileChooser.getSelectedFile();
			return file;
		}
		return null;
	}
	
	/**
	 * This function chooses the file to be edited or loaded
	 * @param load are we loading to play or to edit
	 * @return was the file properly loaded or not
	 */
	private boolean loadMapHelper(boolean load)
	{
		JFileChooser fileChooser =  MapSelector.getJFileChooser("map");
		int result = fileChooser.showOpenDialog(this.gamev.getFrame());
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			this.addToHistoryPanel("Attempting To Load Map");
			
			
			File file = fileChooser.getSelectedFile();
			if (!file.exists() && !file.isDirectory()) 
			{
				JOptionPane.showMessageDialog(null,
						RiskStrings.INVALID_FILE_LOCATION
								+ file.getAbsolutePath());
				
				this.addToHistoryPanel(RiskStrings.INVALID_FILE_LOCATION);
				
				
			}
			else
			{
				this.addToHistoryPanel("file name is: " + file.getName());
				
				if(Utils.loadFile(file))
				{
					this.addToHistoryPanel("The Map File was properly loaded.");
					
					if(load)
					{
						board.update(RiskEvent.GraphUpdate);
					}
					return true;
					
				}
				else
				{
					this.addToHistoryPanel("The Map File was invalid.");
					
					board.clear();
					if(load)
					{
						this.gamev.cleanCenter();
						
					}
					
				}
			}
		}
		return false;
	}  
	
	/**
	 * Edit the Map That was Loaded
	 */
	public void editMap() 
	{
		loadMapHelper(false);
	
		int option =0;
		boolean edited = false;
		while(option!=7)
		{
			Object selected = JOptionPane.showInputDialog(null, "", RiskStrings.MENU_ITEM_EDIT_MAP,
					JOptionPane.DEFAULT_OPTION,
					null, RiskStrings.EDIT_OPTIONS, RiskStrings.EDIT_OPTIONS[0]);
			if ( selected != null )
			{
			    String selectedString = selected.toString();
			    option = Utils.getIndexOf(selectedString,RiskStrings.EDIT_OPTIONS) + 1;
			}
			else
			{
				board.clear();
			    return;
			}
			boolean result = false;
			switch(option)
			{
				case 1:
					this.addToHistoryPanel(RiskStrings.ADD_CONTINENT_ATTEMPT);
					result = addContinent(RiskStrings.MENU_ITEM_EDIT_MAP, "");
					edited = edited || result;
					if(result)
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
					else
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					break;
				case 2:
					this.addToHistoryPanel(RiskStrings.REMOVE_CONTINENT_ATTEMPT);
					result = removeContinent();
					edited = edited || result;
					if(result)
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
					else
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					break;
				case 3:
					this.addToHistoryPanel(RiskStrings.ADD_TERRITORY_ATTEMPT);
					result = addTerritory();
					edited = edited || result;
					if(result)
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
					else
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					break;
				case 4:
					this.addToHistoryPanel(RiskStrings.REMOVE_TERRITORY_ATTEMPT);
					result = removeTerritory();
					edited = edited || result;
					if(result)
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
					else
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					break;
				case 5:
					this.addToHistoryPanel(RiskStrings.ADD_LINK_ATTEMPT);
					result = addLink();
					edited = edited || result;
					if(result)
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
					else
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					break;
				case 6:
					this.addToHistoryPanel(RiskStrings.REMOVE_LINK_ATTEMPT);
					result = removeLink();
					edited = edited || result;
					if(result)
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
					else
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					break;
				default:
					break;			
			}		
		}
		
		if(edited)
			validateAndSave(RiskStrings.MENU_ITEM_EDIT_MAP);
	}
	/**
	 * Validating and saving map
	 * @param dialogbox name/title of the dialog box
	 */
	private void validateAndSave(String dialogbox)
	{
		String str;
		String input;
		this.addToHistoryPanel("\n"+RiskStrings.VALIDATING_MAP);
		boolean valid = board.validateMap();
		 
		if(valid)
		{
			this.addToHistoryPanel("\n"+RiskStrings.VALID_MAP);
			str = "";
			while(str.length() ==0)
			{
				input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.VALID_SAVE_MAP,
						 dialogbox+" - "+RiskStrings.SAVE_MAP, 
						 JOptionPane.PLAIN_MESSAGE, null, null, "");
				if(input == null)
					return;
				
				 str = input.trim();
				
			 }
			 this.addToHistoryPanel("\n"+RiskStrings.MAP_SAVED+str);
			 try {
				Utils.saveMap(str);
				this.addToHistoryPanel("\n"+RiskStrings.DONE);
				
				board.clear();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			 this.addToHistoryPanel("\n"+RiskStrings.INVALID_MAP);
			 JOptionPane.showMessageDialog(gamev.getFrame(),
					 RiskStrings.INVALID_MAP);
			
			 board.clear();
		}
	}

	/**
	 * Removes a link between 2 territories
	 * @return was the link removed
	 */
	private boolean removeLink() {

		Tuple<String, String> info1 = getContinentAndCountry(RiskStrings.FIRST);
		Tuple<String, String> info2 = getContinentAndCountry(RiskStrings.SECOND);
		
		if(info1 == null || info2 == null)
			return false;
		
		String continent1 = info1.getFirst();
		String continent2 = info2.getFirst();
		String ter1 = info1.getSecond();
		String ter2 = info2.getSecond();

		Continent cont1 = board.getContinent(continent1);
		Continent cont2 = board.getContinent(continent2);
		if(cont1 == null || cont2 == null)
			return false;
		Territory terr1 = cont1.getTerritory(ter1);
		Territory terr2 = cont2.getTerritory(ter2);
		if(terr1 == null || terr2 == null)
			return false;
		return terr1.removeNeighbours(ter2) && terr2.removeNeighbours(ter1);	
	}
	/**
	 * Adds a link between 2 territories
	 * @return was the link added or not
	 */
	private boolean addLink() {

		Tuple<String, String> info1 = getContinentAndCountry(RiskStrings.FIRST);
		Tuple<String, String> info2 = getContinentAndCountry(RiskStrings.SECOND);
		
		if(info1 == null || info2 == null)
			return false;
		
		String continent1 = info1.getFirst();
		String continent2 = info2.getFirst();
		String ter1 = info1.getSecond();
		String ter2 = info2.getSecond();

		Continent cont1 = board.getContinent(continent1);
		Continent cont2 = board.getContinent(continent2);
		if(cont1 == null || cont2 == null)
			return false;
		Territory terr1 = cont1.getTerritory(ter1);
		Territory terr2 = cont2.getTerritory(ter2);
		if(terr1 == null || terr2 == null)
			return false;
		return terr1.addNeighbours(ter2) && terr2.addNeighbours(ter1);
	}
	
	/**
	 * Removes a territory
	 * @return was the territory removed or not
	 */
	private boolean removeTerritory() {
		
			Tuple<String, String> info = getContinentAndCountry("");
			if(info == null)
				return false;
			return board.removeTerritory(info.getFirst(), info.getSecond());	
	}
	
	/**
	 * Get the the continent and country from user input
	 * @param param the index id if any
	 * @return a Tuple containing the continent(0) and the country(1) 
	 */
	private Tuple<String,String> getContinentAndCountry(String param)
	{
		return getContinentAndCountry(param, false);
	}
	/**
	 * Get the the continent and country from user input
	 * @param param the string preposition
	 * @param add Are we adding a territory
	 * @return a Tuple containing the continent(0) and the country(1) 
	 */
	private Tuple<String,String> getContinentAndCountry(String param, boolean add)
	{
		String continent ="";
		String input;
		
		while(continent.length() == 0)
		{
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), 
					RiskStrings.PLEASE_NAME+ param  +RiskStrings.CONTINENT_TERRITORY,
					 RiskStrings.MENU_ITEM_EDIT_MAP, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return null;
			
			continent=input.toLowerCase().trim();
			if(!board.containsContinent(continent))
			{
				continent="";
				JOptionPane.showMessageDialog(gamev.getFrame(),
						RiskStrings.INVALID_CONTINENT,
						RiskStrings.MENU_ITEM_EDIT_MAP,
					    JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
		String country ="";
		while(country.length() == 0 )
		{
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), 
					RiskStrings.PLEASE_NAME+ param  +RiskStrings.TERRITORY,
					RiskStrings.MENU_ITEM_EDIT_MAP, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return null;
			
			country=input.toLowerCase().trim();
			
			if(!board.getTerritories().contains(country) && !add)
			{
				country="";
				JOptionPane.showMessageDialog(gamev.getFrame(),
						RiskStrings.INVALID_TERRITORY,
						RiskStrings.MENU_ITEM_EDIT_MAP,
					    JOptionPane.ERROR_MESSAGE);
			}
			else if(board.getTerritories().contains(country) && add)
			{
				country="";
				JOptionPane.showMessageDialog(gamev.getFrame(),
						RiskStrings.INVALID_TERRITORY,
						RiskStrings.MENU_ITEM_EDIT_MAP,
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		return new Tuple<String,String>(continent, country);
	}
	
	/**
	 * Adds a territory
	 * @return was a territory added
	 */
	private boolean addTerritory() 
	{
		Tuple<String, String> info = getContinentAndCountry("", true);
		if(info == null)
			return false;
		
		return board.addTerritory(info.getFirst(), info.getSecond());
	}
	
	/**
	 * Removes a continent
	 * @return was the continent removed
	 */
	private boolean removeContinent() {
		String str ="";
		String input;
		while(str.length() == 0)
		{
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), 
					RiskStrings.PLEASE_NAME  +RiskStrings.CONTINENT +" "+RiskStrings.TO_BE_REMOVED,
					 RiskStrings.MENU_ITEM_EDIT_MAP, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return false;
			
			str=str.toLowerCase().trim();
		}
		return board.removeContinent(str);
	}

	/**
	 * Adds a Continent
	 * @param dialogBox The name of the dialogbox
	 * @param param the string preposition
	 * @return was a continent added
	 */
	private boolean addContinent(String dialogBox, String param) 
	{
		String str ="";
		int bonus = 0;
		String input;
		while(str.length() == 0)
		{
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.PLEASE_NAME + param +RiskStrings.CONTINENT,
					 dialogBox, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return false;
			str = input;
			str = str.toLowerCase().trim();
			if(board.getContinents().contains(str))
			{
				str="";
				JOptionPane.showMessageDialog(gamev.getFrame(),
						RiskStrings.INVALID_CONTINENT,
					    dialogBox,
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		while(bonus <= 0)
		{
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.CONTINENT_BONUS + str,
					 dialogBox, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return false;
			try
			{
				bonus = Integer.parseInt(input);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(gamev.getFrame(),
					   RiskStrings.PLEASE_NUMBER,
					   RiskStrings.MENU_ITEM_CREATE_MAP,
					    JOptionPane.WARNING_MESSAGE);
			}   
		}
		
		board.addContinent(str, bonus);
		
		return true;
	}
	
	/**
	 * Set the current state of the game
	 * @param state the state to be set
	 */
	private void setState(GameState state)
	{
		board.setState(state);
		board.update(RiskEvent.StateChange);
		this.addToHistoryPanel(this.currentPlayer+" : " + state.name());
	}
	/**
	 * This function is called during the startup phase in order to deploy the game
	 * @return true false, was the deployment successful or not
	 */
	private boolean tournament()
	{
		players = new HashMap<Integer, IPlayer>();
		String input;
		ArrayList<String> report = new ArrayList<String>();
		report. add("\nREPORT\nP: ");
		int nbPlayer = 0;
		while(!(nbPlayer >=2 && nbPlayer <= 4))
		{
			
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),"Set The Number of players between 2 and 4", 
					RiskStrings.RISK, JOptionPane.PLAIN_MESSAGE, null, null, "");
			
			if(input == null)
				return false;
			try
			{
				nbPlayer = Integer.parseInt(input);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(gamev.getFrame(),
					   RiskStrings.PLEASE_NUMBER,
					   RiskStrings.MENU_ITEM_CREATE_MAP,
					    JOptionPane.WARNING_MESSAGE);
			}  			
		}
		ArrayList<Strategy> temp2 = new ArrayList<Strategy>();
		Strategy[] temp = Strategy.values();
		for(int i =0; i< temp.length; i++)
		{
			if(temp[i] != Strategy.human)
			{
				temp2.add(temp[i]);
			}
		}
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();
		Strategy[] values = new Strategy[temp2.size()];
		for(int j =0; j< temp2.size(); j++)
			values[j] = temp2.get(j);
		for(int i =0; i < nbPlayer; i++)
		{
			Object selected = JOptionPane.showInputDialog(null, "Start up", "Choose the strategy for Computer " +i,
					JOptionPane.DEFAULT_OPTION,
					null, values,values[0]);
			if ( selected != null )
			{
			    String selectedString = selected.toString().trim();
			    int option = Utils.getIndexOf(selectedString,values) ;
			    strategies.add(values[option]);
			    report.add( (values[option]).name());
			    if(i!= nbPlayer -1)
				{
					report.add(", ");
				}
				else
				{
					report.add("\n");
				}
			}
			else
			{
				return false;
			}
		}
		
		int nbGames = 0;
		while(!(nbGames >=1 && nbGames <= 5))
		{
			
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),"How Many Games between 1 and 5? ", 
					RiskStrings.RISK, JOptionPane.PLAIN_MESSAGE, null, null, "");
			
			if(input == null)
				return false;
			try
			{
				nbGames = Integer.parseInt(input);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(gamev.getFrame(),
					   RiskStrings.PLEASE_NUMBER,
					   RiskStrings.MENU_ITEM_CREATE_MAP,
					    JOptionPane.WARNING_MESSAGE);
			}  			
		}
		int nbMaps = 0;
		while(!(nbMaps >=1 && nbMaps <= 5))
		{
			
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),"How Many Maps between 1 and 5? ", 
					RiskStrings.RISK, JOptionPane.PLAIN_MESSAGE, null, null, "");
			
			if(input == null)
				return false;
			try
			{
				nbMaps = Integer.parseInt(input);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(gamev.getFrame(),
					   RiskStrings.PLEASE_NUMBER,
					   RiskStrings.MENU_ITEM_CREATE_MAP,
					    JOptionPane.WARNING_MESSAGE);
			}  			
		}
		int nbMaxRounds =0;
		while(!(nbMaxRounds >=10 && nbMaxRounds <= 50))
		{
			
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),"How Many rounds between 10 and 50 ? ", 
					RiskStrings.RISK, JOptionPane.PLAIN_MESSAGE, null, null, "");
			
			if(input == null)
				return false;
			try
			{
				nbMaxRounds = Integer.parseInt(input);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(gamev.getFrame(),
					   RiskStrings.PLEASE_NUMBER,
					   RiskStrings.MENU_ITEM_CREATE_MAP,
					    JOptionPane.WARNING_MESSAGE);
			}  			
		}
		ArrayList<File> maps = new ArrayList<File>();
		
		report.add("M: ");
		for(int i = 0; i< nbMaps; i++)
		{
			maps.add( tournamentLoader());
			report.add(maps.get(i).getName());
			if(i!= nbMaps -1)
			{
				report.add(", ");
			}
			else
			{
				report.add("\n");
			}
		}
		report.add("G: " + nbGames + "\n");
		this.maxRounds = nbMaxRounds;
		report.add("Max Rounds = " +this.maxRounds +"\n");
		
		for(int i = 0; i < nbMaps; i++ )
		{
			System.out.println("Map: " + (i+1));
			for(int j = 0; j < nbGames; j++)
			{
				System.out.println("Game: " + (j+1));
				this.board.resetRoundPlayed();
				loadTournament(maps.get(i));
				createBots(nbPlayer, true, PlayerColors.red, strategies);
				generateTurnOrder();
				Collections.shuffle(playerTurnOrder);
				
				setArmiesforPlayers();
				this.addToHistoryPanel(RiskStrings.RANDOM_ASSIGNMENT);
				randomAssignTerritories();
				this.addToHistoryPanel(RiskStrings.PLACE_REM_ARMIES);
				pause();
				placeRemainingArmies();
				pause();
				setState(GameState.NEXT_PLAYER);
	        	play(false,true);
	        	String winner = "draw";
	        	if(board.getOwnerID() != RiskIntegers.INITIAL_OWNER)
	        	{
	        		winner = players.get(board.getOwnerID()).getStrategy().name();
	        	}
	        	report.add(" Game "+ (j+1) +" Map: "+(i+1)+" Winner/Status "+ winner +"\n");
			}
		}
		for(int i =0; i< report.size(); i++)
		{
			System.out.print(report.get(i));
		}
		
		
		
		
		
		
		return true;
	}
	/**
	 * Loads the map for the tournament 
	 * @param file the file of the map
	 * @return was the file loaded properly
	 */
	private boolean loadTournament(File file) {
		this.clear();
		if(Utils.loadFile(file))
		{
			this.addToHistoryPanel("The Map File was properly loaded.");	
			board.update(RiskEvent.GraphUpdate);
			return true;
		}
		return false;
		
	}

	/**
	 * This function is called during the startup phase in order to deploy the game
	 * @return true false, was the deployment successful or not
	 */
	private boolean deploy()
	{
		players = new HashMap<Integer, IPlayer>();
		String input;
	
		int nbPlayer = 0;
		while(!(nbPlayer >=2 && nbPlayer <= 6))
		{
			
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),RiskStrings.NUMBER_PLAYER, 
					RiskStrings.RISK, JOptionPane.PLAIN_MESSAGE, null, null, "");
			
			if(input == null)
				return false;
			try
			{
				nbPlayer = Integer.parseInt(input);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(gamev.getFrame(),
					   RiskStrings.PLEASE_NUMBER,
					   RiskStrings.MENU_ITEM_CREATE_MAP,
					    JOptionPane.WARNING_MESSAGE);
			}  			
		}
		
		
		RiskEnum.PlayerColors humanColor;
		String str ="";
		while(str.length() == 0)
		{
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),RiskStrings.PLAYER_NAME, 
					RiskStrings.RISK, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return false;
			str = input.trim();
		}
		Object selected = JOptionPane.showInputDialog(null, RiskStrings.CHOOSE_COLOR, RiskStrings.MENU_ITEM_OPEN_MAP,
				JOptionPane.DEFAULT_OPTION,
				null, RiskStrings.PLAYER_COLORS, RiskStrings.PLAYER_COLORS[0]);
		if ( selected != null )
		{
		    String selectedString = selected.toString();
		  
		    humanColor = RiskEnum.PlayerColors.valueOf(selectedString);
		    addHumanPlayer(str, humanColor);
		    if(!createBots(nbPlayer, false, humanColor))
		    {
		    	return false;
		    }
		    
		}
		else
		{
			board.clear();
		    return false;
		}
			
		generateTurnOrder();
		Collections.shuffle(playerTurnOrder);
		
		setArmiesforPlayers();
		this.addToHistoryPanel(RiskStrings.RANDOM_ASSIGNMENT);
		randomAssignTerritories();
		this.addToHistoryPanel(RiskStrings.PLACE_REM_ARMIES);
		pause();
		placeRemainingArmies();
		pause();
		
		return true;
	}
	
	/**
	 * Ad user/human player
	 * @param name The players name
	 * @param humanColor the color of the Human player
	 */
	public void addHumanPlayer(String name, PlayerColors humanColor)
	{	
		players.clear();
		short id = IDGenerator();
		players.put(new Integer(id),new HumanPlayerModel(name, humanColor, id));		
	}
	
	/**
	 * Generates a unique ID for the players
	 * @return the generated ID
	 */
	private short IDGenerator()
	{
		short id =  (short)rand.nextInt(250);
		if(players == null)
		{
			return id;
		}
		else
		{
			while(players.keySet().contains(new Integer(id)))
			{
				id =  (short)rand.nextInt(250);
			}
		}
		return id;
	}
	/**
	 * Generates computer player
	 * @param numberOfPlayers The Number of Players in the game
	 * @param tournament  are we in tournament mode or not
	 * @param humanColor the color the human player chose
	 * @return were the bot created properly
	 */
	public boolean createBots(int numberOfPlayers, boolean tournament, RiskEnum.PlayerColors humanColor)
	{
		return createBots(numberOfPlayers, tournament, humanColor, null);
	}
	/**
	 * Generates computer player
	 * @param numberOfPlayers The Number of Players in the game
	 * @param tournament  are we in tournament mode or not
	 * @param humanColor the color the human player chose
	 * @param v the Strategy list
	 * @return were the bot created properly
	 */
	public boolean createBots(int numberOfPlayers, boolean tournament, RiskEnum.PlayerColors humanColor, ArrayList<Strategy> v)
	{
		short nbOfbots = (short) (tournament ? 0 : 1);
		
		ArrayList<PlayerColors> plColor = new ArrayList<PlayerColors>();
		for(int i = 0; i < PlayerColors.values().length; i++)
		{
			if(humanColor!= PlayerColors.values()[i] || tournament)
			{
				plColor.add(PlayerColors.values()[i]);
			}
		}
		short id;
		Collections.shuffle(plColor);
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();
		int option;
		ArrayList<Strategy> temp2 = new ArrayList<Strategy>();
		Strategy[] temp = Strategy.values();
		for(int i =0; i< temp.length; i++)
		{
			if(temp[i] != Strategy.human)
			{
				temp2.add(temp[i]);
			}
		}
		Strategy[] values = new Strategy[temp2.size()];
		for(int j =0; j< temp2.size(); j++)
			values[j] = temp2.get(j);
		
		for(short i = nbOfbots; i< numberOfPlayers; i++)
		{
			if(this.gamev != null  && v == null)
			{
				Object selected = JOptionPane.showInputDialog(null, "Start up", "Choose the strategy for Computer " +i,
						JOptionPane.DEFAULT_OPTION,
						null, values,values[0]);
				if ( selected != null )
				{
				    String selectedString = selected.toString().trim();
				    option = Utils.getIndexOf(selectedString,values) ;
				    strategies.add(values[option]);
				}
				else
				{
					return false;
				}
			}
			else if( v!= null)
			{
				strategies.add(v.get(i));
			}
			else
			{
				strategies.add(Strategy.random);
			}
		}
	
		for(short i = nbOfbots; i< numberOfPlayers; i++)
		{	
			id = this.IDGenerator();
			players.put(new Integer(id), new BotPlayerModel("Computer "+ i, plColor.get(i-nbOfbots), id, strategies.get(i -nbOfbots)));
		}
		return true;
		
	}
	
	/**
	 * Give the armies according to the risk rules
	 * @return the number of armies to be set.
	 */
	public int setArmiesforPlayers()
	{
		int nbArmiesToBePlaced = 0;
		
		switch(players.keySet().size())
		{
			case 6:
				nbArmiesToBePlaced = 20;
				break;
			case 5:
				nbArmiesToBePlaced = 25;
				break;
			case 4:
				nbArmiesToBePlaced = 30;
				break;
			case 3:
				nbArmiesToBePlaced = 35;
				break;
			case 2:
				nbArmiesToBePlaced = 40;
				break;
		}
		
		
		for(int i =0; i < players.keySet().size(); i++)
		{
			players.get(players.keySet().toArray()[i]).setNbArmiesToBePlaced(nbArmiesToBePlaced);
		}
	
		
		this.addToHistoryPanel(""+players.keySet().size()+" players,\n Armies assigned : "+nbArmiesToBePlaced);
	
		board.update(RiskEvent.GeneralUpdate);
		return nbArmiesToBePlaced;
	}
	
	/**
	 * Randomly assign territories according to the risk rules
	 */
	public void randomAssignTerritories()
	{
		ArrayList<String> countries = board.getTerritories();
		Collections.shuffle(countries);
		

		for(int i=0; i< countries.size(); i++)
		{
			
			int index = i % this.playerTurnOrder.size();
			Integer player = playerTurnOrder.get(index);			
			players.get(player).addTerritory(countries.get(i));
			players.get(player).decrementArmies();
			board.getTerritory(countries.get(i)).setOwnerID(players.get(player));
			board.getTerritory(countries.get(i)).setArmyOn(1);
			board.update(RiskEvent.GeneralUpdate);
			smallPause();
		}
		territoryInfo();
	}
	
	/**
	 * Make a small pause 
	 */
	private void smallPause()
	{
		try 
		{
			Thread.sleep(200);
		} 
		catch (InterruptedException e) 
		{			
			e.printStackTrace();
		} 
	}
	
	/**
	 * Place the remaining Armies
	 */
	public void placeRemainingArmies()
	{
		
		for(int i = 0; i < this.playerTurnOrder.size(); i++)
		{
			while(players.get(playerTurnOrder.get(i)).getNbArmiesToBePlaced() > 0)
			{
				int index = rand.nextInt(players.get(playerTurnOrder.get(i)).nbTerritoriesOwned());
				players.get(playerTurnOrder.get(i)).reinforce(players.get(playerTurnOrder.get(i)).getTerritoriesOwned().get(index));
			}
			territoryInfo();
		}
	}
	
	/**
	 * Helper for the play Game
	 * @param integer The player id
	 * @return  end of game
	 */
	private boolean playHelper(Integer integer)
	{
		switch(this.board.getState())
		{
			case NEXT_PLAYER:
				reinforcePhase(integer);
				break;
			case ATTACK:
				attackPhase(integer);
				if(isGameOver())
				{
					this.setState(GameState.IDLE);
					this.addToHistoryPanel("The Game is Over");
					this.addToHistoryPanel("winner: "+ this.players.get(new Integer(board.getOwnerID())).getName());
					board.update(RiskEvent.GeneralUpdate);
					return true;
				}
				break;
			case FORTIFY:
				fortifyPhase(integer);
				break;
			default:
				break;
		}
		return false;
	}
	/**
	 * Play the game
	 */
	private void play()
	{
		play(false,false);
	}
	/**
	 * Play The Game
	 * @param gameloaded was the game loaded
	 * @param tournament Is this a tournament
	 */
	private void play(boolean gameloaded, boolean tournament)
	{
		if(gameloaded)
		{
			ArrayList<Integer> newPlayTurnOrder = new ArrayList<Integer>();
			int initialIndex =  0;
			for(int i =0; i< playerTurnOrder.size(); i++)
			{
				Integer j = playerTurnOrder.get(i);
				if(players.get(j).getType() == RiskPlayerType.Human)
				{
					initialIndex = i;
					break;
				}
			}
			for(int i =0; i < playerTurnOrder.size(); i++)
			{
				int index = (initialIndex + i) % playerTurnOrder.size();
				newPlayTurnOrder.add(playerTurnOrder.get(index));
			}
			playerTurnOrder = newPlayTurnOrder;
			if(board.getnbRoundsPlayed() > 0)
				board.decrementRoundsPlayed();
		}
		
		while(!isGameOver())
		{
			this.board.incrementRoundsPlayed();
			
			for(int i =0; i < this.playerTurnOrder.size(); i++)
			{
				this.updateCardExchange(players.get(this.playerTurnOrder.get(i)));
				board.setCurrentPlayer(players.get(this.playerTurnOrder.get(i)).getName());
				this.currentPlayer = board.getCurrentPlayer();
				
				do
				{
					if(playHelper(this.playerTurnOrder.get(i)))
					{
						break;
					}
				}while(board.getState()!=GameState.NEXT_PLAYER);
				if(this.isGameOver())
				{
					break;
				}
			}
			if(this.isGameOver())
			{
				break;
			}
			
		}
		if(gamev!=null && !tournament)
		{
			JOptionPane.showMessageDialog(this.gamev.getFrame(), "Game Over");
		}
	}
	/**
	 * The fortification phase
	 * @param integer the id of the player
	 */
	private void fortifyPhase(Integer integer) {
		
		
		//Player Object
		if(players.get(integer).getType() == RiskEnum.RiskPlayerType.Human)
		{
			int option = 0;
		
			while(option!=3 && players.get(integer).canFortify())
			{
				Object selected = JOptionPane.showInputDialog(null, RiskStrings.FORTIFY, RiskStrings.FORTIFY,
						JOptionPane.DEFAULT_OPTION,
						null, RiskStrings.FORTIFY_OPTIONS, RiskStrings.FORTIFY_OPTIONS[0]);
				if ( selected != null )
				{
				    String selectedString = selected.toString();
				    option = Utils.getIndexOf(selectedString,RiskStrings.FORTIFY_OPTIONS) + 1;
				}
				else
				{
					break;
				}
				if(option == 2)
				{
					this.addToHistoryPanel(RiskStrings.ATTEMPT_SAVE_GAME);
					if(saveGame())
					{
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
					}
					else
					{
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					}
				}
				else if(option == 3)
				{
					break;
				}
				else if(option == 1) // Fortifying getting input from users
				{
					String input,str;
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.ORIGIN_TERRITORY,
							RiskStrings.FORTIFY, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;
					str = input;
					str = str.toLowerCase().trim();
					if(str.length() == 0)
						continue;
					String origin = str;
					
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.DESTINATION_TERRITORY,
							RiskStrings.FORTIFY, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;
					str = input;
					str = str.toLowerCase().trim();
					if(str.length() == 0)
						continue;
					String destination = str;
					
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.NUMBER_OF_ARMIES,
							RiskStrings.FORTIFY, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;
					str = input;
					str = str.toLowerCase().trim();
					if(str.length() == 0)
						continue;
					int armies ;
					try
					{
						armies = Integer.parseInt(str);
					}
					catch(Exception e)
					{
						JOptionPane.showMessageDialog(gamev.getFrame(),
							   RiskStrings.PLEASE_NUMBER,
							   RiskStrings.FORTIFY,
							    JOptionPane.WARNING_MESSAGE);
						continue;
					}      

					this.addToHistoryPanel(RiskStrings.ATTEMPT_FORTIFY);
					if(players.get(integer).fortify(origin, destination, armies))
					{
						this.addToHistoryPanel(RiskStrings.ATTEMPT_SUCCESFUL);
						territoryInfo();
					}
					else
					{
						this.addToHistoryPanel(RiskStrings.ATTEMPT_FAILED);
					}
				}
			}
		}
		else 
		{
			this.addToHistoryPanel(players.get(integer).fortify());
			this.territoryInfo();
			board.update(RiskEvent.GeneralUpdate);
			pause();
		}
		this.setState(GameState.NEXT_PLAYER);
		
	}
	
	/**
	 * Saves The Game
	 * @return was the game saved
	 */
	private boolean saveGame() 
	{
		String input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.SAVE_GAME,
				RiskStrings.RISK_SYSTEM, JOptionPane.PLAIN_MESSAGE, null, null, "");
		if(input == null || input.length() == 0)
			return false;
		return Utils.saveGame(this, input);
					
		
	}

	/**
	 * Pause before moving to next state or phase
	 */
	private void pause()
	{
		try {
			Thread.sleep(RiskIntegers.PAUSE_DURATION);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} 
	}

	/**
	 * The attack Phase
	 * @param integer the id Of the Player
	 */
	private void attackPhase(Integer integer) 
	{
		IPlayer player = players.get(integer);
		if(players.get(integer).canAttack())
		{
			boolean[] wonOnce = new boolean[1];
			wonOnce[0] = false;
			if(players.get(integer).getType() == RiskEnum.RiskPlayerType.Human)
			{
				int option = 0;
				
				while(option!=2 && player.canAttack())
				{
					// Get User Input
					Object selected = JOptionPane.showInputDialog(null, RiskStrings.ATTACK, RiskStrings.ATTACK,
							JOptionPane.DEFAULT_OPTION,
							null, RiskStrings.ATTACK_OPTIONS, RiskStrings.ATTACK_OPTIONS[0]);
					if ( selected != null )
					{
					    String selectedString = selected.toString();
					    option = Utils.getIndexOf(selectedString,RiskStrings.ATTACK_OPTIONS) + 1;
					}
					else
					{
						break;
					}
					 if(option == 2)
					{
						break;
					}
					
					
					String input,str;
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.ATTACK_TERRITORY,
							RiskStrings.ATTACK, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;
					str = input;
					str = str.toLowerCase().trim();
					if(str.length() == 0)
						continue;
					String origin = str;
					Territory attacker = board.getTerritory(origin);
					if(attacker ==null || attacker.getOwnerID() != player.getPlayerID())
					{
						JOptionPane.showMessageDialog(gamev.getFrame(),
								RiskStrings.INVALID_TERRITORY,
								RiskStrings.ATTACK+" "+RiskStrings.ATTACK_TERRITORY,
							    JOptionPane.ERROR_MESSAGE);
						continue;
					}
					
					
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.DEFENDER_TERRITORY,
							RiskStrings.ATTACK, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;
					str = input;
					str = str.toLowerCase().trim();
					if(str.length() == 0)
						continue;
					String destination = str;
					
					
					
					Territory defender = board.getTerritory(destination);
					if(defender ==null || defender.getOwnerID() == player.getPlayerID() || !attacker.canAttack(destination))
					{
						JOptionPane.showMessageDialog(gamev.getFrame(),
								RiskStrings.INVALID_TERRITORY,
								RiskStrings.ATTACK+" "+RiskStrings.DEFENDER_TERRITORY,
							    JOptionPane.ERROR_MESSAGE);
						continue;
					}
					
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), RiskStrings.NUMBER_OF_DICES +" Max:  "+attacker.potentialNbOfDiceRollAttack(),
							RiskStrings.ATTACK, JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;

					int dices ;
					try
					{
						dices = Integer.parseInt(input);
						
					}
					catch(Exception e)
					{
						JOptionPane.showMessageDialog(gamev.getFrame(),
							   RiskStrings.PLEASE_NUMBER,
							   RiskStrings.ATTACK,
							    JOptionPane.WARNING_MESSAGE);
						continue;
					} 
					if(dices > attacker.potentialNbOfDiceRollAttack() || dices <= 0)
					{
						JOptionPane.showMessageDialog(gamev.getFrame(),
								   "Invalid Dices Count",
								   RiskStrings.ATTACK,
								    JOptionPane.ERROR_MESSAGE);
						continue;
					}
					
					attackHelper( dices,  attacker,  defender,  player,  wonOnce,  integer);
				
				}
				
				
			}
			else //Robot 
			{
				if(player.getStrategy() == Strategy.cheater) // the type cheater behave widely differently
				{
					ArrayList<String>nTerritories = new ArrayList<String>();
					for(int i =0; i < player.getTerritoriesOwned().size(); i++)
					{
						Territory t = board.getTerritory(player.getTerritoriesOwned().get(i));
						for(int j = 0; j < t.getNeighbours().size(); j++ )
						{
							String n = t.getNeighbours().get(j);
							if(!nTerritories.contains(n)&& !player.getTerritoriesOwned().contains(n) )
							{
								nTerritories.add(n);
							}
						}								
					}
					for(int i = 0; i < nTerritories.size(); i ++)
					{
						Territory t = board.getTerritory(nTerritories.get(i));
						players.get(new Integer(t.getOwnerID())).removeTerritory(t.getTerritoryName());
						t.setArmyOn(3);
						player.addTerritory(t.getTerritoryName());
						t.setOwnerID(player);
					}
					
				}
				else
				{
					
					 Tuple<String, Tuple<String, Integer>> data = player.attack();
					 if(isGameOver())
					 {
						 
						 return;
					 }
					 
					 if(data == null)
					 {
						 if(player.getStrategy() == Strategy.benevolent) // check for benevolent
						 {
							 this.addToHistoryPanel("Benevolent Do Nothing");
						 }
						 else
						 {
							 this.addToHistoryPanel(player.getStrategy().name()+" Cant' attack");
						 }
					 }
					 else
					 {
						 Territory attacker = board.getTerritory(data.getFirst());
						 Territory defender = board.getTerritory(data.getSecond().getFirst());
						 this.addToHistoryPanel(player.getType().name() +" Attack "+attacker.getTerritoryName() +" -> " +defender.getTerritoryName());
						 attackHelper( attacker.potentialNbOfDiceRollAttack(),  attacker,  defender,  player,  wonOnce,  integer);
						 
					 }
				}
				
			}
		}

		pause();
		this.setState(GameState.FORTIFY);
		
	}
	
	/**
	 *  Helper method for the attack phase
	 * @param dices  number of dices rolled
	 * @param attacker attacker territory
	 * @param defender defender territory
	 * @param player player object
	 * @param wonOnce card flag
	 * @param integer player ID
	 */
	private void attackHelper(int dices, Territory attacker, Territory defender, IPlayer player, boolean[] wonOnce, Integer integer)
	{
		dices = dices==0? 1 : dices;
		String input;
		// Data Obtained Launching Attack Phase
		int[] attackerDices = new int[dices];
		for(int i=0; i< attackerDices.length;i++)
		{
			attackerDices[i] = rand.nextInt(6) + 1;
		}
		Arrays.sort(attackerDices);
		
		int[] defenderDices  = new int[defender.potentialNbOfDiceRollDefender()];
		for(int i=0; i< defenderDices .length;i++)
		{
			defenderDices [i] = rand.nextInt(6) + 1;
		}
		Arrays.sort(defenderDices);
		this.addToHistoryPanel(player.getName()+" rolled: ");
		this.addToHistoryPanel(Arrays.toString(attackerDices));
		this.addToHistoryPanel(players.get(new Integer(defender.getOwnerID())).getName()+" rolled: ");
		this.addToHistoryPanel(Arrays.toString(defenderDices));
		
		Tuple<Integer,Integer>victories = getVictories(attackerDices,defenderDices);
		int attackerVictories = victories.getFirst();
		int defenderVictories = victories.getSecond();
		defender.setArmyOn(defender.getArmyOn() - attackerVictories);
		attacker.setArmyOn(attacker.getArmyOn() - defenderVictories);
		
		//Won The Territory
		defender.setArmyOn(defender.getArmyOn() - attackerVictories);
		attacker.setArmyOn(attacker.getArmyOn() - defenderVictories);
		if(defender.getArmyOn() == 0)
		{
			if(!wonOnce[0])
			{
				int typeIndex = rand.nextInt(CardType.values().length);
				CardType type = CardType.values()[typeIndex];
				int nameIndex = rand.nextInt(board.getTerritories().size());
				String cardName = board.getTerritories().get(nameIndex);
				players.get(new Integer(attacker.getOwnerID())).getHand().add(new Card(type,cardName));
				wonOnce[0] = true;
				this.addToHistoryPanel(players.get(integer).getName()+" has conquered a territory\n and won a card");
				this.updateCardExchange(player);
			}
			
			players.get(new Integer(defender.getOwnerID())).removeTerritory(defender.getTerritoryName());
			defender.setOwnerID(players.get(attacker.getOwnerID()));
			players.get(new Integer(attacker.getOwnerID())).addTerritory(defender.getTerritoryName());
			territoryInfo();
			if(board.getContinent(defender.getContinentName()).getOwnerID() == attacker.getOwnerID())
			{
				players.get(integer).incrementArmiesBy(board.getContinent(defender.getContinentName()).getContinentBonus());
				this.addToHistoryPanel(players.get(integer).getName()+" has  conquered a continent\n and won a card");
				
				if(isGameOver())
				{
					return;
				}
				territoryInfo();
			}
			players.get(new Integer(attacker.getOwnerID())).fortify(attacker.getTerritoryName(), defender.getTerritoryName(), attackerVictories);
			
			if(player.getType() == RiskPlayerType.Human)
			{
				input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), "How many armies would you like to move? must be at least: "+dices+"\n An incorrect number will result into "+dices+" armies to be moved.",
						RiskStrings.ATTACK, JOptionPane.PLAIN_MESSAGE, null, null, "");
				if(input == null)
				{
					player.fortify(attacker.getTerritoryName(), defender.getTerritoryName(), dices);
					return;
				}
				int move ;
				try
				{
					move = Integer.parseInt(input);
					if(move >= attacker.getArmyOn())
					{
						player.fortify(attacker.getTerritoryName(), defender.getTerritoryName(), dices);
					}
					else
					{
						player.fortify(attacker.getTerritoryName(), defender.getTerritoryName(), move);
					}
					
				}
				catch(Exception e)
				{
					player.fortify(attacker.getTerritoryName(), defender.getTerritoryName(), dices);
				} 
			}
			else
			{
				player.fortify(attacker.getTerritoryName(), defender.getTerritoryName(), dices);
			}
			
		}
	}

	/**
	 * calculates Victories
	 * @param attackerDices Array of attacker dices
	 * @param defenderDices array of defender dices 
	 * @return The tuple of victories
	 */
	private Tuple<Integer, Integer> getVictories(int[] attackerDices, int[] defenderDices) {
		int attackerVictories = 0;
		int defenderVictories = 0;
		if(defenderDices.length > attackerDices.length)
		{
			for(int i = 0; i<defenderDices.length -1 ; i++)
			{
				int attackerIndex = attackerDices.length -1 -i;
				int defenderIndex = defenderDices.length -1 -i;
				if(attackerIndex>=0)
				{
					if(defenderDices[defenderIndex]>attackerDices[attackerIndex])
					{
						defenderVictories++;
					}
					else
					{
						attackerVictories++;
					}
				}
			}
		}
		else
		{
			for(int i = 0; i<attackerDices.length -1 ; i++)
			{
				int attackerIndex = attackerDices.length -1 -i;
				int defenderIndex = defenderDices.length -1 -i;
				if(defenderIndex>=0)
				{
					if(defenderDices[defenderIndex]>attackerDices[attackerIndex])
					{
						defenderVictories++;
					}
					else
					{
						attackerVictories++;
					}
				}
			}
		}
		return new Tuple<Integer,Integer>(attackerVictories,defenderVictories);
	}

	/**
	 * The reinforcement phase
	 * @param integer The id of the player
	 */
	private void reinforcePhase(Integer integer) 
	{
		this.setState(GameState.REINFORCE);
		int newArmies =(int)(players.get(integer).getTerritoriesOwned().size() < 9 ?3 :players.get(integer).getTerritoriesOwned().size()/3);
		this.addToHistoryPanel(players.get(integer).getName()+" has "+players.get(integer).getTerritoriesOwned().size()+" territories");
		this.addToHistoryPanel("Army received: " +newArmies);
		board.update(RiskEvent.GeneralUpdate);
		players.get(integer).incrementArmiesBy(newArmies);
		
		cardExchangeCheck(integer);
		//Player Object
		if(players.get(integer).getType() == RiskEnum.RiskPlayerType.Human)
		{
			
			//Reinforce portion
			
			int option = 0;
		
			while(option!=2 && players.get(integer).canReinforce())
			{
				Object selected = JOptionPane.showInputDialog(null, "Reinforce", "Number of armies to be placed: " + players.get(integer).getNbArmiesToBePlaced(),
						JOptionPane.DEFAULT_OPTION,
						null, RiskStrings.REINFORCE_OPTIONS, RiskStrings.REINFORCE_OPTIONS[0]);
				if ( selected != null )
				{
				    String selectedString = selected.toString();
				    option = Utils.getIndexOf(selectedString,RiskStrings.REINFORCE_OPTIONS) + 1;
				}
				else
				{
					break;
				}
				

				if(option ==2)
				{
					break;
				}
				else if(option == 1)
				{
					String input,str;
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), "Enter the territory to reinforce",
							"Reinforce "+"Number of armies to be placed: " + players.get(integer).getNbArmiesToBePlaced(), JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;
					str = input;
					str = str.toLowerCase().trim();
					
					
					String territory =str;
					
					input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), "Enter number of army to add",
							"Reinforce "+"Number of armies to be placed: " + players.get(integer).getNbArmiesToBePlaced(), JOptionPane.PLAIN_MESSAGE, null, null, "");
					if(input == null)
						continue;
					str = input;
					str = str.toLowerCase().trim();
					
					
					int army = 0;
					try
					{
						army = Integer.parseInt(str);
					}
					catch(Exception e)
					{
						JOptionPane.showMessageDialog(gamev.getFrame(),
							   RiskStrings.PLEASE_NUMBER,
							   "Reinforce",
							    JOptionPane.WARNING_MESSAGE);
						continue;
					}      
					
					
					if(players.get(integer).reinforce(territory, army))
					{
						this.addToHistoryPanel("Reinforcement was successful");
						territoryInfo();
						
					}
					else
					{
						this.addToHistoryPanel("Reinforcement was not successful");
					}
				}
			}
			
		}
		else // Robot object
		{
			this.addToHistoryPanel(players.get(integer).reinforce());
			this.territoryInfo();
			board.update(RiskEvent.GeneralUpdate);
			pause();
		
		}
		
		pause();
		this.setState(GameState.ATTACK);
		
	}

	/**
	 * Checks if the Game is Done Or not
	 * @return true/false is the game over or not
	 */
	private boolean isGameOver() 
	{
		boolean gameOver = board.isGameOver() || this.board.getnbRoundsPlayed() >  this.maxRounds;
		
		return  gameOver;
	}
	
	/**
	 * Performs a card Exchange
	 * @param integer the index of the player 
	 */
	private void cardExchangeCheck(Integer integer)
	{
		int extra = 0;
		boolean mustExchange = false;
		boolean exchange = false;
		if(players.get(integer).getHand().mustTurnInCards() || players.get(integer).getHand().canTurnInCards())
		{
			if(players.get(integer).getHand().mustTurnInCards())
			{
				mustExchange = true;
				exchange = true;
				this.addToHistoryPanel(players.get(integer).getName()+" Must Turn In Cards");
			}
			
			if(players.get(integer).getType() == RiskPlayerType.Human)
			{
				if(!mustExchange && players.get(integer).getHand().canTurnInCards())
				{
					int opt = -1;
					while(opt != 0 && opt != 1 )
					{
						String input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), "Exchange card (0) /(1)",
								"Reinforce "  , JOptionPane.PLAIN_MESSAGE, null, null, "");
						if(input == null)
						{
							exchange = false;
							break;
						}
						String str = input;
						str = str.toLowerCase().trim();
						
						try
						{
							opt = Integer.parseInt(str);
						}
						catch(Exception e)
						{
							JOptionPane.showMessageDialog(gamev.getFrame(),
								   RiskStrings.PLEASE_NUMBER,
								   "Reinforce",
								    JOptionPane.WARNING_MESSAGE);
							opt = -1;
							continue;
						}    
						
						
						exchange = opt == 1;
					}
				}
			}
			if(exchange)
			{
				this.addToHistoryPanel("Card Exchange in progress");
				this.board.incrementCardExchanged();
				updateCardExchange( players.get(integer));
				pause();
				
				int id1 = 0;
				
				int id2 = 1;
				
				int id3 = 2;
				int iArr[] = {id1, id2, id3};
				Arrays.sort(iArr);
				Card card1 = players.get(integer).getHand().getCards().get(id1);
				Card card2 = players.get(integer).getHand().getCards().get(id2);
				Card card3 = players.get(integer).getHand().getCards().get(id3);
				if(players.get(integer).getTerritoriesOwned().contains(card1.getTerritory())
						||players.get(integer).getTerritoriesOwned().contains(card2.getTerritory())
						||players.get(integer).getTerritoriesOwned().contains(card3.getTerritory())
						)
				{
					extra = 2;
					this.addToHistoryPanel("One of the card traded\n shows a country occupied\n by player 2 extra armies ");
					
				}
				players.get(integer).getHand().removeCardsFromHand(iArr[0], iArr[1], iArr[2]);
				this.addToHistoryPanel("Cards Were traded");
				
				board.update(RiskEvent.CardTrade);
				players.get(integer).incrementArmiesBy(extra);
				players.get(integer).incrementArmiesBy(getArmiesFromCardExchange());
				updateCardExchange( players.get(integer));

			}

		}
	}

	/**
	 * Returns the amount of armies when the cards are exchanged
	 * @return amount of armies
	 */
	private int getArmiesFromCardExchange()
	{
		int armies = 0;
		this.board.incrementCardExchanged();;
		if(this.board.getCardExchangeCount() <= 5)
		{
			armies = this.board.getCardExchangeCount() * 2 + 2;
		}
		else if(this.board.getCardExchangeCount() == 6)
		{
			armies = 15;
		}
		else
		{
			armies = 15  + ((this.board.getCardExchangeCount() - 6)*5);
		}
		return armies;
	}

	/**
	 * The number of players currently playing
	 * @return The number of players currently playing
	 */
	public int getNumberOfPlayers() {
		
		return this.players.keySet().size();
	}

	/**
	 * Returns the IPlayer object corresponding to the playerID given by index
	 * @param index the Player ID
	 * @return The IPlayer object
	 */
	public IPlayer getPlayer(int index) {
		return this.players.get(new Integer(index));
	}

	/**
	 * Returns the ID of the first player for testing purposes
	 * @return the ID of the first player for testing purposes
	 */
	public Integer testFirstPlayer() 
	{	
		return   new Integer ((int) players.keySet().toArray()[0]);
	}

	/**
	 * Returns The Maximum amount of rounds
	 * @return the maxRounds
	 */
	public int getMaxRounds() 
	{
		return maxRounds;
	}

	/**
	 * The Number of rounds played
	 * @return the number of rounds played
	 */
	public int getNbRoundsPlayed() 
	{
		
		return this.board.getnbRoundsPlayed();
	}

	/**
	 * The amount of times card were exchanged
	 * @return The amount of times cards were exchanged
	 */
	public int getCardExchangeCount() 
	{	
		return this.board.getCardExchangeCount();
	}

	/**
	 * Returns The players Turn Order
	 * @return The playerTurn Order
	 */
	public ArrayList<Integer> getPlayerTurnOrder() 
	{
		
		return this.playerTurnOrder;
	}

	/**
	 * Returns the current player
	 * @return The current Player
	 */
	public String getCurrentPlayer() {
		return this.currentPlayer;
	}


	
	/**
	 * Returns  The player 
	 * @param i the index of the player
	 * @return the player
	 */
	public IPlayer getPlayer(Integer i)
	{
		return players.get(i);
	}

	/**
	 * The History on the right panel/ history panel
	 * @return the string of history
	 */
	public String getHistory() {
		return board.getHistoryInfo();
	}

	/**
	 * The content of the History Pane
	 * @return The country info
	 */
	public String getCountryInfo() {
		return board.getountryInfo();
	}

	/**
	 * Returns the board object 
	 * @return the board object
	 */
	public RiskBoard getBoard() {
		
		return this.board;
	}

	/**
	 * Sets The panel Info on both side of the game View
	 * @param historyInfo History messages
	 * @param countryInfo Country Messaged
	 */
	public void setPanelInfo(String historyInfo, String countryInfo) 
	{
		this.board.addMessage(historyInfo);
		this.gamev.getCountryPanel().setText(countryInfo);
		this.board.update(RiskEvent.GeneralUpdate);
		
	}
 
	

	/**
	 * This Method sets the turn Order
	 * @param playerTurnOrder2 the new TurnOrder
	 */
	public void setPlayerTurnOrder(ArrayList<Integer> playerTurnOrder2) {
		this.playerTurnOrder = playerTurnOrder2;
		
	}

	/**
	 * Sets the number of time cards were exchanged
	 * @param cardExchangeCounts nb of times cards were exchanged
	 */
	public void setCardExchangeCounts(int cardExchangeCounts) 
	{
		this.board.setNbCardsExchanged(cardExchangeCounts);	
	}
	
	/**
	 * Sets the Maximum number of rounds
	 * @param maxRounds2 the new maximum number of rounds
	 */
	public void setMaxRounds(int maxRounds2) {
		this.maxRounds = maxRounds2;
		
	}

	/**
	 * Sets the number of rounds played
	 * @param nbRoundsPlayed2 new number of rounds played
	 */
	public void setnbRoundsPlayed(int nbRoundsPlayed2) {
		this.board.setNbRoundsPlayed(nbRoundsPlayed2);
		
	}

	/**
	 * Sets the currentPlayer
	 * @param currentPlayer2 the new Current Player
	 */
	public void setCurrentPlayer(String currentPlayer2) {
		this.currentPlayer = currentPlayer2;
		
	}

	/**
	 * Clears the engine
	 */
	public void clear() {
		this.board.clear();
		this.players.clear();
		if (playerTurnOrder != null)
				this.playerTurnOrder.clear();
		
		
	}

	/**
	 * Adds a player to the Game
	 * @param integer the player Id
	 * @param player a player object
	 */
	public void addPlayer(Integer integer, IPlayer player) 
	{
		players.put(integer, player);
	}
	
	/**
	 * Updates the Card Exchange View
	 * @param player the player object
	 */
	public void updateCardExchange(IPlayer player)
	{
		this.gamev.getCardPanel().populate(this.board.getnbRoundsPlayed(), this.board.getCardExchangeCount(), player);
		board.update(RiskEvent.CardTrade);
	}

	
	

}
