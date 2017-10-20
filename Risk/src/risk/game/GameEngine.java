
package risk.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import risk.model.ModelException;
import risk.model.character.ICharacter;
import risk.model.character.PlayerModel;
import risk.util.RiskEnum.RiskColor;
import risk.util.map.RiskBoard;
import risk.util.map.editor.Utilities;

/**
 * Handles most of the operation happening during the Game and is a singleton
 * @author hcanta
 * 
 */
public class GameEngine 
{
	
	private ArrayList<ICharacter> players;
	private short numberOfPlayers;
	
	public static GameEngine Instance = new GameEngine();
	
	public GameEngine ()
	{
		
	}
	/**
	 * Start up phase of the game
	 */
	public void startup()
	{
		Scanner sc = new Scanner(System.in);
		int nbPlayer = 0;
		while(!(nbPlayer >=2 && nbPlayer <= 6))
		{
			System.out.print("Please Set the Number of players between 2 and 6: ");
			nbPlayer = sc.nextInt();
		}
		setNumberofPlayers((short)nbPlayer);
		sc.nextLine();
		createBots();
		String str ="";
		
		while(str.length() == 0)
		{
			System.out.println("Please Enter your name");
			str = sc.nextLine();
		}
		
		addHumanPlayer(str);
		setArmiesforPlayers();
		System.out.println("Random Assign");
		randomAssignTerritories();
		System.out.println("Reinforcement");
		sc.nextLine();
		
		boolean armiesToBeplaced = true;
		checkOwnerStatus();
		Collections.shuffle(players);
		for(int i =0; i< players.size(); i++)
		{
			System.out.println(players.get(i).getName());
			System.out.println("Territories Owned");
			System.out.println(players.get(i).getTerritoriesOwnedWithArmies());
		}
		System.out.println("\nRound robin random reinforcement\n");
		Random rand = new Random();
		while(armiesToBeplaced)
		{
			for(int i =0; i<players.size(); i++)
			{
				try
				{
					int index = rand.nextInt(players.get(i).nbTerritoriesOwned());
					this.reinforce(players.get(i).getName(), players.get(i).getTerritoriesOwned().get(index));
				}
				catch(ModelException e)
				{
					
				}
			}
			armiesToBeplaced = false;
			for(int i =0; i< players.size(); i++)
			{
				if(players.get(i).getNbArmiesToBePlaced() > 0)
				{
					armiesToBeplaced = true;
					break;
				}
			}
		}
		for(int i =0; i< players.size(); i++)
		{
			System.out.println(players.get(i).getName());
			System.out.println("Territories Owned");
			System.out.println(players.get(i).getTerritoriesOwnedWithArmies());
		}
		
		System.out.println("Done");
		
		sc.close();
	}
	
	/**
	 *  Set the number of players
	 * @param numberPl Number of players
	 */
	public void setNumberofPlayers(short numberPl)
	{
		players =  new ArrayList<ICharacter>();
		this.numberOfPlayers = numberPl;
	}
	
	/**
	 * 
	 * @return the number of players
	 */
	public short getNumberOfPlayers()
	{
		return this.numberOfPlayers;
	}
	/**
	 * Generates computer player
	 */
	public void createBots()
	{
		players.clear();
		for(short i = 1; i< this.numberOfPlayers; i++)
		{			
			players.add(new PlayerModel("Computer "+ i, RiskColor.values()[i], (short)(i+1)));
		}
	}
	
	/**
	 * Ad user/human player
	 * @param name The players name
	 */
	public void addHumanPlayer(String name)
	{
		players.add(new PlayerModel(name, RiskColor.values()[0], (short)(1)));
	}
	
