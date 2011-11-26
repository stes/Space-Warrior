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
package sw.client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;

import sw.shared.Shot;
import sw.shared.GameConstants;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ShotGraphics extends Shot
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7174363740068442519L;
	
	private static boolean _isReady;
    private Color _basicColor;
    private double _startTime;
    
    public ShotGraphics(Shot s)
    {
        super(s.startPunkt(), s.richtung(), s.istMaster());
        _basicColor = Color.BLUE;
        _startTime = System.currentTimeMillis();
        _isReady = true;
    }

    public boolean getIsOutOfDate()
    {
        return (this.getAge() > GameConstants.SHOT_TTL);
    }
    
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(new Color(_basicColor.getRed(), _basicColor.getGreen(), _basicColor.getBlue(), (int)this.getAlphaValue(this.getAge())));
        g2d.setStroke(new BasicStroke(3));
        g.drawLine((int)this.startPunkt().getX(), (int)this.startPunkt().getY(), (int)this.endPunkt().getX(), (int)this.endPunkt().getY());
    }
    
    private double getAge()
    {
        return System.currentTimeMillis() - _startTime;
    }
    
    private double getAlphaValue(double zeit)
    {
        double a = -1020 / (double)(GameConstants.SHOT_TTL*GameConstants.SHOT_TTL);
        double d = (double)GameConstants.SHOT_TTL / 2;
        double f = 255;
        return a * (zeit - d) * (zeit - d) + f;
    }
}
