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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class UDPConnection
{
	public final static int MAX_PACKET_LENGTH = 4*1024;
	public final static int PACKET_HEADER_LENGTH = 1;
	
	public final static byte CTRL_NONE = 0;
	public final static byte CTRL_KEEPALIVE = 1;
	public final static byte CTRL_CONNECT = 2;
	public final static byte CTRL_CONNECTACCEPT = 3;
	public final static byte CTRL_CLOSE = 4;
	
	public enum ConnectionState
	{
		OFFLINE,
		CONNECTING,
		ONLINE,
		DISCONNECTED,
		ERROR,
	}
	
    private DatagramSocket _socket;
    private InetSocketAddress _addr;
    private long _lastRecvTime;
    private long _lastSendTime;
    
    private ConnectionState _state;

    public UDPConnection(DatagramSocket socket, InetSocketAddress addr)
    {
    	_socket = socket;
    	_addr = addr;
    	_state = ConnectionState.OFFLINE;
    	this.messageReceived();
    }
    
    public void setState(ConnectionState state)
    {
    	_state = state;
    }
    
    public ConnectionState getState()
    {
    	return _state;
    }
    
    public void messageReceived()
    {
    	_lastRecvTime = System.currentTimeMillis();
    }
    
    public void update()
    {
    	long now = System.currentTimeMillis();
		
		if(_state == ConnectionState.CONNECTING || _state == ConnectionState.ONLINE)
		{
			if((now - _lastRecvTime) > 1000*10)
			{
				System.out.println("timeout: " + _addr);
				_state = ConnectionState.ERROR;
			}
			
			if(now - _lastSendTime > 1000 && _state == ConnectionState.ONLINE)
			{
				//System.out.println("send keepalive to " + _addr);
				this.sendControl(CTRL_KEEPALIVE);
			}
		}
    }
    
    private void send(byte[] data, int len, byte flag)
    {
    	try
    	{
	    	if(_state != ConnectionState.ERROR)
	    	{
	            byte[] buf = new byte[MAX_PACKET_LENGTH];
	            buf[0] = flag;
	            int packetSize = PACKET_HEADER_LENGTH;
	            if(data != null)
	            {
	            	int size = Math.min(data.length, buf.length-PACKET_HEADER_LENGTH);
	            	System.arraycopy(data, 0, buf, PACKET_HEADER_LENGTH, size);
	            	packetSize += size;
	            }
	            DatagramPacket packet = new DatagramPacket(buf, packetSize, _addr);
				_socket.send(packet);
	    		_lastSendTime = System.currentTimeMillis();
	    	}
    	}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }
    
    public void send(byte[] data, int len)
    {
    	if(_state == ConnectionState.ONLINE)
    	{
    		this.send(data, data.length, CTRL_NONE);
    	}
    }
    
    public void sendControl(byte flag)
    {
    	this.send(null, 0, flag);
    }
    
    public void disconnect()
    {
    	System.out.println("disconnected from  " + _addr);
    	this.sendControl(CTRL_CLOSE);
    	_state = ConnectionState.DISCONNECTED;
    }
    
    @Override
    public boolean equals(Object obj)
    {
    	if(obj instanceof UDPConnection)
    	{
    		UDPConnection con = (UDPConnection) obj;
    		return _addr.equals(con._addr);
    	}
    	else if(obj instanceof InetSocketAddress)
    	{
    		InetSocketAddress addr = (InetSocketAddress) obj;
    		return _addr.equals(addr);
    	}
    	else
    	{
    		return false;
    	}
    }
    
    @Override
    public String toString()
    {
    	return _addr.toString();
    }
}
