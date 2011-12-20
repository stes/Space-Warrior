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
package sw.client.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import sw.shared.GameConstants;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ImageContainer
{
	private static ImageContainer _self;

	public static ImageContainer getLocalInstance()
	{
		if (_self == null)
		{
			ImageContainer.init();
		}
		return _self;
	}

	public static void init()
	{
		_self = new ImageContainer();
	}

	private HashMap<Integer, BufferedImage> _images;

	public ImageContainer()
	{
		_images = new HashMap<Integer, BufferedImage>();
		this.loadImages();
	}

	private void loadImages()
	{
		try
		{
			_images.put(
					GameConstants.Images.BACKGROUND.getID(),
					ImageIO.read(getClass().getResourceAsStream(
							"/rsc/background.jpg")));
			_images.put(
					GameConstants.Images.SHIP_1.getID(),
					ImageIO.read(getClass().getResourceAsStream(
							"/rsc/Ship2Grey.gif")));
			_images.put(
					GameConstants.Images.SHIP_2.getID(),
					ImageIO.read(getClass().getResourceAsStream(
							"/rsc/Ship3Grey.gif")));
			_images.put(
					GameConstants.Images.SHIP_3.getID(),
					ImageIO.read(getClass().getResourceAsStream(
							"/rsc/DeathFighter1.gif")));
			_images.put(
					GameConstants.Images.SHIP_4.getID(),
					ImageIO.read(getClass().getResourceAsStream(
							"/rsc/Ship1Brown.gif")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public BufferedImage getImage(GameConstants.Images id)
	{
		return _images.get(id.getID());
	}

	public BufferedImage getImage(int id)
	{
		return _images.get(id);
	}
}
