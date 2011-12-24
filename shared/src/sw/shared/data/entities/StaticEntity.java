package sw.shared.data.entities;

import java.awt.Point;

import sw.shared.GameConstants;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * Represents an entity with a position and a direction
 * @author Redix stes Abbadonn
 * @version 24.12.11
 */
public abstract class StaticEntity extends Entity
{
	public static final double MIN_X = GameConstants.PLAYER_SIZE / 2;
	public static final double MIN_Y = GameConstants.PLAYER_SIZE / 2;
	
	public static final double MAX_X = GameConstants.PLAYING_FIELD_WIDTH - GameConstants.PLAYER_SIZE / 2;
	public static final double MAX_Y = GameConstants.PLAYING_FIELD_HEIGHT - GameConstants.PLAYER_SIZE / 2;
	
	// state variables
	private double _x;
	private double _y;
	private double _direction;
	
	public StaticEntity(byte type)
	{
		super(type);
	}	
	
	public double getX()
	{
		return _x;
	}
	
	public void setX(double x)
	{
		_x = Math.max(Math.min(x, MoveableEntity.MAX_X), MoveableEntity.MIN_X);
	}
	
	public double getY()
	{
		return _y;
	}
	
	public void setY(double y)
	{
		_y = Math.max(Math.min(y, MoveableEntity.MAX_Y), MoveableEntity.MIN_Y);
	}
	
	public void setDirection(double direction)
	{
		_direction = direction % (2 * Math.PI);
	}

	public double getDirection()
	{
		return _direction;
	}
	
	
	public Point.Double getPosition()
	{
		return new Point.Double(getX(), getY());
	}

	@Override
	public void fromSnap(Unpacker p)
	{
		this.setX(p.readDouble());
		this.setY(p.readDouble());
		this.setDirection(p.readDouble());
	}

	@Override
	public void snap(Packer p, String name)
	{
		p.writeDouble(getX());
		p.writeDouble(getY());
		p.writeDouble(getDirection());
	}
}
