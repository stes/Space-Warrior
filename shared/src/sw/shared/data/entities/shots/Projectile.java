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

import sw.shared.GameConstants;
import sw.shared.data.entities.MoveableEntity;
import sw.shared.data.entities.StaticEntity;
import sw.shared.data.entities.players.IDamageable;
import sw.shared.data.entities.players.SpaceShip;

/**
 * Basic class for all shots based on moving projectiles
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.12.11
 */
public abstract class Projectile extends ShotEntity
{
	public Projectile(double x, double y, double direction, SpaceShip owner, byte shottype)
	{
		super(x, y, direction, owner, shottype);

		// TODO improve
		this.setAcceleration(GameConstants.ACCELERATION);
		this.setAngularAcceleration(0);
		this.setMaximumSpeed(GameConstants.MAX_SPEED / 2);
	}

	@Override
	public void causeDamage(IDamageable target)
	{
		super.causeDamage(target);
		this.destroy();
	}

	@Override
	public void setX(double x)
	{
		if (x < StaticEntity.MIN_X || x > StaticEntity.MAX_X)
		{
			this.destroy();
		}
		super.setX(x);
	}

	@Override
	public void setY(double y)
	{
		if (y < StaticEntity.MIN_Y || y > StaticEntity.MAX_Y)
		{
			this.destroy();
		}
		super.setY(y);
	}

	@Override
	public void tick()
	{
		this.setAcceleration(MoveableEntity.ACCELERATION);
		this.fire();
		super.tick();
	}
}
