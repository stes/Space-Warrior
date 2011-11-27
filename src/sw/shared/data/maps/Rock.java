package sw.shared.data.maps;

import java.awt.Point;
import java.awt.Polygon;

/**
 * Represents an rock which is not passable by the space ships
 * 
 * @author Steffen
 * @version 27.11.11
 */
public class Rock extends Obstacle
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4552089753699293931L;

	/**
	 * creates a new rock out of the specified points
	 * @param cornerLocations
	 */
	public Rock(Point[] cornerLocations)
	{
		super(cornerLocations);
	}
	
	/**
	 * creates a new rock out of the specified polygon
	 * @param polygon the polygon
	 */
	public Rock(Polygon polygon)
	{
		super(polygon);
	}

}
