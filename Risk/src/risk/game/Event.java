package risk.game;

import java.io.Serializable;

public class Event implements Serializable
{

	/**
	 * Generated SerialVersion UID
	 */
	private static final long serialVersionUID = 6215663455811987250L;



	public static enum EVENT
	{
		Reinforce, Fortify, Attack
	}
	
	public Event(EVENT pEvent)
	{
		
	}
}