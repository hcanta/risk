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
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import risk.game.cards.Card;
import risk.model.RiskBoard;
import risk.model.maputils.Continent;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.model.playerutils.PlayerModel;
import risk.utils.Utils;
import risk.utils.constants.OtherConstants;
import risk.utils.constants.RiskEnum.CardType;
import risk.utils.constants.RiskEnum.GameState;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskEnum.RiskEvent;
import risk.utils.constants.RiskIntegers;
import risk.utils.constants.RiskStrings;
import risk.views.GameView;
import risk.views.ui.GraphDisplayPanel;
import risk.views.ui.MapSelector;

/**
 * The Implementation of the Game Engine
 * @author hcanta
 * @version 3.3
 */
public class GameEngine implements Serializable 
{
	
	/**
	 * Generated Serialization UID
	 */
	private static final long serialVersionUID = 2572722777333878769L;
	/**
	 * Random Generator for the class
	 */
	private Random rand;
	/**
	 * Number of times cards were exchange
	 */
	private int cardExchangeCount;

	
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
	private GameView gamev;
	
	/**
	 * Set to true if in debugging mode false otherwise
	 */
	private boolean debug;
	/**
	 *Constructor Of the Game Engine
	 *@param gamev The game View
	 *@param debug set to true for debugging or testing
	 */
	public GameEngine(GameView gamev, boolean debug) 
	{
		this.players = new HashMap<Integer, IPlayer>();
		rand = new Random();
		this.cardExchangeCount = 0;
		this.gamev = gamev;
		this.debug = debug;
		board = RiskBoard.ProperInstance(debug);
		if(gamev !=null)
		{
			addMenuItemActionListener();
		}
	}
	
	/**
	 * Simple Constructor of the GameEngine Without a game View
	 */
	public GameEngine()
	{
		this(null, true);
	}
	
