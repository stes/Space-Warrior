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
package sw.shared;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public final class GameConstants
{
	// TODO: use enums!

	// Images
	public static enum Images
	{
		SHIP_1(0x11), SHIP_2(0x12), BACKGROUND(0x00), SHIP_3(0x13), SHIP_4(0x14), SHOT_ROCKET(0x21);

		public static Images max()
		{
			int max = Integer.MIN_VALUE;
			Images img = null;
			for (Images i : Images.values())
			{
				if (max < i.getID() && i.name().toLowerCase().contains("ship"))
				{
					max = i.getID();
					img = i;
				}
			}
			return img;
		}

		public static Images min()
		{
			int min = Integer.MAX_VALUE;
			Images img = null;
			for (Images i : Images.values())
			{
				if (min > i.getID() && i.name().toLowerCase().contains("ship"))
				{
					min = i.getID();
					img = i;
				}
			}
			return img;
		}

		private int _id;

		private Images(int id)
		{
			_id = id;
		}

		public int getID()
		{
			return _id;
		}
	}

	// Network
	public final static int STANDARD_PORT = 2489;
	public final static byte[] SERVER_INFO_REQUEST = new byte[] { 'i', 'n', 'f', 'o', '?' };

	public final static byte[] SERVER_INFO_RESPONSE = new byte[] { 'i', 'n', 'f', 'o', '!' };
	// Gameplay
	public final static int TICKS_PER_SECOND = 40;

	// Players
	public final static int PLAYER_SIZE = 64;
	public final static int ROCKET_SIZE = 40;

	public final static int TICK_INTERVAL = 1000 / GameConstants.TICKS_PER_SECOND;
	// Maximum values
	public final static int MAX_LIVES = 100;
	public final static int MAX_AMMO = 200;
	public final static int MAX_SPEED = 16;
	public final static int MAX_MOVEMENT = GameConstants.MAX_SPEED * GameConstants.TICK_INTERVAL;
	public final static int MAX_RANGE = 200;
	public final static int MAX_MASTER_RANGE = 500;
	public final static int MAX_PLAYERS = 6;
	public final static int MAX_DAMAGE = 10;
	public final static int MAX_MASTER_DAMAGE = 25;
	public final static int MAX_COLLISION_DAMAGE = 5;
	public final static int MAX_SHOT_INTERVAL = 200;
	public final static double MAX_ANGULAR_SPEED = Math.PI / 20;
	public final static int MAX_HITRANGE = GameConstants.PLAYER_SIZE / 2;

	public final static double ANGULAR_ACCELERATION = 0.0005 * GameConstants.TICK_INTERVAL;
	// Shooting
	public final static int AMMO_PER_SHOT = 20;
	public final static int AMMO_PER_MASTER_SHOT = 100;
	public static final int AMMO_PER_ROCKET = 150;
	// Movements
	public final static double ACCELERATION = 0.01 * GameConstants.TICK_INTERVAL;
	public final static double DECELERATION = -0.01 * GameConstants.TICK_INTERVAL;
	// TODO remove if no longer needed
	// public final static double ANGEL_OF_ROTATION = 0.3 * TICK_INTERVAL;

	public final static int MAX_COLLISION_DAMAGE_RANGE = GameConstants.PLAYER_SIZE;

	// Playing field
	public final static int PLAYING_FIELD_WIDTH = 1400;
	public final static int PLAYING_FIELD_HEIGHT = 900;
	public final static int REFERENCE_X = 0;
	public final static int REFERENCE_Y = 0;

	// Shots
	public final static int SHOT_TTL = 20; // ticks

	// Scoring
	public final static int POINTS_PER_HIT = 1;
	public final static int POINTS_PER_KILL = 10;
}
