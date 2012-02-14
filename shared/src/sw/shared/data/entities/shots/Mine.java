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

import java.awt.Dimension;

import sw.shared.GameConstants;
import sw.shared.data.entities.players.SpaceShip;

/**
 * @author Redix stes
 * @version 07.01.2012
 */
public class Mine extends Projectile
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6033320175720353100L;

	private static final int LIFETIME = 80;
	
	private int _lifetime;

	public Mine(double x, double y, double direction, SpaceShip owner)
	{
		super(x, y, direction, owner, WeaponType.MINE.getID());
		_lifetime = 100;
	}

	@Override
	public void fire()
	{
		SpaceShip[] players = this.getWorld().getPlayers();
		for (SpaceShip pl : players)
		{
			if (_lifetime < Mine.LIFETIME && this.distanceTo(pl.getPosition()) < GameConstants.MAX_HITRANGE)
			{
				this.causeDamage(pl);
			}
		}
	}

	@Override
	public int getImageID()
	{
		return GameConstants.Images.SHOT_MINE.getID();
	}

	@Override
	public Dimension getSize()
	{
		return new Dimension(GameConstants.MINE_SIZE, GameConstants.MINE_SIZE);
	}

	@Override
	public void tick()
	{
		super.tick();
		_lifetime = Math.max(_lifetime - 1, 0);
		if (_lifetime == 0)
		{
			this.destroy();
		}
	}
}
