/**
 * Package contains the various static , final and constants for the game
 */
package risk.utils.constants;

import java.io.Serializable;

/**
 * This class contains all the constants final strings
 * @author hcanta
 *
 */
public class RiskStrings implements Serializable
{
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -1938371827358414600L;
	/**
	 * Risk String
	 */
	public static final String RISK = "Risk";
	/**
	 * Continent and Bonus
	 */
	public static final String CREATE_CONTINENT_BONUS = "Naming the continents and set their bonus";
	/**
	 * Create Continent String
	 */
	public static final String CREATE_CONTINENT  = "Creating the continents";
	/**
	 * Create Territories String
	 */
	public static final String CREATE_TERRITORIES  = "Creating the territories";
	/**
	 * File Menu string label content
	 */
	public static final String MENU_FILE = "File";
	
	/**
	 * Create Map string label content
	 */
	public static final String MENU_ITEM_CREATE_MAP = "Create a Map";
	/**
	 * Edit Map label content
	 */
	public static final String MENU_ITEM_EDIT_MAP = "Edit a Map";
	/**
	 * Open and play the game string label content
	 */
	public static final String MENU_ITEM_OPEN_MAP = "Load a Map and Play";
	/**
	 * Quit the game
	 */
	public static final String MENU_ITEM_EXIT = "Exit Game";
	
	/**
	 * Quitting the message closing
	 */
	public static final String MSG_CLOSING_GAME_APPLICATION = "Closing game application.";
	
	/**
	 * Map Editor label content
	 */
	public static final String TITLE_MAP_EDITOR = "Map Editor";
	
	/**
	 * Territory File Test
	 */
	public static final String TERRITORY_FILE_TEST = "SerTest\\Territory.ser";
	
	/**
	 * Continent  File Test
	 */
	public static final String CONTINENT_FILE_TEST = "SerTest\\Continent.ser";
	
	/**
	 * Player  File Test
	 */
	public static final String PLAYER_FILE_TEST = "SerTest\\Player.ser";
	
	/**
	 * Human Player  File Test
	 */
	public static final String HUMAN_PLAYER_FILE_TEST = "SerTest\\HumanPlayer.ser";
	
	/**
	 * Bot Player  File Test
	 */
	public static final String BOT_PLAYER_FILE_TEST = "SerTest\\BotPlayer.ser";
	
	/**
	 * Board  File Test
	 */
	public static final String BOARD_FILE_TEST = "SerTest\\Board.ser";
	
	/**
	 * Help String
	 */
	public static final String MENU_HELP = "Help";

	/**
	 * Toggle History String show
	 */
	public static final String TOGGLE_HISTORY_ON = "Show History";
	
	/**
	 * Toggle History String hide
	 */
	public static final String TOGGLE_HISTORY_OFF = "Hide History";
	
	/**
	 * String path to the reinforce on image
	 */
	public static final String REINFORCE_ON ="img/reinforce_on.png";
	
	/**
	 * String path to the reinforce off image
	 */
	public static final String REINFORCE_OFF ="img/reinforce_off.png";
	
	/**
	 * String path to the attack on image
	 */
	public static final String ATTACK_ON ="img/attack_on.png";
	
	/**
	 * String path to the attack off image
	 */
	public static final String ATTACK_OFF ="img/attack_off.png";
	
	/**
	 * String path to the fortify on image
	 */
	public static final String FORTIFY_ON ="img/fortify_on.png";
	
	/**
	 * String path to the fortify off image
	 */
	public static final String FORTIFY_OFF ="img/fortify_off.png";
	
	/**
	 * Title of the dialog box when we try to load a map
	 */
	public static final String CONQUEST_FILECHOOSER_DIALOG_TITLE = "Conquest Game .MAP File";

	/**
	 * Error message if an invalid file location was passed during loading of files
	 */
	public static final String INVALID_FILE_LOCATION = "ERROR: Unable to find .Map file in the system. \n Please paste a new .map file at the required location.";

	/**
	 * Toggle Country String show
	 */
	public static final String TOGGLE_COUNTRY_ON = "Show Country";
	
	/**
	 * Toggle Country String hide
	 */
	public static final String TOGGLE_COUNTRY_OFF = "Hide Country";

	/**
	 * Tournament String
	 */
	public static final String TOURNAMENT = "Tournament";
	
	/**
	 * Save Game string
	 */
	public static final String SAVE_GAME= "Save Game";
	
	/**
	 * Load Game String
	 */
	public static final String LOAD_GAME = "Load Game";
	
