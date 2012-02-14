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

import sw.shared.data.GameWorld;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * Represents an entity in the game world.
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public abstract class Entity implements IEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6665055916431124020L;
	private boolean _entDestroy;
	private byte _entType;
	private GameWorld _world;
	private int _id;

	/**
	 * creates a new entity with the specified type ID
	 * 
	 * @param type
	 *            the entity's type ID
	 */
	public Entity(byte type)
	{
		_entType = type;
	}

	/**
	 * destroys the entity object
	 */
	@Override
	public void destroy()
	{
		_entDestroy = true;
	}

	/**
	 * creates an entity out of an Unpacker object
	 * 
	 * @param p
	 *            The Unpacker
	 */
	@Override
	public void fromSnap(Unpacker p)
	{
		this.setID(p.readInt());
	}

	/**
	 * @return the ID of this entity
	 */
	@Override
	public int getID()
	{
		return _id;
	}

	/**
	 * @return the entity's type ID
	 */
	@Override
	public byte getMainType()
	{
		return (byte) (this.getType() & 0x0F);
	}

	@Override
	public byte getSubType()
	{
		return (byte) (this.getType() & 0xF0);
	}

	/**
	 * @return the entity's type ID
	 */
	@Override
	public byte getType()
	{
		return _entType;
	}

	/**
	 * @return the GameWorld containing this entity
	 */
	@Override
	public GameWorld getWorld()
	{
		return _world;
	}

	/**
	 * @return true, if this entity is destroyed
	 */
	@Override
	public boolean isDestroyed()
	{
		return _entDestroy;
	}

	// TODO this mustn't be public!
	@Override
	public void setID(int id)
	{
		_id = id;
	}

	/**
	 * Sets the GameWorld in which the entity is integrated
	 * 
	 * @param world
	 *            The GameWorld
	 */
	@Override
	public void setWorld(GameWorld world)
	{
		this._world = world;
	}

	/**
	 * Packs the entity into a Packer object
	 * 
	 * @param p
	 *            The Packer object
	 * @param name
	 *            The entity's name
	 */
	@Override
	public void snap(Packer p, String name)
	{
		p.writeByte(this.getType());
		p.writeInt(this.getID());
	}

	/**
	 * Processes an update step
	 */
	@Override
	public abstract void tick();
}
