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

import java.awt.Dimension;
import java.awt.Graphics2D;

import sw.client.gui.ImageContainer;
import sw.shared.data.entities.IImageEntity;
import sw.shared.data.entities.IStaticEntity;

/**
 * @author Redix, stes
 * @version 05.01.2012
 */
public class ImageSprite extends Sprite
{
	public ImageSprite(IStaticEntity entity)
	{
		super(entity);
		if (!(entity instanceof IImageEntity))
		{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void render(Graphics2D g2d, double scaleX, double scaleY, double time)
	{
		IStaticEntity ent = this.getEntity();
		Dimension d = ((IImageEntity) ent).getSize();
		g2d.drawImage(this.rotateImage(ImageContainer.getLocalInstance().getImage(((IImageEntity) ent).getImageID()),
				-this.getDirection(time)),
				(int) (scaleX * (this.getPosition(time).getX() - d.width / 2)),
				(int) (scaleY * (this.getPosition(time).getY() - d.height / 2)),
				(int) (d.width * scaleX),
				(int) (d.height * scaleY),
				null);
	}

}
