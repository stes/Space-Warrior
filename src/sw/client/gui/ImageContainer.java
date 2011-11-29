/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes Abbadonn
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ImageContainer
{
    // Bezugsobjekte
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
    
    private BufferedImage _localPlayerImg;
    
    // Attribute

    private BufferedImage _opposingPlayerImg;

    private BufferedImage _backgroundImg;
    
    // Konstruktor
    public ImageContainer()
    {
        try
        {
        	//_backgroundImg = ImageIO.read(ClassLoader.getSystemResourceAsStream("rsc/Hintergrund.png"));
        	_backgroundImg = ImageIO.read(new File(System.getProperty("user.dir") + "\\Hintergrund.png"));
        	_localPlayerImg = ImageIO.read(new File(System.getProperty("user.dir") + "\\Ship3Grey.gif"));
            _opposingPlayerImg = ImageIO.read(new File(System.getProperty("user.dir") + "\\Ship2Grey.gif"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public BufferedImage getBackgroundImg()
    {
    	return _backgroundImg.getSubimage(0, 0, _backgroundImg.getWidth(), _backgroundImg.getHeight());
    }
    
    // Dienste
    public BufferedImage getLocalPlayerImg()
    {
        return _localPlayerImg.getSubimage(0, 0, _localPlayerImg.getWidth(), _localPlayerImg.getHeight());
    }
    
    public BufferedImage getOpposingPlayerImg()
    {
        return _opposingPlayerImg.getSubimage(0, 0, _opposingPlayerImg.getWidth(), _opposingPlayerImg.getHeight());
    }
}
