package risk.ui;

import java.awt.BorderLayout;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.apache.log4j.Logger;

import risk.model.character.PlayerModel;

/**
 * @author Ayushi Jain
 *
 */

public class Game extends Canvas implements Runnable {

	
	private static final long serialVersionUID = -228084634482818388L;

	// Added logging function
		final static Logger logger = Logger.getLogger(Game.class);
		private static Game instance = new Game();
		risk.util.map.editor.MiscellaneousHelper helper = new risk.util.map.editor.MiscellaneousHelper();
		
		private int width;
		private int height;
		private String title;
		private static String LOG_CURRENT_SESSION_TAG = "";
		public static final int WINDOW_WIDTH = 840;
		public static final int WINDOW_HEIGHT = (int) WINDOW_WIDTH / 12 * 9;
		private static final String TITLE_GAME_WINDOW = "======== Risk ========";
		
		public static PlayerModel PLAYERMODEL;
		
		private static JMenuBarComponent jMenuBarComponent;
		private JMenuBar gameJMenuBar;
		private JFrame frame;
		
		// THREAD
		private Thread thread;
		
		
		/**
		 * Constructor of the Game Class
		 */
		private Game() 
		{
			setLog_Current_Session_Tag(helper.getCurrentDateStr());
			
			width = WINDOW_WIDTH;
			height = WINDOW_HEIGHT;
			PLAYERMODEL = new PlayerModel();
			title = getTitleGameWindow();
			
			frame = new JFrame();
			frame.setTitle(title);
			frame.setPreferredSize(new Dimension(width, height));
			frame.setMaximumSize(new Dimension(width, height));
			frame.setMinimumSize(new Dimension(width, height));
			frame.setResizable(false);
			frame.setLocationRelativeTo(null); // center window on the screen
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().removeAll();
			frame.setLayout(new BorderLayout());
			jMenuBarComponent = new JMenuBarComponent();
			gameJMenuBar = jMenuBarComponent.getGameJMenuBar(frame);
			frame.setJMenuBar(gameJMenuBar);
			frame.setVisible(true);
			logger.info("Game Loaded!!");
		}
	
	
	
	
	
		
		
		/**
		 * This method starts the thread
		 */
		public synchronized void start() {
			thread = new Thread(this);
			thread.start();
		}

		/**
		 * This method stops the thread
		 */
		public synchronized void stop() {
			try {
				thread.join(0); // stops the thread
				System.out.println("Game loop is stopped.");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("EXCEPTION HERE void stop function.");
			}
		}
		
		/**
		 * If instance was not previously created, then created one and return the
		 * instance.
		 * 
		 * @return game object - instance
		 */
		public static Game getInstance() {
			if (instance == null) {
				instance = new Game();
			}
			return instance;
		}
	
	
	
	@Override
	public void run() {

	}
	
	public static void setLog_Current_Session_Tag(String new_session) 
	{
		LOG_CURRENT_SESSION_TAG = "LOG_CURRENT_SESSION_TAG_" + new_session;
	}

	public static String getLog_Current_Session_Tag() 
	{
		System.out.println("Value of the Current session tag is: "+LOG_CURRENT_SESSION_TAG);
		return LOG_CURRENT_SESSION_TAG;
	}
	
	public static String getTitleGameWindow() {
		if (PLAYERMODEL.playerName.length() > 0)
			return TITLE_GAME_WINDOW + ". Player :(" + PLAYERMODEL.playerName + ")";
		else
			return TITLE_GAME_WINDOW;
	}
}
