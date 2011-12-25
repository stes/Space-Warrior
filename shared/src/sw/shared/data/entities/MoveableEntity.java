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

/**
 * A moveable entity
 * @author Redix stes Abbadonn
 * @version 24.12.11
 */
public abstract class MoveableEntity extends StaticEntity
{	
	public static final double MAX_SPEED = GameConstants.MAX_SPEED;
	public static final double MAX_TURN_SPEED = GameConstants.MAX_ANGULAR_SPEED;
	
	public static final double ACCELERATION = GameConstants.ACCELERATION;
	public static final double ANGULAR_ACCELERATION = GameConstants.ANGULAR_ACCELERATION;
	
	// movements
	private double _speed;
	private double _turnSpeed;
	
	// acceleration variables
	private double _acceleration;
	private double _adirection;
	
	// maximums
	private double _maxSpeed;
	
	public MoveableEntity(byte type)
	{
		super(type);
	}	
	
	public void setSpeed(double speed)
	{
		_speed = Math.max(0, Math.min(GameConstants.MAX_SPEED, speed));
	}

	public double getSpeed()
	{
		return _speed;
	}

	public void setMaximumSpeed(double speed)
	{
		_maxSpeed = speed;
	}
	
	public double getMaximumSpeed()
	{
		return _maxSpeed;
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

	public double getTurnSpeed()
	{
		return _turnSpeed;
	}
	
	public void setAcceleration(double acceleration)
	{
		this._acceleration = Math.min(acceleration, MoveableEntity.ACCELERATION);
	}

	public double getAcceleration()
	{
		return _acceleration;
	}

	public void setAngularAcceleration(double angularAcceleration)
	{
		this._adirection = Math.min(angularAcceleration, MoveableEntity.ANGULAR_ACCELERATION);
	}

	public double getAngularAcceleration()
	{
		return _adirection;
	}
	
	public Point.Double getPosition()
	{
		return new Point.Double(getX(), getY());
	}
	
	@Override
	public void tick()
	{
		this.accelerate();
		this.move();
	}

	/**
	 * moves the entity based on the speed values
	 */
	public void move()
	{
		double x = _speed * Math.sin(getDirection());
		double y = _speed * Math.cos(getDirection());
		this.setX(getX() + x);
		this.setY(getY() + y);
		this.setDirection(getDirection() + _turnSpeed);
	}
	
	/**
	 * accelerates the entity based on the accelerate values
	 */
	public void accelerate()
	{
		this.setSpeed(_speed + _acceleration);
		this.setTurnSpeed(_turnSpeed + _adirection);
	}
}