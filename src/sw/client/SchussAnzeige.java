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
import java.awt.BasicStroke;
import java.awt.Color;

import sw.shared.Schuss;
import sw.shared.Spielkonstanten;
import sw.shared.Punkt;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class SchussAnzeige extends Schuss
{
    private static boolean _istBereit;
    
    // Bezugsobjekte
    private Color _basisFarbe;
    
    // Attribute
    private double _startZeit;
    
    // Konstruktor
    public SchussAnzeige(Schuss s)
    {
        super(s.startPunkt(), s.richtung(), s.istMaster());
        _basisFarbe = Color.BLUE;
        _startZeit = System.currentTimeMillis();
        _istBereit = true;
    }

    // Dienste
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(new Color(_basisFarbe.getRed(), _basisFarbe.getGreen(), _basisFarbe.getBlue(), (int)this.alpha(this.alter())));
        g2d.setStroke(new BasicStroke(3));
        g.drawLine((int)this.startPunkt().getX(), (int)this.startPunkt().getY(), (int)this.endPunkt().getX(), (int)this.endPunkt().getY());
    }
    
    public boolean istVeraltet()
    {
        return (this.alter() > Spielkonstanten.SCHUSS_LEBENSDAUER);
    }
    
    private double alter()
    {
        return System.currentTimeMillis() - _startZeit;
    }
    
    private double alpha(double zeit)
    {
        double a = -1020 / (double)(Spielkonstanten.SCHUSS_LEBENSDAUER*Spielkonstanten.SCHUSS_LEBENSDAUER);
        double d = (double)Spielkonstanten.SCHUSS_LEBENSDAUER / 2;
        double f = 255;
        return a * (zeit - d) * (zeit - d) + f;
    }
}
