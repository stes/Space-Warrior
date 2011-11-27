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
import java.net.InetSocketAddress;
import java.util.ArrayList;

import sw.shared.net.NetworkListener;
import sw.shared.net.NetworkReceiver;
import sw.shared.net.UDPConnection;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class UDPClient extends Thread implements NetworkListener
{
    private DatagramSocket _socket;
    private NetworkReceiver _receiver;
    
    private UDPConnection _server;
    
    private ArrayList<NetworkClientListener> _networkClientListener;
    
    public UDPClient()
    {
        try
        {
            _socket = new DatagramSocket();
            _networkClientListener = new ArrayList<NetworkClientListener>();
            _receiver = new NetworkReceiver(_socket);
            _receiver.addNetworkListener(this);
            _receiver.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void connect(InetSocketAddress addr)
    {
    	_server = new UDPConnection(_socket, addr);
    	_server.sendControl(UDPConnection.CTRL_CONNECT);
    }
    
    public void addNetworkClientListener(NetworkClientListener listener)
	{
    	_networkClientListener.add(listener);
	}
    
    @Override
	public void run()
    {
    	while(_server != null)
    	{
			if(_server.timeout())
			{
				_server = null;
				for (NetworkClientListener l : _networkClientListener)
				{
					l.clientDisconnected();
				}
			}
			else
			{
				_server.update();
			}
			Thread.yield();
    	}
    }
    
    public void send(byte[] data, int len)
    {
    	if(_server != null)
		{
    		_server.send(data, len);
		}
    }
    
    public void disconnect()
    {
    	if(_server != null)
		{
    		_server.disconnect();
    		_server = null;
			for (NetworkClientListener l : _networkClientListener)
			{
				l.clientDisconnected();
			}
		}
    }
    
    @Override
    public void messageReceived(InetSocketAddress addr, byte flag, byte[] data, int len)
    {
    	if(_server != null && _server.equals(addr))
    	{
	    	_server.messageReceived();
			if(flag == UDPConnection.CTRL_NONE)
			{
				for (NetworkClientListener l : _networkClientListener)
				{
					l.clientReceivedMessage(data, len);
				}
			}
			else if(flag == UDPConnection.CTRL_CONNECTACCEPT)
			{
				System.out.println("connected: " + _server);
				_server.setConnected();
				for (NetworkClientListener l : _networkClientListener)
				{
					l.clientConnected();
				}
			}
			else if(flag == UDPConnection.CTRL_CLOSE)
			{
				System.out.println("disconnect");
				_server = null;
				for (NetworkClientListener l : _networkClientListener)
				{
					l.clientDisconnected();
				}
			}
    	}
    }
    
    @Override
    public void messageReceivedConnless(InetSocketAddress addr, byte[] data, int len)
    {
    }
}
