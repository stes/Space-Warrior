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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import sw.shared.data.entities.IStaticEntity;

/**
 * 
 * @author Redix, stes
 * @version 05.01.2012
 */
public abstract class Sprite
{
	private IStaticEntity _entity;
	private IStaticEntity _prevEntity;

	public Sprite(IStaticEntity entity)
	{
		_entity = entity;
		_prevEntity = entity;
	}

	protected BufferedImage rotateImage(BufferedImage src, double degrees)
	{
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(src, this.rotateTransform(src, degrees), null);
		return rotatedImage;
	}

	protected AffineTransform rotateTransform(BufferedImage src, double degrees)
	{
		return AffineTransform.getRotateInstance(degrees, src.getWidth() / 2, src.getHeight() / 2);
	}

	public IStaticEntity getEntity()
	{
		return _entity;
	}

	public IStaticEntity getPreviousEntity()
	{
		return _prevEntity;
	}

	public void updateEntity(IStaticEntity entity, IStaticEntity prevEntity)
	{
		if (_entity.getID() != entity.getID())
			throw new IllegalArgumentException("Player ID does not match");
		// TODO use a copy?
		_prevEntity = prevEntity;
		_entity = entity;
	}

	public abstract void render(Graphics2D g2d, double scaleX, double scaleY, double time);

	protected Point2D.Double getPosition(double time)
	{
		return new Point2D.Double(getPreviousEntity().getPosition().getX()
				+ (getEntity().getPosition().getX() - getPreviousEntity().getPosition().getX())
				* time, getPreviousEntity().getPosition().getY()
				+ (getEntity().getPosition().getY() - getPreviousEntity().getPosition().getY())
				* time);
	}

	protected double getDirection(double time)
	{
		return getPreviousEntity().getDirection()
				+ Math.asin(Math.sin(getEntity().getDirection()
						- getPreviousEntity().getDirection())) * time;
	}
}
