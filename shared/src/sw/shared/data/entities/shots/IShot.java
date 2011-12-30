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

import sw.shared.data.entities.IEntity;
import sw.shared.data.entities.players.IAttacker;

/**
 * Represents any kind of shots who damages a player
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.12.11
 */
public interface IShot extends IEntity
{
	public final static byte LASER = 0x10;
	public final static byte MASTER_LASER = 0x20;
	public final static byte ROCKET = 0x30;
	public final static byte MG = 0x40;

	/**
	 * Fires this shot and causes damages
	 */
	void fire();

	/**
	 * @return the damage this shot causes when hitting a player
	 */
	double getDamage();

	int getNeededAmmo();

	/**
	 * @return the player who has fired this shot
	 */
	IAttacker getOwner();

}