	/**
	 * Created Map Name
	 */
	public static final String CREATED_MAP = "Created Map";
	
	/**
	 * Create Map How Many Continent Continent 
	 */
	public static final String CREATE_MAP_CONTINENT_QUERY = "How many continents would you like to create? please chose a number >= 2";
	
	/**
	 * Ask for number input
	 */
	public static final String PLEASE_NUMBER ="Please enter a number";
	
	/**
	 * Continent String
	 */
	public static final String CONTINENT = " continent";
	
	/**
	 * Continent String
	 */
	public static final String CONTINENT_TERRITORY = "continent the territory belongs.";
	
	
	/**
	 * String that asks for a name
	 */
	public static final String PLEASE_NAME = "Please Enter the name of the ";
	
	/**
	 * String Ask Bonus for continent
	 */
	public static final String CONTINENT_BONUS = "Please enter a bonus for continent ";
	
	/**
	 * Invalid Continent
	 */
	public static final String INVALID_CONTINENT = "This continent name already exists or the name is invalid";
	
	/**
	 * Invalid Territory
	 */
	public static final String INVALID_TERRITORY = "This territory name already exists or the name is invalid";
	
	/**
	 * Continent Created
	 */
	public static final String CONTINENT_CREATED = "The folowing continents were created";
	
	/**
	 * Territory Query Number
	 */
	public static final String TERRITORY_NUMBER_QUERY = "Please Enter the number of territories for ";
	
	/**
	 * String Territory
	 */
	public static final String TERRITORY = " territory";
	
	/**
	 * String Territory Neighbor Query
	 */
	public static final String TERRITORY_NEIGHBOUR_NUMBER_QUERY = "Please enter the number of neighbours for the territory ";

	/**
	 * String Territory neighbor name query
	 */
	public static final String TERRITORY_NEIGHBOUR_NAME_QUERY = "Please enter the neighbour name for territory.";
	
	/**
	 * Done
	 */
	public static final String DONE = "Done";
	
	/**
	 * Invalid Map
	 */
	public static final String INVALID_MAP =" The Map is Invalid.";
	
	/**
	 * Valid Map
	 */
	public static final String VALID_MAP =" The Map is Valid.";
	
	/**
	 * Validating Map
	 */
	public static final String VALIDATING_MAP =" Validating Map.";
	
	/**
	 * Valid Map Save
	 */
	public static final String VALID_SAVE_MAP ="The map is Valid please enter a name to save it";
	
	/**
	 * Save Map
	 */
	public static final String SAVE_MAP =" Save Map.";
	
	/**
	 * Map was saved
	 */
	public static final String MAP_SAVED = " The Map was saved: ";
	
	/**
	 * Number of Player set
	 */
	public static final String NUMBER_PLAYER = "Please Set the Number of players between 2 and 6"; 
	
	/**
	 * Ask For player name
	 */
	public static final String PLAYER_NAME = "Please Enter your name";
	
	/**
	 * Random Assignment
	 */
	public static final String RANDOM_ASSIGNMENT = "Random Assignement of countries";
	
	/**
	 * Placing remaining armies
	 */
	public static final String PLACE_REM_ARMIES = "Placing Remaining armies";
	
	
	/**
	 * Strings for edit option
	 */
	public static final String[] EDIT_OPTIONS  ={"1-Add a Continent",
												"2-remove a Continent",
												"3-Add a territory",
												"4-Remove a territory",
												"5-Add a link between two existing territories",
												"6-remove a link between two existing territories",
												"7-Done"};
	
	/**
	 * To be removed string
	 */
	public static final String TO_BE_REMOVED= " to be removed.";
	
	/**
	 * first string
	 */
	public static final String FIRST = "first ";
	
	/**
	 * second string
	 */
	public static final String SECOND = "second ";
	
	/**
	 *  Attempt success
	 */
	public static final String  ATTEMPT_SUCCESFUL = "The attempt was successful";
	
	/**
	 *  Attempt failed
	 */
	public static final String  ATTEMPT_FAILED = "The attempt was a failure";
	
	/**
	 * attempt add continent
	 */
	public static final String ADD_CONTINENT_ATTEMPT = "Attempting to add a continent...";
	
	/**
	 * attempt add Territory
	 */
	public static final String ADD_TERRITORY_ATTEMPT = "Attempting to add a territory...";
	
	/**
	 * attempt add link
	 */
	public static final String ADD_LINK_ATTEMPT = "Attempting to add a link...";
	
