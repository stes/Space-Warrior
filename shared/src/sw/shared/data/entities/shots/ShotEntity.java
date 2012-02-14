/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
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

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.entities.MoveableEntity;
import sw.shared.data.entities.players.IAttacker;
import sw.shared.data.entities.players.IDamageable;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

public abstract class ShotEntity extends MoveableEntity implements IWeapon
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4770235976651639031L;
	private IAttacker _owner;
	private int _shottype;

	public ShotEntity(double x, double y, double direction, IAttacker owner, byte shottype)
	{
		super((byte) (Packettype.SNAP_SHOT | shottype), x, y, direction);
		this.setOwner(owner);
		this.setShotType(shottype);
	}

	@Override
	public void fire()
	{
		SpaceShip[] players = this.getWorld().getPlayers();
		for (SpaceShip pl : players)
		{
			if (pl.isAlive() && !pl.getName().equals(this.getOwner().getName())
					&& this.distanceTo(pl.getPosition()) < GameConstants.MAX_HITRANGE)
			{
				this.causeDamage(pl);
			}
		}
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		this.setShotType(p.readInt());
	}

	@Override
	public IAttacker getOwner()
	{
		return _owner;
	}

	public int getShotType()
	{
		return _shottype;
	}

	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		p.writeInt(this.getShotType());
	}

	protected void causeDamage(IDamageable target)
	{
		target.takeDamage(this.getDamage());
		// TODO move this & remove get/set score from IAttacker!!
//		this.getOwner().setScore(this.getOwner().getScore() + GameConstants.POINTS_PER_HIT);
		if (!target.isAlive())
		{
			this.getOwner().setScore(this.getOwner().getScore() + GameConstants.POINTS_PER_KILL);
		}
	}

	private void setOwner(IAttacker owner)
	{
		_owner = owner;
	}

	private void setShotType(int shottype)
	{
		this._shottype = shottype;
	}

	@Override
	public double getDamage()
	{
		return WeaponType.getWeaponType(getShotType()).getDamage();
	}

	@Override
	public int getNeededAmmo()
	{
		return WeaponType.getWeaponType(getShotType()).getAmmo();
	}

	@Override
	public double getAcceleration()
	{
		return WeaponType.getWeaponType(getShotType()).getAcceleration();
	}

	@Override
	public double getAngularAcceleration()
	{
		return WeaponType.getWeaponType(getShotType()).getAngularAcceleration();
	}

	@Override
	public double getMaximumSpeed()
	{
		return WeaponType.getWeaponType(getShotType()).getMaximumSpeed();
	}

}
