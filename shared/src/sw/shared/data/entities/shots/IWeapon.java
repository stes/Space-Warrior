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
public interface IWeapon extends IEntity
{
	public enum WeaponType
	{
		LASER((byte)0x10, 10, 20),
		MASTER_LASER((byte)0x20, 50, 30),
		ROCKET((byte)0x30, 50, 50),
		MINE((byte)0x40, 20, 60),
		MG((byte)0x50, 50, 50);
		
		private final int _ammo;
		private final int _dmg;
		private final byte _id;
		
		private WeaponType(byte id, int ammo, int damage)
		{
			_id = id;
			_ammo = ammo;
			_dmg = damage;
		}
		
		public int getAmmo()
		{
			return _ammo;
		}
		
		public int getDamage()
		{
			return _dmg;
		}
		
		public byte getID()
		{
			return _id;
		}
		
		public static WeaponType getWeaponType(int id)
		{
			for (WeaponType w : WeaponType.values())
			{
				if (w.getID() == (byte)id)
					return w;
			}
			throw new IllegalArgumentException("ID does not match to any weapon");
		}
	}

	/**
	 * Fires this shot and causes damages
	 */
	void fire();

	/**
	 * @return the damage this shot causes when hitting a player
	 */
	double getDamage();

	/**
	 * @return the amount of ammo needed to use this weapon
	 */
	int getNeededAmmo();

	/**
	 * @return the player who has fired this shot
	 */
	IAttacker getOwner();

}
