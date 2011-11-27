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

import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packet;
import sw.shared.data.PlayerInput;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWServer implements IServer, NetworkServerListener
{
	private UDPServer _netServer;
    private Client[] _clients;
    private PropertyLoader _propertyLoader;
    
    private int _tick;
    private long _lastUpdate;
    
    private String _serverName;
    
    private GameController _controller;
    
    public SWServer(int port)
    {
    	_propertyLoader = new PropertyLoader();
    	_controller = new GameController(this);
    	_netServer = new UDPServer(_propertyLoader.getPort(), _propertyLoader.getMaxPlayers());
    	_netServer.addNetworkServerListener(this);
    	_netServer.start();
        _clients = new Client[GameConstants.MAX_PLAYERS];
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
    	for(int i = 0; i < _clients.length; i++)
    	{
    		if(_clients[i] != null && _clients[i].name().equals(name))
    		{
    			return _clients[i];
    		}
    	}
    	return null;
    }
    
    @Override
    public void sendPacket(String name, Packet packet)
    {
    	Client cl = this.getClientbyName(name);
    	if(cl != null)
    	{
    		this.sendPacket(cl.getClientID(), packet);
    	}
    }
    
    public void sendBroadcast(Packet packet)
    {
    	this.sendPacket(-1, packet);
    }
    
    private void sendPacket(int clientID, Packet packet)
    {
    	byte[] data = packet.getData();
    	_netServer.send(clientID, data, data.length);
    }
    
	// TODO: remove
	public void drop(int clientID)
	{
		_netServer.drop(clientID);
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
    
    protected Client[] clListe()
    {
        return _clients;
    }

	@Override
	public void serverClientConnected(int clientID) 
	{
		_clients[clientID] = new Client(clientID, "connecting client");
	}

	@Override
	public void serverClientDisconnected(int clientID)
	{
		if(_clients[clientID].isPlaying())
        {
            _controller.playerLeft(_clients[clientID].name());
        }
		_clients[clientID] = null;
	}

	@Override
	public void serverReceivedMessage(int clientID, byte[] data, int len)
	{
		Client client = _clients[clientID];
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
            	_netServer.drop(clientID);
            }
        }
        else if(Packettype.CL_CHAT_MSG == packet.getType() && client.isPlaying())
        {
            String text = packet.getString();
            Packet chat = new Packet(Packettype.SV_CHAT_NACHRICHT);
            chat.addString(client.name());
            chat.addString(text);
            this.sendPacket(-1, chat);
        }
        else if(Packettype.CL_INPUT == packet.getType() && client.isPlaying())
        {
            _controller.processPlayerInput(client.name(), new PlayerInput(packet));
        }
	}
}
