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

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import sw.shared.GameConstants;
import sw.shared.Packet;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class ServerFinder extends Thread
{
    private SWFrame _application;
    private DatagramSocket _socket;
    private boolean _isRunning;
    
    public ServerFinder(SWFrame anwendung)
    {
        try
        {
            _application = anwendung;
            _socket = new DatagramSocket();
            _socket.setBroadcast(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void dispose()
    {
        _isRunning = false;
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
        _isRunning = true;
        try
        {
            String local = InetAddress.getLocalHost().getHostName();
            for(InetAddress a : InetAddress.getAllByName(local))
            {
                byte[] addr = a.getAddress();
                if(addr.length != 4 || addr[0] != (byte)192 || addr[1] != (byte)168)
                    continue;
                addr[3] = (byte)255;
                byte[] buffer = GameConstants.SERVER_INFO_REQUEST;
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                packet.setSocketAddress(new InetSocketAddress(InetAddress.getByAddress(addr), GameConstants.STANDARD_PORT));
                _socket.send(packet);
            }
            
            while(_isRunning)
            {
                byte[] buf = new byte[1024];
                DatagramPacket response = new DatagramPacket(buf, buf.length);
                _socket.receive(response);
                int len = Math.min(response.getLength(), GameConstants.SERVER_INFO_RESPONSE.length);
                String msg = new String(buf, 0, len);
                if(msg.equals(GameConstants.SERVER_INFO_RESPONSE))
                {
                	byte[] data = java.util.Arrays.copyOfRange(buf, len, response.getLength());
                    Packet info = new Packet(data, data.length);
                    _application.foundServer(response.getAddress().getHostAddress().toString(),
                        info.getString(), info.getInt(), info.getInt());
                }
            }
        }
        catch (Exception e)
        {
        }
    }
}
