/**
 * The model package holds the board and the players
 */
package risk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import risk.model.maputils.Continent;
import risk.model.maputils.Territory;
import risk.utils.Utils;
import risk.utils.constants.RiskEnum.GameState;
import risk.utils.constants.RiskEnum.RiskEvent;
import risk.utils.constants.RiskIntegers;

/**
 * This class implements the RiskBoard it contains all the elements of the board. continents and territories
 * @author hcanta
 * @version 4.1
 */
public class RiskBoard extends Observable implements Serializable
{
	/**
	 * The number of rounds played;
	 */
	private int nbRoundsPlayed;
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -9127407334280473749L;
	/**
	 * The current player turn
	 */
	private String currentPlayer;
	/**
	 * The state of the game
	 */
	private GameState state;

	/**
	 * Number of times cards were exchange
	 */
	private int cardExchangeCount;
	
	/**
	 *  The Log of Information
	 */
	public ArrayList<String> log;
	
	/**
	 * The information on the continents
	 */
	private StringBuffer territoryInfo;

	/**
	 * The Owner of the Board
	 */
	private int ownerID;
	
	/**
	 * The name of the current Board/map
	 */
	private String boardName;
	/**
	 * HashMap Containing a mapping of name to continent object
	 */
	private HashMap<String, Continent> continents;
	
	/**
	 * Singleton Of the RisBoard Object
	 */
	public static RiskBoard Instance = new RiskBoard();


	/**
	 * Constructor of the Risk Board
	 */
	public RiskBoard() 
	{
		ownerID = RiskIntegers.INITIAL_OWNER;
		continents = new HashMap<String, Continent>();
		this.cardExchangeCount = 0;
		setState(GameState.IDLE);
		log = new ArrayList<String>();
		territoryInfo = new StringBuffer();
		nbRoundsPlayed = 0;
	}
	


	/**
	 * Returns the Owner ID Of the Board
	 * @return the ownerID
	 */
	public int getOwnerID() 
	{
		checkOwnerStatus();
		return ownerID;
	}


