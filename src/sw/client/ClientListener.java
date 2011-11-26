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
package sw.client;

import sw.shared.Packet;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public interface ClientListener
{
    /**
     * Wird ausgeführt, wenn eine Chatnachricht erhalten wurde
     * 
     * @param name Der Name des Absenders
     * @param text Der Text, der gesendet wurde
     */
    public void chatMessage(String name, String text);
    
    /**
     * Wird ausgeführt, wenn der Server die Verbindung beendet
     * 
     * @param grund Der Trenngrund
     */
    public void connectionLost(String grund);
    
    /**
     * Wird ausgeführt, wenn der Client einen Schuss empfängt
     * 
     * @param packet Der Schuss
     */
    public void shot(Packet packet);
    
    /**
     * Wird ausgeführt, wenn der Client einen Snapshot empfängt
     * 
     * @param packet Der Snapshot
     */
    public void snapshot(Packet packet);
}
