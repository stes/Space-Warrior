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

import java.util.EventObject;

public class LoginEvent extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5796463210269739871L;
	
	private String _ipAdress;
	private String _loginName;

	public LoginEvent(Object source, String ipAdress, String loginName)
	{
		super(source);
		this.setIPAdress(ipAdress);
		this.setLoginName(loginName);
	}

	public void setIPAdress(String ipAdress)
	{
		this._ipAdress = ipAdress;
	}

	public String getIPAdress()
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
}
