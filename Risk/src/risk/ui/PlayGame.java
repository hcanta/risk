package risk.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class PlayGame {

	public PlayGame() 
	{
	    JPanel _west = new JPanel();
	    JPanel _center = new JPanel();
	    JPanel _east = new JPanel();
	
	    _west.setLayout(new GridLayout(4, 1));
	    _center.setLayout(new GridLayout(4, 1));
	    _east.setLayout(new GridLayout(4, 2));
	}

}
