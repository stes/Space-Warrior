package sw.client.psystem;

import java.awt.Graphics2D;

public abstract class Particle
{
	private ValuePair _location;
	private ValuePair _velocity;
	private ValuePair _acceleration;
	
	private double _size;
	// TODO improve
	private int _lifetime = 100;
	
	public Particle(ValuePair location, ValuePair velocity, ValuePair acceleration)
	{
		_location = location;
		_velocity = velocity;
		_acceleration = acceleration;
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
		return (_lifetime > 0);
	}
}
