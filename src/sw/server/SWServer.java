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

import java.net.InetSocketAddress;
import java.util.Vector;

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packet;
import sw.shared.data.PlayerInput;
import sw.shared.net.NetworkListener;
import sw.shared.net.UDPConnection;
import sw.shared.net.UDPHost;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWServer implements IServer, NetworkListener
{
	private UDPHost _netServer;
    private Vector<Client> _clients;
    private PropertyLoader _propertyLoader;
    
    private int _tick;
    private long _lastUpdate;
    
    private String _serverName;
    
    private GameController _controller;
    
    public SWServer(int port)
    {
    	_propertyLoader = new PropertyLoader();
    	_controller = new GameController(this);
    	_netServer = new UDPHost(new InetSocketAddress(_propertyLoader.getPort()), _propertyLoader.getMaxPlayers());
    	_netServer.setAcceptConnections();
    	_netServer.addNetworkListener(this);
    	_netServer.start();
        _clients = new Vector<Client>();
        _lastUpdate = System.currentTimeMillis();
        this.setServerName("Server");
    }
    
    public void setServerName(String name)
    {
    	_serverName = name;
        System.out.println("server name: " + _serverName);
    }
    
    public Packet holeServerInfos()
    {
        Packet info = new Packet(new byte[]{(byte)0}, 0);
        info.addString(_propertyLoader.getServerName());
        info.addNumber(_propertyLoader.getMaxPlayers());
        info.addNumber(0);
        return info;
    }
    
    private Client getClientbyName(String name)
    {
    	for(int i = 0; i < _clients.size(); i++)
    	{
    		if(_clients.get(i).name().equals(name))
    		{
    			return _clients.get(i);
    		}
    	}
    	return null;
    }
    
    private Client getClientbyConnection(UDPConnection connection)
    {
    	for(int i = 0; i < _clients.size(); i++)
    	{
    		if(_clients.get(i).getConnection().equals(connection))
    		{
    			return _clients.get(i);
    		}
    	}
    	return null;
    }
    
    @Override
    public void sendPacket(String name, Packet packet)
    {
    	Client client = this.getClientbyName(name);
    	if(client != null)
    	{
    		this.sendPacket(client, packet);
    	}
    }
    
    @Override
	public void sendBroadcast(Packet packet)
    {
    	byte[] data = packet.getData();
    	_netServer.broadcast(data, data.length);
    }
    
    private void sendPacket(Client client, Packet packet)
    {
    	byte[] data = packet.getData();
    	client.getConnection().send(data, data.length);
    }
    
	// TODO: remove
	public void drop(Client client)
	{
		client.getConnection().disconnect();
	}
   
    public void tick()
    {
        long curTime = System.currentTimeMillis();
        if(curTime - _lastUpdate > GameConstants.TICK_INTERVAL)
        {
            _controller.tick();
            if((_tick % 2) == 0) // save bandwidth
            {
            	_controller.broadcastSnapshots();
            }
            _lastUpdate = curTime;
            _tick++;
        }
    }
    
    protected Vector<Client> clListe()
    {
        return _clients;
    }

	@Override
	public void connected(UDPConnection connection) 
	{
		_clients.add(new Client(connection, "connecting client"));
	}

	@Override
	public void disconnected(UDPConnection connection)
	{
		Client client = this.getClientbyConnection(connection);
		if(client.isPlaying())
        {
            _controller.playerLeft(client.name());
        }
		_clients.remove(client);
	}

	@Override
	public void receivedMessage(UDPConnection connection, byte[] data, int len)
	{
		Client client = this.getClientbyConnection(connection);
        Packet packet = new Packet(data, len);
        
        if(Packettype.CL_START_INFO == packet.getType() && !client.isPlaying())
        {
            String name = packet.getString();
            Client cl = this.getClientbyName(name);
            if(cl == null)
            {
                client.setName(name);
                client.enterGame();
                _controller.bearbeiteNeuenSpieler(client.name());
            }
            else
            {
            	connection.disconnect();
            }
        }
        else if(Packettype.CL_CHAT_MSG == packet.getType() && client.isPlaying())
        {
            String text = packet.getString();
            Packet chat = new Packet(Packettype.SV_CHAT_NACHRICHT);
            chat.addString(client.name());
            chat.addString(text);
            this.sendBroadcast(chat);
        }
        else if(Packettype.CL_INPUT == packet.getType() && client.isPlaying())
        {
            _controller.processPlayerInput(client.name(), new PlayerInput(packet));
        }
	}
}
