/**
 *  Package contains the various static , final and constants for the game
 *  */
package risk.utils.constants;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Class that contains the other constants
 * @author hcanta
 */
public class OtherConstants 
{
	/**
	 * A label that contains the picture background
	 */
	public final static JLabel backGround = new JLabel(new ImageIcon(
			((new ImageIcon("img/g1.png").getImage().getScaledInstance(RiskIntegers.GAME_WIDTH - RiskIntegers.GAME_OFFSET,
					 RiskIntegers.GAME_HEIGHT - RiskIntegers.GAME_OFFSET -30, java.awt.Image.SCALE_SMOOTH)))));
	
	/**
	 * A label that contains the picture artillery
	 */
	public final static JLabel artillery = new JLabel(new ImageIcon(
			((new ImageIcon("img/Artillery.png").getImage().getScaledInstance(RiskIntegers.ICON_WIDTH , RiskIntegers.ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
	
	/**
	 * A label that contains the picture cavalry
	 */
	public final static JLabel cavalry = new JLabel(new ImageIcon(
			((new ImageIcon("img/Cavalry.png").getImage().getScaledInstance(RiskIntegers.ICON_WIDTH , RiskIntegers.ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));
	
	/**
	 * A label that contains the picture infantry
	 */
	public final static JLabel infantry = new JLabel(new ImageIcon(
			((new ImageIcon("img/Infantry.png").getImage().getScaledInstance(RiskIntegers.ICON_WIDTH , RiskIntegers.ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH)))));

}
