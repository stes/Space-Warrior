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

import sw.shared.data.entities.IImageEntity;
import sw.shared.data.entities.IStaticEntity;
import sw.shared.data.entities.players.IDamageable;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * Basic class for all shots based on moving projectiles
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.12.11
 */
public abstract class Projectile extends ShotEntity implements IImageEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4881411750797733615L;
	private boolean _isExploding;

	public Projectile(double x, double y, double direction, SpaceShip owner, byte shottype)
	{
		super(x, y, direction, owner, shottype);
	}

	@Override
	public void causeDamage(IDamageable target)
	{
		super.causeDamage(target);
		this.destroy();
	}

	@Override
	public void destroy()
	{
		if (!this.isDestroyed())
		{
			_isExploding = true;
		}
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		_isExploding = p.readBoolean();
	}

	public boolean isExploding()
	{
		return _isExploding;
	}

	@Override
	public void setX(double x)
	{
		if (x < IStaticEntity.MIN_X || x > IStaticEntity.MAX_X)
		{
			this.destroy();
		}
		else
		{
			super.setX(x);
		}
	}

	@Override
	public void setY(double y)
	{
		if (y < IStaticEntity.MIN_Y || y > IStaticEntity.MAX_Y)
		{
			this.destroy();
		}
		else
		{
			super.setY(y);
		}
	}

	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		p.writeBoolean(_isExploding);
	}

	@Override
	public void tick()
	{
		if (_isExploding)
		{
			_isExploding = false;
			super.destroy();
		}
		else
		{
			this.fire();
		}
		super.tick();
	}
}
