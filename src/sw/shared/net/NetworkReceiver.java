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
package sw.shared.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import sw.shared.GameConstants;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class NetworkReceiver extends Thread
{
	private DatagramSocket _socket;
	private ArrayList<NetworkListener> _networkListener;
	
	public NetworkReceiver(DatagramSocket socket)
	{
		_socket = socket;
		_networkListener = new ArrayList<NetworkListener>();
	}
	
	public void addNetworkListener(NetworkListener listener)
	{
		_networkListener.add(listener);
	}
	
	@Override
	public void run()
	{
		try
        {
        	byte[] buffer = new byte[GameConstants.MAX_PACKET_LENGTH];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            while(true)
            {
            	// TODO: connless
                _socket.receive(packet);
            	byte flag = buffer[0];
            	byte[] data = java.util.Arrays.copyOfRange(buffer, 1, packet.getLength());
            	for (NetworkListener l : _networkListener)
				{
					l.messageReceived((InetSocketAddress)packet.getSocketAddress(), flag, data, data.length);
				}
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
	}
}
