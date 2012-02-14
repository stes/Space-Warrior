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
package sw.client.gui.sprites;

import java.awt.Graphics2D;

import sw.client.psystem.ParticleSystem;
import sw.shared.data.entities.IStaticEntity;

/**
 * A sprite which uses a particle system
 * 
 * @author Redix, stes
 * @version 05.01.2012
 */
public abstract class ParticleSprite extends ImageSprite
{
	private ParticleSystem _particleSystem;
	private long _lastParticleUpdate;

	public ParticleSprite(IStaticEntity entity, ParticleSystem particleSystem)
	{
		super(entity);
		_particleSystem = particleSystem;
	}

	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		super.render(g2d, scaleX, scaleY, time);
		this.processParticles(scaleX, scaleY);
	}

	protected ParticleSystem getParticleSystem()
	{
		return _particleSystem;
	}

	protected abstract void spawnParticles(double scaleX, double scaleY);

	private final void processParticles(double scaleX, double scaleY)
	{
		if (System.currentTimeMillis() - this._lastParticleUpdate < 10)
		{
			return;
		}
		_lastParticleUpdate = System.currentTimeMillis();
		this.spawnParticles(scaleX, scaleY);
	}
}
