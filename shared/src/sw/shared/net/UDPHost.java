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
package sw.shared.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class UDPHost extends Thread
{
	private class ConnectionUpdater extends Thread
	{
		@Override
		public void run()
		{
			while (_socket != null && !_socket.isClosed())
			{
				for (int i = 0; i < _connections.length; i++)
				{
					if (_connections[i] != null && !_connections[i].error())
					{
						_connections[i].update();
					}
				}
				try
				{
					Thread.sleep(5);
				}
				catch (InterruptedException e) {}
			}
		}
	}
	private final static int MAX_PACKET_LENGTH = 2 * 1024;

	private final static int PACKET_HEADER_LENGTH = 1;
	private DatagramSocket _socket;

	private UDPConnection[] _connections;
	private ConnectionUpdater _updater;

	private ArrayList<NetworkListener> _networkListener;

	private boolean _acceptConnections;

	public UDPHost(InetSocketAddress addr, int maxConnections)
	{
		try
		{
			_acceptConnections = false;
			_connections = new UDPConnection[maxConnections];
			if (addr != null)
			{
				_socket = new DatagramSocket(addr);
			}
			else
			{
				_socket = new DatagramSocket();
			}
			_updater = new ConnectionUpdater();
			_updater.start();
			_networkListener = new ArrayList<NetworkListener>();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addNetworkListener(NetworkListener listener)
	{
		_networkListener.add(listener);
	}

	public void broadcast(byte[] data, int len)
	{
		for (UDPConnection con : _connections)
		{
			if (con != null)
			{
				con.send(data, len);
			}
		}
	}

	public void close()
	{
		this.close("");
	}

	public void close(String info)
	{
		for (UDPConnection con : _connections)
		{
			if (con != null)
			{
				con.disconnect(info);
			}
		}
		_socket.close();
	}

	public void connect(InetSocketAddress addr)
	{
		int slot = this.getFreeSlot();
		if (slot != -1)
		{
			_connections[slot] = new UDPConnection(this, addr);
			_connections[slot].connect();
		}
	}

	@Override
	public void run()
	{
		try
		{
			byte[] buffer = new byte[MAX_PACKET_LENGTH];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			while (true)
			{
				_socket.receive(packet);
				byte flag = buffer[0];
				byte[] data = java.util.Arrays.copyOfRange(buffer,
						PACKET_HEADER_LENGTH, packet.getLength());
				this.messageReceived(
						(InetSocketAddress) packet.getSocketAddress(), flag,
						data, data.length);
			}
		}
		catch (SocketException e)
		{
			System.out.println(e.getMessage());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendConnless(InetSocketAddress addr, byte[] data, int len)
	{
		byte[] buf = new byte[2 + len];
		buf[0] = 's';
		buf[1] = 'w';
		System.arraycopy(data, 0, buf, 2, len);
		this.send(addr, UDPConnection.FLAG_CONNLESS, buf, buf.length);
	}

	public void setAcceptConnections()
	{
		_acceptConnections = true;
	}

	protected void invokeConnected(UDPConnection con)
	{
		for (NetworkListener l : _networkListener)
			l.connected(con);
	}

	protected void invokeDisconnected(UDPConnection con, String reason)
	{
		for (NetworkListener l : _networkListener)
			l.disconnected(con, reason);
		for (int i = 0; i < _connections.length; i++)
		{
			if (_connections[i] != null && _connections[i].equals(con))
				_connections[i] = null;
		}
	}

	protected void invokeReceivedMessage(UDPConnection con, byte[] data, int len)
	{
		for (NetworkListener l : _networkListener)
			l.receivedMessage(con, data, len);
	}

	protected void send(InetSocketAddress addr, byte[] data, int len)
	{
		this.send(addr, (byte) 0, data, len);
	}

	protected void sendControl(InetSocketAddress addr, byte msg, byte[] data,
			int len)
	{
		byte[] buf = new byte[1 + len];
		buf[0] = msg;
		if (len > 0)
			System.arraycopy(data, 0, buf, 1, len);
		this.send(addr, UDPConnection.FLAG_CONTROL, buf, buf.length);
	}

	private int getFreeSlot()
	{
		for (int i = 0; i < _connections.length; i++)
		{
			if (_connections[i] == null)
			{
				return i;
			}
		}
		return -1;
	}

	private void messageReceived(InetSocketAddress addr, byte flag,
			byte[] data, int len)
	{
		if ((flag & UDPConnection.FLAG_CONNLESS) > 0 && data.length >= 2
				&& data[0] == 's' && data[1] == 'w')
		{
			byte[] buf = java.util.Arrays.copyOfRange(data, 2, len);
			for (NetworkListener l : _networkListener)
				l.receivedMessageConnless(addr, buf, buf.length);
			return;
		}

		for (int i = 0; i < _connections.length; i++)
		{
			if (_connections[i] != null && _connections[i].equals(addr))
			{
				_connections[i].received(flag, data, len);
				return;
			}
		}

		if (data.length > 0 && data[0] == UDPConnection.CTRL_CONNECT
				&& _acceptConnections)
		{
			int slot = this.getFreeSlot();
			if (slot != -1)
			{
				_connections[slot] = new UDPConnection(this, addr);
				_connections[slot].received(flag, data, len);
			}
			else
			{
				byte[] info = "The server is full".getBytes();
				this.sendControl(addr, UDPConnection.CTRL_CLOSE, info,
						info.length);
			}
		}
	}

	private void send(InetSocketAddress addr, byte flag, byte[] data, int len)
	{
		int size = PACKET_HEADER_LENGTH + len;
		if (size > MAX_PACKET_LENGTH) // TODO: exception
		{
			System.out.println("error: packet is too big");
			return;
		}

		byte[] buf = new byte[size];
		buf[0] = flag;
		if (data != null && len > 0)
		{
			System.arraycopy(data, 0, buf, PACKET_HEADER_LENGTH, len);
		}

		try
		{
			DatagramPacket packet = new DatagramPacket(buf, buf.length, addr);
			_socket.send(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