	/**
	 * Adds The action Listener to the menu items of the Game View
	 */
	private void addMenuItemActionListener()
	{
		this.gamev.getRiskMenu().getMenuItemCreatetMap().addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				addToHistoryPanel("\n Entering Create Map Mode");
				Thread thread = new Thread(new Runnable() {
			         @Override
			         public void run() {
			             createMap();
			         }
				});
				thread.start();
			}
		});
		
		this.gamev.getRiskMenu().getMenuItemEditMap().addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					addToHistoryPanel("\n Entering Edit Map Mode");
					Thread thread = new Thread(new Runnable() {
				         @Override
				         public void run() {
				             editMap();
				         }
					});
					thread.start();
			
				}
		});
		
		this.gamev.getRiskMenu().menuItemOpenMap.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					addToHistoryPanel("\n Loading  Map Mode");

					if(loadMapHelper(true))
					{
						
						setState(GameState.STARTUP);
						Thread thread = new Thread(new Runnable() {
					         @Override
					         public void run() {
					             if(deploy(false))
					            	 play();
					         }
						});
						thread.start();
					}
					
			
				}
		});
		
	}
	
	/**
	 * Adds a message to the history panel
	 * @param message The message To be Added To the history panel
	 */
	private void addToHistoryPanel(String message)
	{
		gamev.getHistoryPanel().addMessage(message);
		board.update(RiskEvent.HistoryUpdate);
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
		
		JOptionPane.showMessageDialog(gamev.getHistoryPanel(),
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
	 * This function chooses the file to be edited or loaded
	 * @param load are we loading to play or to edit
	 * @return was the file properly loaded or not
	 */
	private boolean loadMapHelper(boolean load)
	{
		JFileChooser fileChooser =  MapSelector.getJFileChooser();
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
				
				if(Utils.loadFile(file, this.debug))
				{
					this.addToHistoryPanel("The Map File was properly loaded.");
					
					if(load)
					{
						this.gamev.getCenter().removeAll();
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
						this.gamev.getCenter().add((new GraphDisplayPanel(RiskBoard.ProperInstance(false).getGraph()).getContentPane()));
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
					}
					return true;
					
				}
				else
				{
					this.addToHistoryPanel("The Map File was invalid.");
					
					board.clear();
					if(load)
					{
						
						this.gamev.getCenter().removeAll();
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
						this.gamev.getCenter().add(OtherConstants.backGround);
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
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
			
			switch(option)
			{
				case 1:
					
					edited = edited || addContinent(RiskStrings.MENU_ITEM_EDIT_MAP, "");
					break;
				case 2:
					edited = edited || removeContinent();
					break;
				case 3:
					edited = edited || addTerritory();
					break;
				case 4:
					edited = edited || removeTerritory();
					break;
				case 5:
					edited = edited || addLink();
					break;
				case 6:
					edited = edited || removeLink();
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
				Utils.saveMap(str, debug);
				this.addToHistoryPanel("\n"+RiskStrings.DONE);
				
				board.clear();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		else
		{
			 this.addToHistoryPanel("\n"+RiskStrings.INVALID_MAP);
			 JOptionPane.showMessageDialog(gamev.getHistoryPanel(),
					 RiskStrings.INVALID_MAP);
			
			 board.clear();
		}
	}

	/**
	 * Removes a link between 2 territories
	 * @return was the link removed
	 */
	private boolean removeLink() {
		String[] info1 = getContinentAndCountry(RiskStrings.FIRST);
		String[] info2 = getContinentAndCountry(RiskStrings.SECOND);
		String continent1 = info1[0];
		String continent2 = info2[0];
		String ter1 = info1[1];
		String ter2 = info2[1];

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

		String[] info1 = getContinentAndCountry(RiskStrings.FIRST);
		String[] info2 = getContinentAndCountry(RiskStrings.SECOND);
		String continent1 = info1[0];
		String continent2 = info2[0];
		String ter1 = info1[1];
		String ter2 = info2[1];

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
		
			String[] info = getContinentAndCountry("");
			if(info == null)
				return false;
			return board.removeTerritory(info[0], info[1]);	
	}
	
	/**
	 * Get the the continent and country from user input
	 * @return an array containing the continent(0) and the country(1) 
	 * @param the index id if any
	 */
	private String[] getContinentAndCountry(String param)
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
			
			continent=continent.toLowerCase().trim();
			if(board.containsContinent(continent))
			{
				continent="";
				JOptionPane.showMessageDialog(gamev.getFrame(),
						RiskStrings.INVALID_CONTINENT,
						RiskStrings.MENU_ITEM_EDIT_MAP,
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		
		String country ="";
		while(country.length() == 0)
		{
			input =  (String)JOptionPane.showInputDialog(gamev.getFrame(), 
					RiskStrings.PLEASE_NAME+ param  +RiskStrings.TERRITORY,
					 RiskStrings.MENU_ITEM_EDIT_MAP, JOptionPane.PLAIN_MESSAGE, null, null, "");
			if(input == null)
				return null;
			
			country=country.toLowerCase().trim();
			if(board.getTerritories().contains(country))
			{
				country="";
				JOptionPane.showMessageDialog(gamev.getFrame(),
						RiskStrings.INVALID_TERRITORY,
						RiskStrings.MENU_ITEM_EDIT_MAP,
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		return new String[]{continent, country};
	}
	
	/**
	 * Adds a territory
	 * @return was a territory added
	 */
	private boolean addTerritory() 
	{
		String[] info = getContinentAndCountry("");
		if(info == null)
			return false;
		
		return board.addTerritory(info[0], info[1]);
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
	 * @return was a continent added
	 * @param dialogbox The name of the dialogbox
	 * @param the string preposition
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
		this.addToHistoryPanel(RiskStrings.NEW_STATE + state.name());
	}
	
	/**
	 * This function is called during the startup phase in order to deploy the game
	 * @param tournament are we in tournament mode or not
	 * @return true false, was the deployment successful or not
	 */
	private boolean deploy(boolean tournament)
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
		
		createBots(nbPlayer, tournament);
		if(!tournament)
		{
			String str ="";
			while(str.length() == 0)
			{
				input =  (String)JOptionPane.showInputDialog(gamev.getFrame(),RiskStrings.PLAYER_NAME, 
						RiskStrings.RISK, JOptionPane.PLAIN_MESSAGE, null, null, "");
				if(input == null)
					return false;
				str = input.trim();
			}
			
			addHumanPlayer(str);
		}
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
	 */
	public void addHumanPlayer(String name)
	{
		players.put(new Integer(0),new PlayerModel(name, PlayerColors.values()[0], (short)(0), debug));
		playerTurnOrder = new ArrayList<Integer>();
		for(int i=0; i< players.keySet().size(); ++i)
		{
			playerTurnOrder.add(new Integer(i));
		}
	}
	
	/**
	 * Generates computer player
	 * @param numberOfPlayers The Number of Players in the game
	 * @param tournament  are we in tournament mode or not
	 */
	public void createBots(int numberOfPlayers, boolean tournament)
	{
		if(!tournament)
		{
		players.clear();
			for(short i = 1; i< numberOfPlayers; i++)
			{			
				players.put(new Integer(i), new PlayerModel("Computer "+ i, PlayerColors.values()[i], (short)(i),debug));
			}
		}
		else //Creating specific bots
		{
			
		}
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
		
		for(int i =0; i < players.size(); i++)
		{
			players.get(new Integer(i)).setNbArmiesToBePlaced(nbArmiesToBePlaced);
		}
		if(gamev != null)
		{
			this.addToHistoryPanel(""+players.keySet().size()+" players,\n Armies assigned : "+nbArmiesToBePlaced);
		}
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
			Integer player = new Integer(i%this.playerTurnOrder.size());			
			players.get(player).addTerritory(countries.get(i));
			players.get(player).decrementArmies();
			board.getTerritory(countries.get(i)).setOwnerID(players.get(player).getTurnID());
			board.getTerritory(countries.get(i)).setArmyOn(1);
			board.update(RiskEvent.CountryUpdate);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			} 
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
		}
	}
	
	/**
	 * Play The Game
	 */
	public void play()
	{
		while(!isGameOver())
		{
			for(int i =0; i < this.playerTurnOrder.size(); i++)
			{
				board.setCurrentPlayer(players.get(this.playerTurnOrder.get(i)).getName());
				reinforcePhase(this.playerTurnOrder.get(i));
				attackPhase(this.playerTurnOrder.get(i));
				if(isGameOver())
				{
					this.setState(GameState.IDLE);
					this.addToHistoryPanel("The Game is Over");
					this.addToHistoryPanel("winner: "+ this.players.get(new Integer(board.getOwnerID())).getName());
					board.update(RiskEvent.GeneralUpdate);
					return;
				}
					
				fortifyPhase(this.playerTurnOrder.get(i));
			}
			
		}
	}
	/**
	 * The fortification phase
	 * @param integer the id of the player
	 */
	private void fortifyPhase(Integer integer) {
		
		this.setState(GameState.FORTIFY);
		//Player Object
		if((int)integer == 0)
		{
			int option = 0;
		
			while(option!=2)
			{
				System.out.println("1-Attempt Fortification");
				System.out.println("2-End fortification phase");

				
				option = sc.nextInt();
				if(option == 2)
				{
					break;
				}
				else if(option == 1)
				{
					sc.nextLine();
					System.out.print("Enter origin territory: ");
					String origin = sc.nextLine();
					System.out.print("Enter destination territory: ");
					String destination = sc.nextLine();
					System.out.print("Enter the number of armies to be moved: ");
					int armies = sc.nextInt();
					sc.nextLine();
					if(players.get(new Integer(0)).fortify(origin, destination, armies))
					{
						System.out.println("Fortification was successful");
					}
					else
					{
						System.out.println("Fortification was not successful");
					}
				}
			}
			
			
		}
		else 
		{
			pause();
		}
		
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
	private void attackPhase(Integer integer) {
		this.setState(GameState.ATTACK);
		if(canAttack(integer))
		{
			if((int)integer == 0)
			{
				int option = 0;
				boolean oneOnce = false;
				while(option!=2)
				{
					System.out.println("1-Attempt Attack");
					System.out.println("2-End Attack phase");
	
					
					option = sc.nextInt();
					if(option == 2)
					{
						break;
					}
					else if(option == 1)
					{
						sc.nextLine();
						System.out.print("Enter attacker territory: ");
						String strAttacker = sc.nextLine();
						Territory attacker = board.getTerritory(strAttacker);
						if(attacker.getArmyOn() > 1)
						{
							System.out.println("Here are the neighbours to "+strAttacker);
							System.out.println(attacker.getNeighbours());
							System.out.print("Enter defender: ");
							String strDefender = sc.nextLine();
							
							Territory defender = board.getTerritory(strDefender);
							System.out.println("Attacking country: " +attacker.getTerritoryName()+" Defending country: "+defender.getTerritoryName());
							System.out.println("there are "+attacker.getArmyOn()+" available for the attack\nHow many would you like to use ?");
							int nbOfdiceRollAttacker = sc.nextInt();sc.nextLine();
							if(attacker.getArmyOn()>= 4)
							{
								nbOfdiceRollAttacker = 3;
							}
							else if(attacker.getArmyOn() == 3)
							{
								nbOfdiceRollAttacker = 2;
							}
							else
							{
								nbOfdiceRollAttacker = 1;
							}
							
							int[] attackerDices = new int[nbOfdiceRollAttacker];
							for(int i=0; i< attackerDices.length;i++)
							{
								attackerDices[i] = rand.nextInt(6) + 1;
							}
							Arrays.sort(attackerDices);
							int nbOfdiceRollDefender;
						
							nbOfdiceRollDefender = 0;
							if(defender.getArmyOn()>= 2)
							{
								nbOfdiceRollDefender = 2;
							}
							else
							{
								nbOfdiceRollDefender = 1;
							}
							
							
							
							int[] defenderDices = new int[nbOfdiceRollDefender];
							for(int i=0; i< defenderDices.length;i++)
							{
								defenderDices[i] = rand.nextInt(6) + 1;
							}
							Arrays.sort(defenderDices);
							
							System.out.println(players.get(new Integer(attacker.getOwnerID())).getName()+" rolled: ");
							System.out.println(Arrays.toString(attackerDices));
							System.out.println(players.get(new Integer(defender.getOwnerID())).getName()+" rolled: ");
							System.out.println(Arrays.toString(defenderDices));
							this.addToHistoryPanel(players.get(new Integer(attacker.getOwnerID())).getName()+" rolled: ");
							this.addToHistoryPanel(Arrays.toString(attackerDices));
							this.addToHistoryPanel(players.get(new Integer(defender.getOwnerID())).getName()+" rolled: ");
							this.addToHistoryPanel(Arrays.toString(defenderDices));
							
							
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
							defender.setArmyOn(defender.getArmyOn() - attackerVictories);
							attacker.setArmyOn(attacker.getArmyOn() - defenderVictories);
							if(defender.getArmyOn() == 0)
							{
								if(!oneOnce)
								{
									int typeIndex = rand.nextInt(CardType.values().length);
									CardType type = CardType.values()[typeIndex];
									int nameIndex = rand.nextInt(board.getTerritories().size());
									String cardName = board.getTerritories().get(nameIndex);
									players.get(new Integer(attacker.getOwnerID())).getHand().add(new Card(type,cardName));
									oneOnce = true;
									this.addToHistoryPanel(players.get(integer).getName()+" has conquered a territory\n and won a card");
									
									board.update(RiskEvent.CountryUpdate);
								}
								
								players.get(new Integer(defender.getOwnerID())).removeTerritory(defender.getTerritoryName());
								defender.setOwnerID(attacker.getOwnerID());
								players.get(new Integer(attacker.getOwnerID())).addTerritory(defender.getTerritoryName());
								board.update(RiskEvent.CountryUpdate);
								if(board.getContinent(defender.getContinentName()).getOwnerID() == attacker.getOwnerID())
								{
									players.get(integer).incrementArmiesBy(board.getContinent(defender.getContinentName()).getContinentBonus());
									this.addToHistoryPanel(players.get(integer).getName()+" has  conquered a continent\n and won a card");
									
									if(isGameOver())
									{
										break;
									}
								}
								players.get(new Integer(attacker.getOwnerID())).fortify(attacker.getTerritoryName(), defender.getTerritoryName(), attackerVictories);
							
							}
						}
						else
						{
							System.out.println("Not enough armies to perform an attack");
						}
					}
				}
				
			}
			else //Robot Randomly picks a country  belonging to it, and attack one of the neighbors
			{
				boolean oneOnce = false;
				int countryindex = 0;
				for(String origin: players.get(integer).getTerritoriesOwned())
				{
					Territory attacker = board.getTerritory(origin);
					countryindex=  rand.nextInt(attacker.getNeighbours().size());
					if(!players.get(integer).getTerritoriesOwned().contains(attacker.getNeighbours().get(countryindex)))
					{
						
						Territory defender =  board.getTerritory(attacker.getNeighbours().get(countryindex));
						if(attacker.getArmyOn() >=2 )
						{
							System.out.println("Attacking country: " +attacker.getTerritoryName()+" Defending country: "+defender.getTerritoryName());
							int nbOfdiceRollAttacker = 0;
							if(attacker.getArmyOn()>= 4)
							{
								nbOfdiceRollAttacker = 3;
							}
							else if(attacker.getArmyOn() == 3)
							{
								nbOfdiceRollAttacker = 2;
							}
							else
							{
								nbOfdiceRollAttacker = 1;
							}
							
							int[] attackerDices = new int[nbOfdiceRollAttacker];
							for(int i=0; i< attackerDices.length;i++)
							{
								attackerDices[i] = rand.nextInt(6) + 1;
							}
							Arrays.sort(attackerDices);
							int nbOfdiceRollDefender;
							if(defender.getOwnerID() == 0 && defender.getArmyOn()>= 2)
							{
								System.out.print("How many dices would you like to roll 1 or 2? ");
								nbOfdiceRollDefender = sc.nextInt();
								sc.nextLine();
							}
							else
							{
								nbOfdiceRollDefender = 0;
								if(defender.getArmyOn()>= 2)
								{
									nbOfdiceRollDefender = 2;
								}
								else
								{
									nbOfdiceRollDefender = 1;
								}
							}
							
							
							int[] defenderDices = new int[nbOfdiceRollDefender];
							for(int i=0; i< defenderDices.length;i++)
							{
								defenderDices[i] = rand.nextInt(6) + 1;
							}
							Arrays.sort(defenderDices);
							System.out.println(players.get(new Integer(attacker.getOwnerID())).getName()+" rolled: ");
							System.out.println(Arrays.toString(attackerDices));
							System.out.println(players.get(new Integer(defender.getOwnerID())).getName()+" rolled: ");
							System.out.println(Arrays.toString(defenderDices));
							this.addToHistoryPanel(players.get(new Integer(attacker.getOwnerID())).getName()+" rolled: ");
							this.addToHistoryPanel(Arrays.toString(attackerDices));
							this.addToHistoryPanel(players.get(new Integer(defender.getOwnerID())).getName()+" rolled: ");
							this.addToHistoryPanel(Arrays.toString(defenderDices));
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
							defender.setArmyOn(defender.getArmyOn() - attackerVictories);
							attacker.setArmyOn(attacker.getArmyOn() - defenderVictories);
							if(defender.getArmyOn() == 0)
							{
								if(!oneOnce)
								{
									int typeIndex = rand.nextInt(CardType.values().length);
									CardType type = CardType.values()[typeIndex];
									int nameIndex = rand.nextInt(board.getTerritories().size());
									String cardName = board.getTerritories().get(nameIndex);
									players.get(new Integer(attacker.getOwnerID())).getHand().add(new Card(type,cardName));
									oneOnce = true;
									this.addToHistoryPanel(players.get(integer).getName()+" has conquered a territory\n and won a card");
									board.update(RiskEvent.CountryUpdate);
									
								}
								
								players.get(new Integer(defender.getOwnerID())).removeTerritory(defender.getTerritoryName());
								defender.setOwnerID(attacker.getOwnerID());
								players.get(new Integer(attacker.getOwnerID())).addTerritory(defender.getTerritoryName());
								board.update(RiskEvent.CountryUpdate);
								if(board.getContinent(defender.getContinentName()).getOwnerID() == attacker.getOwnerID())
								{
									players.get(integer).incrementArmiesBy(board.getContinent(defender.getContinentName()).getContinentBonus());
									this.addToHistoryPanel(players.get(integer).getName()+" has  conquered a continent\n and won a card");
									
									if(isGameOver())
									{
										break;
									}
								}
								players.get(new Integer(attacker.getOwnerID())).fortify(attacker.getTerritoryName(), defender.getTerritoryName(), attackerVictories);
								
							}
						}
					}
				}
				
			}
		}

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} 
		
	}

	/**
	 * Checks if the player with the owner ID integer can Attack
	 * @param integer owner/player ID
	 * @return can attack true/false
	 */
	public boolean canAttack(Integer integer) {
		
		for(int i =0; i < players.get(integer).getTerritoriesOwned().size(); i++)
		{
			String territory =  players.get(integer).getTerritoriesOwned().get(i);
			for(int  j =0; j <board.getTerritory(territory).getNeighbours().size(); j++)
			{
				String neighbor = board.getTerritory(territory).getNeighbours().get(j);
				if(board.getTerritory(territory).getArmyOn() > 1 && board.getTerritory(neighbor).getOwnerID() != (int)integer)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * The reinforcement phase
	 * @param integer The id of the player
	 */
	private void reinforcePhase(Integer integer) {
		this.setState(GameState.REINFORCE);
		int newArmies =(int)(players.get(integer).getTerritoriesOwned().size() < 9 ?3 :players.get(integer).getTerritoriesOwned().size()/3);
		this.addToHistoryPanel(players.get(integer).getName()+" has\n "+players.get(integer).getTerritoriesOwned().size()+" territories");
		this.addToHistoryPanel("Army received: " +newArmies);
		board.update(RiskEvent.GeneralUpdate);
		players.get(integer).incrementArmiesBy(newArmies);
		//Player Object
		if((int)integer == 0)
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
				
				if(!mustExchange && players.get(integer).getHand().canTurnInCards())
				{
					System.out.println("Do You whish to exchange cards ? 0 for No 1 for yes ");
					int opt = sc.nextInt();
					sc.nextLine();
					exchange = 1 == opt;
				}
				if(exchange)
				{
					this.addToHistoryPanel("Card Exchange in progress");
					
					board.update(RiskEvent.CardTrade);
					System.out.println("Here are the cards that you own");
					System.out.println(players.get(integer).getHand().toString());
					System.out.println("First index of card to be traded");
					int id1 = sc.nextInt();
					System.out.println("Second index of card to be traded");
					int id2 = sc.nextInt();
					System.out.println("third index of card to be traded");
					int id3 = sc.nextInt();
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
					
					
				}

			}
			//Reinforce portion

			int option = 0;
		
			while(option!=2)
			{
				System.out.println("Number of armies to be placed: " + players.get(integer).getNbArmiesToBePlaced());
				System.out.println("1-Attempt Reinforcement");
				System.out.println("2-End Reinforcement phase");

				
				option = sc.nextInt();
				if(option == 2)
				{
					break;
				}
				else if(option == 1)
				{
					sc.nextLine();
					System.out.print("Enter  territory to reinforce: ");
					String territory = sc.nextLine();
					System.out.print("Enter number of army to add: ");
					int army = sc.nextInt();
					sc.nextLine();
					
					if(players.get(integer).reinforce(territory, army))
					{
						System.out.println("Reinforcement was successful");
					}
					else
					{
						System.out.println("Reinforcement was not successful");
					}
				}
			}
			
		}
		else // Robot object
		{
		
		}
		
		pause();
		
	}

	/**
	 * Checks if the Game is Done Or not
	 * @return true/false is the game over or not
	 */
	private boolean isGameOver() {
		return board.isGameOver();
	}
	
	/**
	 * Returns the amount of armies when the cards are exchanged
	 * @return amount of armies
	 */
	private int getArmiesFromCardExchange()
	{
		int armies = 0;
		this.cardExchangeCount ++;
		if(this.cardExchangeCount <= 5)
		{
			armies = this.cardExchangeCount * 2 + 2;
		}
		else if(this.cardExchangeCount == 6)
		{
			armies = 15;
		}
		else
		{
			armies = 15  + ((this.cardExchangeCount - 6)*5);
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
	 * The Scanner object to read from standard input
	 */
	private Scanner sc = new Scanner(System.in);

}
