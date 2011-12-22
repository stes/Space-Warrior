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
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

import sw.client.gui.GamePanel;
import sw.client.gui.LoginEvent;
import sw.client.gui.LoginListener;
import sw.client.gui.LoginPanel;
import sw.shared.Packer;
import sw.shared.Packettype;
import sw.shared.Unpacker;
import sw.shared.data.ServerInfo;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class SWFrame extends JFrame implements ClientListener,
		LoginListener
{
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
	private BufferedImage _drawing;
	private Insets _insets;

	/**
	 * Creates a new SWFrame
	 */
	public SWFrame()
	{
		super("Space Warrior");

		this.setIgnoreRepaint(true);
		
		RepaintManager repaintManager = new UnRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(d.width / 2, d.height / 2);
		this.setMinimumSize(new Dimension(800, 600));
        _insets = this.getInsets();
        int insetWide = _insets.left + _insets.right;
        int insetTall = _insets.top + _insets.bottom;
        setSize(getWidth() + insetWide, getHeight() + insetTall);

		((JComponent) getContentPane()).setOpaque(false);
		this.init();

		createBufferStrategy(2);
		_bufferStrategy = this.getBufferStrategy();

		_isRunning = true;
		gameLoop();
	}

	private void init()
	{
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
		_gamePanel = new GamePanel(this.getWidth(), this.getHeight(),
				_controller, _client);
		_loginPanel = new LoginPanel(this.getWidth(), this.getHeight());
		_loginPanel.setLocation(0, 30);

		_client.addClientListener(_gamePanel);
		_client.addClientListener(_loginPanel);

		_loginPanel.addLoginListener(this);

		this.setGUIMode(GUIMode.LOGIN);
		this.setVisible(true);
		this.toFront();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

//		try
//		{
//			System.setErr(new PrintStream(System.getProperty("user.dir")
//					+ "/buglog.txt"));
//		}
//		catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
	}

    /**
     * Method containing the game's loop.
     * Each iteration of the loop updates all animations and sprite locations
     * and draws the graphics to the screen
     */
    public void gameLoop()
    {
        long oldTime = System.nanoTime();
        long nanoseconds = 0;
        int frames = 0;
        _fps = 0;

        _drawing = (BufferedImage) this.createImage(getWidth(),getHeight());
        
        while(_isRunning)
        {
            // relating to updating animations and calculating FPS
            long elapsedTime = System.nanoTime() - oldTime;
            oldTime = oldTime + elapsedTime; //update for the next loop iteration
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
                g = (Graphics2D)_bufferStrategy.getDrawGraphics();
                draw(g); // enter the method to draw everything                 
            }
            finally
            {
                g.dispose();
            }
            if (!_bufferStrategy.contentsLost())
            {
                _bufferStrategy.show();
            }
            Toolkit.getDefaultToolkit().sync(); // prevents possible event queue problems in Linux
        }
    }

	private void draw(Graphics2D g)
	{
        Graphics2D drawingBoard = _drawing.createGraphics();
        drawingBoard.setColor(Color.LIGHT_GRAY);
        drawingBoard.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawingBoard.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);
        // draw over it to create a blank background again, (or you could draw
        // a background image if you had one
        drawingBoard.fillRect(0, 0, _drawing.getWidth(), _drawing.getHeight());
        
        // now draw everything to drawingBoard, location 0,0 will be top left corner
        // within the borders of the window
        if (_activePanel.equals(_gamePanel))
        {
        	_gamePanel.render(drawingBoard);
        }
        
        
        drawingBoard.setColor(Color.WHITE);
        drawingBoard.drawString("FPS: " + _fps, 0, drawingBoard.getFont().getSize());
        // NOTE: this will now cap the FPS (frames per second), of the program to
        // a max of 100 (1000 nanoseconds in a second, divided by 10 nanoseconds
        // of rest per update = 100 updates max).
    
         getLayeredPane().paintComponents(drawingBoard); // paint our Swing components
        // NOTE: make sure you do paint your own graphics first
        
         // show fps
         drawingBoard.setColor(Color.WHITE);
         //drawingBoard.drawString("FPS: " + _fps, 100, 100);
         
        // now draw the drawing board to correct area of the JFrame's buffer
        g.drawImage(_drawing, _insets.left, 30, null);
        
        drawingBoard.dispose();
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
		// this.repaint();
	}

	public void tick()
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

	class UnRepaintManager extends RepaintManager
	{
		public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
		{
		}

		public void addInvalidComponent(JComponent invalidComponent)
		{
		}

		public void markCompletelyDirty(JComponent aComponent)
		{
		}

		public void paintDirtyRegions()
		{
		}
	}

}
