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
import sw.shared.GameConstants.Images;
import sw.shared.data.entities.players.SpaceShip;

public class Rocket extends Projectile
{
	public Rocket(double x, double y, double direction, SpaceShip owner)
	{
		super(x, y, direction, owner, WeaponType.ROCKET.getID());
	}

	@Override
	public double getAcceleration()
	{
		// TODO modify
		return GameConstants.ACCELERATION * 2;
	}

	@Override
	public double getAngularAcceleration()
	{
		return 0;
	}

	@Override
	public int getImageID()
	{
		return Images.SHOT_ROCKET.getID();
	}

	@Override
	public double getMaximumSpeed()
	{
		return GameConstants.MAX_SPEED * 2;
	}

	@Override
	public Dimension getSize()
	{
		return new Dimension(GameConstants.ROCKET_SIZE, GameConstants.ROCKET_SIZE);
	}
}