	/**
	 * attempt remove continent
	 */
	public static final String REMOVE_CONTINENT_ATTEMPT = "Attempting to remove a continent...";
	
	/**
	 * attempt remove Territory
	 */
	public static final String REMOVE_TERRITORY_ATTEMPT = "Attempting to remove a territory...";
	
	/**
	 * attempt remove link
	 */
	public static final String REMOVE_LINK_ATTEMPT = "Attempting to remove a link...";
	
	/**
	 * Initiate Create Map
	 */
	public static final String INITIATE_CREATE_MAP = "Initiating Create Map Mode...";
	
	/**
	 * Initiate Edit Map
	 */
	public static final String INITIATE_EDIT_MAP = "Initiating Edit Map Mode...";
	/**
	 * Initiate Load and Play
	 */
	public static final String INITIATE_LOAD_PLAY = "Initiating Load and Play Mode...";
	
	/**
	 * The colors that can be assigned to a player 
	 */
	public static final String[] PLAYER_COLORS =
	{
		"red", "yellow", "gray", "green",  "pink", "blue"
	};
	
	/**
	 * Choose your color String
	 */
	public static final String CHOOSE_COLOR = "Choose your color";
	
	/**
	 * Fortification Options
	 */
	public static final String [] FORTIFY_OPTIONS = {"1-Attempt Fortification",
													 "2-Save Game",
													 "3-End fortification phase"};
	
	/**
	 * Attack Options
	 */
	public static final String [] ATTACK_OPTIONS = {"1-Attempt Attack",
													 "2-End attack phase"};
	
	/**
	 * Reinforce Options
	 */
	public static final String [] REINFORCE_OPTIONS = {"1-Attempt Reinforcement",
													 "2-End Reinforcement phase"};
	/**
	 * FortiFy String
	 */
	public static final String FORTIFY = "Fortify";
	
	/**
	 * Reinforce String
	 */
	public static final String REINFORCE = "Reinforce";
	
	/**
	 * Reinforcement Phase
	 */
	public static final String REINFORCE_PHASE = "Reinforcement Phase In Progress";
	
	/**
	 * Attack Phase
	 */
	public static final String ATTACK_PHASE = "Attack Phase In Progress";
	
	/**
	 * Attack 
	 */
	public static final String ATTACK = "Attack ";
	
	/**
	 * Fortify Phase
	 */
	public static final String FORTIFY_PHASE = "Fortification Phase In Progress";
	
	/**
	 * Attempt Reinforcement Phase
	 */
	public static final String ATTEMPT_REINFORCE = "Attempting Reinforcement ...";
	
	/**
	 * Attempt Attack Phase
	 */
	public static final String ATTEMPT_ATTACK = "Attempting Attack ...";
	
	/**
	 * Attempt Fortify Phase
	 */
	public static final String ATTEMPT_FORTIFY = "Attempting Fortify...";
	
	/**
	 * Origin Territory
	 */
	public static final String ORIGIN_TERRITORY = "Enter origin territory";
	
	/**
	 * Destination Territory
	 */
	public static final String DESTINATION_TERRITORY = "Enter Destination territory";
	
	/**
	 * Number Of Armies
	 */
	public static final String NUMBER_OF_ARMIES = " Enter The Number Of Armies";
	
	/**
	 * Risk System String
	 */
	public static final String RISK_SYSTEM = "Risk System ";
	/**
	 * Attempting to save
	 */
	public static final String ATTEMPT_SAVE_GAME = "Attempt Save Game ...";
	/**
	 * Attempting Load Game
	 */
	public static final String ATTEMPT_LOAD_GAME = "Attempt Load Game ... ";
	
	/**
	 * The card File test
	 */
	public static final String CARD_FILE_TEST = "SerTest\\Card.ser";
	
	/**
	 * The card File test
	 */
	public static final String HAND_FILE_TEST = "SerTest\\Hand.ser";
	/**
	 * hAND bOT Player Test
	 */
	public static final String BOT_HAND_PLAYER_FILE_TEST = "SerTest\\HandbotPlayer.ser";
	/**
	 * Attack territory
	 */
	public static final String ATTACK_TERRITORY = "Enter The Attacking Territory";
	/**
	 * Defender territory
	 */
	public static final String DEFENDER_TERRITORY = "Enter The Defending Territory";
	/**
	 * Dices 
	 */
	public static final  String NUMBER_OF_DICES = "Enter The number of dices to roll between 1 and 3";
}
