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
