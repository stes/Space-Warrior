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
package sw.client.gui.sprites;

import sw.client.psystem.Particle;
import sw.client.psystem.ParticleSystem.ParticleType;
import sw.client.psystem.ValuePair;
import sw.shared.data.entities.MoveableEntity;
import sw.shared.data.entities.shots.Projectile;

/**
 * 
 * @author Redix, stes
 * @version 05.01.2012
 */
public class ProjectileSprite extends ParticleSprite
{
	public ProjectileSprite(Projectile entity)
	{
		super(entity);
	}
	
	@Override
	protected void processParticles(double scaleX, double scaleY)
	{
		super.processParticles(scaleX, scaleY);
		MoveableEntity projectile = (MoveableEntity)getEntity();

		// TODO improve
		if (getParticleSystem().countParticles() < 1000)
		{
			for (int i = 0; i < 20; i++)
			{
				double dir = -projectile.getDirection() + _random.nextDouble() * Math.PI/4 - Math.PI/8 + Math.PI/2;
				ValuePair v = new ValuePair(Math.cos(dir) * projectile.getSpeed(), Math.sin(dir) * projectile.getSpeed());
				getParticleSystem().spawnParticle(
						ParticleType.CIRCULAR,
						Particle.REMOVE_WHEN_HALTED,
						new ValuePair(projectile.getPosition()).multiply(scaleX, scaleY),
						v,
						v.multiply(-0.1, -0.1));
			}
		}
	}
}
