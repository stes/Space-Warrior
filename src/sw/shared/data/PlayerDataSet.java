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
	private int _points;
	
	private boolean _isLocal;

	/**
	* creates a new data record from the given packet
	* 
	* @param p the packet
	* @return a new player data-Instance
	* @throws IllegalArgumentException if packet type is wrong
	*/
	public static PlayerDataSet hole(Unpacker p)
	{
		if (p.readByte() != Packettype.SNAP_SPIELERDATEN)
			throw new IllegalArgumentException();
		String name = p.readUTF();
		boolean lokal = p.readBoolean();
		
		PlayerDataSet daten = new PlayerDataSet(name, lokal);
		
		daten.setzePosition(new Point.Double(p.readDouble(), p.readDouble()));
		daten.setzeRichtung(p.readDouble());
		daten.setzePunkte(p.readInt());
		
		daten.setzeLeben(p.readInt());
		daten.setzeMunition(p.readInt());

		return daten;
	}
	
	/**
	* Creates a new Player Data record
	*/
	public PlayerDataSet()
	{
		_lifepoints = GameConstants.MAX_LIVES;
		_ammo = GameConstants.MAX_AMMO;
		_points = 0;
		_location = new Point.Double(0, 0);
	}
	
	/**
	* Creates a new Player Data record out of an existing
	* 
	* @param dataset The source data record which should be copied
	*/
	public PlayerDataSet(PlayerDataSet dataset)
	{
		_name = dataset.name();
		_isLocal = dataset.lokal();
		_location = dataset.position();
		_direction = dataset.richtung();
		_points = dataset.punkte();
		
		if (_isLocal)
		{
			_lifepoints = dataset.leben();
			_ammo = dataset.munition();
			_speed = dataset.geschwindigkeit();
		}
	}
	
	/**
	* Creates a new Player Data record
	* 
	* @param name The player's Name
	* @param local true, if player should be local
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
	* @param name The player's name
	* @param position the initial location
	* @param lokal true, if player should be local
	*/
	public PlayerDataSet(String name, Point position, boolean local) 
	{
		this(name, local);
		_location = new Point.Double(position.x, position.y);
	}

	/**
	* increases the speed to a constant value
	*/
	public void beschleunige(double betrag) 
	{
		setzeGeschwindigkeit(geschwindigkeit() + betrag);
	}
	
	/**
	* moves the character with the actual speed
	*/
	public void bewege() 
	{
		double b = Math.toRadians(richtung());
		int x = (int)(geschwindigkeit() * Math.sin(b));
		int y = (int)(geschwindigkeit() * Math.cos(b));
		this.setzePosition(new Point.Double(this.xPosition() + x, this.yPosition() + y));
	}
	
	@Override
	public int compareTo(PlayerDataSet spieler)
	{
		if(_points < spieler.punkte())
			return -1;
		if(_points > spieler.punkte())
			return 1;
		return 0;
	}
	
	/**
	* turns the character indicated by the angel anti-clockwise
	* 
	* @param angel the rotation angle in degrees
	*/
	public void dreheUm(double winkel) 
	{
		setzeRichtung(richtung() + winkel);
	}
	
	/**
	* Indicates the current speed back
	* 
	* @return the current speed
	* @throws IllegalStateException if the player is not local
	*/
	public double geschwindigkeit() throws IllegalStateException
	{
		if (_isLocal)
			return _speed;
		else
			throw new IllegalStateException("Spieler ist nicht lokal");
	}

	/**
	* initializes the player data with standard values
	*/
	public void init() 
	{
		int rand = GameConstants.PLAYER_SIZE/2+1;
		int x = rand + _random.nextInt(GameConstants.PLAYING_FIELD_WIDTH - rand);
		int y = rand + _random.nextInt(GameConstants.PLAYING_FIELD_HEIGHT - rand);
		_location = new Point.Double(x, y);
		setzeGeschwindigkeit(0);
		setzeRichtung(_random.nextInt(360));
		setzeLeben(GameConstants.MAX_LIVES);
		setzeMunition(GameConstants.MAX_AMMO);
	}
	
	/**
	* increases the munition
	*/
	public void ladeNach() 
	{
		_ammo += (_ammo < GameConstants.MAX_AMMO-1 ? 1 : 0);
	}

	/**
	* return the current health
	* 
	* @return the current health
	* @throws IllegalStateException if the player is not local
	*/
	public int leben() throws IllegalStateException
	{
		if (_isLocal)
			return _lifepoints;
		else
			throw new IllegalStateException("Spieler ist nicht lokal");
	}
	
	/**
	* @return true, if the player is local
	*/
	public boolean lokal()
	{
		return _isLocal;
	}
	
	/**
	* returns the current munition
	* 
	* @return the current munition
	* @throws IllegalStateException if the player is not local
	*/
	public int munition() throws IllegalStateException
	{
		if (_isLocal)
			return _ammo;
		else
			throw new IllegalStateException("Spieler ist nicht lokal");
	}
	
	/**
	* returns the name of the player
	* 
	* @return the name
	*/
	public String name()
	{
		return _name;
	}
	
	/**
	* Writes the player data into a packet and returns it
	* 
	* @param lokal true, although local values ??should be taken
	* @return the packet
	*/
	public void pack(Packer p, boolean lokal)
	{
		p.writeByte(Packettype.SNAP_SPIELERDATEN);
		p.writeUTF(this.name());
		p.writeBoolean(lokal);
		p.writeDouble(this.position().getX());
		p.writeDouble(this.position().getY());
		p.writeDouble(this.richtung());
		p.writeInt(this.punkte());
		
		int x = lokal ? 1 : 0;
		
		p.writeInt(this.leben() * x);
		p.writeInt(this.munition() * x);
	}
	
	/**
	* @return the position of the player
	*/
	public Point.Double position()
	{
		return _location;
	}
	
	/**
	* returns the current score
	* 
	* @return the current score
	*/
	public int punkte()
	{
		return _points;
	}
	
	/**
	* returns the current direction
	* 
	* @returns the current direction in degrees
	*/
	public double richtung()
	{
		return _direction;
	}
	
	/**
	* decreases the munition and returns a new object shot
	*/
	public Shot schiesse() 
	{
		return this.schiesse(false);
	}
	
	/**
	* decreases the munition and returns a new object shot
	* 
	* @param master true, if a master shot will be given
	*/
	public Shot schiesse(boolean master)
	{
		int noetigeMunition = master ? GameConstants.AMMO_PER_MASTER_SHOT : GameConstants.AMMO_PER_SHOT;
		if (_ammo >= noetigeMunition)
		{
			_ammo -= noetigeMunition;
			
			double zeit = GameConstants.SHOT_TTL / 2 /
					((double)GameConstants.TICK_INTERVAL);
			return new Shot(this.positionNach(zeit), (int)_direction, master);
		}
		return null;
	}
	
	/**
	* assigns a new value to the health
	* 
	* @param value new health
	*/
	public void setzeLeben(int wert)
	{
		_lifepoints = wert;
	}
	
	/**
	* assigns a new value to the score
	* 
	* @param value new score
	*/
	public void setzePunkte(int wert)
	{
		_points = wert;
	}
	
	/**
	* assigns a new direction
	* 
	* @param speed new direction in degrees
	*/
	public void setzeRichtung(double value)
	{
		double v = value;
		while(v<0)
			v+=360;
		_direction = v % 360;
	}
	
	/**
	* assigns a new direction
	* 
	* @param speed new direction in degrees
	*/
	public void setzeRichtung(int value)
	{
		this.setzeRichtung((double)value);
	}
	
	/**
	* @return the horizontal position of the player
	*/
	public int xPosition()
	{
		return (int)position().getX();
	}
	
	/**
	* @return The vertical position of the player
	*/
	public int yPosition()
	{
		return (int)position().getY();
	}
	
	/**
	* assigns a new speed
	* 
	* @param speed new speed
	*/
	protected void setzeGeschwindigkeit(double geschwindigkeit)
	{
		_speed = geschwindigkeit;
		if (_speed < 0)
			_speed = 0;
		else if (_speed > GameConstants.MAX_SPEED)
			_speed = GameConstants.MAX_SPEED;
	}
	
	/**
	* assigns a new value to the ammunition
	* 
	* @param value new value of the ammunition
	*/
	protected void setzeMunition(int wert)
	{
		if (wert >= 0 && wert <= GameConstants.MAX_AMMO)
		{
			_ammo = wert;
		}
		else
		{
			throw new IllegalArgumentException("Munition zwischen 0 und " + GameConstants.MAX_AMMO + " waehlen");
		}
	}
	
	/**
	* assigns a new value to the position
	* 
	* @param value new position
	*/
	protected void setzePosition(Point.Double wert)
	{
		double x = wert.getX();
		double y = wert.getY();

		if (x + GameConstants.PLAYER_SIZE/2 > GameConstants.PLAYING_FIELD_WIDTH)
			x = GameConstants.PLAYING_FIELD_WIDTH - GameConstants.PLAYER_SIZE/2;
		else if (x - GameConstants.PLAYER_SIZE/2 < 0)
			x = GameConstants.PLAYER_SIZE/2;
		if (y + GameConstants.PLAYER_SIZE/2 > GameConstants.PLAYING_FIELD_HEIGHT)
			y = GameConstants.PLAYING_FIELD_HEIGHT - GameConstants.PLAYER_SIZE/2;
		else if (y - GameConstants.PLAYER_SIZE/2 < 0)
			y = GameConstants.PLAYER_SIZE/2;

		_location = new Point.Double(x, y);
	}
	private Point.Double positionNach(double zeitintervall)
	{
		double weg = zeitintervall * this.geschwindigkeit();
		return new Point.Double(
			this.position().getX() + weg * Math.sin(Math.toRadians(this.richtung())),
			this.position().getY() + weg * Math.cos(Math.toRadians(this.richtung())));
	}
}
