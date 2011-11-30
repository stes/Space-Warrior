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
package sw.shared.data;

import java.awt.Point;

import sw.shared.GameConstants;
import sw.shared.Packettype;

/**
 * data structure to represent a shot
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Shot extends java.awt.geom.Line2D.Double
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7824231109006024749L;

   /** 
     * Creates a new record from the given parcel
     *
     * @Param p The packet
     * @Return a new instance of shot-
     * @Throws IllegalArgumentException if packet type is incorrect
     */
    public static Shot hole(Packet p)
    {
        if (p.getType() != Packettype.SV_SCHUSS)
            throw new IllegalArgumentException();
        return new Shot(
            new Point(p.getNumber(), p.getNumber()),
            p.getNumber(),
            p.getBoolean());
    }
    // Attribute
    private boolean _istMaster;
    
    // Konstruktor

    private int _richtung;
    
    /**
     * creates a new shot
     *
     * @param startpoint startpoint of the shot
     * @param direction direction of the shot
     */
    public Shot(Point startPunkt, int richtung)
    {
        this(startPunkt, richtung, false);
    }

    /**
     * creates a new shot
     *
     * @param startpoint startpoint of the shot
     * @param direction direction of the shot
     * @param master true, if a mastershot is given
     */
    public Shot(Point startPunkt, int richtung, boolean master)
    {
        super(startPunkt, new Point(0,0));
        _istMaster = master;
        setzeRichtung(richtung);
    }
   /**
    * Calculates the decency to specified point
    *
    * @Param p The point
    * @Return The distance
    */
    public double abstandZu(Point p)
    {
        return this.ptLineDist(p.getX(), p.getY());
    }
    
    public double abstandZu(Point.Double p)
	{
		return this.ptLineDist(p.getX(), p.getY());
	}
    
    /**
     * @return the endpoint
     */
    public Point endPunkt()
    {
        return new Point((int)this.getX2(), (int)this.getY2());
    }
    
    /**
     * @return true, if there is a master shot
     */
    public boolean istMaster()
    {
        return _istMaster;
    }
    
    /**
     * writes the shot into a packet and passes it back
     * 
     * @return the packet
     */
    public Packet pack()
    {
        Packet p = new Packet(Packettype.SV_SCHUSS);
        p.addNumber((int)startPunkt().getX());
        p.addNumber((int)startPunkt().getY());
        p.addNumber(this.richtung());
        p.addBoolean(this.istMaster());
        return p;
    }
    
	/**
     * @return the direction in degrees
     */
    public int richtung()
    {
        return _richtung;
    }
    
    /**
     * @return the damage from the shot
     */
    public int schaden()
    {
        return this.istMaster() ? GameConstants.MAX_MASTER_DAMAGE : GameConstants.MAX_DAMAGE;
    }
    
    /**
     * assigns a new direction to the shot
     * 
     * @param direction new direction in degrees
     */
    public void setzeRichtung(int richtung)
    {
        _richtung = richtung;
        double reichweite = _istMaster ? GameConstants.MAX_MASTER_RANGE : GameConstants.MAX_RANGE;
        this.setLine(startPunkt(), new Point(
            (int)(startPunkt().getX() + reichweite * Math.sin(Math.toRadians(richtung))),
            (int)(startPunkt().getY() + reichweite * Math.cos(Math.toRadians(richtung)))));
    }
    
    // Dienste
    /**
     * @return the startpoint
     */
    public Point startPunkt()
    {
        return new Point((int)this.getX1(), (int)this.getY1());
    }
}
