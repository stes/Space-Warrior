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

import java.awt.Graphics2D;
import java.util.Random;

/**
 * Base class for a particle
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12 
 */
public abstract class Particle
{
	protected static Random _random = new Random(System.currentTimeMillis());
	
	private final static int LIFETIME = 50;
	private final static double SIZE = 3;
	
	public final static int REMOVE_WHEN_HALTED = -1;
	
	private ValuePair _location;
	private ValuePair _velocity;
	private ValuePair _acceleration;
	
	private double _size;
	private int _lifetime;
	
	public Particle(ValuePair location, ValuePair velocity, ValuePair acceleration)
	{
		this(location, velocity, acceleration, LIFETIME);
	}

	public Particle(ValuePair location, ValuePair velocity, ValuePair acceleration, int lifetime)
	{
		_location = location;
		_velocity = velocity;
		_acceleration = acceleration;
		_size = SIZE;
		_lifetime = lifetime;
	}

	private void move()
	{
		_location = _location.add(_velocity);
		_velocity = _velocity.add(_acceleration);
	}
	
	public void tick()
	{
		if (!isAlive())
			return;
		if (_lifetime != REMOVE_WHEN_HALTED)
			setLifetime(_lifetime - 1);
		this.move();
	}
	
	public abstract void render(Graphics2D g);
	
	/**
	 * @return the location
	 */
	public ValuePair getLocation()
	{
		return _location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(ValuePair location)
	{
		this._location = location;
	}

	/**
	 * @return the velocity
	 */
	public ValuePair getVelocity()
	{
		return _velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(ValuePair velocity)
	{
		this._velocity = velocity;
	}

	/**
	 * @return the acceleration
	 */
	public ValuePair getAcceleration()
	{
		return _acceleration;
	}

	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(ValuePair acceleration)
	{
		this._acceleration = acceleration;
	}
	
	public int getLifetime()
	{
		return _lifetime;
	}
	
	public void setLifetime(int lifetime)
	{
		_lifetime = Math.max(0, lifetime);
	}
	
	public double getSize()
	{
		return _size;
	}
	
	public boolean isAlive()
	{
		return (_lifetime > 0 || (_lifetime == REMOVE_WHEN_HALTED && this.getVelocity().getVectorLength() > 0.00001));
	}
}