	/**
	 * Set The OwnerID of the bOARD
	 * @param ownerID the ownerID to set
	 */
	public void setOwnerID(int ownerID) 
	{
		this.ownerID = ownerID;
	}

	
	/**
	 * Verifies if the Continent exists on the board
	 * @param name country or continent the query is about
	 * @return True/False
	 */
	public boolean containsContinent(String name)
	{
		String n_name = name.toLowerCase();
		return continents.containsKey(n_name);
	}
	
	
	/**
	 * Returns The continent Object associated with the name if it exists null otherwise
	 * @param name  Continent searched
	 * @return The continent with the name passed
	 */
	public Continent getContinent(String name)
	{
		String n_name = name.toLowerCase();
		if(continents.containsKey(n_name))
		{
			return continents.get(n_name);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns a copy of the list of continents belonging to the board
	 * @return A list of name of all the continents.
	 */
	public ArrayList<String> getContinents()
	{
		return new ArrayList<String>(this.continents.keySet());
	}
	
	/**
	 * Add a continent to the board based if it doesn t already exist
	 * @param name e name of the continent to be added to the board
	 * @param bonus Bonus of army associated with the continent
	 */
	public void addContinent(String name, int bonus)
	{
		String n_name = name.toLowerCase();
		if(!continents.containsKey(n_name))
		{
			continents.put(n_name, new Continent(n_name,bonus));
		}
	}
	
	
	/**
	 * Returns the name of the board
	 * @return the boardName
	 */
	public String getBoardName() 
	{
		return boardName;
	}


	/**
	 * Set the name of the board
	 * @param boardName the boardName to set
	 */
	public void setBoardName(String boardName) 
	{
		this.boardName = boardName;
	}
	
	/**
	 * Empties the list of territories belonging to that 
	 */
	public void clear()
	{
		this.ownerID = RiskIntegers.INITIAL_OWNER;
		for(String value: continents.keySet())
		{
			try
			{
				continents.get(value).clear();
			}
			catch(Exception e) {}
		}
		continents.clear();
		this.cardExchangeCount = 0;
		setState(GameState.IDLE);
		log.clear();
		territoryInfo.setLength(0);
		nbRoundsPlayed = 0;
	}
	
	/**
	 * Removes the mentioned continent if it exists
	 * @param continent to be removed from the map
	 * @return Was the continent removed or not
	 */
	public boolean removeContinent(String continent)
	{
		String n_continent = continent.toLowerCase();
		if(this.continents.containsKey(n_continent))
		{
			continents.get(n_continent).clear();
			this.continents.remove(n_continent);
			return true;
		}
		return false;
		
	}
	
	/**
	 * Creates a string representation of the Board
	 * @return a string representation of the Board
	 */
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append("\nBoard: " + this.boardName+ "\n ");
		for(String continent : this.continents.keySet())
		{
			str.append(this.continents.get(continent).toString());
		}
		str.append("\n");
		
		return str.toString();
	}
	
	/**
	 * Creates a string representation of the Continents and its territories to be saved in a .map file
	 * @return a string representation of the continents to be saved in a .map file
	 */
	public String continentsToString()
	{
		StringBuffer str = new StringBuffer();
		for(String continent : this.continents.keySet())
		{
			str.append(this.continents.get(continent).getContinentName()+"="+this.continents.get(continent).getContinentBonus()+"\n");
		}
		return str.toString();
	}
	/**
	 * Returns a string representation of the territories to be saved in a .map file
	 * @return  a string representation of the territories to be saved in a .map file
	 */
	public String territoriesToString() {
		
		StringBuffer str = new StringBuffer();
		for(String continent : this.continents.keySet())
		{
			str.append(this.continents.get(continent).territoriesToString());
		}
		str.append("\n");
		return str.toString();
	}
	
	/**
	 * Add a territory to the Board
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @return was the territory added
	 */
	public boolean addTerritory(String continentName, String territoryName)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			return continents.get(c_name).addTerritory(t_name);
		}
		return false;
	}
	
	/**
	 * Add a territory to the Board
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @param xcoord The x coordinate of the territory
	 * @param ycoord the y coordinate of the territory
	 */
	public void addTerritory(String continentName, String territoryName,int xcoord, int ycoord)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			continents.get(c_name).addTerritory(t_name, xcoord, ycoord);
		}
	}
	
	/**
	 * Add a territory to the board
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @param neighbours the list of neighbors of said territory
	 */
	public void addTerritory(String continentName, String territoryName, String[] neighbours)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			continents.get(c_name).addTerritory(t_name, neighbours);
		}
	}
	
	/**
	 * Add a territory to the board
	 * @param continentName the name of the continent to which we need to add a territory
	 * @param territoryName the name of the territory to be added to said continent
	 * @param neighbours the list of neighbors of said territory
	 * @param xcoord The x coordinate of the territory
	 * @param ycoord the y coordinate of the territory 
	 */
	public void addTerritory(String continentName, String territoryName, String[] neighbours,int xcoord, int ycoord)
	{
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			continents.get(c_name).addTerritory(t_name, neighbours, xcoord, ycoord);
		}
	}
	
	/**
	 * Returns a List containing the territories present on the board
	 * @return all the territories name on the board
	 */
	public ArrayList<String>getTerritories()
	{
		ArrayList<String> countries = new ArrayList<String>();
		for(String continent : this.continents.keySet())
		{
			countries.addAll(this.continents.get(continent).getTerritories());
		}
		
		return countries;
		
	}

	/**
	 * Returns a territory object that matches the string passed. null if it can t be found
	 * @param territory we re searching for
	 * @return the territory
	 */
	public Territory getTerritory(String territory) 
	{
		for(String continent : this.continents.keySet())
		{
			if(continents.get(continent).getTerritories().contains(territory))
			{
				return continents.get(continent).getTerritory(territory);
			}
		}
		return null;
	}
	

	/**
	 * This method returns the number of territories
	 * @return the number of Territories
	 */
	public int getNbTerritories() 
	{
		return getTerritories().size();
	}
	
	/**
	 * This function is used to notify the observers
	 * @param event The event that triggered the notification to the observers
	 */
	public void update(RiskEvent event)
	{
		this.setChanged();
		
		this.notifyObservers(event);
	}
	


	/**
	 * Returns the current game state
	 * @return the state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Set The Current Game State
	 * @param state the current game state to set
	 */
	public void setState(GameState state) {
		this.state = state;
		update(RiskEvent.StateChange);
	}
	/**
	 * Check if the current Game Map is Valid or not
	 * @return True/false is the GameMap Valid or not
	 */
	public boolean validateMap() 
	{
		boolean valid = true;
		if(!(this.continents.keySet().size()>=1))
			return false;
		if((this.getTerritories().size())< 2)
			return false;
		ArrayList<String> n_continents = new ArrayList<String>();
		for(int i =0; i< this.continents.keySet().size(); i++)
		{
			if(n_continents.contains((String)this.continents.keySet().toArray()[i]))
			{
				return false;
			}
			else
			{
				n_continents.add((String)this.continents.keySet().toArray()[i]);
			}
		}
		n_continents.clear();
		for(String continent : this.continents.keySet())
		{
			valid = this.continents.get(continent).validateContinent();
			if(!valid)
			{
				
				return false;
			}
		}
		boolean result  = valid && validateMapHelper();
		if(result)
			update(RiskEvent.GeneralUpdate);
		return result;
	}
	
	/**
	 * This is a helper function to see if we re dealing with a connected Graph or not
	 * @return True/false is the GameMap Valid or not
	 */
	private boolean validateMapHelper()
	{
		ArrayList<String> territories = this.getTerritories();
		int[][] adjMatrix = new int[territories.size()][territories.size()];
		
		for(int i = 0; i < territories.size(); i ++)
		{
			for(int j =0; j< territories.size(); j++)
			{
				adjMatrix[i][j]= this.getTerritory(territories.get(i)).getNeighbours().contains(territories.get(j)) ? 1 : 0;				
			}
		}
		
		return Utils.performTraversal(adjMatrix) == territories.size();
	}
	
	/**
	 * Given a continent and a territory removes said territory
	 * @param continentName The continent to which the territory belongs
	 * @param territoryName the territory to be removed
	 * @return was the territory removed or not
	 */
	public boolean removeTerritory(String continentName, String territoryName) {
		String c_name = continentName.toLowerCase();
		String t_name = territoryName.toLowerCase();
		if(continents.containsKey(c_name))
		{
			return continents.get(c_name).removeTerritory(t_name);
		}
		return false;
		
	}
	
	/**
	 *  Update the OwnerID if necessary
	 */
	private void checkOwnerStatus()
	{
		int id = continents.get(continents.keySet().toArray()[0]).getOwnerID();
		boolean update = true;
		for(String value: continents.keySet())
		{
	
			if (continents.get(value).getOwnerID()!= id)
			{
				update = false;
				break;
			}
		}
		
		if(update)
		{
			this.ownerID = id;
		}
	}

	/**
	 * Checks if the Game is Done Or not
	 * @return true/false is the game over or not
	 */
	public boolean isGameOver() {
		checkOwnerStatus();
		return this.ownerID != RiskIntegers.INITIAL_OWNER;
	}

	/**
	 * Get the current player name whose turn it is
	 * @return the currentPlayer
	 */
	public String getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Set the current Player name
	 * @param currentPlayer the currentPlayer name to set
	 */
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	/**
	 * Adds a continent to the board
	 * @param cont the continent to be added
	 */
	public void addContinent (Continent cont) 
	{
		String n_name = cont.getContinentName().toLowerCase();
		if(!continents.containsKey(n_name))
		{
			continents.put(n_name, cont);
		}		
	}



	/**
	 * Returns the amount of times the cards were exchanged
	 * @return the cardExchangeCount
	 */
	public int getCardExchangeCount() {
		return cardExchangeCount;
	}



	/**
	 * Increments the card Count Exchange by 1
	 */
	public void incrementExchangeCardCount() {
		this.cardExchangeCount++;
	}


	/**
	 * Adds a message to the history panel
	 * @param message The message to be added
	 */
	public void addMessage(String message) {
		this.log.add(message);
		
	}
	
	/**
	 * Returns a copy of the Log
	 * @return a copy of the log
	 */
	public ArrayList<String> getHistory()
	{
		return new ArrayList<String>(log);
	}
	/**
	 * Returns the content of the pane
	 * @return the info displayed
	 */
	public String getHistoryInfo() 
	{
		StringBuilder toPrint = new StringBuilder();
		for (String text : log)
		{
			toPrint.append(" "+text + "\n");
		}
		return (toPrint.toString());
	}



	/**
	 * Returns a string containing all the territory Info
	 * @return string containing all the territory Info
	 */
	public String getountryInfo() 
	{
		return this.territoryInfo.toString();
	}

	/**
	 * Returns the String Buffer that contains the territory
	 * @return the territory String buffer
	 */
	public StringBuffer getTerritoryInfo() 
	{
		
		return this.territoryInfo;
	}
	
	/**
	 * Resets the number of rounds played
	 */
	public void resetRoundPlayed()
	{
		this.nbRoundsPlayed = 0;
	}
	
	/**
	 * Returns the number of rounds played
	 * @return the number of rounds played
	 */
	public int getnbRoundsPlayed()
	{
		return this.nbRoundsPlayed;
	}
	
	/**
	 * Increments the number of rounds played
	 */
	public void incrementRoundsPlayed()
	{
		 this.nbRoundsPlayed ++;
	}
	
	/**
	 * Decrements the number of rounds played
	 */
	public void decrementRoundsPlayed()
	{
		 this.nbRoundsPlayed --;
	}
	
	/**
	 * Sets the amount of rounds that were played
	 * @param rounds The number of rounds that were played
	 */
	public void setNbRoundsPlayed(int rounds)
	{
		this.nbRoundsPlayed = rounds;
	}
	
	/**
	 * Resets the number of card exchanged
	 */
	public void resetCardExchangeCount()
	{
		this.cardExchangeCount = 0;
	}
	
	/**
	 * Returns the number of card exchanged
	 * @return The number of times cars were exchanged
	 */
	public int getnbCardExchanged()
	{
		return this.cardExchangeCount;
	}
	
	/**
	 * Increments the number of card exchanged
	 */
	public void incrementCardExchanged()
	{
		 this.cardExchangeCount ++;
	}
	
	/**
	 * Decrements the number of card exchanged
	 */
	public void decrementCardExchanged()
	{
		 this.cardExchangeCount --;
	}
	
	/**
	 * Sets the amount of cards that were exchanged
	 * @param count The number of cards that were exchanged
	 */
	public void setNbCardsExchanged(int count)
	{
		this.cardExchangeCount = count;
	}
}
