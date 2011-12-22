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
package sw.client.gui;

import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.util.EventObject;

import sw.client.gui.ConnectionEvent.ActionType;

public class ConnectionEvent extends EventObject
{
	public enum ActionType
	{
		LOGIN, LOGOUT
	}

	private static final long serialVersionUID = -5796463210269739871L;

	private InetSocketAddress _ipAdress;
	private String _loginName;
	private int _imageID;

	private ActionType _actionType;

	public ConnectionEvent(Object source, ActionType actionType)
	{
		super(source);
		this.setActionType(actionType);
	}

	public void setIPAdress(InetSocketAddress ipAdress)
	{
		this._ipAdress = ipAdress;
	}

	public InetSocketAddress getIPAdress()
	{
		return _ipAdress;
	}

	public void setLoginName(String loginName)
	{
		this._loginName = loginName;
	}

	public String getLoginName()
	{
		return _loginName;
	}

	public void setImageID(int imageID)
	{
		_imageID = imageID;
	}

	public int getImageID()
	{
		return _imageID;
	}

	public void setActionType(ActionType _actionType)
	{
		this._actionType = _actionType;
	}

	public ActionType getActionType()
	{
		return _actionType;
	}
}
