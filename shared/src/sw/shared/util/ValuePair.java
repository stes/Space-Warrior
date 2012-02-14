/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
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
package sw.shared.util;

import java.awt.Point;

/**
 * Represents a pair of two double values
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12
 */
public class ValuePair
{
	private double _x;
	private double _y;

	/**
	 * Constructs a new ValuePair
	 * 
	 * @param x the first value
	 * @param y the second value
	 */
	public ValuePair(double x, double y)
	{
		_x = x;
		_y = y;
	}

	/**
	 * Constructs a new ValuePair using a point
	 * 
	 * @param p The point
	 */
	public ValuePair(Point p)
	{
		this(p.x, p.y);
	}

	/**
	 * Constructs a new ValuePair using a Point.Double
	 * 
	 * @param p The point
	 */
	public ValuePair(Point.Double p)
	{
		this(p.x, p.y);
	}

	/**
	 * Adds the values of the specified ValuePair to the current instance
	 * and returns the resulting ValuePair
	 * @param p The second value pair
	 * @return The value pair containing the single ValuePairs' values' sums
	 */
	public ValuePair add(ValuePair p)
	{
		return new ValuePair(this.getX() + p.getX(), this.getY() + p.getY());
	}

	/**
	 * Returns the length of the vector this ValuePair represents
	 * @return The vector length
	 */
	public double getVectorLength()
	{
		return Math.sqrt(_x * _x + _y * _y);
	}

	/**
	 * @return the first value
	 */
	public double getX()
	{
		return _x;
	}

	/**
	 * @return the second value
	 */
	public double getY()
	{
		return _y;
	}

	/**
	 * 
	 * @param scalar the scalar the values should be multiplied with
	 * @return a value pair whose values are products of the current instance one's and the given scalar value
	 */
	public ValuePair multiply(double scalar)
	{
		return this.multiply(scalar, scalar);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return a value pair whose values are products of the current instance one's and the given instance one's
	 */
	public ValuePair multiply(double x, double y)
	{
		return new ValuePair(this.getX() * x, this.getY() * y);
	}

	/**
	 * Sets the first value
	 * @param x the new value
	 */
	public void setX(double x)
	{
		_x = x;
	}

	/**
	 * Sets the second value
	 * @param y the second value
	 */
	public void setY(double y)
	{
		_y = y;
	}
}
