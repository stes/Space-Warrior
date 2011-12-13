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
package sw.client;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetSocketAddress;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import sw.client.gui.GamePanel;
import sw.client.gui.LoginEvent;
import sw.client.gui.LoginListener;
import sw.client.gui.LoginPanel;
import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packer;
import sw.shared.data.PlayerDataSet;
import sw.shared.data.ServerInfo;
import sw.shared.data.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWFrame extends JFrame implements WindowListener, ClientListener,
		LoginListener
{
	private enum GUIMode
	{
		LOGIN, GAME
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1575599799999464878L;
	private GameController _controller;

	private SWClient _client;
	private GamePanel _gamePanel;
	private LoginPanel _loginPanel;

	private JPanel _activePanel;

	/**
	 * Creates a new SWFrame
	 */
	public SWFrame()
	{
		super("Space Warrior");

		_client = new SWClient();
		_controller = new GameController(_client);
		_client.addClientListener(this);
		_client.addClientListener(_controller);

		this.addWindowListener(this);

		System.out.println("init");

		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		
		//this.setSize(1000, 500);
		
		this.setExtendedState(MAXIMIZED_BOTH);
		System.out.println(this.getSize().toString());
		//this.setSize(GameConstants.REFERENCE_X + GameConstants.PLAYING_FIELD_WIDTH,
		//		GameConstants.REFERENCE_Y+ GameConstants.PLAYING_FIELD_HEIGHT);

		this.setResizable(false);
		
		
		
		_gamePanel = new GamePanel(this.getWidth(), this.getHeight(), _controller, _client);
		_loginPanel = new LoginPanel(this.getWidth(), this.getHeight());

		_client.addClientListener(_gamePanel);
		_client.addClientListener(_loginPanel);

		_loginPanel.addLoginListener(this);

		this.setGUIMode(GUIMode.LOGIN);
		this.setVisible(true);
		this.toFront();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// this.initEastereggs();
		this.updateServerList();
	}

	@Override
	public void chatMessage(String name, String text)
	{
	}

	@Override
	public void connected()
	{
		this.setGUIMode(GUIMode.GAME);

		Packer start = new Packer(Packettype.CL_START_INFO);
		start.writeUTF(_loginPanel.getName());
		_client.sendPacket(start);
	}

	@Override
	public void disconnected(String reason)
	{
		this.setGUIMode(GUIMode.LOGIN);
	}

	@Override
	public void login(LoginEvent e)
	{
		this.connect(e.getIPAdress());
	}

	@Override
	public void scan()
	{
		_client.scan();
	}

	@Override
	public void serverInfo(ServerInfo info)
	{
	}

	@Override
	public void shot(Unpacker packet)
	{
	}

	@Override
	public void snapshot(Unpacker packet)
	{
		if (_controller.getPlayerList().count() == 0)
			return;
		PlayerDataSet[] spielerScore = new PlayerDataSet[_controller
				.getPlayerList().count()];
		int anzahl = 0;
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++)
		{
			if (_controller.getPlayerList().dataAt(i) != null)
			{
				spielerScore[anzahl] = _controller.getPlayerList().dataAt(i);
				anzahl++;
			}
		}

		java.util.Arrays.sort(spielerScore);
		this.repaint();

		/*
		 * for(int i = 0; i < spielerScore.length; i++) {
		 * _tblPoints.setValueAt(spielerScore[spielerScore.length-i].name(), i,
		 * 0);
		 * _tblPoints.setValueAt(spielerScore[spielerScore.length-i].punkte(),
		 * i, 1); } for(int i = spielerScore.length; i <
		 * _tblPoints.getRowCount(); i++) { _tblPoints.setValueAt("", i, 0);
		 * _tblPoints.setValueAt("", i, 1); }
		 */
	}

	public void tick()
	{
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	// private void initEastereggs()
	// {
	// OutputStream output = new OutputStream()
	// {
	// @Override
	// public void write(byte[] b, int off, int len)
	// {
	// _lstChathistory.append(new String(b, off, len));
	// }
	//
	// @Override
	// public void write(int b) throws IOException
	// {
	// _lstChathistory.append(String.valueOf((char) b));
	// }
	// };
	//
	// InputStream input = new InputStream()
	// {
	// @Override
	// public int read()
	// {
	// return 0;
	// }
	// };
	//
	// //System.setOut(new PrintStream(output, true));
	// // -.-
	// System.setIn(input);
	//
	// _bfInterpreter = new BfInterpreter();
	// }

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		// TODO: is this everything?
		_client.close();
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

	/**
	 * Connects to a server
	 */
	private void connect(InetSocketAddress ip)
	{
		_client.connect(ip.getAddress().getHostAddress(), ip.getPort());
	}

	private void setGUIMode(GUIMode mode)
	{
		if (_activePanel != null)
			this.remove(_activePanel);
		if (mode == GUIMode.LOGIN)
		{
			_activePanel = _loginPanel;
		}
		else if (mode == GUIMode.GAME)
		{
			_activePanel = _gamePanel;
		}
		this.add(_activePanel);
		System.out.println("switch mode");
		this.setVisible(true);
		this.repaint();
	}

	/**
	 * updates server list
	 */
	private void updateServerList()
	{
	}
}
