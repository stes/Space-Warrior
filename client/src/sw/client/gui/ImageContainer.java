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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
			_images.put(GameConstants.Images.BACKGROUND.getID(),
					ImageIO.read(getClass().getResourceAsStream("/rsc/background.jpg")));
			_images.put(GameConstants.Images.SHIP_1.getID(),
					scale(ImageIO.read(getClass().getResourceAsStream("/rsc/Ship2Grey.gif"))));
			_images.put(GameConstants.Images.SHIP_2.getID(),
					scale(ImageIO.read(getClass().getResourceAsStream("/rsc/Ship3Grey.gif"))));
			_images.put(GameConstants.Images.SHIP_3.getID(),
					scale(ImageIO.read(getClass().getResourceAsStream("/rsc/DeathFighter1.gif"))));
			_images.put(GameConstants.Images.SHIP_4.getID(),
					scale(ImageIO.read(getClass().getResourceAsStream("/rsc/Ship1Brown.gif"))));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private BufferedImage scale(BufferedImage source)
	{
		return scale(source, 1, 1);
	}

	private BufferedImage scale(BufferedImage source, double scaleX, double scaleY)
	{
		// Create new (blank) image of required (scaled) size

		int width = (int) (GameConstants.PLAYER_SIZE * scaleX);
		int height = (int) (GameConstants.PLAYER_SIZE * scaleY);
		BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// Paint scaled version of image to new image

		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(source, 0, 0, width, height, null);

		// clean up

		graphics2D.dispose();

		return scaledImage;
	}

	public void scaleImages(double scaleX, double scaleY)
	{
		HashMap<Integer, BufferedImage> newImg = new HashMap<Integer, BufferedImage>();
		for (int i : _images.keySet())
		{
			newImg.put(i, scale(_images.get(i), scaleX, scaleY));
		}
		_images = newImg;
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
