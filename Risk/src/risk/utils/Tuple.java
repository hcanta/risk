/**
 *  Package contains the various static, constant and helper method of the game and objects
 */
package risk.utils;
/**
 * Implementation of the Tuple class
 * @author hcanta
 */
public class Tuple <X, Y> 
{
	/**
	 * The first Element of the Tuple
	 */
	private final X x; 
	/**
	 * The second element of the Tuple
	 */
	private final Y y;
	
	/**
	 * Simple constructor for the tuple object
	 * @param x First element of the tuple
	 * @param y Second Element of the tuple
	 */
	public Tuple(X x, Y y) 
	{ 
	    this.x = x; 
	    this.y = y; 
	}

	/**
	 * Return the first element of the tuple
	 * @return the x the first element of the tuple
	 */
	public X getFirst() {
		return x;
	}

	/**
	 * Return the second element of the tuple
	 * @return the y the second element of  the tuple
	 */
	public Y getSecond() {
		return y;
	} 

}