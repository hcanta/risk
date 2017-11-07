/**
 * The game package holds the driver and the Game Engine
 */
package risk.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import risk.model.PlayerModel;
import risk.model.RiskBoard;
import risk.model.playerutils.IPlayer;
import risk.utils.MapUtils;
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
		Scanner sc = new Scanner(System.in);
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
		
		sc.close();
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
		 sc.close();
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
		playerTurnOrder = new ArrayList<Integer>();
		Scanner sc = new Scanner(System.in);
		int nbPlayer = 0;
		while(!(nbPlayer >=2 && nbPlayer <= 6))
		{
			System.out.print("Please Set the Number of players between 2 and 6: ");
			nbPlayer = sc.nextInt();
		}
		
		sc.nextLine();
		createBots(nbPlayer);
		for(int i=0; i< nbPlayer; ++i)
		{
			playerTurnOrder.add(new Integer(i));
		}
		Collections.shuffle(playerTurnOrder);
		String str ="";
		while(str.length() == 0)
		{
			System.out.println("Please Enter your name");
			str = sc.nextLine();
		}
		
		addHumanPlayer(str);
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
		sc.close();
		
	}
	
	/**
	 * Ad user/human player
	 * @param name The players name
	 */
	public void addHumanPlayer(String name)
	{
		players.put(new Integer(0),new PlayerModel(name, PlayerColors.values()[0], (short)(0), debug));
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
		Random rand = new Random();
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
		// TODO Auto-generated method stub
		this.setState(GameState.FORTIFY);
		//Player Object
		if((int)integer == 0)
		{
			
		}
		else //Robot Randomly picks a country and perform reinforce otherwise skip
		{
			
		}
		
	}

	/**
	 * The attack Phase
	 * @param integer the id Of the Player
	 */
	private void attackPhase(Integer integer) {
		// TODO Auto-generated method stub
		this.setState(GameState.ATTACK);
		
	}

	/**
	 * The reinforcement phase
	 * @param integer The id of the player
	 */
	private void reinforcePhase(Integer integer) {
		// TODO Auto-generated method stub
		this.setState(GameState.REINFORCE);
		
	}

	/**
	 * Checks if the Game is Done Or not
	 * @return true/false is the game over or not
	 */
	private boolean isGameOver() {
		return RiskBoard.ProperInstance(debug).isGameOver();
	}

}
