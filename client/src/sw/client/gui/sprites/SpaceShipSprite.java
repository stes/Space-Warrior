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

import java.awt.Graphics2D;
import java.util.Random;

import sw.client.psystem.Particle;
import sw.client.psystem.ParticleSystem;
import sw.client.psystem.ParticleSystem.ParticleType;
import sw.client.psystem.ValuePair;
import sw.shared.data.entities.players.SpaceShip;

/**
 * 
 * @author Redix, stes
 * @version 05.01.2012
 */
public class SpaceShipSprite extends ImageSprite
{
	private static Random _random = new Random(System.currentTimeMillis());
	
	private ParticleSystem _particleSystem;
	private long _lastParticleUpdate;
	
	public SpaceShipSprite(SpaceShip player)
	{
		super(player);
		_particleSystem = new ParticleSystem();
	}
	
	public void render(
			Graphics2D g2d,
			double scaleX,
			double scaleY,
			double time)
	{
		_particleSystem.render(g2d);
		super.render(g2d, scaleX, scaleY, time);
		this.processParticles(scaleX, scaleY);
	}

	private void processParticles(double scaleX, double scaleY)
	{
		// TODO get an own thread for this
		if (System.currentTimeMillis() - this._lastParticleUpdate < 10)
		{
			return;
		}
		_lastParticleUpdate = System.currentTimeMillis();
		
		_particleSystem.tick();
		SpaceShip player = (SpaceShip)getEntity();

		// TODO improve
		if (_particleSystem.countParticles() < 200)
		{
			for (int i = 0; i < 5; i++)
			{
				double dir = -player.getDirection() + _random.nextDouble() * Math.PI/2 - Math.PI/4 + Math.PI/2;
				ValuePair v = new ValuePair(Math.cos(dir) * player.getSpeed(), Math.sin(dir) * player.getSpeed());
				_particleSystem.spawnParticle(
						ParticleType.CIRCULAR,
						Particle.REMOVE_WHEN_HALTED,
						new ValuePair(player.getPosition()).multiply(scaleX, scaleY),
						v,
						v.multiply(-0.1, -0.1));
			}
		}
	}
}
