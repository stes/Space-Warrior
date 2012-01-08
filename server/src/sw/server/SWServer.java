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
import java.util.ArrayList;
import java.util.Vector;

import sw.server.cli.Command;
import sw.server.cli.SWCommandParser;
import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.PlayerInput;
import sw.shared.data.ServerInfo;
import sw.shared.net.NetworkListener;
import sw.shared.net.Packer;
import sw.shared.net.UDPConnection;
import sw.shared.net.UDPHost;
import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWServer implements IServer, NetworkListener, Runnable
{
	private UDPHost _netServer;
	private Vector<Client> _clients;
	private PropertyLoader _propertyLoader;
	private ServerInfo _serverInfo;
	private GameController _controller;
	private SWCommandParser _cmdParser;

	private int _tick;
	private long _lastUpdate;

	private ArrayList<ServerListener> _serverListener;

	public SWServer(int port)
	{
		_propertyLoader = new PropertyLoader();
		_serverListener = new ArrayList<ServerListener>();
		_netServer = new UDPHost(new InetSocketAddress(_propertyLoader.getPort()),
				_propertyLoader.getMaxPlayers());
		_netServer.setAcceptConnections();
		_netServer.addNetworkListener(this);
		_netServer.start();
		_clients = new Vector<Client>();
		_serverInfo = new ServerInfo("Server", _propertyLoader.getMaxPlayers(), 0);
		_lastUpdate = System.currentTimeMillis();
		_controller = new GameController(this);
		this.addServerListener(_controller);
		_cmdParser = new SWCommandParser(this, _controller);
		new Thread(this).start();
	}

	public void addServerListener(ServerListener listener)
	{
		_serverListener.add(listener);
	}

	@Override
	public void ban(String ip)
	{
		// TODO implement
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void close()
	{
		_netServer.close("Server shutdown");
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
		if (client.isPlaying())
		{
			for (ServerListener l : _serverListener)
			{
				l.playerLeft(client.getName(), reason);
			}
		}
		_clients.remove(client);
	}

	public void drop(Client client)
	{
		this.drop(client, "");
	}

	public void drop(Client client, String reason)
	{
		client.getConnection().disconnect(reason);
	}

	public Packer getServerInfos()
	{
		_serverInfo.setNumPayers(_clients.size());
		return _serverInfo.pack();
	}

	@Override
	public void kick(String client)
	{
		// TODO implement
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void receivedMessage(UDPConnection connection, byte[] data, int len)
	{
		Client client = this.getClientbyConnection(connection);
		Unpacker packet = new Unpacker(data);

		if (Packettype.CL_START_INFO == packet.getType() && !client.isPlaying())
		{
			String wantedName = packet.readUTF();
			String name = wantedName;
			int imageID = packet.readInt();
			int num = 0;
			while (this.getClientbyName(name) != null)
			{
				num++;
				name = wantedName + " (" + num + ")";
			}
			client.setName(name);
			client.enterGame();
			for (ServerListener l : _serverListener)
			{
				l.playerConnected(client.getName(), imageID);
			}
		}
		else if (Packettype.CL_CHAT_MESSAGE == packet.getType() && client.isPlaying())
		{
			String text = packet.readUTF();
			Packer chat = new Packer(Packettype.SV_CHAT_MESSAGE);
			chat.writeUTF(client.getName());
			chat.writeUTF(text);
			this.sendBroadcast(chat);
		}
		else if (Packettype.CL_INPUT == packet.getType() && client.isPlaying())
		{
			PlayerInput input = PlayerInput.unpack(packet);
			for (ServerListener l : _serverListener)
			{
				l.processPlayerInput(client.getName(), input);
			}
		}
		else if (Packettype.CL_COMMAND == packet.getType())
		{
			String cmd = packet.readUTF();
			try
			{
				_cmdParser.performAction(new Command(cmd, client.getName()));
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receivedMessageConnless(InetSocketAddress addr, byte[] data, int len)
	{
		if (java.util.Arrays.equals(data, GameConstants.SERVER_INFO_REQUEST))
		{
			byte[] info = this.getServerInfos().toByteArray();
			byte[] buf = new byte[GameConstants.SERVER_INFO_RESPONSE.length + info.length];
			System.arraycopy(GameConstants.SERVER_INFO_RESPONSE,
					0,
					buf,
					0,
					GameConstants.SERVER_INFO_RESPONSE.length);
			System.arraycopy(info, 0, buf, GameConstants.SERVER_INFO_RESPONSE.length, info.length);
			_netServer.sendConnless(addr, buf, buf.length);
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			this.tick();
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{}
		}
	}

	@Override
	public void sendBroadcast(Packer packet)
	{
		byte[] data = packet.toByteArray();
		_netServer.broadcast(data, data.length);
	}

	@Override
	public void sendPacket(String name, Packer packet)
	{
		Client client = this.getClientbyName(name);
		if (client != null)
		{
			this.sendPacket(client, packet);
		}
	}

	@Override
	public void setServerName(String name)
	{
		_serverInfo.setServerName(name);
		System.out.println("server name: " + name);
	}

	public void tick()
	{
		long curTime = System.currentTimeMillis();
		if (curTime - _lastUpdate >= GameConstants.TICK_INTERVAL)
		{
			for (ServerListener l : _serverListener)
			{
				l.tick();
				if ((_tick % 2) == 0) // save bandwidth
				{
					l.broadcastSnapshots();
				}
			}
			_lastUpdate = curTime;
			_tick++;
		}
	}

	protected Vector<Client> clList()
	{
		return _clients;
	}

	private Client getClientbyConnection(UDPConnection connection)
	{
		for (int i = 0; i < _clients.size(); i++)
		{
			if (_clients.get(i).getConnection().equals(connection))
			{
				return _clients.get(i);
			}
		}
		return null;
	}

	private Client getClientbyName(String name)
	{
		for (int i = 0; i < _clients.size(); i++)
		{
			if (_clients.get(i).getName().equals(name))
			{
				return _clients.get(i);
			}
		}
		return null;
	}

	private void sendPacket(Client client, Packer packet)
	{
		byte[] data = packet.toByteArray();
		client.getConnection().send(data, data.length);
	}
}
