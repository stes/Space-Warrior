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
import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.Unpacker;
import sw.shared.data.entities.MoveableEntity;
import sw.shared.data.entities.Shot;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerData extends MoveableEntity implements Comparable<PlayerData>
{
	private static Random _random = new Random();

	private String _name;
	private int _lifepoints;
	private int _ammo;
	private int _score;
	private boolean _alive;
	private long _lastShot;
	private boolean _local;
	private int _imageID;

	private PlayerInput _input;
	
	private PlayerData(byte type)
	{
		super(type);
		// TODO improve?
		this.setAcceleration(GameConstants.ACCELERATION);
		this.setAngularAcceleration(GameConstants.ANGULAR_ACCELERATION);
		this.setMaximumSpeed(GameConstants.MAX_SPEED);
	}
	
	public PlayerData(PlayerData dataset)
	{
		this(Packettype.SNAP_PLAYERDATA);
		_name = new String(dataset.getName());
		_local = dataset.isLocal();
		_score = dataset.getScore();
		_alive = dataset.isAlive();
		this.setX(dataset.getX());
		this.setY(dataset.getY());
		this.setDirection(dataset.getDirection());
		this.setImageID(dataset.getImageID());
		_lifepoints = dataset.getLifepoints();
		_ammo = dataset.getAmmo();
		this.setSpeed(dataset.getSpeed());
		this.setTurnSpeed(dataset.getTurnSpeed());
		_input = dataset._input;
	}

	/**
	 * Creates a new Player Data record
	 */
	public PlayerData(String name)
	{
		this(name, GameConstants.Images.SHIP_3.getID());
	}

	/**
	 * Creates a new Player Data record
	 */
	public PlayerData(String name, int imageID)
	{
		this(Packettype.SNAP_PLAYERDATA);
		_name = name;
		_score = 0;
		_alive = false;
		this.setX(0);
		this.setY(0);
		_lifepoints = GameConstants.MAX_LIVES;
		_ammo = GameConstants.MAX_AMMO;
		_imageID = imageID;
		_input = new PlayerInput();
	}

	/**
	 * increases the speed by a constant value
	 */
	public void accelerate(double value)
	{
		this.setSpeed(getSpeed() + value);
	}

//	public void angularAccelerate(double value)
//	{
//		setTurnSpeed(_turnSpeed + value);
//	}

	public void angularDecelerate(double value)
	{
		double dec = Math.abs(value);
		if (getTurnSpeed() < 0 && Math.abs(getTurnSpeed()) > dec)
		{
			setTurnSpeed(getTurnSpeed() + dec);
		}
		else if (getTurnSpeed() > 0 && Math.abs(getTurnSpeed()) > dec)
		{
			setTurnSpeed(getTurnSpeed() - dec);
		}
		else
		{
			setTurnSpeed(0);
		}
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

	public void die()
	{
		_alive = false;
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		_name = p.readUTF();
		_local = p.readBoolean();
		_score = p.readShort();
		_alive = p.readBoolean();
		this.setX(p.readDouble());
		this.setY(p.readDouble());
		this.setDirection(p.readDouble());
		this.setImageID(p.readInt());
		_lifepoints = p.readShort();
		_ammo = p.readShort();
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

	public int getImageID()
	{
		return _imageID;
	}

	public long getLastShot()
	{
		return _lastShot;
	}

	public int getLifepoints()
	{
		return _lifepoints;
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
	 * returns the current score
	 * 
	 * @return the current score
	 */
	public int getScore()
	{
		return _score;
	}

	public boolean intersects(PlayerData d)
	{
		if (d == null || this.equals(d))
			return false;
		double diff = this.getPosition().distance(d.getPosition());
		return diff < GameConstants.MAX_COLLISION_DAMAGE_RANGE;
	}

	public boolean isAlive()
	{
		return _alive;
	}

	/**
	 * @return true, if the player is local
	 */
	public boolean isLocal()
	{
		return _local;
	}

	public boolean isReadyToShoot()
	{
		return Math.abs(_lastShot - System.currentTimeMillis()) >= GameConstants.MAX_SHOT_INTERVAL;
	}

	/**
	 * moves the character with the actual speed
	 */
	@Override
	public void move()
	{
		boolean intersects = false;
		PlayerData pred = this.predict();
		for (PlayerData d : getWorld().getPlayers())
		{
			if (!this.equals(d) && d.intersects(pred) && d.isAlive())
				intersects = true;
		}
		if (_alive)
		{
			if (!intersects)
			{
				super.move();
			}
			else
			{
				this.takeDamage((int) (GameConstants.MAX_COLLISION_DAMAGE * this.getSpeed() / this.getMaximumSpeed()));
				this.setSpeed(this.getSpeed()/2);
				this.rotate(getTurnSpeed());
			}
		}
	}

	public PlayerData predict()
	{
		PlayerData d = new PlayerData(this);
		if (_alive)
		{
			double x = d.getSpeed() * Math.sin(d.getDirection());
			double y = d.getSpeed() * Math.cos(d.getDirection());
			d.setX(d.getX() + x);
			d.setY(d.getY() + y);
			d.rotate(getTurnSpeed());
		}
		return d;
	}

	/**
	 * increases the munition
	 */
	public void reload()
	{
		_ammo += (_ammo < GameConstants.MAX_AMMO - 1 ? 1 : 0);
	}

	/**
	 * initializes the player data with standard values
	 */
	public void respawn()
	{
		int rand = GameConstants.PLAYER_SIZE / 2 + 1;
		int x = rand + _random.nextInt((int)MoveableEntity.MAX_X - rand);
		int y = rand + _random.nextInt((int)MoveableEntity.MAX_Y - rand);
		setX(x);
		setY(y);
		setSpeed(0);
		setDirection(_random.nextDouble() * 2 * Math.PI);
		_lifepoints = GameConstants.MAX_LIVES;
		_ammo = GameConstants.MAX_AMMO;
		_alive = true;
		setTurnSpeed(0);
	}

	/**
	 * turns the character indicated by the angel anti-clockwise
	 * 
	 * @param angle
	 *            the rotation angle in degrees
	 */
	public void rotate(double angle)
	{
		setDirection(getDirection() + angle);
	}

	public void setImageID(int id)
	{
		_imageID = id;
	}

	public void setInput(PlayerInput input)
	{
		_input = input;
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

	public void shoot(boolean master)
	{
		int neededAmmo = master ? GameConstants.AMMO_PER_MASTER_SHOT : GameConstants.AMMO_PER_SHOT;
		if (_ammo >= neededAmmo && this.isReadyToShoot())
		{
			_ammo -= neededAmmo;
			_lastShot = System.currentTimeMillis();
			double time = GameConstants.SHOT_TTL / 2 / ((double) GameConstants.TICK_INTERVAL);
			Shot s = new Shot(this.positionAfter(time), getDirection(), master);
			this.getWorld().insert(s);
			s.fire(this);
		}
	}

	@Override
	public void snap(Packer p, String name)
	{
		boolean local = this.getName().equals(name);
		p.writeByte(this.getType());
		p.writeUTF(_name);
		p.writeBoolean(local);
		p.writeShort(_score);
		p.writeBoolean(_alive);
		p.writeDouble(getX());
		p.writeDouble(getY());
		p.writeDouble(getDirection());
		p.writeInt(_imageID);

		int l = local ? 1 : 0;
		p.writeShort(_lifepoints * l);
		p.writeShort(_ammo * l);
	}

	public void takeDamage(int dmg)
	{
		_lifepoints -= dmg;
		if (_lifepoints <= 0)
		{
			this.die();
		}
	}

	@Override
	public void tick()
	{
		if (!this.isAlive())
			return;

		if (_input.shot() > 0)
		{
			this.shoot(_input.shot() == 2);
		}

		if (_input.moveDirection() == 0)
		{
			this.accelerate(-this.getAcceleration());
		}
		else
		{
			this.accelerate(this.getAcceleration() * _input.moveDirection());
		}

		if (_input.turnDirection() == 0)
		{
			this.angularDecelerate(this.getAngularAcceleration());
		}
		else
		{
			this.angularAccelerate(this.getAngularAcceleration() * _input.turnDirection());
		}

		this.reload();
		this.move();
	}

	private Point.Double positionAfter(double time)
	{
		double way = time * getSpeed();
		return new Point.Double(getX() + way * Math.sin(getDirection()), getY()
				+ way * Math.cos(getDirection()));
	}

//	/**
//	 * assigns a new value to the position
//	 * 
//	 * @param value
//	 *            new position
//	 */
//	private void setPosition(Point.Double value)
//	{
//		double x = value.getX();
//		double y = value.getY();
//
//		if (x + GameConstants.PLAYER_SIZE / 2 > GameConstants.PLAYING_FIELD_WIDTH)
//			x = GameConstants.PLAYING_FIELD_WIDTH - GameConstants.PLAYER_SIZE / 2;
//		else if (x - GameConstants.PLAYER_SIZE / 2 < 0)
//			x = GameConstants.PLAYER_SIZE / 2;
//		if (y + GameConstants.PLAYER_SIZE / 2 > GameConstants.PLAYING_FIELD_HEIGHT)
//			y = GameConstants.PLAYING_FIELD_HEIGHT - GameConstants.PLAYER_SIZE / 2;
//		else if (y - GameConstants.PLAYER_SIZE / 2 < 0)
//			y = GameConstants.PLAYER_SIZE / 2;
//
//		_location = new Point.Double(x, y);
//	}

	public void angularAccelerate(double value)
	{
		setTurnSpeed(getTurnSpeed() + value);
	}
}
