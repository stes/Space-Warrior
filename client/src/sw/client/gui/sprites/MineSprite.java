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

import java.awt.Color;
import java.awt.Graphics2D;

import sw.shared.data.entities.shots.Projectile;

public class MineSprite extends Sprite implements IShotSprite
{
	public MineSprite(Projectile entity)
	{
		super(entity);

	}

	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) this.getEntity().getX() - 10, (int) this.getEntity().getY() - 10, 20, 20);
	}
}
