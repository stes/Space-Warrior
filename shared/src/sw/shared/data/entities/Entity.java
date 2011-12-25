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
package sw.shared.data.entities;

import sw.shared.data.GameWorld;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public abstract class Entity implements IEntity
{
	private boolean _entDestroy;
	private byte _entType;
	private GameWorld _world;

	public Entity(byte type)
	{
		_entType = type;
	}

	public void destroy()
	{
		_entDestroy = true;
	}

	public abstract void fromSnap(Unpacker p);

	public byte getType()
	{
		return _entType;
	}

	public GameWorld getWorld()
	{
		return _world;
	}

	public boolean isDestroyed()
	{
		return _entDestroy;
	}

	public void setWorld(GameWorld world)
	{
		this._world = world;
	}

	public abstract void snap(Packer p, String name);

	public abstract void tick();
}