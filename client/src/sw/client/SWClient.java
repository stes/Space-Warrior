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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import sw.shared.GameConstants;
import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.Unpacker;
import sw.shared.data.ServerInfo;
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

	public SWClient()
	{
		_netClient = new UDPHost(null, 1);
		_netClient.addNetworkListener(this);
		_clientListener = new ArrayList<ClientListener>();
		_netClient.start();
	}

	public void addClientListener(ClientListener listener)
	{
		_clientListener.add(listener);
	}

	public void close()
	{
		_netClient.close("quit");
	}

	public void connect(String ip, int port)
	{
		_netClient.connect(new InetSocketAddress(ip, port));
	}

	public void disconnect(String reason)
	{
		if (_server != null)
		{
			_server.disconnect(reason);
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
		Unpacker packet = new Unpacker(data);

		if (Packettype.SV_CHAT_MESSAGE == packet.getType())
		{
			String name = packet.readUTF();
			String text = packet.readUTF();
			for (ClientListener l : _clientListener)
			{
				l.chatMessage(name, text);
			}
		}
		else if (Packettype.SV_SNAPSHOT == packet.getType())
		{
			for (ClientListener l : _clientListener)
			{
				l.snapshot(packet);
			}
		}
	}

	@Override
	public void receivedMessageConnless(InetSocketAddress addr, byte[] data,
			int len)
	{
		byte[] header = java.util.Arrays.copyOf(data,
				GameConstants.SERVER_INFO_RESPONSE.length);
		if (java.util.Arrays.equals(header, GameConstants.SERVER_INFO_RESPONSE))
		{
			byte[] info = java.util.Arrays.copyOfRange(data,
					GameConstants.SERVER_INFO_RESPONSE.length, len);
			ServerInfo serverInfo = ServerInfo.unpack(new Unpacker(info));
			serverInfo.setAddress(addr);

			for (ClientListener l : _clientListener)
			{
				l.serverInfo(serverInfo);
			}
		}
	}

	public void scan()
	{
		// TODO: improve this
		try
		{
			String local = InetAddress.getLocalHost().getHostName();
			for (InetAddress a : InetAddress.getAllByName(local))
			{
				byte[] addr = a.getAddress();
				if (addr.length != 4 || addr[0] != (byte) 192
						|| addr[1] != (byte) 168)
					continue;
				addr[3] = (byte) 255;
				byte[] buffer = GameConstants.SERVER_INFO_REQUEST;
				InetSocketAddress sockAddr = new InetSocketAddress(
						InetAddress.getByAddress(addr),
						GameConstants.STANDARD_PORT);
				_netClient.sendConnless(sockAddr, buffer, buffer.length);
			}
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public void sendPacket(Packer packet)
	{
		if (_server != null)
		{
			byte[] data = packet.toByteArray();
			_server.send(data, data.length);
		}
	}
}
