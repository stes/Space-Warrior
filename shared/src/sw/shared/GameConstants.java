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
    // Network
    public final static int STANDARD_PORT = 2489;
    public final static byte[] SERVER_INFO_REQUEST = new byte[]{'i', 'n', 'f', 'o', '?'};
    public final static byte[] SERVER_INFO_RESPONSE = new byte[]{'i', 'n', 'f', 'o', '!'};
    
    // Gameplay
    public final static int TICKS_PER_SECOND = 40;
    public final static int TICK_INTERVAL = 1000 / TICKS_PER_SECOND;
    
    // Maximum values
    public final static int MAX_LIVES = 100;
    public final static int MAX_AMMO = 200;
    public final static int MAX_SPEED = 16;
    public final static int MAX_MOVEMENT = MAX_SPEED * TICK_INTERVAL;
    public final static int MAX_RANGE = 200;
    public final static int MAX_MASTER_RANGE = 500;
    public final static int MAX_PLAYERS = 6;
    public final static int MAX_DAMAGE = 10;
    public final static int MAX_MASTER_DAMAGE = 25;
    public final static int MAX_COLLISION_DAMAGE = 5;
    
    // Shooting    
    public final static int AMMO_PER_SHOT = 20;
    public final static int AMMO_PER_MASTER_SHOT = 100;

    // Movements
    public final static double ACCELERATION = 0.01 * TICK_INTERVAL;
    public final static double DECELERATION = -0.01 * TICK_INTERVAL;
    public final static double ANGEL_OF_ROTATION = 0.3 * TICK_INTERVAL;
    
    // Players
    public final static int PLAYER_SIZE = 64;
    public final static int MAX_COLLISION_DAMAGE_RANGE = PLAYER_SIZE / 2;
    
    // Playing field
    public final static int PLAYING_FIELD_WIDTH = 1400;
    public final static int PLAYING_FIELD_HEIGHT = 900;
    public final static int REFERENCE_X = 0;
    public final static int REFERENCE_Y = 0;
    
	// Shots
    public final static int SHOT_TTL = 400;
}