/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
