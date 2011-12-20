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

import java.awt.Point;
import java.util.Random;

import sw.shared.GameConstants;
import sw.shared.Packettype;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerData implements Comparable<PlayerData>
{
	private static Random _random = new Random();

	private String _name;
	private Point.Double _location;
	private int _lifepoints;
	private int _ammo;
	private double _direction;
	private double _turnSpeed;
	private double _speed;
	private int _score;
	private boolean _alive;
	private long _lastShot;
	private boolean _local;
	private int _imageID;

	/**
	 * creates a new data record from the given packet
	 * 
	 * @param p
	 *            the packet
	 * @return a new player data-Instance
	 * @throws IllegalArgumentException
	 *             if packet type is wrong
	 */
	public static PlayerData fromSnapshot(Unpacker p)
	{
		if (p.readByte() != Packettype.SNAP_PLAYERDATA)
		{
			throw new IllegalArgumentException();
		}
		
		PlayerData data = new PlayerData(p.readUTF());
		
		data._local = p.readBoolean();
		data._score = p.readShort();
		data._alive = p.readBoolean();
		data.setPosition(new Point.Double(p.readDouble(), p.readDouble()));
		data._direction = p.readDouble();
		data.setImageID(p.readInt());
		data._lifepoints = p.readShort();
		data._ammo = p.readShort();
		
		return data;
	}

	/**
	 * Creates a new Player Data record
	 */
	public PlayerData(String name)
	{
		this(name, GameConstants.Images.SHIP_3.getID());
	}
	
	public PlayerData(PlayerData dataset)
	{
		_name = new String(dataset.getName());
		_local = dataset.isLocal();
		_location = new Point.Double(dataset.getPosition().getX(), dataset.getPosition().getY());
		_direction = dataset.getDirection();
		_score = dataset.getScore();

		if (_local)
		{
			_lifepoints = dataset.getLifepoints();
			_ammo = dataset.getAmmo();
			_speed = dataset.getSpeed();
		}
	}
	
	private double getSpeed()
	{
		return _speed;
	}

	/**
	 * Creates a new Player Data record
	 */
	public PlayerData(String name, int imageID)
	{
		_name = name;
		_lifepoints = GameConstants.MAX_LIVES;
		_ammo = GameConstants.MAX_AMMO;
		_score = 0;
		_location = new Point.Double(0, 0);
		_alive = false;
		_imageID = imageID;
	}
	
	@Override
	public int compareTo(PlayerData player)
	{
		if (_score < player.getScore())
			return -1;
		if (_score > player.getScore())
			return 1;
		return 0;
	}
	
	/**
	 * initializes the player data with standard values
	 */
	public void respawn()
	{
		int rand = GameConstants.PLAYER_SIZE / 2 + 1;
		int x = rand + _random.nextInt(GameConstants.PLAYING_FIELD_WIDTH - rand);
		int y = rand + _random.nextInt(GameConstants.PLAYING_FIELD_HEIGHT - rand);
		_location = new Point.Double(x, y);
		_speed = 0;
		_direction = _random.nextDouble() * 2 * Math.PI;
		_lifepoints = GameConstants.MAX_LIVES;
		_ammo = GameConstants.MAX_AMMO;
		_alive = true;
		_turnSpeed = 0;
	}
	
	public void die()
	{
		_alive = false;
	}
	
	/**
	 * Writes the player data into a packet and returns it
	 * 
	 * @param lokal
	 *            true, although local values ??should be taken
	 * @return the packet
	 */
	public void snap(Packer p, String name)
	{
		boolean local = this.getName().equals(name);
		p.writeByte(Packettype.SNAP_PLAYERDATA);
		p.writeUTF(_name);
		p.writeBoolean(local);
		p.writeShort(_score);
		p.writeBoolean(_alive);
		p.writeDouble(_location.getX());
		p.writeDouble(_location.getY());
		p.writeDouble(_direction);
		p.writeInt(_imageID);

		int l = local ? 1 : 0;
		p.writeShort(_lifepoints * l);
		p.writeShort(_ammo * l);
	}

	/**
	 * moves the character with the actual speed
	 */
	public void move()
	{
		if(_alive)
		{
			double x = _speed * Math.sin(getDirection());
			double y = _speed * Math.cos(getDirection());
			this.setPosition(new Point.Double(_location.getX() + x, _location.getY() + y));
			this.rotate(_turnSpeed);
		}
	}

	public void angularAccelerate(double value)
	{
		setTurnSpeed(_turnSpeed + value);
	}
	
	public void setTurnSpeed(double value)
	{
		_turnSpeed = value;
		if (_turnSpeed > GameConstants.MAX_ANGULAR_SPEED)
		{
			_turnSpeed = GameConstants.MAX_ANGULAR_SPEED;
		}
		else if (_turnSpeed < -GameConstants.MAX_ANGULAR_SPEED)
		{
			_turnSpeed = -GameConstants.MAX_ANGULAR_SPEED;
		}
		
	}
	
	public void angularDecelerate(double value)
	{
		double dec = Math.abs(value);
		if (_turnSpeed < 0 && Math.abs(_turnSpeed) > dec)
		{
			setTurnSpeed(_turnSpeed + dec);
		}
		else if (_turnSpeed > 0 && Math.abs(_turnSpeed) > dec)
		{
			setTurnSpeed(_turnSpeed - dec);
		}
		else
		{
			setTurnSpeed(0);
		}
		
	}
	
	/**
	 * turns the character indicated by the angel anti-clockwise
	 * 
	 * @param angle
	 *            the rotation angle in degrees
	 */
	public void rotate(double angle)
	{
		setDirection(_direction + angle);
	}
	
	/**
	 * increases the speed by a constant value
	 */
	public void accelerate(double value)
	{
		this.setSpeed(getSpeed() + value);
	}

	public void setSpeed(double value)
	{
		_speed = Math.max(0, Math.min(GameConstants.MAX_SPEED, value));
	}
	
	/**
	 * increases the munition
	 */
	public void reload()
	{
		_ammo += (_ammo < GameConstants.MAX_AMMO - 1 ? 1 : 0);
	}

	/**
	 * decreases the munition and returns a new object shot
	 * 
	 * @param master
	 *            true, if a master shot will be given
	 */
	public Shot shoot(boolean master)
	{
		int neededAmmo = master ? GameConstants.AMMO_PER_MASTER_SHOT : GameConstants.AMMO_PER_SHOT;
		if (_ammo >= neededAmmo && this.isReadyToShoot())
		{
			_ammo -= neededAmmo;
			_lastShot = System.currentTimeMillis();
			double time = GameConstants.SHOT_TTL / 2 / ((double) GameConstants.TICK_INTERVAL);
			return new Shot(this.positionAfter(time), _direction, master);
		}
		return null;
	}
	
	public void takeDamage(int dmg)
	{
		_lifepoints -= dmg;
		if(_lifepoints <= 0)
		{
			this.die();
		}
	}

	/**
	 * assigns a new value to the score
	 * 
	 * @param value
	 *            new score
	 */
	public void setScore(int value)
	{
		_score = value;
	}

	public boolean isAlive()
	{
		return _alive;
	}
	
	/**
	 * assigns a new value to the position
	 * 
	 * @param value
	 *            new position
	 */
	private void setPosition(Point.Double value)
	{
		double x = value.getX();
		double y = value.getY();

		if (x + GameConstants.PLAYER_SIZE / 2 > GameConstants.PLAYING_FIELD_WIDTH)
			x = GameConstants.PLAYING_FIELD_WIDTH - GameConstants.PLAYER_SIZE / 2;
		else if (x - GameConstants.PLAYER_SIZE / 2 < 0)
			x = GameConstants.PLAYER_SIZE / 2;
		if (y + GameConstants.PLAYER_SIZE / 2 > GameConstants.PLAYING_FIELD_HEIGHT)
			y = GameConstants.PLAYING_FIELD_HEIGHT - GameConstants.PLAYER_SIZE / 2;
		else if (y - GameConstants.PLAYER_SIZE / 2 < 0)
			y = GameConstants.PLAYER_SIZE / 2;

		_location = new Point.Double(x, y);
	}

	private Point.Double positionAfter(double time)
	{
		double way = time * _speed;
		return new Point.Double(_location.getX() + way * Math.sin(_direction),
				_location.getY() + way * Math.cos(_direction));
	}
	
	/**
	 * @return true, if the player is local
	 */
	public boolean isLocal()
	{
		return _local;
	}
	
	public void setDirection(double direction)
	{
		_direction = direction % (2 * Math.PI);
	}
	
	/**
	 * returns the name of the player
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * returns the current munition
	 * 
	 * @return the current munition
	 */
	public int getAmmo()
	{
		return _ammo;
	}
	
	/**
	 * @return the position of the player
	 */
	public Point.Double getPosition()
	{
		return _location;
	}

	/**
	 * returns the current score
	 * 
	 * @return the current score
	 */
	public int getScore()
	{
		return _score;
	}

	/**
	 * returns the current direction
	 * 
	 * @returns the current direction in degrees
	 */
	public double getDirection()
	{
		return _direction;
	}
	
	public int getLifepoints()
	{
		return _lifepoints;
	}
	
	public long getLastShot()
	{
		return _lastShot;
	}
	
	public double getTurnSpeed()
	{
		return _turnSpeed;
	}
	
	public boolean isReadyToShoot()
	{
		return Math.abs(_lastShot-System.currentTimeMillis()) >= GameConstants.MAX_SHOT_INTERVAL;
	}

	public int getImageID()
	{
		return _imageID;
	}
	
	public void setImageID(int id)
	{
		_imageID = id;
	}
	
}
