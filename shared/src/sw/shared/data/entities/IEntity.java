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
package sw.shared.data.entities;

import java.io.Serializable;

import sw.shared.data.GameWorld;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public interface IEntity extends Serializable
{
	public void destroy();

	public void fromSnap(Unpacker p);

	public int getID();

	public byte getMainType();

	public byte getSubType();

	public byte getType();

	public GameWorld getWorld();

	public boolean isDestroyed();

	public void setID(int id);

	public void setWorld(GameWorld world);

	public abstract void snap(Packer p, String name);

	public abstract void tick();
}
