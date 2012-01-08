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
 * Particle with rectangular shape
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12
 */
public class SquareParticle extends Particle
{
	public SquareParticle(
			ValuePair location,
			ValuePair velocity,
			ValuePair acceleration,
			int lifetime,
			double size,
			Color color)
	{
		super(location, velocity, acceleration, lifetime, size, color);
	}

	@Override
	public void render(Graphics2D g, double scaleX, double scaleY)
	{
		if (!this.isAlive())
		{
			return;
		}
		g.fillRect((int) (scaleX * (this.getLocation().getX() - this.getSize() / 2)),
				(int) (scaleY * (this.getLocation().getY() - this.getSize() / 2)),
				(int) (scaleX * this.getSize()),
				(int) (scaleY * this.getSize()));
	}
}
