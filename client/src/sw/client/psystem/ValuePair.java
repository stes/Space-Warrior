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
package sw.client.psystem;

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
	
	public ValuePair(double x, double y)
	{
		_x = x;
		_y = y;
	}
	
	public ValuePair(Point p)
	{
		this(p.x, p.y);
	}
	
	public ValuePair(Point.Double p)
	{
		this(p.x, p.y);
	}
	
	public double getX()
	{
		return _x;
	}
	
	public void setX(double x)
	{
		_x = x;
	}
	
	public double getY()
	{
		return _y;
	}
	
	public void setY(double y)
	{
		_y = y;
	}
	
	public ValuePair add(ValuePair p)
	{
		return new ValuePair(getX() + p.getX(), getY() + p.getY());
	}
	
//	public ValuePair multiply(double scalar)
//	{
//		return this.multiply(scalar, scalar);
//	}

	public ValuePair multiply(double x, double y)
	{
		return new ValuePair(getX() * x, getY() * y);
	}
	
	public double getVectorLength()
	{
		return Math.sqrt(_x * _x + _y * _y);
	}
}
