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

import java.net.InetSocketAddress;
import java.util.Vector;

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packer;
import sw.shared.data.PlayerInput;
import sw.shared.data.Unpacker;
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
    
    public void close()
    {
    	_netServer.close("Server shutdown");
    }
    
    public void setServerName(String name)
    {
    	_serverName = name;
        System.out.println("server name: " + _serverName);
    }
    
    public Packer getServerInfos()
    {
        Packer info = new Packer((byte)0);
        info.writeUTF(_propertyLoader.getServerName());
        info.writeInt(_propertyLoader.getMaxPlayers());
        info.writeInt(0);
        return info;
    }
    
    private Client getClientbyName(String name)
    {
    	for(int i = 0; i < _clients.size(); i++)
    	{
    		if(_clients.get(i).getName().equals(name))
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
    public void sendPacket(String name, Packer packet)
    {
    	Client client = this.getClientbyName(name);
    	if(client != null)
    	{
    		this.sendPacket(client, packet);
    	}
    }
    
    @Override
    public void sendBroadcast(Packer packet)
    {
    	byte[] data = packet.toByteArray();
    	_netServer.broadcast(data, data.length);
    }
    
    private void sendPacket(Client client, Packer packet)
    {
    	byte[] data = packet.toByteArray();
    	client.getConnection().send(data, data.length);
    }
    
	// TODO: remove
	public void drop(Client client, String reason)
	{
		client.getConnection().disconnect(reason);
	}
	
	public void drop(Client client)
	{
		this.drop(client, "");
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
	public void disconnected(UDPConnection connection, String reason)
	{
		Client client = this.getClientbyConnection(connection);
		if(client.isPlaying())
        {
            _controller.playerLeft(client.getName(), reason);
        }
		_clients.remove(client);
	}

	@Override
	public void receivedMessage(UDPConnection connection, byte[] data, int len)
	{
		Client client = this.getClientbyConnection(connection);
        Unpacker packet = new Unpacker(data);
        
        if(Packettype.CL_START_INFO == packet.getType() && !client.isPlaying())
        {
            String name = packet.readUTF();
            Client cl = this.getClientbyName(name);
            if(cl == null)
            {
                client.setName(name);
                client.enterGame();
                _controller.bearbeiteNeuenSpieler(client.getName());
            }
            else
            {
            	connection.disconnect("The name '" + name + "' is already in use");
            }
        }
        else if(Packettype.CL_CHAT_MESSAGE == packet.getType() && client.isPlaying())
        {
            String text = packet.readUTF();
            Packer chat = new Packer(Packettype.SV_CHAT_MESSAGE);
            chat.writeUTF(client.getName());
            chat.writeUTF(text);
            this.sendBroadcast(chat);
        }
        else if(Packettype.CL_INPUT == packet.getType() && client.isPlaying())
        {
            _controller.processPlayerInput(client.getName(), new PlayerInput(packet));
        }
	}
	
	@Override
    public void receivedMessageConnless(InetSocketAddress addr, byte[] data, int len) {}
}
