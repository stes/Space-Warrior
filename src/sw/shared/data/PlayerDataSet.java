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
 * Klasse zur Verwaltung von Spielerdaten
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerDataSet implements Comparable<PlayerDataSet>
{
	private static Random _random = new Random();

	private String _name;
	private Point.Double _location;
	private int _lifepoints;
	private int _ammo;
	private double _direction;
	private double _speed;
	private int _score;

	private boolean _isLocal;

	/**
	 * creates a new data record from the given packet
	 * 
	 * @param p
	 *            the packet
	 * @return a new player data-Instance
	 * @throws IllegalArgumentException
	 *             if packet type is wrong
	 */
	public static PlayerDataSet read(Unpacker p)
	{
		if (p.readByte() != Packettype.SNAP_PLAYERDATA)
		{
			throw new IllegalArgumentException();
		}
		String name = p.readUTF();
		boolean lokal = p.readBoolean();

		PlayerDataSet data = new PlayerDataSet(name, lokal);

		data.setPosition(new Point.Double(p.readDouble(), p.readDouble()));
		data.setDirection(p.readDouble());
		data.setScore(p.readShort());

		data.setLifepoints(p.readShort());
		data.setAmmo(p.readShort());

		return data;
	}

	/**
	 * Creates a new Player Data record
	 */
	public PlayerDataSet()
	{
		_lifepoints = GameConstants.MAX_LIVES;
		_ammo = GameConstants.MAX_AMMO;
		_score = 0;
		_location = new Point.Double(0, 0);
	}

	/**
	 * Creates a new Player Data record out of an existing
	 * 
	 * @param dataset
	 *            The source data record which should be copied
	 */
	public PlayerDataSet(PlayerDataSet dataset)
	{
		_name = dataset.getName();
		_isLocal = dataset.local();
		_location = dataset.getPosition();
		_direction = dataset.getDirection();
		_score = dataset.getScore();

		if (_isLocal)
		{
			_lifepoints = dataset.getLifepoints();
			_ammo = dataset.getAmmo();
			_speed = dataset.getSpeed();
		}
	}

	/**
	 * Creates a new Player Data record
	 * 
	 * @param name
	 *            The player's Name
	 * @param local
	 *            true, if player should be local
	 */
	public PlayerDataSet(String name, boolean local)
	{
		this();
		_name = name;
		_isLocal = local;
	}

	/**
	 * Creates a new Player Data record
	 * 
	 * @param name
	 *            The player's name
	 * @param position
	 *            the initial location
	 * @param lokal
	 *            true, if player should be local
	 */
	public PlayerDataSet(String name, Point.Double position, boolean local)
	{
		this(name, local);
		_location = position;
	}

	/**
	 * increases the speed to a constant value
	 */
	public void accelerate(double value)
	{
		this.setSpeed(getSpeed() + value);
	}

	/**
	 * moves the character with the actual speed
	 */
	public void move()
	{
		double b = Math.toRadians(getDirection());
		double x = getSpeed() * Math.sin(b);
		double y = getSpeed() * Math.cos(b);
		this.setPosition(new Point.Double(this.xPosition() + x, this
				.yPosition() + y));
	}

	@Override
	public int compareTo(PlayerDataSet spieler)
	{
		if (_score < spieler.getScore())
			return -1;
		if (_score > spieler.getScore())
			return 1;
		return 0;
	}

	/**
	 * turns the character indicated by the angel anti-clockwise
	 * 
	 * @param angel
	 *            the rotation angle in degrees
	 */
	public void rotate(double winkel)
	{
		setDirection(getDirection() + winkel);
	}

	/**
	 * Indicates the current speed back
	 * 
	 * @return the current speed
	 * @throws IllegalStateException
	 *             if the player is not local
	 */
	public double getSpeed() throws IllegalStateException
	{
		if (_isLocal)
		{
			return _speed;
		}
		else
		{
			throw new IllegalStateException("Player is not local");
		}
	}

	/**
	 * initializes the player data with standard values
	 */
	public void init()
	{
		int rand = GameConstants.PLAYER_SIZE / 2 + 1;
		int x = rand
				+ _random.nextInt(GameConstants.PLAYING_FIELD_WIDTH - rand);
		int y = rand
				+ _random.nextInt(GameConstants.PLAYING_FIELD_HEIGHT - rand);
		_location = new Point.Double(x, y);
		this.setSpeed(0);
		this.setDirection((double) _random.nextInt(360));
		this.setLifepoints(GameConstants.MAX_LIVES);
		this.setAmmo(GameConstants.MAX_AMMO);
	}

	/**
	 * increases the munition
	 */
	public void ladeNach()
	{
		_ammo += (_ammo < GameConstants.MAX_AMMO - 1 ? 1 : 0);
	}

	/**
	 * return the current health
	 * 
	 * @return the current health
	 * @throws IllegalStateException
	 *             if the player is not local
	 */
	public int getLifepoints() throws IllegalStateException
	{
		if (_isLocal)
		{
			return _lifepoints;
		}
		else
		{
			throw new IllegalStateException("Spieler ist nicht lokal");
		}
	}

	/**
	 * @return true, if the player is local
	 */
	public boolean local()
	{
		return _isLocal;
	}

	/**
	 * returns the current munition
	 * 
	 * @return the current munition
	 * @throws IllegalStateException
	 *             if the player is not local
	 */
	public int getAmmo() throws IllegalStateException
	{
		if (_isLocal)
		{
			return _ammo;
		}
		else
		{
			throw new IllegalStateException("Spieler ist nicht lokal");
		}
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
	 * Writes the player data into a packet and returns it
	 * 
	 * @param lokal
	 *            true, although local values ??should be taken
	 * @return the packet
	 */
	public void write(Packer p, boolean lokal)
	{
		p.writeByte(Packettype.SNAP_PLAYERDATA);
		p.writeUTF(this.getName());
		p.writeBoolean(lokal);
		p.writeDouble(this.getPosition().getX());
		p.writeDouble(this.getPosition().getY());
		p.writeDouble(this.getDirection());
		p.writeShort(this.getScore());

		int x = lokal ? 1 : 0;

		p.writeShort(this.getLifepoints() * x);
		p.writeShort(this.getAmmo() * x);
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

	/**
	 * decreases the munition and returns a new object shot
	 * 
	 * @param master
	 *            true, if a master shot will be given
	 */
	public Shot shoot(boolean master)
	{
		int neededAmmo = master ? GameConstants.AMMO_PER_MASTER_SHOT
				: GameConstants.AMMO_PER_SHOT;
		if (_ammo >= neededAmmo)
		{
			_ammo -= neededAmmo;

			double time = GameConstants.SHOT_TTL / 2
					/ ((double) GameConstants.TICK_INTERVAL);
			return new Shot(this.positionAfter(time), _direction, master);
		}
		return null;
	}

	/**
	 * assigns a new value to the health
	 * 
	 * @param value
	 *            new health
	 */
	public void setLifepoints(int wert)
	{
		_lifepoints = wert;
	}

	/**
	 * assigns a new value to the score
	 * 
	 * @param value
	 *            new score
	 */
	public void setScore(int wert)
	{
		_score = wert;
	}

	/**
	 * assigns a new direction
	 * 
	 * @param speed
	 *            new direction in degrees
	 */
	public void setDirection(double value)
	{
		double v = value;
		while (v < 0)
		{
			v += 360;
		}
		_direction = v % 360;
	}

	/**
	 * @return the horizontal position of the player
	 */
	public double xPosition()
	{
		return getPosition().getX();
	}

	/**
	 * @return The vertical position of the player
	 */
	public double yPosition()
	{
		return getPosition().getY();
	}

	/**
	 * assigns a new speed
	 * 
	 * @param speed
	 *            new speed
	 */
	protected void setSpeed(double geschwindigkeit)
	{
		_speed = geschwindigkeit;
		if (_speed < 0)
		{
			_speed = 0;
		}
		else if (_speed > GameConstants.MAX_SPEED)
		{
			_speed = GameConstants.MAX_SPEED;
		}
	}

	/**
	 * assigns a new value to the ammunition
	 * 
	 * @param value
	 *            new value of the ammunition
	 */
	protected void setAmmo(int wert)
	{
		if (wert >= 0 && wert <= GameConstants.MAX_AMMO)
		{
			_ammo = wert;
		}
		else
		{
			throw new IllegalArgumentException("Munition zwischen 0 und "
					+ GameConstants.MAX_AMMO + " waehlen");
		}
	}

	/**
	 * assigns a new value to the position
	 * 
	 * @param value
	 *            new position
	 */
	protected void setPosition(Point.Double wert)
	{
		double x = wert.getX();
		double y = wert.getY();

		if (x + GameConstants.PLAYER_SIZE / 2 > GameConstants.PLAYING_FIELD_WIDTH)
			x = GameConstants.PLAYING_FIELD_WIDTH - GameConstants.PLAYER_SIZE
					/ 2;
		else if (x - GameConstants.PLAYER_SIZE / 2 < 0)
			x = GameConstants.PLAYER_SIZE / 2;
		if (y + GameConstants.PLAYER_SIZE / 2 > GameConstants.PLAYING_FIELD_HEIGHT)
			y = GameConstants.PLAYING_FIELD_HEIGHT - GameConstants.PLAYER_SIZE
					/ 2;
		else if (y - GameConstants.PLAYER_SIZE / 2 < 0)
			y = GameConstants.PLAYER_SIZE / 2;

		_location = new Point.Double(x, y);
	}

	private Point.Double positionAfter(double zeitintervall)
	{
		double weg = zeitintervall * this.getSpeed();
		return new Point.Double(this.getPosition().getX() + weg
				* Math.sin(Math.toRadians(this.getDirection())), this
				.getPosition().getY()
				+ weg
				* Math.cos(Math.toRadians(this.getDirection())));
	}
}
