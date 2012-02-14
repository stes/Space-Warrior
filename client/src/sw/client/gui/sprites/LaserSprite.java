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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import sw.shared.GameConstants;
import sw.shared.data.entities.shots.LaserBeam;

public class LaserSprite extends Sprite implements IShotSprite
{
	public LaserSprite(LaserBeam entity)
	{
		super(entity);
	}

	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		LaserBeam s = (LaserBeam) this.getEntity();
		Color c = new Color(0, 0, 255, (int) this.alpha(s.getLifetime()));
		g2d.setColor(c);
		g2d.setStroke(new BasicStroke(3));
		g2d.drawLine((int) (s.getX() * scaleX),
				(int) (s.getY() * scaleY),
				(int) (s.endPoint().getX() * scaleX),
				(int) (s.endPoint().getY() * scaleY));
	}

	private double alpha(double time)
	{
		double a = -1020 / (double) (GameConstants.SHOT_TTL * GameConstants.SHOT_TTL);
		double d = (double) GameConstants.SHOT_TTL / 2;
		double f = 255;
		return a * (time - d) * (time - d) + f;
	}

}
