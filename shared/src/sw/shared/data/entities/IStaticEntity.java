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

	public double getDirection();

	public Point2D.Double getPosition();

	public double getX();

	public double getY();
}
