/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes Abbadonn
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.shared;
/**
 * Eine Sammlung von Konstanten fuer den Netzwerkverkehr
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public final class Packettype
{
    // Client
    public final static char CL_START_INFO = 0;
    public final static char CL_CHAT_NACHRICHT = 1;
    public final static char CL_EINGABE = 2;
    
    // Server
    public final static char SV_TRENN_INFO = 0;
    public final static char SV_CHAT_NACHRICHT = 1;
    public final static char SV_SNAPSHOT = 2;
    public final static char SV_SCHUSS = 3;
    
    // Snapshot Typen
    public final static char SNAP_SPIELERDATEN = 4;
}
