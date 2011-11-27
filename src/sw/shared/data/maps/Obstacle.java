package sw.shared.data.maps;

import java.awt.Point;
import java.awt.Polygon;

/**
 * Base class for all kinds of obstacles
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 */
public abstract class Obstacle extends Polygon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7248733131969739658L;
	
	/**
	 * creates a new obstacle out of the specified points
	 * @param cornerLocations the corners
	 */
	public Obstacle(Point[] cornerLocations)
	{
		super();
		for (Point p : cornerLocations)
			this.addPoint(p.x, p.y);
	}
	
	/**
	 * creates a new obstacle out of the specified polygon
	 * @param polygon the polygon
	 */
	public Obstacle(Polygon polygon)
	{
		super(polygon.xpoints, polygon.ypoints, polygon.npoints);
	}
}
