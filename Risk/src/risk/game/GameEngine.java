/**
 * The game package holds the driver and the Game Engine
 */
package risk.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import risk.game.cards.Card;
import risk.model.PlayerModel;
import risk.model.RiskBoard;
import risk.model.maputils.Territory;
import risk.model.playerutils.IPlayer;
import risk.utils.MapUtils;
import risk.utils.constants.RiskEnum.CardType;
import risk.utils.constants.RiskEnum.GameState;
import risk.utils.constants.RiskEnum.PlayerColors;
import risk.utils.constants.RiskStrings;
import risk.views.GameView;
import risk.views.ui.GraphDisplayPanel;
import risk.views.ui.MapSelector;

/**
 * The Implementation of the Game Engine
 * @author hcanta
 * @version 2.2
 */
public class GameEngine {
	
	/**
	 * Random Generator for the class
	 */
	private Random rand;
	/**
	 * Number of times cards were exchange
	 */
	private int cardExchangeCount;

	/**
	 * The Scanner object to read from standard input
	 */
	private Scanner sc = new Scanner(System.in);
	/**
	 * Creates a mapping of player IDs to IPlayer Object
	 */
	private HashMap<Integer, IPlayer> players;
	
	/**
	 * Contains the order in which the players will play
	 */
	private ArrayList<Integer> playerTurnOrder;
	/**
	 * Set to true if we re debugging or not
	 */
	private boolean debug;
	/**
	 * The Game View
	 */
	private GameView gamev;
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
		this.gamev.getRiskMenu().menuItemCreateMap.addActionListener(new ActionListener()
		{
		
			public void actionPerformed(ActionEvent e)
			{
				gamev.getHistoryPanel().addMessage("\n Entering Create Map Mode");
				Thread thread = new Thread(new Runnable() {
			         @Override
			         public void run() {
			             createMap();
			         }
				});
				thread.start();
			}
		});
		
		this.gamev.getRiskMenu().menuItemEditMap.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					gamev.getHistoryPanel().addMessage("\n Entering Edit Map Mode");
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
					gamev.getHistoryPanel().addMessage("\n Loading  Map Mode");

