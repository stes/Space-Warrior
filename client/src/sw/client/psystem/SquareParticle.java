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

/**
 * Particle with rectangular shape
 * 
 * @author Redix stes Abbadonn
 * @version 02.01.12 
 */
public class SquareParticle extends Particle
{
	public SquareParticle(ValuePair location, ValuePair velocity, ValuePair acceleration)
	{
		super(location, velocity, acceleration);
	}

	@Override
	public void render(Graphics2D g)
	{
		if (!isAlive())
			return;
		g.fillRect((int)(getLocation().getX() - getSize() / 2),
				(int)(getLocation().getY() - getSize() / 2),
				(int)getSize(),
				(int)getSize());
	}
}
