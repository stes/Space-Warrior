package sw.client.gui;

import java.awt.AWTEvent;
import java.awt.Event;
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
