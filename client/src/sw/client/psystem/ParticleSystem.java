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
import java.util.ArrayList;
import java.util.Random;

/**
 * Particle system to create animations such as explosions, fog etc.
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12
 */
public class ParticleSystem
{
	public enum ParticleType
	{
		CIRCULAR
	}

	private static final Random _random = new Random(System.currentTimeMillis());

	public final static int REMOVE_WHEN_HALTED = -1;;

	private ArrayList<Particle> _particles;

	public ParticleSystem()
	{
		_particles = new ArrayList<Particle>();
	}

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
			double dir = ParticleSystem._random.nextDouble() * 2 * Math.PI;
			ValuePair v = new ValuePair(Math.cos(dir) * 10 + 10
					* ParticleSystem._random.nextDouble(), Math.sin(dir) * 10 + 10
					* ParticleSystem._random.nextDouble());
			this.spawnParticle(ParticleType.CIRCULAR,
					30,
					new ValuePair(x, y),
					v.multiply(3),
					v.multiply(0.1),
					5,
					new Color(ParticleSystem._random.nextInt(255), 0, 0));
		}
	}

	public void render(Graphics2D g, double scaleX, double scaleY)
	{
		for (int i = 0; i < _particles.size(); i++)
		{
			_particles.get(i).render(g, scaleX, scaleY);
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

	public void tick()
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