	/**
	 * Give the armies according to the risk rules
	 * @return the number of armies to be set.
	 */
	public int setArmiesforPlayers()
	{
		int nbArmiesToBePlaced = 0;
		
		switch(players.size())
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
			players.get(i).setNbArmiesToBePlaced(nbArmiesToBePlaced);
		}
		return nbArmiesToBePlaced;
	}
	
	/**
	 * Randomly assign territories according to the risk rules
	 */
	public void randomAssignTerritories()
	{
		ArrayList<String> countries = RiskBoard.Instance.getTerritories();
		Collections.shuffle(countries);
		
		ArrayList<ICharacter> pl  = new ArrayList<ICharacter>();
		for(int i =0; i< players.size(); i++)
		{
			pl.add(players.get(i));
		}
		
		Collections.shuffle(pl);
		for(int i=0; i< countries.size(); i++)
		{
			int player = i%pl.size();			
			pl.get(player).addTerritory(countries.get(i));
			pl.get(player).decrementArmies();
			RiskBoard.Instance.getTerritory(countries.get(i)).setOwnerID(pl.get(player).getTurnID());
			RiskBoard.Instance.getTerritory(countries.get(i)).setArmyOn(1);
		}
		
		
		
		
	}
	
	/** 
	 * @param playername The player name
	 * @param territory1 The origin territory
	 * @param territory2 The destination territory
	 * @param armies The number of armies to be moved  
	 */
	public void fortify(String playername, String territory1, String territory2, int armies) throws ModelException
	{
		for(int i =0; i < players.size(); i++)
		{
			if(players.get(i).getName().equalsIgnoreCase(playername))
			{
				players.get(i).fortify( territory1, territory2,  armies);
				break;
			}
		}
	}
	
	
	/**
	 * 
	 * @param playername The player name
	 * @param territory the territory to reinforce
	 * @throws ModelException one or more of the parameter given is invalid
	 */
	public void reinforce(String playername, String territory) throws ModelException
	{
		
		for(int i =0; i < players.size(); i++)
		{
			if(players.get(i).getName().equalsIgnoreCase(playername))
			{
				players.get(i).reinforce( territory);
				break;
			}
		}
	}
	
	/**
	 *  Update the OwnerID if necessary
	 */
	public void checkOwnerStatus()
	{
		RiskBoard.Instance.checkOwnerStatus();
		for(String continent: RiskBoard.Instance.getContinents() )
		{
			for(int i =0; i < players.size(); i++)
			{
				if(players.get(i).getTurnID()==(short)RiskBoard.Instance.getContinent(continent).getOwnerID())
				{
					players.get(i).incrementArmiesBy(RiskBoard.Instance.getContinent(continent).getContinentBonus());
				}
			}
			
		}
	}
	/**
	 * Creates a Map from scratch with user input
	 */
	public void createMap() 
	{
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
			if(str.length()== 0 || !RiskBoard.Instance.getContinents().contains(str))
			{
				while(bonus <= 0)
				{
					System.out.print("Please enter a bonus for continent "+str+": ");
					bonus = sc.nextInt();
				}
				sc.nextLine();
				RiskBoard.Instance.addContinent(str, bonus);
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
		System.out.println(RiskBoard.Instance.continentsToString());
		
		int nbCountries = 0;
		for(int i =0; i<nbContinent;i++)
		{
			System.out.println(RiskBoard.Instance.getContinents().get(i));
			
			while(nbCountries <= 0)
			{
				System.out.print("Please Enter the number of territories for " +RiskBoard.Instance.getContinents().get(i)+": ");
				
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
					System.out.print("Please enter the number of neighbours for territory: "+country+": ");
					nbNeighbours = sc.nextInt();
					
				}
				sc.nextLine();
				str = "";
				
				RiskBoard.Instance.addTerritory(RiskBoard.Instance.getContinents().get(i), country);
				for(int k =0; k <nbNeighbours; k++)
				{
					while(str.length()==0)
					{
						System.out.print("Please enter the neighbour name for territory: "+country+": ");
						str = sc.nextLine().toLowerCase().trim();
						
						
					}
					RiskBoard.Instance.getTerritory(country).addNeighbours(str);
					str= "";
				}
				
			}
		}
		 System.out.println(RiskBoard.Instance.toString());
		 boolean valid = RiskBoard.Instance.validateMap();
		 if(valid)
		 {
			 str = "";
			 while(str.length() ==0)
			 {
				 System.out.print("The map is Valid please enter a name to save it: ");
				 str = sc.nextLine().trim();
			 }
			 try {
				Utilities.saveMap(str);
				System.out.println("Done");
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
	 * Edit an existing Map
	 */

	public void editMap() 
	{
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
		boolean valid = RiskBoard.Instance.validateMap();
		 if(valid)
		 {
			 str = "";
			 while(str.length() ==0)
			 {
				 System.out.print("The map is Valid please enter a name to save it: ");
				 str = sc.nextLine().trim();
			 }
			 try {
				Utilities.saveMap(str);
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
			if(!RiskBoard.Instance.containsContinent(continent1))
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
				if(!RiskBoard.Instance.containsContinent(continent2))
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
					
					if(!RiskBoard.Instance.getContinent(continent1).getTerritories().contains(ter1))
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
					
					if(!RiskBoard.Instance.getContinent(continent2).getTerritories().contains(ter2))
					{
						System.out.println("This territory does not belong to the map");
						ter2 = "";
					}
				}
				RiskBoard.Instance.getContinent(continent1).getTerritory(ter1).removeNeighbours(ter2);
				RiskBoard.Instance.getContinent(continent2).getTerritory(ter2).removeNeighbours(ter1);
				
				
			}
		}
		
		
	}

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
			if(!RiskBoard.Instance.containsContinent(continent1))
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
				if(!RiskBoard.Instance.containsContinent(continent2))
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
					
					if(!RiskBoard.Instance.getContinent(continent1).getTerritories().contains(ter1))
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
					
					if(!RiskBoard.Instance.getContinent(continent2).getTerritories().contains(ter2))
					{
						System.out.println("This territory does not belong to the map");
						ter2 = "";
					}
				}
				RiskBoard.Instance.getContinent(continent1).getTerritory(ter1).addNeighbours(ter2);
				RiskBoard.Instance.getContinent(continent2).getTerritory(ter2).addNeighbours(ter1);
				
				
			}
		}
		
	}

	private void removeTerritory(Scanner sc) {
		String continent ="";
		
		while(continent.length() == 0 )
		{
			System.out.print("Please enter the name of the continent to which the country need to be removed belong: " );
			continent=sc.nextLine();
			continent= continent.toLowerCase().trim();
			if(!RiskBoard.Instance.containsContinent(continent))
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
			
			RiskBoard.Instance.removeTerritory(continent, country);
		}
		
	}

	private void addTerritory(Scanner sc) 
	{
		String continent ="";
		
		while(continent.length() == 0 )
		{
			System.out.print("Please enter the name of the continent to which the country need to be added: " );
			continent=sc.nextLine();
			continent= continent.toLowerCase().trim();
			if(!RiskBoard.Instance.containsContinent(continent))
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
			
			RiskBoard.Instance.addTerritory(continent, country);
		}
		
	}

	private void removeContinent(Scanner sc) {
		String str ="";

		while(str.length() == 0)
		{
			System.out.print("Please enter the name of the continent to be removed: " );
			str=sc.nextLine();
		}
		RiskBoard.Instance.removeContinent(str);
	}

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
		RiskBoard.Instance.addContinent(str, bonus);
	}

		
}
