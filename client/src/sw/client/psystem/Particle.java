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

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Base class for a particle
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12
 */
abstract class Particle
{
	protected static Random _random = new Random(System.currentTimeMillis());

	private final static int LIFETIME = 50;
	private final static double SIZE = 3;

	private ValuePair _location;
	private ValuePair _velocity;
	private ValuePair _acceleration;

	private double _size;
	private int _lifetime;
	private Color _color;

	public Particle(ValuePair location, ValuePair velocity, ValuePair acceleration, Color color)
	{
		this(location, velocity, acceleration, Particle.LIFETIME, Particle.SIZE, color);
	}

	public Particle(
			ValuePair location,
			ValuePair velocity,
			ValuePair acceleration,
			int lifetime,
			double size,
			Color color)
	{
		_location = location;
		_velocity = velocity;
		_acceleration = acceleration;
		_size = size;
		_lifetime = lifetime;
		_color = color;
	}

	/**
	 * @return the acceleration
	 */
	public ValuePair getAcceleration()
	{
		return _acceleration;
	}

	public int getLifetime()
	{
		return _lifetime;
	}

	/**
	 * @return the location
	 */
	public ValuePair getLocation()
	{
		return _location;
	}

	public double getSize()
	{
		return _size;
	}

	/**
	 * @return the color
	 */
	public Color getColor()
	{
		return _color;
	}

	/**
	 * @return the velocity
	 */
	public ValuePair getVelocity()
	{
		return _velocity;
	}

	public boolean isAlive()
	{
		return (_lifetime > 0 || (_lifetime == ParticleSystem.REMOVE_WHEN_HALTED && this.getVelocity().getVectorLength() > 0.00001));
	}

	public abstract void render(Graphics2D g, double scaleX, double scaleY);

	/**
	 * @param acceleration
	 *            the acceleration to set
	 */
	public void setAcceleration(ValuePair acceleration)
	{
		this._acceleration = acceleration;
	}

	public void setLifetime(int lifetime)
	{
		_lifetime = Math.max(0, lifetime);
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(ValuePair location)
	{
		this._location = location;
	}

	/**
	 * @param velocity
	 *            the velocity to set
	 */
	public void setVelocity(ValuePair velocity)
	{
		this._velocity = velocity;
	}

	public void tick()
	{
		if (!this.isAlive())
		{
			return;
		}
		if (_lifetime != ParticleSystem.REMOVE_WHEN_HALTED)
		{
			this.setLifetime(_lifetime - 1);
		}
		this.move();
	}

	private void move()
	{
		_location = _location.add(_velocity);
		_velocity = _velocity.add(_acceleration);
	}
}
