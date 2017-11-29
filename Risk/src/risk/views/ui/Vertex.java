/**
 * 
 */
package risk.views.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import risk.utils.Utils;
import risk.utils.constants.RiskEnum;
import risk.utils.constants.RiskIntegers;

/**
 * Implements the Vertex Class
 * @author hcanta
 *
 */
public class Vertex extends JLabel implements Serializable
{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -8237654963261127904L;

	/**
	 *  The ID of the Color of the OWner
	 */
	private Color color;
	
	/**
	 * Constructor of the Vertex
	 * @param name The name of the vertex
	 */
	public Vertex(String name) 
	{
		super(name);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(RiskIntegers.GRAPH_CELL_DIMENTION_X,RiskIntegers.GRAPH_CELL_DIMENTION_Y));
		EmptyBorder border = new EmptyBorder(RiskIntegers.GRAPH_CELL_Y_MARGIN, RiskIntegers.GRAPH_CELL_X_OFFSET, RiskIntegers.GRAPH_CELL_Y_MARGIN, RiskIntegers.GRAPH_CELL_X_OFFSET);
		LineBorder line = new LineBorder(Color.black, 2, true);
		CompoundBorder compound = new CompoundBorder(line, border);
		this.setBorder(compound);
		this.setHorizontalAlignment(CENTER);
		this.setOpaque(true);
	}
	
	/**
	 * Sets the color of the Vertex
	 * @param color The color of the owner of the vertex
	 */
	public void setOwner(RiskEnum.PlayerColors color)
	{
		this.color = Utils.getColor(color);
	}
	
	/**
	 * Update the vertex on the display
	 */
	public void update()
	{
		this.setBackground(this.color);
		
		this.setVisible(true);
		this.validate();
		this.repaint();
	}


}
