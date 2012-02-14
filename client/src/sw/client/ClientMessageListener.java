/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
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
package sw.client;

import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */

public interface ClientMessageListener
{
	/**
	 * Invoked when a chat message was received
	 * 
	 * @param name
	 *            the addresser's name
	 * @param text
	 *            the chat message
	 */
	public void chatMessage(String name, String text);

	/**
	 * Invoked when a snapshot was received
	 * 
	 * @param packet
	 *            snapshot
	 */
	public void snapshot(Unpacker packet);
}
