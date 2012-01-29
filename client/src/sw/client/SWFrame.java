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

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

import sw.client.gui.ConnectionEvent;
import sw.client.gui.ConnectionListener;
import sw.client.gui.GamePanel;
import sw.client.gui.LoginPanel;
import sw.shared.GameConstants;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public final class SWFrame extends JFrame implements ClientConnectionListener, ConnectionListener,
		AWTEventListener
{
	public static PrintStream out;

	private enum GUIMode
	{
		LOGIN, GAME
	}

	class UnRepaintManager extends RepaintManager
	{
		@Override
		public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
		{}

		@Override
		public void addInvalidComponent(JComponent invalidComponent)
		{}

		@Override
		public void markCompletelyDirty(JComponent aComponent)
		{}

		@Override
		public void paintDirtyRegions()
		{}
	}

	// change to limit fps in order to minimize cpu usage
	public final int SLEEP_TIME = 0;
	private static final long serialVersionUID = 1575599799999464878L;

	private final GameController _controller;

	private GamePanel _gamePanel;
	private LoginPanel _loginPanel;

	private JPanel _activePanel;
	// should be okay for now
	private VolatileImage _screen;
	private BufferStrategy _bufferStrategy;
	private boolean _isRunning;
	private int _fps;
	private SWFrame _self;

	/**
	 * Creates a new SWFrame
	 */
	public SWFrame(boolean debugMode)
	{
		super("Space Warrior");

		out = new PrintStream(new OutputStream()
		{
			@Override
			public void write(int i) throws IOException
			{}
		}, true);

		// TODO change to redirect the outputs
		out = System.out;

		_self = this;
		this.setIgnoreRepaint(true);
		RepaintManager repaintManager = new UnRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(d.width, d.height);
		this.setMinimumSize(new Dimension(800, 600));

		((JComponent) this.getContentPane()).setOpaque(false);

		_controller = new GameController();

		this.init(debugMode);

		this.createBufferStrategy(2);
		_bufferStrategy = this.getBufferStrategy();
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
		_screen = this.createVolatileImage(this.getWidth(), this.getHeight());
		_isRunning = true;
		new Thread()
		{
			@Override
			public void run()
			{
				_self.renderLoop();
			}
		}.start();
		new Thread()
		{
			private long _lastUpdate;

			@Override
			public void run()
			{
				_lastUpdate = System.currentTimeMillis();
				while (true)
				{
					long curTime = System.currentTimeMillis();
					if (curTime - _lastUpdate >= GameConstants.TICK_INTERVAL)
					{
						_controller.tick();
						_controller.broadcastSnapshots();
						_lastUpdate = curTime;
					}
					Thread.yield();
				}
			}
		}.start();

		this.setFullscreen(false);
	}

	private void setFullscreen(boolean active)
	{
		this.dispose();
		this.setUndecorated(active);
		this.pack();
		this.setVisible(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.toFront();
	}

	private void switchFullscreen()
	{
		setFullscreen(!isUndecorated());
	}

	@Override
	public void connected()
	{
		this.setGUIMode(GUIMode.GAME);
		// Packer start = new Packer(Packettype.CL_START_INFO);
		// start.writeUTF(_loginPanel.getName());
		// start.writeInt(_loginPanel.getImageID());
		// _client.sendPacket(start);
		_controller.playerConnected(_loginPanel.getName(), _loginPanel.getImageID());
	}

	@Override
	public void disconnected(String reason)
	{
		this.setGUIMode(GUIMode.LOGIN);
		// enough for now
		if (!reason.equals(""))
		{
			JOptionPane.showMessageDialog(this, reason);
		}
	}

	@Override
	public void login(ConnectionEvent e)
	{
		// InetSocketAddress addr = e.getIPAdress();
		// _client.connect(addr.getAddress().getHostAddress(), addr.getPort());
		this.connected();
	}

	@Override
	public void logout(ConnectionEvent e)
	{
		// _client.disconnect("user logout");
		this.disconnected("user logout");
	}

	public void render(Graphics2D g2d)
	{
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		if (_activePanel.equals(_gamePanel))
		{
			_gamePanel.render(g2d);
		}
		else if (_activePanel.equals(_loginPanel))
		{
			_loginPanel.render(g2d);
		}
		g2d.setColor(Color.WHITE);
		g2d.drawString("FPS: " + _fps, 0, 100 + g2d.getFont().getSize());
	}

	/**
	 * Method containing the game's loop. Each iteration of the loop updates all
	 * animations and sprite locations and draws the graphics to the screen
	 */
	public void renderLoop()
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
			Graphics g = null;
			try
			{
				g = _bufferStrategy.getDrawGraphics();
			}
			catch (IllegalStateException e)
			{
				continue;
			}
			synchronized (g)
			{
				try
				{
					do
					{
						int state = _screen.validate(this.getGraphicsConfiguration());
						if (state == VolatileImage.IMAGE_INCOMPATIBLE)
						{
							_screen = this.createVolatileImage(this.getWidth(), this.getHeight());
						}
						Graphics2D g2d = _screen.createGraphics();
						this.render(g2d);
						g2d.dispose();
						Insets insets = this.getInsets();
						g.drawImage(_screen, insets.left, insets.top, null);
					}
					while (_screen.contentsLost());
				}
				finally
				{
					g.dispose();
				}
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

	@Override
	public void scan()
	{
		// _client.scan();
	}

	private void init(boolean debugMode)
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets insets = this.getInsets();
		int insetWide = insets.left + insets.right;
		int insetTall = insets.top + insets.bottom;
		this.setSize(this.getWidth() + insetWide, this.getHeight() + insetTall);

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				// _client.close();
				_loginPanel.close();
			}
		});

		_gamePanel = new GamePanel(this.getWidth(), this.getHeight(), _controller, null);
		_loginPanel = new LoginPanel(this.getWidth(), this.getHeight());

		_controller.addGameStateChangedListener(_gamePanel);

		// _client.addClientConnectionListener(this);
		// _client.addClientConnectionListener(_controller);
		// _client.addClientMessageListener(_controller);
		// _client.addClientMessageListener(_gamePanel);
		// _client.addClientConnlessListener(_loginPanel);

		_loginPanel.addConnectionListener(this);
		_gamePanel.addConnectionListener(this);

		this.setGUIMode(GUIMode.LOGIN);

		if (!debugMode)
		{
			this.initBugLogger();
		}
		
		_controller.init();
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

	private void setGUIMode(GUIMode mode)
	{
		if (_activePanel != null)
		{
			this.remove(_activePanel);
		}
		if (mode == GUIMode.LOGIN)
		{
			_activePanel = _loginPanel;
			// _gamePanel.removed();
		}
		else if (mode == GUIMode.GAME)
		{
			_activePanel = _gamePanel;
			_gamePanel.added();
		}
		this.add(_activePanel);
		this.setVisible(true);
	}

	@Override
	public void eventDispatched(AWTEvent event)
	{
		if (event instanceof KeyEvent)
		{
			KeyEvent e = (KeyEvent) event;
			if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_F2)
				this.switchFullscreen();
		}
	}
}
