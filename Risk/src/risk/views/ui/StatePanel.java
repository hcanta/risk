/**
* Package contains the views of the game
 */
package risk.views.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import risk.model.RiskBoard;
import risk.utils.constants.RiskIntegers;
import risk.utils.constants.RiskStrings;

/**
 *The State Panel  extends JPanel Implements Observer It is used to show the current state of the game 
 * @author hcanta
 *
 */
public class StatePanel extends JPanel implements Observer, Serializable
{

	/**
	 * Label that indicates in which state the game is currently in
	 */
	public JLabel stateLabel;
	
	/**
	 * Label that indicates if it s a reinforcement phase or not
	 */
	public JLabel reinforceLabel;
	
	/**
	 * Label that indicates if it s an attack phase or not
	 */
	public JLabel attackLabel;
	
	/**
	 * Label that indicates if it s a fortify phase or not
	 */
	public JLabel fortifyLabel;
	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = 4851515870927199272L;

	/**
	 * the bottom panel that will hold the reinforce/attack/fortify signals
	 */
	private JPanel bottomPanel;
	/**
	 * Constructor of the state Panel
	 */
	public StatePanel() 
	{
		super();
		
		this.setLayout(new GridLayout(2,1));
		stateLabel = new JLabel();

		bottomPanel = new JPanel();
		
		this.setPreferredSize(new Dimension(RiskIntegers.GAME_WIDTH, RiskIntegers.STATE_PANEL_HEIGHT));
		this.setVisible(true);
		stateLabel.setVisible(true);
	
		stateLabel.setText("Welcome To Risk!");
		
		
		stateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(stateLabel);
		bottomPanel.setLayout(new GridLayout(1,3));
		bottomPanel.setVisible(true);
		this.add(bottomPanel);
		turnOff();
		this.repaint();
		this.validate();
	}

	/**
	 * Updates the observer/view regarding the ongoing phase or state of the game
	 * This method is called whenever the observed object is changed. An application calls an Observable object's notifyObservers method to have all the object's observers notified of the change.
	 * @param arg0 The object Changed
	 * @param arg1 a parameter for possible observers
	 */
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		
		RiskBoard board = (RiskBoard) arg0;
		
		switch(board.getState())
		{
			case REINFORCE:
				stateLabel.setText(board.getCurrentPlayer()+": "+RiskStrings.REINFORCE_PHASE);
				reinforceON();
				break;
			case ATTACK:
				stateLabel.setText(board.getCurrentPlayer()+": "+RiskStrings.ATTACK_PHASE);
				attackON();
				break;
			case FORTIFY:
				stateLabel.setText(board.getCurrentPlayer()+": "+RiskStrings.FORTIFY_PHASE);
				fortifyON();
				break;
			case IDLE:
				stateLabel.setText("Welcome To Risk!");
				turnOff();
				break;
			case STARTUP:
				stateLabel.setText("Start Up Phase In Progress");
				turnOff();
				break;
			default:
				stateLabel.setText("Welcome To Risk!");
				turnOff();
				break;
		}
		
	}
	
	/**
	 * Turns on the reinforce label, turns off the other ones
	 */
	private void reinforceON()
	{
		if(reinforceLabel!=null)
			bottomPanel.remove(reinforceLabel);
		if(attackLabel!=null)
			bottomPanel.remove(attackLabel);
		if(fortifyLabel!=null)
			bottomPanel.remove(fortifyLabel);
		
		reinforceLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.REINFORCE_ON).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		fortifyLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.FORTIFY_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		attackLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.ATTACK_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		
		addPlayerStateLabel();
	}
	
	/**
	 * Turns on the attack label, turns off the other ones
	 */
	private void attackON()
	{
		if(reinforceLabel!=null)
			bottomPanel.remove(reinforceLabel);
		if(attackLabel!=null)
			bottomPanel.remove(attackLabel);
		if(fortifyLabel!=null)
			bottomPanel.remove(fortifyLabel);
		
		reinforceLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.REINFORCE_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		fortifyLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.FORTIFY_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		attackLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.ATTACK_ON).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		
		addPlayerStateLabel();
	}
	
	/**
	 *  Turns on the fortify label, turns off the other ones
	 */
	private void fortifyON()
	{
		if(reinforceLabel!=null)
			bottomPanel.remove(reinforceLabel);
		if(attackLabel!=null)
			bottomPanel.remove(attackLabel);
		if(fortifyLabel!=null)
			bottomPanel.remove(fortifyLabel);
		
		reinforceLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.REINFORCE_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		fortifyLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.FORTIFY_ON).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		attackLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.ATTACK_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		
		addPlayerStateLabel();
	}
	
	/**
	 * Turn all labels off
	 */
	private void turnOff()
	{
		if(reinforceLabel!=null)
			bottomPanel.remove(reinforceLabel);
		if(attackLabel!=null)
			bottomPanel.remove(attackLabel);
		if(fortifyLabel!=null)
			bottomPanel.remove(fortifyLabel);
		
		reinforceLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.REINFORCE_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		fortifyLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.FORTIFY_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
		attackLabel =  new JLabel(new ImageIcon(
				((new ImageIcon(RiskStrings.ATTACK_OFF).getImage().getScaledInstance(RiskIntegers.PLAYER_PHASE_WIDTH, RiskIntegers.PLAYER_PHASE_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));

		 addPlayerStateLabel();
	}
	/**
	 * Adds or add back the player labels 
	 */
	private void addPlayerStateLabel()
	{
		reinforceLabel.setVisible(true);
		attackLabel.setVisible(true);
		fortifyLabel.setVisible(true);
		
		bottomPanel.add(reinforceLabel);
		bottomPanel.add(attackLabel);
		bottomPanel.add(fortifyLabel);
	}

}
