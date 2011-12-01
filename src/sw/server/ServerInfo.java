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
package sw.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import sw.shared.GameConstants;
import sw.shared.data.Packer;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ServerInfo extends Thread
{
    private DatagramSocket _socket;
    private SWServer _netServer;
    private boolean _laeuft;

    // Konstruktor
    public ServerInfo(SWServer netServer)
    {
        _netServer = netServer;
        try
        {
            _socket = new DatagramSocket(GameConstants.STANDARD_PORT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Dienste
    public void gibFrei()
    {
        _laeuft = false;
        try
        {
            _socket.close();
            _socket = null;
        }
        catch (Exception e)
        {
        }
    }
    
    @Override
	public void run()
    {
        _laeuft = true;
        try
        {
            while(_laeuft)
            {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                _socket.receive(packet);
                String msg = new String(buffer, 0, packet.getLength());
                if(msg.equals(GameConstants.SERVER_INFO_REQUEST))
                {
                    Packer info = _netServer.holeServerInfos();
                    byte[] buf = (GameConstants.SERVER_INFO_RESPONSE + info.toString()).getBytes();
                    DatagramPacket response = new DatagramPacket(buf, buf.length);
                    response.setSocketAddress(packet.getSocketAddress());
                    _socket.send(response);
                }
            }
        }
        catch (Exception e)
        {
        }
    }
}
