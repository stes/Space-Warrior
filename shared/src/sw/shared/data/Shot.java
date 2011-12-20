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
	private static final long serialVersionUID = 7824231109006024749L;

	private boolean _isMaster;
	private double _direction;

	/**
	 * Creates a new record from the given parcel
	 * 
	 * @Param p The packet
	 * @Return a new instance of shot-
	 * @Throws IllegalArgumentException if packet type is incorrect
	 */
	public static Shot read(Unpacker p)
	{
		if (p.getType() != Packettype.SV_SHOT)
		{
			throw new IllegalArgumentException();
		}
		return new Shot(new Point.Double(p.readDouble(), p.readDouble()),
				p.readDouble(), p.readBoolean());
	}

	/**
	 * creates a new shot
	 * 
	 * @param startpoint
	 *            startpoint of the shot
	 * @param direction
	 *            direction of the shot
	 */
	public Shot(Point.Double startPunkt, double direction)
	{
		this(startPunkt, direction, false);
	}

	/**
	 * creates a new shot
	 * 
	 * @param startpoint
	 *            startpoint of the shot
	 * @param direction
	 *            direction of the shot
	 * @param master
	 *            true, if a mastershot is given
	 */
	public Shot(Point.Double startPunkt, double direction, boolean master)
	{
		super(startPunkt, new Point.Double(0, 0));
		_isMaster = master;
		setDirection(direction);
	}

	/**
	 * Calculates the decency to specified point
	 * 
	 * @Param p The point
	 * @Return The distance
	 */
	public double distanceTo(Point.Double p)
	{
		return this.ptLineDist(p.getX(), p.getY());
	}

	/**
	 * @return the endpoint
	 */
	public Point.Double endPoint()
	{
		return new Point.Double(this.getX2(), this.getY2());
	}

	/**
	 * @return true, if there is a master shot
	 */
	public boolean isMaster()
	{
		return _isMaster;
	}

	/**
	 * writes the shot into a packet and passes it back
	 * 
	 * @return the packet
	 */
	public Packer write()
	{
		Packer p = new Packer(Packettype.SV_SHOT);
		p.writeDouble(startPoint().getX());
		p.writeDouble(startPoint().getY());
		p.writeDouble(this.getDirection());
		p.writeBoolean(this.isMaster());
		return p;
	}

	/**
	 * @return the direction in degrees
	 */
	public double getDirection()
	{
		return _direction;
	}

	/**
	 * @return the damage from the shot
	 */
	public int getDamage()
	{
		return _isMaster ? GameConstants.MAX_MASTER_DAMAGE
				: GameConstants.MAX_DAMAGE;
	}

	/**
	 * assigns a new direction to the shot
	 * 
	 * @param direction
	 *            new direction in degrees
	 */
	public void setDirection(double direction)
	{
		_direction = direction;
		double range = _isMaster ? GameConstants.MAX_MASTER_RANGE
				: GameConstants.MAX_RANGE;
		this.setLine(
				startPoint(),
				new Point.Double((startPoint().getX() + range
						* Math.sin(direction)), (startPoint()
						.getY() + range * Math.cos(direction))));
	}

	/**
	 * @return the startpoint
	 */
	public Point.Double startPoint()
	{
		return new Point.Double(this.getX1(), this.getY1());
	}
}
