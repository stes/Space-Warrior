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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import sw.shared.GameConstants;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class Program implements WindowListener
{
	public static void main(String[] args)
	{
		(new Program()).run();
	}

	private ServerGUI _serverGUI;

	private SWServer _server;

	public Program()
	{
		_serverGUI = new ServerGUI(800, 400);
		_server = new SWServer(GameConstants.STANDARD_PORT);
		_serverGUI.setNetServer(_server);

		_serverGUI.addWindowListener(this);
	}

	public void run()
	{
		int lastSize = 0;
		while (true)
		{
			Vector<Client> clientList = _server.clListe();
			if (clientList.size() != lastSize)
			{
				_serverGUI.setClientList(clientList);
				lastSize = clientList.size();
			}
			_server.tick();
		}
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		// TODO: is this everything?
		_server.close();
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}
}
