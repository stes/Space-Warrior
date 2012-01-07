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

import java.awt.Dimension;

import sw.shared.GameConstants;
import sw.shared.data.entities.players.SpaceShip;

public class MGProjectile extends Projectile
{
	public MGProjectile(double x, double y, double direction, SpaceShip owner)
	{
		super(x, y, direction, owner, IWeapon.MG);
	}

	@Override
	public double getAcceleration()
	{
		// TODO modify
		return GameConstants.ACCELERATION;
	}

	@Override
	public double getAngularAcceleration()
	{
		return 0;
	}

	@Override
	public double getDamage()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getImageID()
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public double getMaximumSpeed()
	{
		// TODO modify
		return GameConstants.MAX_SPEED * 3;
	}

	@Override
	public int getNeededAmmo()
	{
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Dimension getSize()
	{
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
