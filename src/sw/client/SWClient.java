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
package sw.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import sw.shared.Packettype;
import sw.shared.data.Packet;
import sw.shared.net.NetworkListener;
import sw.shared.net.UDPConnection;
import sw.shared.net.UDPHost;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWClient implements IClient, NetworkListener
{
	private UDPHost _netClient;
	private UDPConnection _server;
	
    private ArrayList<ClientListener> _clientListener;
    
    /**
     * Erzeugt einen neuen NetClient
     * 
     * @param ip Die IP Adresse des Servers
     * @param port Der Port
     */
    public SWClient()
    {
    	_netClient = new UDPHost(null, 1);
    	_netClient.addNetworkListener(this);
        _clientListener = new ArrayList<ClientListener>();
        _netClient.start();
    }
    
    public void close()
    {
    	_netClient.close("quit");
    }
    
    public void connect(String ip, int port)
    {
    	_netClient.connect(new InetSocketAddress(ip, port));
    }
    
    public void addClientListener(ClientListener listener)
    {
        _clientListener.add(listener);
    }
    
    @Override
    public void sendPacket(Packet packet)
    {
    	if(_server != null)
    	{
	    	byte[] data = packet.getData();
	    	_server.send(data, data.length);
    	}
    }
    
    @Override
    public void connected(UDPConnection connection)
    {
    	_server = connection;
        for (ClientListener l : _clientListener)
        {
            l.connected();
        }
    }
    
    @Override
    public void disconnected(UDPConnection connection, String reason)
    {
    	_server = null;
        for (ClientListener l : _clientListener)
        {
            l.disconnected(reason);
        }
    }
    
    @Override
    public void receivedMessage(UDPConnection connection, byte[] data, int len)
    {
        Packet packet = new Packet(data, len);
        
        if(Packettype.SV_CHAT_NACHRICHT == packet.getType())
        {
            String name = packet.getString();
            String text = packet.getString();
            for (ClientListener l : _clientListener)
            {
                l.chatMessage(name, text);
            }
        }
        else if(Packettype.SV_SNAPSHOT == packet.getType())
        {
            for (ClientListener l : _clientListener)
            {
                l.snapshot(packet);
            }
        }
        else if(Packettype.SV_SCHUSS == packet.getType())
        {
            for (ClientListener l : _clientListener)
            {
                l.shot(packet);
            }
        }
    }
    
    @Override
    public void receivedMessageConnless(InetSocketAddress addr, byte[] data, int len) {}
}
