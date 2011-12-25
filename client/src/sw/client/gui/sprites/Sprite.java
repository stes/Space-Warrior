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
import java.awt.image.BufferedImage;

import sw.client.gui.ImageContainer;

public abstract class Sprite
{
	private BufferedImage _image;

	private int _x;
	private int _y;
	private double _angle;
	private int _id;

	public Sprite(int id)
	{
		_id = id;
		this.loadImg(_id);
	}

	private void loadImg(int id)
	{
		_image = ImageContainer.getLocalInstance().getImage(id);
	}

	protected AffineTransform affineTransform(BufferedImage src, double degrees)
	{
		return AffineTransform.getRotateInstance(Math.toRadians(degrees),
				src.getWidth() / 2, src.getHeight() / 2);
	}

	protected BufferedImage rotateImage(BufferedImage src, double degrees)
	{
		BufferedImage rotatedImage = new BufferedImage(src.getWidth(),
				src.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotatedImage.createGraphics();
		g.drawImage(src, affineTransform(src, degrees), null);
		return rotatedImage;
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(rotateImage(_image, 180 - getAngle()), null, (int) getX()
				- _image.getWidth() / 2, (int) getY() - _image.getHeight() / 2);
	}

	public void setAngle(double _angle)
	{
		this._angle = _angle;
	}

	public double getAngle()
	{
		return _angle;
	}

	public void setY(int _y)
	{
		this._y = _y;
	}

	public int getY()
	{
		return _y;
	}

	public void setX(int _x)
	{
		this._x = _x;
	}

	public int getX()
	{
		return _x;
	}
	
	public BufferedImage getImage()
	{
		return _image;
	}
}
