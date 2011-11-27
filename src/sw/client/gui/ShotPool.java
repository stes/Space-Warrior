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

import java.util.ArrayList;
import java.awt.Graphics;

import sw.shared.data.Shot;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ShotPool
{
    // Bezugsobjekte
    private static ArrayList<ShotGraphics> _schuesse;
    private static PlayingFieldGraphics _spielfeld;
    
    // Attribute
 
    /**
     * Fügt einen neuen Schuss hinzu
     * 
     * @param shot Der Schuss
     */
    public static void addShot(Shot shot)
    {
        _schuesse.add(new ShotGraphics(shot));
    }
    
    /**
     * Initialisiert den Schusspool
     * 
     * @param playingFieldGraphics Das Spielfeld, auf dem gezeichnet werden soll
     */
    public static void init(PlayingFieldGraphics playingFieldGraphics)
    {
        _schuesse = new ArrayList<ShotGraphics>(100);
        _spielfeld = playingFieldGraphics;
    }
    
    // Dienste    
    /**
     * Zeigt die Schüsse an
     */
    public static void paint(Graphics g)
    {
        if (_schuesse.size() > 0)
        {
            for (int i = 0; i < _schuesse.size(); i++)
            {
                if (_schuesse.get(i).getIsOutOfDate())
                {
                    _schuesse.remove(i);
                    _spielfeld.repaint();
                }
                else
                {
                    _schuesse.get(i).paint(g);
                }
            }
        }
    }
}
