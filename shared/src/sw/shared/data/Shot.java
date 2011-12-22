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
import java.awt.geom.Line2D;

import sw.shared.GameConstants;
import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.Unpacker;
/**
 * data structure to represent a shot
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Shot extends Entity
{
	private Line2D.Double _line;
	private boolean _isMaster;
	private double _direction;
	private int _lifetime;

	/**
	 * creates a new shot
	 * 
	 * @param startpoint
	 *            startpoint of the shot
	 * @param direction
	 *            direction of the shot
	 */
	public Shot(Point.Double startPoint, double direction)
	{
		this(startPoint, direction, false);
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
	public Shot(Point.Double startPoint, double direction, boolean master)
	{
		super(Packettype.SNAP_SHOT);
		_line = new Line2D.Double(startPoint, new Point.Double(0, 0));
		_isMaster = master;
		setDirection(direction);
		_lifetime = GameConstants.SHOT_TTL; // not nice but enough for now
	}

	/**
	 * Calculates the decency to specified point
	 * 
	 * @Param p The point
	 * @Return The distance
	 */
	public double distanceTo(Point.Double p)
	{
		return _line.ptLineDist(p.getX(), p.getY());
	}
	
	/**
	 * @return the startpoint
	 */
	public Point.Double startPoint()
	{
		return new Point.Double(_line.getX1(), _line.getY1());
	}

	/**
	 * @return the endpoint
	 */
	public Point.Double endPoint()
	{
		return new Point.Double(_line.getX2(), _line.getY2());
	}

	/**
	 * @return true, if there is a master shot
	 */
	public boolean isMaster()
	{
		return _isMaster;
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
		return _isMaster ? GameConstants.MAX_MASTER_DAMAGE : GameConstants.MAX_DAMAGE;
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
		_line.setLine(
				startPoint(),
				new Point.Double(
						(startPoint().getX() + range * Math.sin(direction)),
						(startPoint().getY() + range * Math.cos(direction))));
	}
	
	public void fire(PlayerData attacker)
	{
		PlayerData[] players = this.getWorld().getPlayers();
		for(PlayerData pl : players)
		{
			if (pl.isAlive() && !pl.getName().equals(attacker.getName()) &&
				this.distanceTo(pl.getPosition()) < GameConstants.PLAYER_SIZE / 2)
			{
				pl.takeDamage(this.getDamage());
				if (!pl.isAlive())
					attacker.setScore(attacker.getScore() + 1);
			}
		}
	}

	@Override
	public void snap(Packer p, String name)
	{
		p.writeByte(this.getType());
		p.writeDouble(startPoint().getX());
		p.writeDouble(startPoint().getY());
		p.writeDouble(this.getDirection());
		p.writeBoolean(this.isMaster());
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		_line = new Line2D.Double(new Point.Double(p.readDouble(), p.readDouble()), new Point.Double(0, 0));
		setDirection(p.readDouble());
		_isMaster = p.readBoolean();
	}

	@Override
	public void tick()
	{
		_lifetime--;
		if(_lifetime <= 0)
			this.destroy();
	}
}
