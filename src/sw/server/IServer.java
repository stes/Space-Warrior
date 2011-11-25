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
package sw.server;

import sw.shared.Paket;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public interface IServer
{
    // Dienste
     /**
     * Sendet eine Nachricht an einen Client
     * 
     * @param name Name des Empfängers
     * @param nachricht Die Nachricht
     */
    public void sendeNachricht(String name, Paket nachricht);
    
    /**
     * Sendet eine Nachricht an alle verbundenen Clients
     * 
     * @param nachricht Die Nachricht
     */
    public void sendeRundnachricht(Paket nachricht);
}