					loadMapHelper(true);
					RiskBoard.ProperInstance(debug).update();
					setState(GameState.STARTUP);
					Thread thread = new Thread(new Runnable() {
				         @Override
				         public void run() {
				             deploy();
				             play();
				         }
					});
					thread.start();
					
			
				}
		});
		
	}
	
	/**
	 * Creates a Map from scratch with user input
	 */
	public void createMap() 
	{
		RiskBoard.ProperInstance(debug).clear();
		RiskBoard.ProperInstance(debug).setBoardName("Created Map");
		
		String str = "";
		int nbContinent = 0;
		while(nbContinent < 2)
		{
			System.out.print("How many continents would you like to create? please chose a number >= 2");
			nbContinent = sc.nextInt();
		}
		sc.nextLine();
		int bonus = 0;
		for(int i =0; i<nbContinent; i++)
		{
			System.out.print("Please Enter the name of the " + i +" continent: " );
			str = sc.nextLine();
			str = str.toLowerCase().trim();
			if(str.length()== 0 || !RiskBoard.ProperInstance(this.debug).getContinents().contains(str))
			{
				while(bonus <= 0)
				{
					System.out.print("Please enter a bonus for continent "+str+": ");
					bonus = sc.nextInt();
				}
				sc.nextLine();
				RiskBoard.ProperInstance(this.debug).addContinent(str, bonus);
				bonus = 0;
				str = "";
			}
			else
			{
				i--;
				System.out.println("This continent name already exists or the name is invalid");
			}
		}
		
		System.out.println("The folowing continents were created");
		System.out.println(RiskBoard.ProperInstance(this.debug).continentsToString());
		
		int nbCountries;
		for(int i =0; i<nbContinent;i++)
		{
			System.out.println("\nContinent: " + RiskBoard.ProperInstance(this.debug).getContinents().get(i));
			nbCountries = 0;
			while(nbCountries <= 0)
			{
				System.out.print("Please Enter the number of territories for " +RiskBoard.ProperInstance(this.debug).getContinents().get(i)+": ");
				
				nbCountries = sc.nextInt();
			}
			sc.nextLine();
			String country= "";
			for(int j =0; j< nbCountries; j++)
			{
				country= "";
				while(country.length()==0)
				{
					System.out.print("Please enter the name of the "+j+"th territory: ");
					country = sc.nextLine().toLowerCase().trim();
				}
				
				int nbNeighbours =0;
				while(nbNeighbours <=0)
				{
					System.out.print("Please enter the number of neighbours for territory "+country+": ");
					nbNeighbours = sc.nextInt();
					
				}
				sc.nextLine();
				str = "";
				
				RiskBoard.ProperInstance(this.debug).addTerritory(RiskBoard.ProperInstance(this.debug).getContinents().get(i), country);
				for(int k =0; k <nbNeighbours; k++)
				{
					while(str.length()==0)
					{
						System.out.print("Please enter the neighbour name for territory "+country+": ");
						str = sc.nextLine().toLowerCase().trim();
						
						
					}
					RiskBoard.ProperInstance(this.debug).getTerritory(country).addNeighbours(str);
					str= "";
				}
				
			}
		}
		 System.out.println(RiskBoard.ProperInstance(this.debug).toString());
		 
		 boolean valid = RiskBoard.ProperInstance(this.debug).validateMap();
		 
		 if(valid)
		 {
			 str = "";
			 while(str.length() ==0)
			 {
				 System.out.print("The map is Valid please enter a name to save it: ");
				 str = sc.nextLine().trim();
				 this.gamev.getHistoryPanel().addMessage("\n The Map was saved: "+str);
			 }
			 try {
				MapUtils.saveMap(str, debug);
				this.gamev.getHistoryPanel().addMessage("\n Done");
				System.out.println("Done");
				RiskBoard.ProperInstance(this.debug).clear();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		 }
		 else
		 {
			 this.gamev.getHistoryPanel().addMessage("\n The Map is Invalid");
			 System.out.println("The map is Invalid");
			 RiskBoard.ProperInstance(this.debug).clear();
		 }
		
		
	}
	/**
	 * This function chooses the file to be edited or loaded
	 * @param load are we loading to play or to edit
	 */
	private void loadMapHelper(boolean load)
	{
		JFileChooser fileChooser =  MapSelector.getJFileChooser();
		int result = fileChooser.showOpenDialog(this.gamev.getFrame());
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			this.gamev.getHistoryPanel().addMessage("Attempting To Load Map");
			RiskBoard.ProperInstance(this.debug).update();
			
			File file = fileChooser.getSelectedFile();
			if (!file.exists() && !file.isDirectory()) 
			{
				JOptionPane.showMessageDialog(null,
						RiskStrings.INVALID_FILE_LOCATION
								+ file.getAbsolutePath());
				
				this.gamev.getHistoryPanel().addMessage(RiskStrings.INVALID_FILE_LOCATION);
				RiskBoard.ProperInstance(this.debug).update();
			}
			else
			{
				this.gamev.getHistoryPanel().addMessage("file name is: " + file.getName());
				RiskBoard.ProperInstance(this.debug).update();
				if(MapUtils.loadFile(file, this.debug))
				{
					this.gamev.getHistoryPanel().addMessage("The Map File was properly loaded.");
					RiskBoard.ProperInstance(this.debug).update();
					if(load)
					{
						this.gamev.getCenter().removeAll();
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
						this.gamev.getCenter().add((new GraphDisplayPanel(RiskBoard.ProperInstance(false).getGraph()).getContentPane()));
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
					}
					
				}
				else
				{
					this.gamev.getHistoryPanel().addMessage("The Map File was invalid.");
					RiskBoard.ProperInstance(this.debug).update();
					RiskBoard.ProperInstance(this.debug).clear();
					if(load)
					{
						
						this.gamev.getCenter().removeAll();
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
						this.gamev.getCenter().add(GameView.backGround);
						this.gamev.getCenter().repaint();
						this.gamev.getCenter().validate();
					}
				}
			}
		}
	}  
	
	
	/**
	 * Edit the Map That was Loaded
	 */
	public void editMap() 
	{
		loadMapHelper(false);
		Scanner sc = new Scanner(System.in);
		String str = "";
		int option =0;
		while(option!=7)
		{
			System.out.println("1-Add a Continent");
			System.out.println("2-remove a Continent");
			System.out.println("3-Add a territory");
			System.out.println("4-Remove a territory");
			System.out.println("5-Add a link between two existing territories");
			System.out.println("6 remove a link between two existing territories");
			System.out.println("7-Done");
			
			option = sc.nextInt();
			sc.nextLine();
			switch(option)
			{
				case 1:
					
					addContinent(sc);
					break;
				case 2:
					removeContinent(sc);
					break;
				case 3:
					addTerritory(sc);
					break;
				case 4:
					removeTerritory(sc);
					break;
				case 5:
					addLink(sc);
					break;
				case 6:
					removeLink(sc);
					break;
				default:
					break;
				
			}
			
			
		}
		boolean valid = RiskBoard.ProperInstance(this.debug).validateMap();
		 if(valid)
		 {
			 str = "";
			 while(str.length() ==0)
			 {
				 gamev.getHistoryPanel().addMessage(" \n The Map was properly Edited");
				 System.out.print("The map is Valid please enter a name to save it: ");
				 str = sc.nextLine().trim();
				 
			 }
			 try {
				MapUtils.saveMap(str, debug);
				this.gamev.getHistoryPanel().addMessage("\n The Map was saved: "+str);
				System.out.println("Done");
			} catch (IOException e) {

				e.printStackTrace();
			}
		 }
		 else
		 {
			 System.out.println("The map is Invalid");
		 }
		// sc.close();
	}

	/**
	 * Removes a link between 2 territories
	 * @param sc the scanner parameter for display to console
	 */
	private void removeLink(Scanner sc) {
		String continent1 ="";
		String continent2="";
		
		String ter1="";
		String ter2="";
		
		while(continent1.length() == 0 )
		{
			System.out.print("Please enter the name of the continent to which the first territory belongs: " );
			continent1=sc.nextLine();
			continent1= continent1.toLowerCase().trim();
			if(!RiskBoard.ProperInstance(this.debug).containsContinent(continent1))
			{
				System.out.print("This continent Does not exist");
				continent1 = "";
				break;
				
			}
		}
		if(continent1.length()>0)
		{
			while(continent2.length() == 0 )
			{
				System.out.print("Please enter the name of the continent to which the second territory belongs: " );
				continent2=sc.nextLine();
				continent2= continent2.toLowerCase().trim();
				if(!RiskBoard.ProperInstance(this.debug).containsContinent(continent2))
				{
					System.out.print("This continent Does not exist");
					continent2 = "";
					break;
					
				}
			}
			if(continent2.length()>0)
			{
				while(ter1.length()==0)
				{
					System.out.print("Please enter the name of the first territory: ");
					ter1 =sc.nextLine();
					ter1.toLowerCase().trim();
					
					if(!RiskBoard.ProperInstance(this.debug).getContinent(continent1).getTerritories().contains(ter1))
					{
						System.out.println("This territory does not belong to the map");
						ter1 = "";
					}
				}
				while(ter2.length()==0)
				{
					System.out.print("Please enter the name of the second territory: ");
					ter2 =sc.nextLine();
					ter2.toLowerCase().trim();
					
					if(!RiskBoard.ProperInstance(this.debug).getContinent(continent2).getTerritories().contains(ter2))
					{
						System.out.println("This territory does not belong to the map");
						ter2 = "";
					}
				}
				RiskBoard.ProperInstance(this.debug).getContinent(continent1).getTerritory(ter1).removeNeighbours(ter2);
				RiskBoard.ProperInstance(this.debug).getContinent(continent2).getTerritory(ter2).removeNeighbours(ter1);
				
				
			}
		}
		
		
	}
	/**
	 * Adds a link between 2 territories
	 * @param sc the scanner parameter for display to console
	 */
	private void addLink(Scanner sc) {
		String continent1 ="";
		String continent2="";
		
		String ter1="";
		String ter2="";
		
		while(continent1.length() == 0 )
		{
			System.out.print("Please enter the name of the continent to which the first territory belongs: " );
			continent1=sc.nextLine();
			continent1= continent1.toLowerCase().trim();
			if(!RiskBoard.ProperInstance(this.debug).containsContinent(continent1))
			{
				System.out.print("This continent Does not exist");
				continent1 = "";
				break;
				
			}
		}
		if(continent1.length()>0)
		{
			while(continent2.length() == 0 )
			{
				System.out.print("Please enter the name of the continent to which the second territory belongs: " );
				continent2=sc.nextLine();
				continent2= continent2.toLowerCase().trim();
				if(!RiskBoard.ProperInstance(this.debug).containsContinent(continent2))
				{
					System.out.print("This continent Does not exist");
					continent2 = "";
					break;
					
				}
			}
			if(continent2.length()>0)
			{
				while(ter1.length()==0)
				{
					System.out.print("Please enter the name of the first territory: ");
					ter1 =sc.nextLine();
					ter1.toLowerCase().trim();
					
					if(!RiskBoard.ProperInstance(this.debug).getContinent(continent1).getTerritories().contains(ter1))
					{
						System.out.println("This territory does not belong to the map");
						ter1 = "";
					}
				}
				while(ter2.length()==0)
				{
					System.out.print("Please enter the name of the second territory: ");
					ter2 =sc.nextLine();
					ter2.toLowerCase().trim();
					
					if(!RiskBoard.ProperInstance(this.debug).getContinent(continent2).getTerritories().contains(ter2))
					{
						System.out.println("This territory does not belong to the map");
						ter2 = "";
					}
				}
				RiskBoard.ProperInstance(this.debug).getContinent(continent1).getTerritory(ter1).addNeighbours(ter2);
				RiskBoard.ProperInstance(this.debug).getContinent(continent2).getTerritory(ter2).addNeighbours(ter1);
				
				
			}
		}
		
	}
	/**
	 * Removes a territory
	 * @param sc the scanner parameter for display to console
	 */
	private void removeTerritory(Scanner sc) {
		String continent ="";
		
		while(continent.length() == 0 )
		{
			System.out.print("Please enter the name of the continent to which the country need to be removed belong: " );
			continent=sc.nextLine();
			continent= continent.toLowerCase().trim();
			if(!RiskBoard.ProperInstance(this.debug).containsContinent(continent))
			{
				System.out.print("This continent Does not exist");
				continent = "";
				break;
				
			}
		}
		if(continent.length() > 0)
		{
			String country ="";
			while(country.length()==0)
			{
				System.out.println("Please enter the name of the territory to be removed: ");
				country = sc.nextLine();
				country = country.toLowerCase().trim();
			}
			
			RiskBoard.ProperInstance(this.debug).removeTerritory(continent, country);
		}
		
	}
	/**
	 * Adds a territory
	 * @param sc the scanner parameter for display to console
	 */
	private void addTerritory(Scanner sc) 
	{
		String continent ="";
		
		while(continent.length() == 0 )
		{
			System.out.print("Please enter the name of the continent to which the country need to be added: " );
			continent=sc.nextLine();
			continent= continent.toLowerCase().trim();
			if(!RiskBoard.ProperInstance(this.debug).containsContinent(continent))
			{
				System.out.print("This continent Does not exist");
				continent = "";
				break;
				
			}
		}
		if(continent.length() > 0)
		{
			String country ="";
			while(country.length()==0)
			{
				System.out.println("Please enter the name of the territory to be added: ");
				country = sc.nextLine();
				country = country.toLowerCase().trim();
			}
			
			RiskBoard.ProperInstance(this.debug).addTerritory(continent, country);
		}
		
	}
	/**
	 * Removes a continent
	 * @param sc the scanner parameter for display to console
	 */
	private void removeContinent(Scanner sc) {
		String str ="";

		while(str.length() == 0)
		{
			System.out.print("Please enter the name of the continent to be removed: " );
			str=sc.nextLine();
		}
		RiskBoard.ProperInstance(this.debug).removeContinent(str);
	}

	/**
	 * Adds a Continent
	 * @param sc the scanner parameter for display to console
	 */
	private void addContinent(Scanner sc) 
	{
		String str ="";
		int bonus = 0;
		while(str.length() == 0)
		{
			System.out.print("Please enter the name of the continent to be added: " );
			str=sc.nextLine();
			str = str.toLowerCase().trim();
		}
		
		while(bonus <= 0)
		{
			System.out.print("Please enter the associated bonus: " );
			bonus=sc.nextInt();
		}
		sc.nextLine();
		RiskBoard.ProperInstance(this.debug).addContinent(str, bonus);
	}
	
	/**
	 * Set the current state of the game
	 * @param state the state to be set
	 */
	private void setState(GameState state)
	{
		RiskBoard.ProperInstance(this.debug).setState(state);
	}
	
	/**
	 * This function is called during the startup phase in order to deploy the game
	 */
	private void deploy()
	{
		players = new HashMap<Integer, IPlayer>();
		
	
		int nbPlayer = 0;
		while(!(nbPlayer >=2 && nbPlayer <= 6))
		{
			System.out.print("Please Set the Number of players between 2 and 6: ");
			nbPlayer = sc.nextInt();
		}
		
		sc.nextLine();
		createBots(nbPlayer);
		String str ="";
		while(str.length() == 0)
		{
			System.out.println("Please Enter your name");
			str = sc.nextLine();
		}
		
		addHumanPlayer(str);
		Collections.shuffle(playerTurnOrder);

		setArmiesforPlayers();
		System.out.println("Random Assignement of countries");
		randomAssignTerritories();
		System.out.println("Placing Remaining armies");
		placeRemainingArmies();
		for(int i =0; i< players.keySet().size(); i++)
		{
			System.out.println(players.get(new Integer(i)).getName());
			System.out.println("Territories Owned");
			System.out.println(players.get(new Integer(i)).getTerritoriesOwnedWithArmies());
		}
		
		
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
	 */
	public void createBots(int numberOfPlayers)
	{
		players.clear();
		for(short i = 1; i< numberOfPlayers; i++)
		{			
			players.put(new Integer(i), new PlayerModel("Computer "+ i, PlayerColors.values()[i], (short)(i),debug));
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
		this.gamev.getHistoryPanel().addMessage(""+players.keySet().size()+" players,\n Armies assigned : "+nbArmiesToBePlaced);
		RiskBoard.ProperInstance(debug).update();
		return nbArmiesToBePlaced;
	}
	
	/**
	 * Randomly assign territories according to the risk rules
	 */
	public void randomAssignTerritories()
	{
		ArrayList<String> countries = RiskBoard.ProperInstance(debug).getTerritories();
		Collections.shuffle(countries);
		

		for(int i=0; i< countries.size(); i++)
		{
			Integer player = new Integer(i%this.playerTurnOrder.size());			
			players.get(player).addTerritory(countries.get(i));
			players.get(player).decrementArmies();
			RiskBoard.ProperInstance(debug).getTerritory(countries.get(i)).setOwnerID(players.get(player).getTurnID());
			RiskBoard.ProperInstance(debug).getTerritory(countries.get(i)).setArmyOn(1);
			RiskBoard.ProperInstance(debug).update();
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
				RiskBoard.ProperInstance(debug).setCurrentPlayer(players.get(this.playerTurnOrder.get(i)).getName());
				reinforcePhase(this.playerTurnOrder.get(i));
				attackPhase(this.playerTurnOrder.get(i));
				if(isGameOver())
				{
					this.setState(GameState.IDLE);
					this.gamev.getHistoryPanel().addMessage("The Game is Over");
					this.gamev.getHistoryPanel().addMessage("winner: "+ this.players.get(new Integer(RiskBoard.ProperInstance(debug).getOwnerID())).getName());
					RiskBoard.ProperInstance(debug).update();
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
		else //Robot Randomly picks a country and perform reinforce otherwise skip at the next iteration will be changed by strategy
		{ 
			String o1 = "";
			String d1 = "";
			for(String origin: players.get(integer).getTerritoriesOwned())
			{
				d1 = "";
				o1 = origin;
				for(String destination: players.get(integer).getTerritoriesOwned())
				{
					
					if(!origin.equals(destination))
					{
						if(RiskBoard.ProperInstance(debug).getTerritory(o1).getNeighbours().contains(destination))
						{
							d1 = destination;
							break;
						}
					}
				}
				if(d1.length() >0)
				{
					break;
				}
			}
			if(d1.length() > 0)
			{
				players.get(integer).fortify(o1, d1, 1);
			}
		}
		try {
			Thread.sleep(2500);
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
						Territory attacker = RiskBoard.ProperInstance(debug).getTerritory(strAttacker);
						if(attacker.getArmyOn() > 1)
						{
							System.out.println("Here are the neighbours to "+strAttacker);
							System.out.println(attacker.getNeighbours());
							System.out.print("Enter defender: ");
							String strDefender = sc.nextLine();
							
							Territory defender = RiskBoard.ProperInstance(debug).getTerritory(strDefender);
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
							this.gamev.getHistoryPanel().addMessage(players.get(new Integer(attacker.getOwnerID())).getName()+" rolled: ");
							this.gamev.getHistoryPanel().addMessage(Arrays.toString(attackerDices));
							this.gamev.getHistoryPanel().addMessage(players.get(new Integer(defender.getOwnerID())).getName()+" rolled: ");
							this.gamev.getHistoryPanel().addMessage(Arrays.toString(defenderDices));
							
							RiskBoard.ProperInstance(this.debug).update();
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
									int nameIndex = rand.nextInt(RiskBoard.ProperInstance(debug).getTerritories().size());
									String cardName = RiskBoard.ProperInstance(debug).getTerritories().get(nameIndex);
									players.get(new Integer(attacker.getOwnerID())).getHand().add(new Card(type,cardName));
									oneOnce = true;
									this.gamev.getHistoryPanel().addMessage(players.get(integer).getName()+" has conquered a territory\n and won a card");
									RiskBoard.ProperInstance(this.debug).update();
								}
								
								players.get(new Integer(defender.getOwnerID())).removeTerritory(defender.getTerritoryName());
								defender.setOwnerID(attacker.getOwnerID());
								players.get(new Integer(attacker.getOwnerID())).addTerritory(defender.getTerritoryName());
								RiskBoard.ProperInstance(this.debug).update();
								if(RiskBoard.ProperInstance(debug).getContinent(defender.getContinentName()).getOwnerID() == attacker.getOwnerID())
								{
									players.get(integer).incrementArmiesBy(RiskBoard.ProperInstance(debug).getContinent(defender.getContinentName()).getContinentBonus());
									this.gamev.getHistoryPanel().addMessage(players.get(integer).getName()+" has  conquered a continent\n and won a card");
									RiskBoard.ProperInstance(this.debug).update();
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
					Territory attacker = RiskBoard.ProperInstance(debug).getTerritory(origin);
					countryindex=  rand.nextInt(attacker.getNeighbours().size());
					if(!players.get(integer).getTerritoriesOwned().contains(attacker.getNeighbours().get(countryindex)))
					{
						
						Territory defender =  RiskBoard.ProperInstance(debug).getTerritory(attacker.getNeighbours().get(countryindex));
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
							this.gamev.getHistoryPanel().addMessage(players.get(new Integer(attacker.getOwnerID())).getName()+" rolled: ");
							this.gamev.getHistoryPanel().addMessage(Arrays.toString(attackerDices));
							this.gamev.getHistoryPanel().addMessage(players.get(new Integer(defender.getOwnerID())).getName()+" rolled: ");
							this.gamev.getHistoryPanel().addMessage(Arrays.toString(defenderDices));
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
									int nameIndex = rand.nextInt(RiskBoard.ProperInstance(debug).getTerritories().size());
									String cardName = RiskBoard.ProperInstance(debug).getTerritories().get(nameIndex);
									players.get(new Integer(attacker.getOwnerID())).getHand().add(new Card(type,cardName));
									oneOnce = true;
									this.gamev.getHistoryPanel().addMessage(players.get(integer).getName()+" has conquered a territory\n and won a card");
									RiskBoard.ProperInstance(this.debug).update();
								}
								
								players.get(new Integer(defender.getOwnerID())).removeTerritory(defender.getTerritoryName());
								defender.setOwnerID(attacker.getOwnerID());
								players.get(new Integer(attacker.getOwnerID())).addTerritory(defender.getTerritoryName());
								RiskBoard.ProperInstance(this.debug).update();
								if(RiskBoard.ProperInstance(debug).getContinent(defender.getContinentName()).getOwnerID() == attacker.getOwnerID())
								{
									players.get(integer).incrementArmiesBy(RiskBoard.ProperInstance(debug).getContinent(defender.getContinentName()).getContinentBonus());
									this.gamev.getHistoryPanel().addMessage(players.get(integer).getName()+" has  conquered a continent\n and won a card");
									RiskBoard.ProperInstance(this.debug).update();
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
			for(int  j =0; j <RiskBoard.ProperInstance(debug).getTerritory(territory).getNeighbours().size(); j++)
			{
				String neighbor = RiskBoard.ProperInstance(debug).getTerritory(territory).getNeighbours().get(j);
				if(RiskBoard.ProperInstance(debug).getTerritory(territory).getArmyOn() > 1 && RiskBoard.ProperInstance(debug).getTerritory(neighbor).getOwnerID() != (int)integer)
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
		this.gamev.getHistoryPanel().addMessage(players.get(integer).getName()+" has\n "+players.get(integer).getTerritoriesOwned().size()+" territories");
		this.gamev.getHistoryPanel().addMessage("Army received: " +newArmies);
		RiskBoard.ProperInstance(debug).update();
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
					this.gamev.getHistoryPanel().addMessage(players.get(integer).getName()+" Must Turn In Cards");
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
					this.gamev.getHistoryPanel().addMessage("Card Exchange in progress");
					RiskBoard.ProperInstance(debug).update();
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
						this.gamev.getHistoryPanel().addMessage("One of the card traded\n shows a country occupied\n by player 2 extra armies ");
						RiskBoard.ProperInstance(debug).update();
					}
					players.get(integer).getHand().removeCardsFromHand(iArr[0], iArr[1], iArr[2]);
					this.gamev.getHistoryPanel().addMessage("Cards Were traded");
					RiskBoard.ProperInstance(debug).update();
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
			int extra = 0;
			if(players.get(integer).getHand().mustTurnInCards() || players.get(integer).getHand().canTurnInCards())
			{
				if(players.get(integer).getHand().mustTurnInCards())
				{
					this.gamev.getHistoryPanel().addMessage(players.get(integer).getName()+" Must Turn In Cards");
				}
				Card card1 = players.get(integer).getHand().getCards().get(0);
				Card card2 = players.get(integer).getHand().getCards().get(1);
				Card card3 = players.get(integer).getHand().getCards().get(2);
				if(players.get(integer).getTerritoriesOwned().contains(card1.getTerritory())
						||players.get(integer).getTerritoriesOwned().contains(card2.getTerritory())
						||players.get(integer).getTerritoriesOwned().contains(card3.getTerritory())
						)
				{
					extra = 2;
					this.gamev.getHistoryPanel().addMessage("One of the card traded\n shows a country occupied\n by player 2 extra armies ");
				}
				players.get(integer).getHand().removeCardsFromHand(0, 1, 2);
				this.gamev.getHistoryPanel().addMessage("Cards Were traded");
				RiskBoard.ProperInstance(debug).update();
				players.get(integer).incrementArmiesBy(extra);
				players.get(integer).incrementArmiesBy(getArmiesFromCardExchange());
			}
			while(players.get(integer).getNbArmiesToBePlaced() > 0)
			{
				int index = rand.nextInt(players.get(integer).nbTerritoriesOwned());
				players.get(integer).reinforce(players.get(integer).getTerritoriesOwned().get(index));
			}
			
		}
		
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} 
		
	}

	/**
	 * Checks if the Game is Done Or not
	 * @return true/false is the game over or not
	 */
	private boolean isGameOver() {
		return RiskBoard.ProperInstance(debug).isGameOver();
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
}
