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
package sw.shared.data.entities.players;

import java.awt.Dimension;

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.Tools;
import sw.shared.data.PlayerInput;
import sw.shared.data.entities.IImageEntity;
import sw.shared.data.entities.IStaticEntity;
import sw.shared.data.entities.MoveableEntity;
import sw.shared.data.entities.shots.IWeapon;
import sw.shared.data.entities.shots.IWeapon.WeaponType;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SpaceShip extends MoveableEntity implements Comparable<SpaceShip>, IDamageable,
		IAttacker, IImageEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7605634024138964210L;
	private String _name;
	private int _lifepoints;
	private int _ammo;
	private int _score;
	private boolean _alive;
	private long _lastShot;
	private boolean _local;
	private int _imageID;

	private double _acceleration;
	private double _angularAcceleration;

	private PlayerInput _input;

	public SpaceShip(SpaceShip dataset)
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
	public SpaceShip(String name)
	{
		this(name, GameConstants.Images.SHIP_3.getID());
	}

	/**
	 * Creates a new Player Data record
	 */
	public SpaceShip(String name, int imageID)
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

	public SpaceShip(String name, int imageID, boolean local)
	{
		this(name, imageID);
		_local = local;
	}
	
	private SpaceShip(byte type)
	{
		super(type);
	}

	/**
	 * increases the speed by a constant value
	 */
	public void accelerate(double value)
	{
		this.setSpeed(this.getSpeed() + value);
	}

	public void angularAccelerate(double value)
	{
		this.setTurnSpeed(this.getTurnSpeed() + value);
	}

	public void angularDecelerate(double value)
	{
		double dec = Math.abs(value);
		if (this.getTurnSpeed() < 0 && Math.abs(this.getTurnSpeed()) > dec)
		{
			this.setTurnSpeed(this.getTurnSpeed() + dec);
		}
		else if (this.getTurnSpeed() > 0 && Math.abs(this.getTurnSpeed()) > dec)
		{
			this.setTurnSpeed(this.getTurnSpeed() - dec);
		}
		else
		{
			this.setTurnSpeed(0);
		}
	}

	@Override
	public int compareTo(SpaceShip player)
	{
		if (_score < player.getScore())
		{
			return -1;
		}
		if (_score > player.getScore())
		{
			return 1;
		}
		return 0;
	}

	public void die()
	{
		_alive = false;
		this.setScore(this.getScore() - 1);
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		super.fromSnap(p);
		_name = p.readUTF();
		_local = p.readBoolean();
		_score = p.readShort();
		_alive = p.readBoolean();
		this.setImageID(p.readInt());
		_lifepoints = p.readShort();
		_ammo = p.readShort();
	}

	@Override
	public double getAcceleration()
	{
		return _acceleration;
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

	@Override
	public double getAngularAcceleration()
	{
		return _angularAcceleration;
	}

	@Override
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

	@Override
	public double getMaximumSpeed()
	{
		return GameConstants.MAX_SPEED;
	}

	/**
	 * returns the name of the player
	 * 
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return _name;
	}

	/**
	 * returns the current score
	 * 
	 * @return the current score
	 */
	@Override
	public int getScore()
	{
		return _score;
	}

	@Override
	public Dimension getSize()
	{
		return new Dimension(GameConstants.PLAYER_SIZE, GameConstants.PLAYER_SIZE);
	}

	public boolean intersects(SpaceShip d)
	{
		if (d == null || this.equals(d))
		{
			return false;
		}
		double diff = this.getPosition().distance(d.getPosition());
		return diff < GameConstants.MAX_COLLISION_DAMAGE_RANGE;
	}

	@Override
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
	 * moves the spaceship
	 */
	@Override
	public void move()
	{
		boolean intersects = false;
		SpaceShip pred = this.predict();
		for (SpaceShip d : this.getWorld().getPlayers())
		{
			if (!this.equals(d) && d.intersects(pred) && d.isAlive())
			{
				intersects = true;
			}
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
				this.setSpeed(this.getSpeed() / 2);
				this.rotate(this.getTurnSpeed());
			}
		}
	}

	public SpaceShip predict()
	{
		return this.predict(1);
	}

	public SpaceShip predict(int ticks)
	{
		SpaceShip d = new SpaceShip(this);
		for (int i = 0; i < ticks; i++)
		{
			if (_alive)
			{
				double x = -d.getSpeed() * Math.sin(d.getDirection());
				double y = -d.getSpeed() * Math.cos(d.getDirection());
				d.setX(d.getX() + x);
				d.setY(d.getY() + y);
				d.rotate(this.getTurnSpeed());
			}
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
		int x = rand + Tools.getRandom().nextInt((int) IStaticEntity.MAX_X - rand);
		int y = rand + Tools.getRandom().nextInt((int) IStaticEntity.MAX_Y - rand);
		this.setX(x);
		this.setY(y);
		this.setSpeed(0);
		this.setDirection(Tools.getRandom().nextDouble() * 2 * Math.PI);
		_lifepoints = GameConstants.MAX_LIVES;
		_ammo = GameConstants.MAX_AMMO;
		_alive = true;
		this.setTurnSpeed(0);
	}

	/**
	 * turns the character indicated by the angel anti-clockwise
	 * 
	 * @param angle
	 *            the rotation angle in degrees
	 */
	public void rotate(double angle)
	{
		this.setDirection(this.getDirection() + angle);
	}

	public void setImageID(int id)
	{
		_imageID = id;
	}

	public void setInput(PlayerInput input)
	{
		_input = input;
	}
	
	public PlayerInput getInput()
	{
		return _input;
	}

	/**
	 * assigns a new value to the score
	 * 
	 * @param value
	 *            new score
	 */
	@Override
	public void setScore(int value)
	{
		_score = value;
	}

	public void shoot(int id)
	{
		IWeapon s = null;
		if (this.isReadyToShoot())
		{
			// TODO improve the prediction
			int time = 0;
			if (id == WeaponType.LASER.getID() || id == WeaponType.MASTER_LASER.getID())
				time = (int) (GameConstants.SHOT_TTL * 0.35);
			SpaceShip pred = this.predict(time);
			try
			{
				s = WeaponType.getWeaponType(id).createInstance(pred.getX(),
						pred.getY(),
						this.getDirection(),
						this);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (_ammo >= s.getNeededAmmo())
			{
				_ammo -= s.getNeededAmmo();
				_lastShot = System.currentTimeMillis();
				this.getWorld().insert(s);
				s.fire();
			}
		}
	}

	@Override
	public void snap(Packer p, String name)
	{
		super.snap(p, name);
		boolean local = this.getName().equals(name);
		p.writeUTF(_name);
		p.writeBoolean(local);
		p.writeShort(_score);
		p.writeBoolean(_alive);
		p.writeInt(_imageID);

		int l = local ? 1 : 0;
		p.writeShort(_lifepoints * l);
		p.writeShort(_ammo * l);
	}

	@Override
	public void takeDamage(double d)
	{
		_lifepoints -= d;
		if (_lifepoints <= 0)
		{
			this.die();
		}
	}

	@Override
	public void tick()
	{
		if (!this.isAlive())
		{
			return;
		}

		if (_input.shot() > 0)
		{
			this.shoot(_input.shot());
		}

		if (_input.moveDirection() == 0)
		{
			this.setAcceleration(-MoveableEntity.ACCELERATION);
		}
		else
		{
			this.setAcceleration(MoveableEntity.ACCELERATION * _input.moveDirection());
		}

		this.setAngularAcceleration(MoveableEntity.ANGULAR_ACCELERATION * _input.turnDirection());
		if (_input.turnDirection() == 0)
		{
			this.angularDecelerate(MoveableEntity.ANGULAR_ACCELERATION);
		}

		this.reload();
		super.tick();
	}

	private void setAcceleration(double value)
	{
		_acceleration = value;
	}

	private void setAngularAcceleration(double value)
	{
		_angularAcceleration = value;
	}
}
