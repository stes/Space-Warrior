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

import java.net.InetSocketAddress;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class UDPConnection
{
	protected final static byte FLAG_CONTROL = 1;
	protected final static byte FLAG_CONNLESS = 2;
	
	protected final static byte CTRL_KEEPALIVE = 0;
	protected final static byte CTRL_CONNECT = 1;
	protected final static byte CTRL_CONNECTACCEPT = 2;
	protected final static byte CTRL_CLOSE = 3;
	
	private enum ConnectionState
	{
		OFFLINE,
		CONNECTING,
		ONLINE,
		ERROR
	}
	
    private UDPHost _host;
    private InetSocketAddress _addr;
    private long _lastRecvTime;
    private long _lastSendTime;
    
    private ConnectionState _state;

    public UDPConnection(UDPHost host, InetSocketAddress addr)
    {
    	_host = host;
    	_addr = addr;
    	_state = ConnectionState.OFFLINE;
    	_lastRecvTime = System.currentTimeMillis();
    	_lastSendTime = System.currentTimeMillis();
    }
    
    protected boolean error()
    {
    	return _state == ConnectionState.ERROR;
    }
    
    protected void connect()
    {
    	if(_state == ConnectionState.OFFLINE)
    	{
	    	System.out.println("connecting to " + _addr);
	    	_state = ConnectionState.CONNECTING;
			this.sendControl(UDPConnection.CTRL_CONNECT, null, 0);
    	}
    }
    
    protected void received(byte flag, byte[] data, int len)
    {
    	_lastRecvTime = System.currentTimeMillis();
    	byte control = data[0];
    	
    	if((flag & UDPConnection.FLAG_CONTROL) > 0)
    	{
	    	if(_state == ConnectionState.OFFLINE && control == CTRL_CONNECT)
			{
	    		System.out.println("accepted connection from " + _addr);
	    		_state = ConnectionState.ONLINE;
				this.sendControl(UDPConnection.CTRL_CONNECTACCEPT, null, 0);
				_host.invokeConnected(this);
			}
			else if(_state == ConnectionState.CONNECTING && control == UDPConnection.CTRL_CONNECTACCEPT)
			{
				System.out.println("connected to " + _addr);
				_state = ConnectionState.ONLINE;
				_host.invokeConnected(this);
			}
			else if(control == UDPConnection.CTRL_CLOSE)
			{
				String reason = new String(data, 1, len);
				if(reason.length() > 0)
		    		System.out.println("disconnected (" + reason + ")");
		    	else
		    		System.out.println("disconnected");
				_host.invokeDisconnected(this, reason);
			}
    	}
    	else if(_state == ConnectionState.ONLINE)
		{
			_host.invokeReceivedMessage(this, data, len);
		}
    }
    
    protected void update()
    {
    	long now = System.currentTimeMillis();
		
		if(_state == ConnectionState.CONNECTING || _state == ConnectionState.ONLINE)
		{
			if((now - _lastRecvTime) > 1000*10)
			{
				System.out.println("timeout: " + _addr);
				_state = ConnectionState.ERROR;
				_host.invokeDisconnected(this, "timeout");
			}
			
			if(now - _lastSendTime > 1000 && _state == ConnectionState.ONLINE)
			{
				//System.out.println("send keepalive to " + _addr);
				this.sendControl(CTRL_KEEPALIVE, null, 0);
			}
		}
    }
    
    private void sendControl(byte msg, byte[] data, int len)
    {
    	_host.sendControl(_addr, msg, data, len);
    	_lastSendTime = System.currentTimeMillis();
    }
    
    public void send(byte[] data, int len)
    {
    	if(_state == ConnectionState.ONLINE)
    	{
            _host.send(_addr, data, len);
    		_lastSendTime = System.currentTimeMillis();
    	}
    }
    
    public void disconnect(String reason)
    {
    	if(_state == ConnectionState.CONNECTING || _state == ConnectionState.ONLINE)
    	{
    		if(reason.length() > 0)
    			System.out.println("disconnected from  " + _addr + " (" + reason + ")");
    		else
    			System.out.println("disconnected from  " + _addr);
	    	byte[] data = reason.getBytes();
	    	this.sendControl(CTRL_CLOSE, data, data.length);
	    	_state = ConnectionState.ERROR;
	    	_host.invokeDisconnected(this, "");
    	}
    }
    
    public void disconnect()
    {
    	this.disconnect("");
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
}
