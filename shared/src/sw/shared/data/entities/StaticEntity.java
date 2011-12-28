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
package sw.shared.data.entities;

import java.awt.Point;

import sw.shared.GameConstants;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * Represents an entity with a position and a direction
 * 
 * @author Redix stes Abbadonn
 * @version 24.12.11
 */
public abstract class StaticEntity extends Entity
{
	public static final double MIN_X = GameConstants.PLAYER_SIZE / 2;
	public static final double MIN_Y = GameConstants.PLAYER_SIZE / 2;

	public static final double MAX_X = GameConstants.PLAYING_FIELD_WIDTH
			- GameConstants.PLAYER_SIZE / 2;
	public static final double MAX_Y = GameConstants.PLAYING_FIELD_HEIGHT
			- GameConstants.PLAYER_SIZE / 2;

	// state variables
	private double _x;
	private double _y;
	private double _direction;

	public StaticEntity(byte type)
	{
		super(type);
	}

	public StaticEntity(byte type, double x, double y, double direction)
	{
		this(type);
		this.setX(x);
		this.setY(y);
		this.setDirection(direction);
	}

	public double distanceTo(Point.Double p)
	{
		return this.getPosition().distance(p);
	}

	public double distanceTo(StaticEntity entity)
	{
		return this.getPosition().distance(entity.getPosition());
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		this.setX(p.readDouble());
		this.setY(p.readDouble());
		this.setDirection(p.readDouble());
	}

	public double getDirection()
	{
		return _direction;
	}

	public Point.Double getPosition()
	{
		return new Point.Double(this.getX(), this.getY());
	}

	public double getX()
	{
		return _x;
	}

	public double getY()
	{
		return _y;
	}

	public void setDirection(double direction)
	{
		_direction = direction;
		if (_direction > Math.PI)
		{
			_direction = -Math.PI;
		}
		else if (_direction < -Math.PI)
		{
			_direction = Math.PI;
		}
	}

	public void setX(double x)
	{
		_x = Math.max(Math.min(x, StaticEntity.MAX_X), StaticEntity.MIN_X);
	}

	public void setY(double y)
	{
		_y = Math.max(Math.min(y, StaticEntity.MAX_Y), StaticEntity.MIN_Y);
	}

	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		p.writeDouble(this.getX());
		p.writeDouble(this.getY());
		p.writeDouble(this.getDirection());
	}
}
