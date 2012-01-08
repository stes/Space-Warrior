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
import java.util.Vector;

/**
 * Particle system to create animations such as explosions, fog etc.
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12
 */
public final class ParticleSystem
{
	public enum ParticleType
	{
		CIRCULAR
	}

	private static final Random _random = new Random(System.currentTimeMillis());

	public final static int REMOVE_WHEN_HALTED = -1;;

	private Vector<Particle> _particles;
	private Thread _tickThread;
	private ParticleSystem _self;
	private long _lastTick;
	
	public ParticleSystem()
	{
		_self = this;
		_particles = new Vector<Particle>();
	}

	public int countParticles()
	{
		return _particles.size();
	}

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
	
	public void stop()
	{
		if (_tickThread.isAlive())
			_tickThread.interrupt();
		_particles.clear();
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
			double dir = ParticleSystem._random.nextDouble() * 2 * Math.PI;
			ValuePair v = new ValuePair(Math.cos(dir) * 10 + 10
					* ParticleSystem._random.nextDouble(), Math.sin(dir) * 10 + 10
					* ParticleSystem._random.nextDouble());
			this.spawnParticle(ParticleType.CIRCULAR,
					30,
					new ValuePair(x, y),
					v.multiply(2),
					v.multiply(0.1),
					5,
					new Color(ParticleSystem._random.nextInt(255), 0, 0));
		}
	}

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

	private void tick()
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
