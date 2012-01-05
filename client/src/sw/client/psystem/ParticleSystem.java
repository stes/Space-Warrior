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
import java.util.ArrayList;

/**
 * Particle system to create animations such as explosions, fog etc.
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12 
 */
public class ParticleSystem
{
	public enum ParticleType {CIRCULAR};
	
	private ArrayList<Particle> _particles;
	
	private ValuePair _spawnPoint;
//	private ValuePair _initialVelocity;
//	private ValuePair _initialAcceleration;
	
	public ParticleSystem(ValuePair spawnPoint)
	{
		_spawnPoint = spawnPoint;
//		_initialVelocity = velocity;
//		_initialAcceleration = acceleration;
		_particles = new ArrayList<Particle>();
	}
	
	public void spawnParticle(ParticleType type, ValuePair velocity, ValuePair acceleration)
	{
		Particle particle = null;
		switch (type)
		{
			case CIRCULAR:
				particle = new CircularParticle(_spawnPoint, velocity, acceleration);
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
				_particles.remove(i);
		}
	}
	
	public void render(Graphics2D g)
	{
		for (int i = 0; i<_particles.size(); i++)
		{
			_particles.get(i).render(g);
		}
	}
	
	public int countParticles()
	{
		return _particles.size();
	}
}