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
package sw.client.psystem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

import sw.shared.Tools;
import sw.shared.util.ValuePair;

/**
 * Particle system to create animations such as explosions, fog etc.
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12
 */
public final class ParticleSystem
{
	/**
	 * The particle types
	 * 
	 * @author stes
	 * @version 13.01.2012
	 */
	public enum ParticleType
	{
		CIRCULAR
	}

	public final static int REMOVE_WHEN_HALTED = -1;;

	private Vector<Particle> _particles;
	private Thread _tickThread;
	private ParticleSystem _self;
	private long _lastTick;

	/**
	 * Constructs a new Particle System
	 */
	public ParticleSystem()
	{
		_self = this;
		_particles = new Vector<Particle>();
	}

	/**
	 * @return the number of particles currently active
	 */
	public int countParticles()
	{
		return _particles.size();
	}

	/**
	 * Spawns particles to simulate an explosion at the specific point
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            this y coordinate
	 */
	public void explosion(double x, double y)
	{
		for (int i = 0; i < 500; i++)
		{
			double dir = Tools.getRandom().nextDouble() * 2 * Math.PI;
			ValuePair v = new ValuePair(Math.cos(dir) * 10 + 10 * Tools.getRandom().nextDouble(),
					Math.sin(dir) * 10 + 10 * Tools.getRandom().nextDouble());
			this.spawnParticle(ParticleType.CIRCULAR,
					20,
					new ValuePair(x, y),
					v.multiply(1.5),
					v.multiply(0.1),
					4,
					new Color(Tools.getRandom().nextInt(255), 0, 0));
		}
	}

	/**
	 * Renders all particles
	 * 
	 * @param g
	 *            a Graphics2D instance
	 */
	public void render(Graphics2D g)
	{
		this.render(g, 1, 1);
	}

	/**
	 * Renders all particles
	 * 
	 * @param g
	 *            a Graphics2D instance
	 * @param scaleX
	 *            the horizontal scale factor
	 * @param scaleY
	 *            the vertical scale factor
	 */
	public void render(Graphics2D g, double scaleX, double scaleY)
	{
		synchronized (_particles)
		{
			for (int i = 0; i < _particles.size(); i++)
			{
				Particle p = _particles.get(i);
				p.render(g, scaleX, scaleY);
			}
		}
	}

	/**
	 * Spawns a new particle with the specified attributes
	 * 
	 * @param type
	 *            The particle type
	 * @param lifetime
	 *            The particle's lifetime in particle system ticks
	 * @param spawnPoint
	 *            The particle's initial location
	 * @param velocity
	 *            The particle's initial velocity
	 * @param acceleration
	 *            The particle's initial acceleration
	 * @param size
	 *            The particle's size
	 * @param color
	 *            The particle's color
	 */
	public void spawnParticle(
			ParticleType type,
			int lifetime,
			ValuePair spawnPoint,
			ValuePair velocity,
			ValuePair acceleration,
			double size,
			Color color)
	{
		Particle particle = null;
		switch (type)
		{
			case CIRCULAR:
				particle = new CircularParticle(spawnPoint,
						velocity,
						acceleration,
						lifetime,
						size,
						color);
				break;
		}
		_particles.add(particle);
	}

	/**
	 * starts the particle update thread
	 */
	public void start()
	{
		_tickThread = new Thread()
		{
			@Override
			public void run()
			{
				_lastTick = System.currentTimeMillis();
				while (true)
				{
					while (System.currentTimeMillis() - _lastTick < 20)
					{
						try
						{
							Thread.sleep(4);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					_lastTick = System.currentTimeMillis();
					_self.tick();
				}
			}
		};
		_tickThread.start();
	}

	/**
	 * stops the particle update thread
	 */
	public void stop()
	{
		if (_tickThread.isAlive())
		{
			_tickThread.interrupt();
		}
		_particles.clear();
	}

	/**
	 * performs an update on all particles
	 */
	private synchronized void tick()
	{
		for (int i = 0; i < _particles.size(); i++)
		{
			Particle p = _particles.get(i);
			p.tick();
			if (!p.isAlive())
			{
				_particles.remove(i);
			}
		}
	}
}
