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
package sw.shared.data.entities.shots;

import java.awt.Point;
import java.awt.geom.Line2D;

import sw.shared.GameConstants;
import sw.shared.data.entities.StaticEntity;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * data structure to represent a shot
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class LaserBeam extends ShotEntity
{
	private boolean _isMaster;
	private int _lifetime;
	private SpaceShip _owner;

	public LaserBeam(double x, double y, double direction, SpaceShip owner)
	{
		this(x, y, direction, owner, false);
	}

	public LaserBeam(double x, double y, double direction, SpaceShip owner, boolean master)
	{
		super(x, y, direction, owner, master ? IShot.MASTER_LASER : IShot.LASER);
		_isMaster = master;
		_owner = owner;
		_lifetime = GameConstants.SHOT_TTL; // TODO not nice but enough for now
	}

	/**
	 * Calculates the distance to specified point
	 * 
	 * @Param p The point
	 * @Return The distance
	 */
	@Override
	public double distanceTo(Point.Double p)
	{
		return this.getLine().ptLineDist(p.getX(), p.getY());
	}

	@Override
	public double distanceTo(StaticEntity entity)
	{
		return this.distanceTo(entity.getPosition());
	}

	/**
	 * @return the endpoint
	 */
	public Point.Double endPoint()
	{
		Line2D l = this.getLine();
		return new Point.Double(l.getX2(), l.getY2());
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		_isMaster = p.readBoolean();
	}

	@Override
	public double getAcceleration()
	{
		return 0;
	}

	@Override
	public double getAngularAcceleration()
	{
		return 0;
	}

	/**
	 * @return the damage from the shot
	 */
	@Override
	public double getDamage()
	{
		return _isMaster ? GameConstants.MAX_MASTER_DAMAGE : GameConstants.MAX_DAMAGE;
	}

	public Line2D.Double getLine()
	{
		double range = _isMaster ? GameConstants.MAX_MASTER_RANGE : GameConstants.MAX_RANGE;
		return new Line2D.Double(this.getX(), this.getY(), this.getX() - range
				* Math.sin(this.getDirection()), this.getY() - range
				* Math.cos(this.getDirection()));
	}

	@Override
	public double getMaximumSpeed()
	{
		return 0;
	}

	@Override
	public int getNeededAmmo()
	{
		return this.isMaster() ? GameConstants.AMMO_PER_MASTER_SHOT : GameConstants.AMMO_PER_SHOT;
	}

	@Override
	public SpaceShip getOwner()
	{
		return _owner;
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
		super.snap(p, name);
		p.writeBoolean(this.isMaster());
	}

	@Override
	public void tick()
	{
		_lifetime--;
		if (_lifetime <= 0)
		{
			this.destroy();
		}
	}
}
