/**
 *  Package contains the various UI components of the risk game
 */
package risk.views.ui;

import java.io.Serializable;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import risk.utils.constants.RiskIntegers;

/**
 * This Class Creates A Frame Object That will be used to display the graph
 * @author hcanta
 */
public class GraphDisplayPanel extends JFrame implements Serializable
{

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -5292025599718626833L;

	/**
	 * The mxGraphComponent of our graph
	 */
	private mxGraphComponent graphComponent;
	
	/**
	 *  Constructor for the Display
	 * @param graph  The graph that will be displayed
	 */
	public GraphDisplayPanel(mxGraph graph) {
		graphComponent = new mxGraphComponent(graph);
		graph.getModel().endUpdate();
		graphComponent.refresh();
		setSize(RiskIntegers.GAME_WIDTH - RiskIntegers.GAME_OFFSET,
				 RiskIntegers.GAME_HEIGHT - RiskIntegers.GAME_OFFSET -30);
		getContentPane().add(graphComponent);
	}

}
