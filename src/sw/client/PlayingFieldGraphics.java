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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;

import javax.swing.JPanel;

import sw.shared.SpielerListe;
import sw.shared.PlayerDataSet;
import sw.shared.Spielkonstanten;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class PlayingFieldGraphics extends JPanel
{
    private SpielerListe _spieler;
    private BufferedImage _localPlayerImg;
    private BufferedImage _opposingPlayerImg;
    private BufferedImage _backgroundImg;
    
    /**
     * Spielfeld wird erzeugt
     * 
     */
    public PlayingFieldGraphics(SpielerListe spielerListe)
    {
        super();
        _localPlayerImg = ImageContainer.lokaleInstanz().spielerBild();
        _opposingPlayerImg = ImageContainer.lokaleInstanz().gegnerBild();
        _spieler = spielerListe;
        ShotPool.init(this);
    }
    /**
     * Zeichnet das Spielfeld
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        ShotPool.paint(g);
        for (int i = 0 ; i < _spieler.laenge(); i++)
        {
            if (_spieler.elementAn(i) == null)
                continue;
            PlayerDataSet d = _spieler.elementAn(i);
            if (d.lokal())
            {
                this.zeigeStatusbalken(g2d, d);
                g2d.drawImage(
                    rotateImage(_localPlayerImg, 180-d.richtung()),
                    null,
                    (int)d.position().getX() - Spielkonstanten.SPIELERGROESSE/2,
                    (int)d.position().getY() - Spielkonstanten.SPIELERGROESSE/2);
            }
            else
            {
                g2d.drawImage(
                    rotateImage(_opposingPlayerImg, 180-d.richtung()),
                    null,
                    (int)d.position().getX() - Spielkonstanten.SPIELERGROESSE/2,
                    (int)d.position().getY() - Spielkonstanten.SPIELERGROESSE/2);
            }   
        }
    }
    
    private void zeigeStatusbalken(Graphics2D g2d, PlayerDataSet d)
    {
        g2d.setStroke(new BasicStroke(15));
       
        int start_x = Spielkonstanten.BALKEN_X;
        int end_x = start_x + d.leben() * Spielkonstanten.BALKEN_LAENGE / Spielkonstanten.MAX_LEBEN;
        int y = 10;
        GradientPaint pat
           = new GradientPaint(start_x, 10, Color.RED,
                               end_x, 60, new Color(255, 0, 0, 100));
        g2d.setPaint(pat);
        g2d.drawLine(start_x, y, end_x, y);
        
        end_x = start_x + d.munition() * Spielkonstanten.BALKEN_LAENGE / Spielkonstanten.MAX_MUNITION;
        y = 30;
        pat
           = new GradientPaint(start_x, 10, Color.GRAY,
                               end_x, 60, new Color(100, 100, 100, 100));
        g2d.setPaint(pat);
        g2d.drawLine(start_x, y, end_x, y);
    }
    
    private AffineTransform affineTransform(BufferedImage src, double degrees)
    {
        return AffineTransform.getRotateInstance(
            Math.toRadians(degrees),
            src.getWidth() / 2,
            src.getHeight() / 2);
    }
    
    private BufferedImage rotateImage(BufferedImage src, double degrees)
    {
        BufferedImage rotatedImage = new BufferedImage(src.getWidth(), src
                .getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = rotatedImage.createGraphics();
        g.drawImage(src, affineTransform(src, degrees), null);
        return rotatedImage;
    }
}