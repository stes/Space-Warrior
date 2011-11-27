package sw.shared.data.maps;

/**
 * Represents a specific map, including background image, size,
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.11
 */
public class Map
{
	private int _ID;
	
	public Map(int id)
	{
		_ID = id;
	}
	
	/**
	 * @return the map ID
	 */
	public int getID()
	{
		return _ID;
	}
}
