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

import java.net.InetSocketAddress;
import java.util.ArrayList;

import sw.shared.Packet;
import sw.shared.Packettype;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWClient implements IClient, NetworkClientListener
{
	private UDPClient _netClient;
	
    private ArrayList<ClientListener> _clientListener;
    
    /**
     * Erzeugt einen neuen NetClient
     * 
     * @param ip Die IP Adresse des Servers
     * @param port Der Port
     */
    public SWClient()
    {
    	_netClient = new UDPClient();
    	_netClient.addNetworkClientListener(this);
        _clientListener = new ArrayList<ClientListener>();
    }
    
    public void connect(String ip, int port, String name)
    {
    	_netClient.connect(new InetSocketAddress(ip, port));
    	_netClient.start();
    	Packet start = new Packet(Packettype.CL_START_INFO);
        start.fuegeStringAn(name);
        this.sendPacket(start);
    }
    
    public void addClientListener(ClientListener listener)
    {
        _clientListener.add(listener);
    }
    
    @Override
    public void sendPacket(Packet packet)
    {
    	byte[] data = packet.getData();
        _netClient.send(data, data.length);
    }
    
    @Override
    public void clientConnected()
    {
        for (ClientListener l : _clientListener)
        {
            l.connected();
        }
    }
    
    @Override
    public void clientDisconnected()
    {
        for (ClientListener l : _clientListener)
        {
            l.disconnected();
        }
    }
    
    @Override
    public void clientReceivedMessage(byte[] data, int len)
    {
        Packet packet = new Packet(data, len);
        
        if(Packettype.SV_CHAT_NACHRICHT == packet.type())
        {
            String name = packet.holeString();
            String text = packet.holeString();
            for (ClientListener l : _clientListener)
            {
                l.chatMessage(name, text);
            }
        }
        else if(Packettype.SV_SNAPSHOT == packet.type())
        {
            for (ClientListener l : _clientListener)
            {
                l.snapshot(packet);
            }
        }
        else if(Packettype.SV_SCHUSS == packet.type())
        {
            for (ClientListener l : _clientListener)
            {
                l.shot(packet);
            }
        }
    }
}
