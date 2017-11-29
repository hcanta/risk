/**
 *  Package contains the various UI components of the risk game
 */
package risk.views.ui;

import java.awt.GridLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import risk.model.RiskBoard;
import risk.utils.constants.RiskIntegers;

/**
 * Implementation Of the Risk Graph
 * @author hcanta
 *
 */
public class RiskGraph extends JPanel implements Serializable, Observer
{

	/**
	 * A list of all the vertices
	 */
	private ArrayList<Vertex>vertices;

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -4219845485977672561L;

	/**
	 * The display Graph
	 * @param board The RiskBoard
	 */
	public RiskGraph(RiskBoard board) 
	{
		super(new GridLayout((int) Math.round(board.getNbTerritories()/ RiskIntegers.CELL_PER_ROWS), RiskIntegers.CELL_PER_ROWS, RiskIntegers.GRAPH_CELL_X_OFFSET, RiskIntegers.GRAPH_CELL_Y_OFFSET));
		this.setVisible(true);
		EmptyBorder panelBorder = new EmptyBorder(RiskIntegers.GRAPH_CELL_Y_OFFSET, 10, 10, 10);
		this.setBorder(panelBorder);
		vertices = new ArrayList<Vertex>();
		
		for(int i =0; i< board.getNbTerritories(); i++)
		{
			addVertex(board.getTerritory(board.getTerritories().get(i)).getVertex());
		}
	}
	
	/**
	 * Adds a vertex to the graph
	 * @param v vertex tp be added
	 */
	private void  addVertex( Vertex v)
	{
		vertices.add(v);
		this.add(v);
	}

	/**
	 * Updates The graph
	 * @param arg0 The riskBoard
	 * @param arg1 Some parameter for the update
	 */
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		for(int i =0; i< vertices.size(); i++)
		{
			vertices.get(i).update();
		}
		this.validate();
		this.setVisible(true);
		this.repaint();
		
	}
	

}
