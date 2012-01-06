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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import sw.shared.Packettype;
import sw.shared.data.entities.Entity;
import sw.shared.data.entities.GameState;
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
	private HashMap<Integer, IEntity> _entities;

	/**
	 * Creates a new, empty GameWorld
	 */
	public GameWorld()
	{
		_entities = new HashMap<Integer, IEntity>();
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
		HashMap<Integer, IEntity> tmp = new HashMap<Integer, IEntity>();

		int size = p.readInt();
		for (int i = 0; i < size; i++)
		{
			IEntity newEnt;
			// TODO move this somewhere else?
			int val = p.readByte();
			int type = val & 0x0F;
			int subtype = val & 0xF0;
			if (type == Packettype.SNAP_GAMESTATE)
			{
				newEnt = new GameState();
			}
			else if (type == Packettype.SNAP_PLAYERDATA)
			{
				newEnt = new SpaceShip("");
			}
			else if (type == Packettype.SNAP_SHOT)
			{
				switch (subtype)
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
				System.out.println("Error: unknown snap item (" + type + ")");
				return;
			}
			newEnt.fromSnap(p);
			tmp.put(newEnt.getID(), newEnt);
		}

		_entities = tmp;
	}

	/**
	 * @return an array of all entities in this game world
	 */
	public IEntity[] getAllEntities()
	{
		return _entities.values().toArray(new Entity[] {});
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
		for (IEntity ent : _entities.values())
		{
			if (ent.getMainType() == type)
			{
				tmp.add(ent);
			}
		}
		return tmp.toArray(a);
	}

	public IEntity getEntityByID(Integer ID)
	{
		return _entities.get(ID);
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
		e.setID(this.nextID());
		_entities.put(e.getID(), e);
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

		for (IEntity ent : _entities.values())
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
		for (Iterator<IEntity> i = _entities.values().iterator(); i.hasNext();)
		{
			while (i.hasNext() && i.next().isDestroyed())
			{
				i.remove();
			}
		}

		Integer[] keys = _entities.keySet().toArray(new Integer[] {});
		for (int i = 0; i < keys.length; i++)
		{
			_entities.get(keys[i]).tick();
		}
	}

	private Integer nextID()
	{
		int i = 0;
		for (; _entities.containsKey(i); i++)
		{
			;
		}
		return i;
	}
}
