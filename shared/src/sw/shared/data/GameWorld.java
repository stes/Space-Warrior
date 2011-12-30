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
package sw.shared.data;

import java.util.Iterator;
import java.util.Vector;

import sw.shared.Packettype;
import sw.shared.data.entities.Entity;
import sw.shared.data.entities.IEntity;
import sw.shared.data.entities.players.SpaceShip;
import sw.shared.data.entities.shots.IShot;
import sw.shared.data.entities.shots.LaserBeam;
import sw.shared.data.entities.shots.MGProjectile;
import sw.shared.data.entities.shots.Rocket;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * Represents the world which contains the various kinds of entities
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class GameWorld
{
	private Vector<IEntity> _entities;

	/**
	 * Creates a new, empty GameWorld
	 */
	public GameWorld()
	{
		_entities = new Vector<IEntity>();
	}

	/**
	 * Removes all entities
	 */
	public void clear()
	{
		_entities.clear();
	}

	/**
	 * Derives a GameWorld from a snapshot
	 * 
	 * @param p
	 *            The Unpacker representing the snapshot
	 */
	public void fromSnap(Unpacker p)
	{
		Vector<IEntity> tmp = new Vector<IEntity>();

		int size = p.readInt();
		for (int i = 0; i < size; i++)
		{
			IEntity newEnt;
			// TODO move this somewhere else?
			byte type = p.readByte();
			if ((type & 0x0F) == Packettype.SNAP_PLAYERDATA)
			{
				newEnt = new SpaceShip("");
			}
			else if ((type & 0x0F) == Packettype.SNAP_SHOT)
			{
				switch (type & 0xF0)
				{
					case IShot.LASER:
						newEnt = new LaserBeam(0, 0, 0, null);
						break;
					case IShot.MASTER_LASER:
						newEnt = new LaserBeam(0, 0, 0, null);
						break;
					case IShot.ROCKET:
						newEnt = new Rocket(0, 0, 0, null);
						break;
					case IShot.MG:
						newEnt = new MGProjectile(0, 0, 0, null);
						break;
					default:
						return;
				}
			}
			else
			{
				return;
			}
			newEnt.fromSnap(p);
			tmp.add(newEnt);
		}

		_entities = tmp;
	}

	private Integer nextID()
	{
		return 0;
	}

	/**
	 * @return an array of all entities in this game world
	 */
	public IEntity[] getAllEntities()
	{
		return _entities.toArray(new Entity[]{});
	}

	/**
	 * Returns all entities of the specified type Note that the vector's content
	 * is based on the specified entity ID by the parameter "type", not by the
	 * type of the returned array
	 * 
	 * @param <T>
	 *            The entity type
	 * @param type
	 *            The type id
	 * @param a
	 *            An arbitrary array (may even be empty) which specifies the
	 *            type of the returned array
	 * @return All entities matching the specified ID
	 */
	public <T> T[] getEntitiesByType(byte type, T[] a)
	{
		Vector<IEntity> tmp = new Vector<IEntity>();
		for (IEntity ent : _entities)
		{
			if (ent.getMainType() == type)
			{
				tmp.add(ent);
			}
		}
		return tmp.toArray(a);
	}

	/**
	 * @return all player entities
	 */
	public SpaceShip[] getPlayers()
	{
		return this.getEntitiesByType(Packettype.SNAP_PLAYERDATA, new SpaceShip[] {});
	}

	/**
	 * Inserts the specified entity
	 * 
	 * @param e
	 *            The entity to integrate in this game world
	 */
	public void insert(IEntity e)
	{
		e.setWorld(this);
		_entities.add(e);
	}

	/**
	 * Removes the specified entity from this game world
	 * 
	 * @param e
	 *            The entity to remove from this game world
	 */
	public void remove(IEntity e)
	{
		_entities.remove(e.getID());
	}

	// TODO name?
	/**
	 * Packs this game world into a Packer instance
	 * 
	 * @param p
	 *            The Packer instance
	 * @param name
	 *            The name
	 */
	public void snap(Packer p, String name)
	{
		p.writeInt(_entities.size());

		for (IEntity ent : _entities)
		{
			ent.snap(p, name);
		}
	}

	/**
	 * Processes one update step in this game world Calls all update methods in
	 * the entities in this game world
	 */
	public void tick()
	{
		for (Iterator<IEntity> i = _entities.iterator(); i.hasNext();)
		{
			while (i.hasNext() && i.next().isDestroyed())
			{
				i.remove();
			}
		}

		for (int i = 0; i < _entities.size(); i++)
		{
			_entities.get(i).tick();
		}
	}
}
