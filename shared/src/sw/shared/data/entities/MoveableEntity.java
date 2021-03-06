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
package sw.shared.data.entities;

import java.awt.Point;

import sw.shared.GameConstants;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * A moveable entity
 * 
 * @author Redix stes Abbadonn
 * @version 24.12.11
 */
public abstract class MoveableEntity extends StaticEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3423631869017040528L;

	// public static final double MAX_SPEED = GameConstants.MAX_SPEED;
	public static final double MAX_TURN_SPEED = GameConstants.MAX_ANGULAR_SPEED;

	public static final double ACCELERATION = GameConstants.ACCELERATION;
	public static final double ANGULAR_ACCELERATION = GameConstants.ANGULAR_ACCELERATION;

	// movements
	private double _speed;
	private double _turnSpeed;

	public MoveableEntity(byte type)
	{
		this(type, 0, 0, 0);
	}

	public MoveableEntity(byte i, double x, double y, double direction)
	{
		super(i, x, y, direction);
	}

	/**
	 * accelerates the entity based on the accelerate values
	 */
	public void accelerate()
	{
		this.setSpeed(_speed + this.getAcceleration());
		this.setTurnSpeed(_turnSpeed + this.getAngularAcceleration());
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		this.setSpeed(p.readDouble());
	}

	public abstract double getAcceleration();

	public abstract double getAngularAcceleration();

	public abstract double getMaximumSpeed();

	@Override
	public Point.Double getPosition()
	{
		return new Point.Double(this.getX(), this.getY());
	}

	public double getSpeed()
	{
		return _speed;
	}

	public double getTurnSpeed()
	{
		return _turnSpeed;
	}

	/**
	 * moves the entity based on the speed values
	 */
	public void move()
	{
		double x = -_speed * Math.sin(this.getDirection());
		double y = -_speed * Math.cos(this.getDirection());
		this.setX(this.getX() + x);
		this.setY(this.getY() + y);
		this.setDirection(this.getDirection() + _turnSpeed);
	}

	public void setSpeed(double speed)
	{
		_speed = Math.max(0, Math.min(this.getMaximumSpeed(), speed));
	}

	public void setTurnSpeed(double turnSpeed)
	{
		_turnSpeed = turnSpeed;
		if (_turnSpeed > MoveableEntity.MAX_TURN_SPEED)
		{
			_turnSpeed = MoveableEntity.MAX_TURN_SPEED;
		}
		else if (_turnSpeed < -MoveableEntity.MAX_TURN_SPEED)
		{
			_turnSpeed = -MoveableEntity.MAX_TURN_SPEED;
		}
	}

	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		p.writeDouble(this.getSpeed());
	}

	@Override
	public void tick()
	{
		this.accelerate();
		this.move();
	}
}
