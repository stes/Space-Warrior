/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
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

import javax.swing.JPanel;

import sw.client.control.IGameController;
import sw.client.events.gcontrol.GameStateChangedEvent;
import sw.client.events.gcontrol.IGameStateChangedListener;
import sw.client.net.IClient;
import sw.shared.GameConstants;

/**
 * @author Redix, stes
 * @version 08.01.2012
 */
public class GamePanel extends JPanel implements
		IGameStateChangedListener
{
	private static final long serialVersionUID = -8751902318746091633L;
	private PlayingFieldPanel _playingField;
	private StateBarPanel _stateBarPanel;
	// other references
	private IGameController _stateManager;
	private IClient _client;

	public GamePanel(int width, int height, IGameController stateManager, IClient client)
	{
		super();
		_stateManager = stateManager;
		_client = client;

		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.BLACK);
		this.initComponents();
		this.resizeComponents();

		_client.addClientMessageListener(_stateBarPanel);

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


	public void addConnectionListener(ILoginPanelListener l)
	{
		_stateBarPanel.addConnectionListener(l);
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
		System.out.println(_stateManager.getLocalPlayer());
		if (_stateManager.getLocalPlayer() instanceof IWeaponChangedListener)
		{
			_stateBarPanel.addWeaponChangedListener((IWeaponChangedListener)_stateManager.getLocalPlayer());
		}
	}

	public void removeConnecionListener(ILoginPanelListener l)
	{
		_stateBarPanel.removeConnecionListener(l);
	}

	public void removed()
	{
		_playingField.stopThreads();
	}

	public void render(Graphics2D g)
	{
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponents(g.create());
	}

	private void initComponents()
	{
		int height = (int) (GameConstants.PLAYING_FIELD_HEIGHT*getScale());
		int width = (int) (GameConstants.PLAYING_FIELD_WIDTH*getScale());
		_playingField = new PlayingFieldPanel(width, height, _stateManager);
		this.add(_playingField);
		_stateBarPanel = new StateBarPanel(getWidth(), getHeight(), _stateManager, _client);
		this.add(_stateBarPanel);
	}
	
	private double getScale()
	{
		return Math.min(getScaleX(), getScaleY());
	}
	
	private double getScaleX()
	{
		return (double) this.getWidth() / (double) GameConstants.PLAYING_FIELD_WIDTH;
	}

	private double getScaleY()
	{
		return (double) this.getHeight()*0.8 / (double) GameConstants.PLAYING_FIELD_HEIGHT;
	}

	private void resizeComponents()
	{
		int x = 8;
		int height = (int) (GameConstants.PLAYING_FIELD_HEIGHT*getScale());
		int width = (int) (GameConstants.PLAYING_FIELD_WIDTH*getScale());
		_playingField.setSize(width, height);
		_playingField.setLocation(getWidth()/2 - width/2, (getHeight()*x/10)/2-height/2);
		_stateBarPanel.setSize(getWidth(), getHeight()*(10-x)/10);
		_stateBarPanel.setLocation(0, getHeight()*x/10);
	}

}
