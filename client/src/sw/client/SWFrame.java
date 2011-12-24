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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.InetSocketAddress;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

import sw.client.gui.ConnectionEvent;
import sw.client.gui.ConnectionListener;
import sw.client.gui.GamePanel;
import sw.client.gui.LoginPanel;
import sw.shared.Packettype;
import sw.shared.data.ServerInfo;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWFrame extends JFrame implements ClientListener, ConnectionListener
{
	// change to limit fps in order to minimize cpu usage
	public final int SLEEP_TIME = 1;
	
	private enum GUIMode
	{
		LOGIN, GAME
	}

	private static final long serialVersionUID = 1575599799999464878L;
	private GameController _controller;

	private SWClient _client;
	private GamePanel _gamePanel;
	private LoginPanel _loginPanel;

	private JPanel _activePanel;

	private BufferStrategy _bufferStrategy;
	private boolean _isRunning;
	private int _fps;
	private Insets _insets;

	/**
	 * Creates a new SWFrame
	 */
	public SWFrame(boolean debugMode)
	{
		super("Space Warrior");

		this.setIgnoreRepaint(true);

		RepaintManager repaintManager = new UnRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(d.width / 2, d.height / 2);
		this.setMinimumSize(new Dimension(800, 600));

		((JComponent) getContentPane()).setOpaque(false);
		
		this.init();
		
		createBufferStrategy(2);
		_bufferStrategy = this.getBufferStrategy();

		if (!debugMode)
			this.initBugLogger();

		
		_isRunning = true;
		gameLoop();
	}

	private void initBugLogger()
	{
		try
		{
			System.setErr(new PrintStream(System.getProperty("user.dir") + "/buglog.txt"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private void init()
	{
		this.setVisible(true);
		this.toFront();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		_insets = this.getInsets();
		System.out.println(_insets);
		int insetWide = _insets.left + _insets.right;
		int insetTall = _insets.top + _insets.bottom;
		setSize(getWidth() + insetWide, getHeight() + insetTall);
		
		_client = new SWClient();
		_controller = new GameController(_client);
		_client.addClientListener(this);
		_client.addClientListener(_controller);
		//
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				_client.close();
			}
		});
		System.out.println("init");
		this.setExtendedState(MAXIMIZED_BOTH);
		_gamePanel = new GamePanel(this.getWidth(), this.getHeight(), _controller, _client);
		_gamePanel.setLocation(_insets.left, _insets.top + 100);
		
		_loginPanel = new LoginPanel(this.getWidth(), this.getHeight());
		_loginPanel.setLocation(_insets.left, _insets.top);

		_client.addClientListener(_gamePanel);
		_client.addClientListener(_loginPanel);

		_loginPanel.addConnectionListener(this);
		_gamePanel.addConnectionListener(this);

		this.setGUIMode(GUIMode.LOGIN);

	}

	/**
	 * Method containing the game's loop. Each iteration of the loop updates all
	 * animations and sprite locations and draws the graphics to the screen
	 */
	public void gameLoop()
	{
		long oldTime = System.nanoTime();
		long nanoseconds = 0;
		int frames = 0;
		_fps = 0;

		while (_isRunning)
		{
			// fps
			long elapsedTime = System.nanoTime() - oldTime;
			oldTime = oldTime + elapsedTime;
			nanoseconds = nanoseconds + elapsedTime;
			frames = frames + 1;
			if (nanoseconds >= 1000000000)
			{
				_fps = frames;
				nanoseconds = nanoseconds - 1000000000;
				frames = 0;
			}
			// related to drawing
			Graphics2D g = null;
			try
			{
				g = (Graphics2D) _bufferStrategy.getDrawGraphics();
				render(g);
			}
			finally
			{
				g.dispose();
			}
			if (!_bufferStrategy.contentsLost())
			{
				_bufferStrategy.show();
			}
			Toolkit.getDefaultToolkit().sync();
			if (this.SLEEP_TIME > 0)
			{
				try
				{
					Thread.sleep(this.SLEEP_TIME);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void render(Graphics2D g2d)
	{
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (_activePanel.equals(_gamePanel))
		{
			_gamePanel.render(g2d);
		}
		else if (_activePanel.equals(_loginPanel))
		{
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, getWidth(), getHeight());
			_loginPanel.render(g2d);
		}
		g2d.setColor(Color.WHITE);
		g2d.drawString("FPS: " + _fps, 0, 100+g2d.getFont().getSize());
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
		start.writeInt(_loginPanel.getImageID());
		_client.sendPacket(start);
	}

	@Override
	public void disconnected(String reason)
	{
		this.setGUIMode(GUIMode.LOGIN);
	}

	@Override
	public void login(ConnectionEvent e)
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
	public void snapshot(Unpacker packet)
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
		{
			this.remove(_activePanel);
		}
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
	}

	class UnRepaintManager extends RepaintManager
	{
		@Override
		public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
		{
		}

		@Override
		public void addInvalidComponent(JComponent invalidComponent)
		{
		}

		@Override
		public void markCompletelyDirty(JComponent aComponent)
		{
		}

		@Override
		public void paintDirtyRegions()
		{
		}
	}

	@Override
	public void logout(ConnectionEvent e)
	{
		_client.disconnect("user logout");
	}
}
