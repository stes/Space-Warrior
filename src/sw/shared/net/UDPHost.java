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
package sw.shared.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import sw.shared.net.UDPConnection.ConnectionState;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class UDPHost extends Thread
{
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
        	if(addr != null)
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
    
    public void connect(InetSocketAddress addr)
    {
    	int slot = this.getFreeSlot();
    	if(slot != -1)
    	{
    		System.out.println("connecting to " + addr);
    		_connections[slot] = new UDPConnection(_socket, addr);
    		_connections[slot].setState(ConnectionState.CONNECTING);
    		_connections[slot].sendControl(UDPConnection.CTRL_CONNECT);
    	}
    }
    
    public void addNetworkListener(NetworkListener listener)
	{
    	_networkListener.add(listener);
	}
    
    public void setAcceptConnections()
    {
    	_acceptConnections = true;
    }
    
    public void run()
	{
		try
        {
        	byte[] buffer = new byte[UDPConnection.MAX_PACKET_LENGTH];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            while(true)
            {
            	// TODO: connless
                _socket.receive(packet);
            	byte flag = buffer[0];
            	byte[] data = java.util.Arrays.copyOfRange(buffer, UDPConnection.PACKET_HEADER_LENGTH, packet.getLength());
				this.messageReceived((InetSocketAddress)packet.getSocketAddress(), flag, data, data.length);
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
	}
    
    private int getFreeSlot()
    {
		for(int i = 0; i < _connections.length; i++)
    	{
    		if(_connections[i] == null)
    		{
    			return i;
    		}
    	}
		return -1;
    }
    
    public void broadcast(byte[] data, int len)
    {
		for(int i = 0; i < _connections.length; i++)
    	{
    		if(_connections[i] != null)
    		{
    			_connections[i].send(data, len);
    		}
    	}
    }
    
    private void messageReceived(InetSocketAddress addr, byte flag, byte[] data, int len)
    {
    	for(int i = 0; i < _connections.length; i++)
    	{
    		UDPConnection con = _connections[i];
    		if(con != null && con.equals(addr))
    		{
    			con.messageReceived();
    			if(flag == UDPConnection.CTRL_NONE && con.getState() == ConnectionState.ONLINE)
    			{
    				for (NetworkListener l : _networkListener)
    				{
    					l.receivedMessage(con, data, len);
    				}
    			}
    			else if(flag == UDPConnection.CTRL_CONNECTACCEPT && con.getState() == ConnectionState.CONNECTING)
				{
    				System.out.println("connected to " + addr);
    				_connections[i].setState(ConnectionState.ONLINE);
					for (NetworkListener l : _networkListener)
    				{
    					l.connected(con);
    				}
				}
				else if(flag == UDPConnection.CTRL_CLOSE)
				{
					System.out.println(addr + " disconnected");
					for (NetworkListener l : _networkListener)
    				{
    					l.disconnected(con);
    				}
					_connections[i] = null;
				}
    			return;
    		}
    	}
    	
    	if(flag == UDPConnection.CTRL_CONNECT && _acceptConnections)
		{
    		int slot = this.getFreeSlot();
    		if(slot != -1)
    		{
    			System.out.println("accepted connection from " + addr);
    			_connections[slot] = new UDPConnection(_socket, addr);
    			_connections[slot].setState(ConnectionState.ONLINE);
    			_connections[slot].sendControl(UDPConnection.CTRL_CONNECTACCEPT);
    			for (NetworkListener l : _networkListener)
				{
					l.connected(_connections[slot]);
				}
    			return;
    		}
		}
    }
    
    /*private void messageReceivedConnless(InetSocketAddress addr, byte[] data, int len)
    {
    }*/
    
    private class ConnectionUpdater extends Thread
    {
		public void run()
	    {
	    	while(_socket != null && !_socket.isClosed())
	    	{
		    	for(int i = 0; i < _connections.length; i++)
		    	{
		    		if(_connections[i] == null)
		    			continue;
		    		
	    			if(_connections[i].getState() == ConnectionState.ERROR)
	    			{
	    				System.out.println(_connections[i] + " disconnected (error)");
	    				for (NetworkListener l : _networkListener)
	    				{
	    					l.disconnected(_connections[i]);
	    				}
	    				_connections[i] = null;
	    			}
	    			else
	    			{
	    				_connections[i].update();
	    			}
		    	}
		    	Thread.yield();
	    	}
	    }
    }
}
