package sw.shared.data.entities;

import java.awt.geom.Point2D;

import sw.shared.GameConstants;

/**
 * Entity with a position and a direction
 * 
 * @author Redix stes Abbadonn
 * @version 24.12.11
 */
public interface IStaticEntity extends IEntity
{
	public static final double MIN_X = GameConstants.PLAYER_SIZE / 2;
	public static final double MIN_Y = GameConstants.PLAYER_SIZE / 2;

	public static final double MAX_X = GameConstants.PLAYING_FIELD_WIDTH
			- GameConstants.PLAYER_SIZE / 2;
	public static final double MAX_Y = GameConstants.PLAYING_FIELD_HEIGHT
			- GameConstants.PLAYER_SIZE / 2;
	
	public Point2D.Double getPosition();
	public double getDirection();
	public double getX();
	public double getY();
}
