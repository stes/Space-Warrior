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

import java.util.ArrayList;
import java.awt.Graphics;

import sw.shared.data.Shot;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ShotPool
{
    private static ArrayList<ShotGraphics> _shots;
    
    public static void addShot(Shot shot)
    {
        _shots.add(new ShotGraphics(shot));
    }
    
    public static void init()
    {
        _shots = new ArrayList<ShotGraphics>(100);
    }
    
    public static void paint(Graphics g)
    {
        if (_shots.size() > 0)
        {
            for (int i = 0; i < _shots.size(); i++)
            {
                if (_shots.get(i).getIsOutOfDate())
                {
                    _shots.remove(i);
                }
                else
                {
                    _shots.get(i).paint(g);
                }
            }
        }
    }
}
