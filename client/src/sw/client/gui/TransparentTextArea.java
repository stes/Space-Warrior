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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JTextArea;

class TransparentTextArea extends JTextArea
{
	private static final long serialVersionUID = 7617582110497995855L;


	public TransparentTextArea ()
    {
        this.setOpaque(false);
    }
 
 
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        this.setBackground(Color.white);
        Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f);
        g2d.setComposite(alphaComp);
        g2d.setColor(getBackground());
        Rectangle tBounds = g2d.getClip().getBounds();
        g2d.fillRect((int) tBounds.getX(),(int)tBounds.getY(),(int)tBounds.getWidth(),(int)tBounds.getHeight());
        super.paintComponent(g2d);
        this.setForeground(Color.black);
    }   
    
}
