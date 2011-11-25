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

import java.net.DatagramSocket;
import java.net.DatagramPacket;

import sw.shared.Spielkonstanten;
import sw.shared.Paket;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ServerInfo extends Thread
{
    private DatagramSocket _socket;
    private NetServer _netServer;
    private boolean _laeuft;

    // Konstruktor
    public ServerInfo(NetServer netServer)
    {
        _netServer = netServer;
        try
        {
            _socket = new DatagramSocket(Spielkonstanten.STANDARD_PORT);
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
                if(msg.equals(Spielkonstanten.SERVER_INFO_ANFRAGE))
                {
                    Paket info = _netServer.holeServerInfos();
                    byte[] buf = (Spielkonstanten.SERVER_INFO_ANTWORT + info.toString()).getBytes();
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
