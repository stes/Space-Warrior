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
public final class Packettype
{
    // client
    public final static byte CL_START_INFO = 0;
    public final static byte CL_CHAT_MESSAGE = 1;
    public final static byte CL_INPUT = 2;
    
    // server
    public final static byte SV_CHAT_MESSAGE = 1;
    public final static byte SV_SNAPSHOT = 2;
    public final static byte SV_SHOT = 3;
    
    // snapshot types
    public final static byte SNAP_PLAYERDATA = 4;
}
