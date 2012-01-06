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

/**
 * Particle with circular shape
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12
 */
public class CircularParticle extends Particle
{
	public CircularParticle(
			ValuePair location,
			ValuePair velocity,
			ValuePair acceleration,
			int lifetime)
	{
		super(location, velocity, acceleration, lifetime);
	}

	@Override
	public void render(Graphics2D g)
	{
		if (!this.isAlive())
		{
			return;
		}
		g.setColor(Color.RED);
		// g.setColor(new Color(_random.nextInt(255), 0, 0));
		g.fillOval((int) (this.getLocation().getX() - this.getSize() / 2),
				(int) (this.getLocation().getY() - this.getSize() / 2),
				(int) this.getSize(),
				(int) this.getSize());
	}

}
