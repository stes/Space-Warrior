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
package sw.shared.data.entities;

import java.awt.Point;
import java.awt.geom.Line2D;

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * data structure to represent a shot
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class LaserBeam extends StaticEntity
{
	private boolean _isMaster;
	private int _lifetime;

	public LaserBeam(double x, double y, double direction)
	{
		this(x, y, direction, false);
	}

	public LaserBeam(double x, double y, double direction, boolean master)
	{
		super(Packettype.SNAP_SHOT);
		_isMaster = master;
		setDirection(direction);
		setX(x);
		setY(y);
		_lifetime = GameConstants.SHOT_TTL; // TODO not nice but enough for now
	}

	public Line2D.Double getLine()
	{
		double range = _isMaster ? GameConstants.MAX_MASTER_RANGE : GameConstants.MAX_RANGE;
		return new Line2D.Double(getX(),
				getY(),
				getX() + range * Math.sin(getDirection()),
				(getY() + range * Math.cos(getDirection())));
	}

	/**
	 * Calculates the decency to specified point
	 * 
	 * @Param p The point
	 * @Return The distance
	 */
	public double distanceTo(Point.Double p)
	{
		return getLine().ptLineDist(p.getX(), p.getY());
	}

	/**
	 * @return the endpoint
	 */
	public Point.Double endPoint()
	{
		Line2D l = getLine();
		return new Point.Double(l.getX2(), l.getY2());
	}

	public void fire(SpaceShip attacker)
	{
		SpaceShip[] players = this.getWorld().getPlayers();
		for (SpaceShip pl : players)
		{
			if (pl.isAlive() && !pl.getName().equals(attacker.getName())
					&& this.distanceTo(pl.getPosition()) < GameConstants.PLAYER_SIZE / 2)
			{
				pl.takeDamage(this.getDamage());
				if (!pl.isAlive())
					attacker.setScore(attacker.getScore() + 1);
			}
		}
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		_isMaster = p.readBoolean();
	}

	/**
	 * @return the damage from the shot
	 */
	public int getDamage()
	{
		return _isMaster ? GameConstants.MAX_MASTER_DAMAGE : GameConstants.MAX_DAMAGE;
	}

	/**
	 * @return true, if there is a master shot
	 */
	public boolean isMaster()
	{
		return _isMaster;
	}

	@Override
	public void snap(Packer p, String name)
	{
		p.writeByte(this.getType());
		super.snap(p, name);
		p.writeBoolean(this.isMaster());
	}

	@Override
	public void tick()
	{
		_lifetime--;
		if (_lifetime <= 0)
			this.destroy();
	}
}
