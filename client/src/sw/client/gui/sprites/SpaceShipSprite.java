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

import java.awt.Color;

import sw.client.psystem.ParticleSystem;
import sw.client.psystem.ParticleSystem.ParticleType;
import sw.client.psystem.ValuePair;
import sw.shared.Tools;
import sw.shared.data.entities.MoveableEntity;
import sw.shared.data.entities.players.SpaceShip;

/**
 * @author Redix, stes
 * @version 05.01.2012
 */
public class SpaceShipSprite extends ParticleSprite
{
	public SpaceShipSprite(SpaceShip player, ParticleSystem particleSystem)
	{
		super(player, particleSystem);
	}

	@Override
	protected void spawnParticles(double scaleX, double scaleY)
	{
		MoveableEntity player = (MoveableEntity) this.getEntity();

		// TODO improve
		if (this.getParticleSystem().countParticles() < 400)
		{
			for (int i = 0; i < 5; i++)
			{
				double dir = -player.getDirection() + Tools.getRandom().nextDouble() * Math.PI
						* 0.3 - Math.PI * 0.15 + Math.PI / 2;
				double speed = player.getSpeed()
						* Math.abs((1 + Tools.getRandom().nextGaussian()));
				ValuePair v = new ValuePair(Math.cos(dir) * speed, Math.sin(dir) * speed);
				this.getParticleSystem().spawnParticle(ParticleType.CIRCULAR,
						ParticleSystem.REMOVE_WHEN_HALTED,
						new ValuePair(player.getPosition()),
						v,
						v.multiply(-0.25, -0.25),
						3,
						Color.YELLOW.darker());
			}
		}
	}
}
