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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

import sw.client.IClient;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.GameStateChangedListener;
import sw.client.gcontrol.IGameStateManager;

/**
 * @author Redix, stes
 * @version 08.01.2012
 */
public class GamePanel extends JPanel implements
		GameStateChangedListener
{
	private static final long serialVersionUID = -8751902318746091633L;
	private PlayingFieldPanel _playingField;
	private StateBarPanel _stateBarPanel;
	// other references
	private IGameStateManager _stateManager;
	private IClient _client;
	private ArrayList<ConnectionListener> _connectionListener;

	public GamePanel(int width, int height, IGameStateManager stateManager, IClient client)
	{
		super();
		_connectionListener = new ArrayList<ConnectionListener>();
		_stateManager = stateManager;
		_client = client;
		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.BLACK);
		this.initComponents();
		this.resizeComponents();

		this.setIgnoreRepaint(true);

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				resizeComponents();
			}
		});
	}


	public void addConnectionListener(ConnectionListener l)
	{
		_connectionListener.add(l);
	}

	public void added()
	{
		_playingField.startThreads();
	}


	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{}

	@Override
	public void newRound(GameStateChangedEvent e)
	{}

	@Override
	public void playerInit(GameStateChangedEvent e)
	{
		// TODO best practice?
		_playingField.playerInit();
	}

	public void removeConnecionListener(ConnectionListener l)
	{
		_connectionListener.remove(l);
	}

	public void removed()
	{
		_playingField.stopThreads();
	}

	public void render(Graphics2D g)
	{
		super.paintComponents(g.create());
	}

	protected void invokeDisconnect(ConnectionEvent e)
	{
		if (_connectionListener.size() == 0)
		{
			return;
		}
		for (ConnectionListener l : _connectionListener)
		{
			l.logout(e);
		}

	}

	private void initComponents()
	{
		_playingField = new PlayingFieldPanel(this.getWidth(), this.getHeight(), _stateManager);
		this.add(_playingField);
		_stateBarPanel = new StateBarPanel(getWidth(), getHeight(), _stateManager, _client);
		this.add(_stateBarPanel);
	}

	private void resizeComponents()
	{
		int x = 8;
		
		_playingField.setSize(getWidth(), getHeight()*x/10);
		_stateBarPanel.setSize(getWidth(), getHeight()*(10-x)/10);
		_stateBarPanel.setLocation(0, getHeight()*x/10);
	}

}
