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
package sw.shared;

import java.awt.Point;
/**
 * Stellt ein Paar aus zwei ganzzahligen Werten dar
 * 
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Punkt extends Point
{
    // Bezugsobjekte

    // Attribute

    // Konstruktor
    /**
     * Erzeugt einen neuen Punkt
     */
    public Punkt()
    {
        super();
    }
    /**
     * Erzeugt einen neuen Punkt
     * 
     * @param x Die x-Koordinate
     * @param y Die y-Koordinate
     */
    public Punkt(int x, int y)
    {
        super(x, y);
    }
    /**
     * Erzeugt einen neuen Punkt
     * 
     * @param x Die x-Koordinate
     * @param y Die y-Koordinate
     */
    public Punkt(double x, double y)
    {
        this((int)x, (int)y);
    }
    /**
     * Erzeugt einen neuen Punkt
     * 
     * @param p Ein Punkt, dessen Koordinaten uebernommen werden
     */
    public Punkt(Point p)
    {
        super(p);
    }
}
