/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes
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
package sw.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class BilderContainer
{
    // Bezugsobjekte
    private static BilderContainer _ich;
    
    private BufferedImage _spielerBild;
    private BufferedImage _gegnerBild;
    // Attribute

    // Konstruktor
    public BilderContainer()
    {
        try
        {
            _spielerBild = ImageIO.read(new File(System.getProperty("user.dir") + "\\Ship3Grey.gif"));
            _gegnerBild = ImageIO.read(new File(System.getProperty("user.dir") + "\\Ship2Grey.gif"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Dienste
    public BufferedImage spielerBild()
    {
        return _spielerBild.getSubimage(0, 0, _spielerBild.getWidth(), _spielerBild.getHeight());
    }
    
    public BufferedImage gegnerBild()
    {
        return _gegnerBild.getSubimage(0, 0, _spielerBild.getWidth(), _spielerBild.getHeight());
    }
    
    public static BilderContainer lokaleInstanz()
    {
        if (_ich == null)
        {
            BilderContainer.init();
        }
        return _ich;
    }
    
    public static void init()
    {
        _ich = new BilderContainer();
    }
}
