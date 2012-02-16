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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import sw.client.ClientConnlessListener;
import sw.client.SWFrame;
import sw.client.gui.LoginPanelEvent.ActionType;
import sw.client.plugins.PluginLoader;
import sw.server.SWServer;
import sw.shared.GameConstants;
import sw.shared.GameConstants.Images;
import sw.shared.data.ServerInfo;

/**
 * @author Redix, stes
 * @version 08.01.2012
 */
public class LoginPanel extends JPanel implements ClientConnlessListener
{
	private class ServerTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 6220705373064394932L;

		private String[] _headers = { "Server", "Players/Max" };

		@Override
		public int getColumnCount()
		{
			return _headers.length;
		}

		@Override
		public String getColumnName(int col)
		{
			return _headers[col];
		}

		@Override
		public int getRowCount()
		{
			return _servers.size();
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			switch (col)
			{
				case 0:
					return _servers.get(row).getServerName();
				case 1:
					return _servers.get(row).getMaxPlayers() + "/"
							+ _servers.get(row).getNumPayers();
				default:
					return null;
			}
		}

		@Override
		public boolean isCellEditable(int row, int col)
		{
			return false;
		}
	}

	private static final long serialVersionUID = -2058618925690808825L;

	private LoginPanel _self;
	private JTextField _txtIPAddress;
	private JTextField _txtPort;
	private JTextField _txtName;
	private JButton _btnConnect;
	private JButton _btnUpdate;
	private JButton _btnHost;
	private JLabel _lblIPAdress;
	private JLabel _lblName;
	private JScrollPane _sclServerTable;
	private JTable _tblServers;
	private JTable _tblAIPlayers;
	private JButton _btnImage;
	private JButton _btnExit;
	private JLabel _lblPort;
	private Vector<ServerInfo> _servers;
	private ServerTableModel _serverTableModel;
	private JCheckBox _chkIsMultiplayer;

	private ArrayList<JComponent> _mpOnly;
	private boolean _isMultiplayer;

	private JPanel _connectionPanel;

	private ArrayList<ILoginPanelListener> _connectionListener;
	private int _imageID;
	protected SWServer _server;
	private PluginLoader _pluginLoader;

	private BufferedImage _background;

	public LoginPanel(int width, int height)
	{
		super();
		_self = this;
		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.WHITE);

		_mpOnly = new ArrayList<JComponent>();
		_isMultiplayer = true;
		_connectionListener = new ArrayList<ILoginPanelListener>();

		_servers = new Vector<ServerInfo>();
		_serverTableModel = new ServerTableModel();

		_imageID = Images.min().getID();
		_background = ImageContainer.getLocalInstance().getImage(Images.TITLE);
		_pluginLoader = new PluginLoader();
		// _pluginLoader.addDirectory("C:\\Users\\Steffen\\Projekte\\Projekte\\SpaceWarrior\\current_build\\ai_players",
		// "sample");
		this.initComponents();

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				_connectionPanel.setLocation(getWidth() / 2 - _connectionPanel.getWidth() / 2,
						getHeight() / 2 - _connectionPanel.getHeight() / 2);
				// TODO rework
				if (_tblAIPlayers != null)
				{
					_tblAIPlayers.setBounds(_self.getWidth() - 600, 300, 200, 300);
				}
				_self.repaint();
			}
		});
	}

	public void addConnectionListener(ILoginPanelListener l)
	{
		_connectionListener.add(l);
	}

	public void close()
	{
		if (_server != null)
		{
			_server.close();
		}
	}

	public int getImageID()
	{
		return _imageID;
	}

	public String getIP()
	{
		return _txtIPAddress.getText();
	}

	@Override
	public String getName()
	{
		return _txtName.getText();
	}

	public void removeConnecionListener(ILoginPanelListener l)
	{
		_connectionListener.remove(l);
	}

	public void render(Graphics g)
	{
		g.drawImage(_background, 0, 0, getWidth(), getHeight(), null);
		this.paintComponents(g);
	}

	@Override
	public void serverInfo(ServerInfo info)
	{
		_servers.add(info);
		_serverTableModel.fireTableDataChanged();
	}

	protected void invokeLogin(LoginPanelEvent e)
	{
		if (_connectionListener.size() == 0)
		{
			return;
		}
		for (ILoginPanelListener l : _connectionListener)
		{
			l.login(e);
		}

	}
	
	protected void invokeSwitchMode(LoginPanelEvent e)
	{
		if (_connectionListener.size() == 0)
		{
			return;
		}
		for (ILoginPanelListener l : _connectionListener)
		{
			l.switchMode(e);
		}

	}

	private void addComponent(JComponent comp, int gridX, int gridY)
	{
		comp.setLocation(50 + gridX * 100, 30 + gridY * 30 + (gridY >= 3 ? 10 : 0));
		_connectionPanel.add(comp);
	}

	private void addComponent(JComponent comp, int gridX, int gridY, int dist)
	{
		comp.setLocation(50 + gridX * 100 + dist, 30 + gridY * 30 + (gridY >= 3 ? 10 :0));
		_connectionPanel.add(comp);
	}

	private void initLoginComponents()
	{
		_connectionPanel = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -5620238839722226151L;

			public void paintComponent(Graphics g)
			{
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
				g2d.setColor(this.getBackground());
				Rectangle bounds = g2d.getClip().getBounds();
				g2d.fillRect((int) bounds.getX(),
						(int) bounds.getY(),
						(int) bounds.getWidth(),
						(int) bounds.getHeight());
				super.paintComponents(g);

			}
		};

		_connectionPanel.setLayout(null);
		_connectionPanel.setBackground(Color.GRAY);
		_connectionPanel.setLocation(getWidth() / 2 - _connectionPanel.getWidth() / 2, getHeight()
				/ 2 - _connectionPanel.getHeight() / 2);
		_connectionPanel.setSize(600, 250);
		this.add(_connectionPanel);

		_txtIPAddress = new JTextField();
		_txtIPAddress.setSize(100, 25);
		_txtIPAddress.setText("localhost");
		_mpOnly.add(_txtIPAddress);
		addComponent(_txtIPAddress, 1, 0);

		_txtPort = new JTextField();
		_txtPort.setSize(50, 25);
		_txtPort.setText(GameConstants.STANDARD_PORT + "");
		_mpOnly.add(_txtPort);
		addComponent(_txtPort, 1, 1);

		_txtName = new JTextField();
		_txtName.setSize(300, 25);
		_txtName.setText("test");
		addComponent(_txtName, 1, 2);

		_lblIPAdress = new JLabel("IP-Address");
		_lblIPAdress.setSize(100, 25);
		_mpOnly.add(_lblIPAdress);
		addComponent(_lblIPAdress, 0, 0);

		_lblPort = new JLabel("Port");
		_lblPort.setSize(100, 25);
		_mpOnly.add(_lblPort);
		addComponent(_lblPort, 0, 1);

		_lblName = new JLabel("Name");
		_lblName.setSize(100, 25);
		addComponent(_lblName, 0, 2);

		_btnConnect = new JButton("Start");
		_btnConnect.setSize(100, 25);
		_btnConnect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (!_txtName.getText().isEmpty())
				{
					SWFrame.out.println("connect");
					LoginPanelEvent e = new LoginPanelEvent(this, ActionType.LOGIN);
					e.setIPAdress(new InetSocketAddress(_txtIPAddress.getText(),
							Integer.parseInt(_txtPort.getText())));
					e.setLoginName(_txtName.getText());
					e.setImageID(_imageID);
					_self.invokeLogin(e);
				}
			}
		});
		addComponent(_btnConnect, 3, 0);

		_btnHost = new JButton("Host");
		_btnHost.setSize(100, 25);
		_btnHost.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				_server = new SWServer(GameConstants.STANDARD_PORT);
			}
		});
		_mpOnly.add(_btnHost);
		addComponent(_btnHost, 4, 3, 20);

		_btnImage = new JButton();
		_btnImage.setIcon(new ImageIcon(ImageContainer.getLocalInstance().getImage(_imageID)));
		_btnImage.setSize(64, 64);
		_btnImage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				_imageID++;
				if (_imageID > Images.max().getID())
				{
					_imageID = Images.min().getID();
				}
				if (_imageID < Images.min().getID())
				{
					_imageID = Images.max().getID();
				}
				_btnImage.setIcon(new ImageIcon(ImageContainer.getLocalInstance().getImage(_imageID).getScaledInstance(64,
						64,
						1)));
			}
		});
		addComponent(_btnImage, 4, 0, 20);

		_chkIsMultiplayer = new JCheckBox("Singleplayer");
		_chkIsMultiplayer.setSize(100, 25);
		_chkIsMultiplayer.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				_isMultiplayer = (e.getStateChange() != ItemEvent.SELECTED);
				for (JComponent c : _mpOnly)
				{
					c.setEnabled(_isMultiplayer);
				}
				LoginPanelEvent ev = new LoginPanelEvent(this, ActionType.SWITCH_MODE);
				ev.setMultiplayer(_isMultiplayer);
				invokeSwitchMode(ev);
			}
		});
		addComponent(_chkIsMultiplayer, 0, 3);

		_btnExit = new JButton("Exit");
		_btnExit.setSize(100, 25);
		_btnExit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		addComponent(_btnExit, 0, 5);
		
		_btnUpdate = new JButton("Update");
		_btnUpdate.setSize(100, 25);
		_btnUpdate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				_servers.clear();
				for (ILoginPanelListener l : _connectionListener)
				{
					l.scan();
				}
			}
		});
		addComponent(_btnUpdate, 4, 4, 20);
		_mpOnly.add(_btnUpdate);
		
		_tblServers = new JTable(_serverTableModel);
		_tblServers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_tblServers.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int index = _tblServers.getSelectedRow();
				_txtIPAddress.setText(_servers.get(index).getAddress().getAddress().getHostAddress());
				_txtPort.setText(_servers.get(index).getAddress().getPort() + "");
			}
		});
		_mpOnly.add(_tblServers);

		_sclServerTable = new JScrollPane(_tblServers);
		_sclServerTable.setSize(200, 85);
		addComponent(_sclServerTable, 2, 3);
		_mpOnly.add(_sclServerTable);
	}

	/**
	 * Initializes the GUI components
	 */
	private void initComponents()
	{
		this.initLoginComponents();

		File[] pluginFiles = _pluginLoader.getAIs("sample");
		File[][] f = new File[][] { pluginFiles };
		// TODO improve
		if (pluginFiles.length > 0)
		{
			_tblAIPlayers = new JTable(f, new String[] { "Path" });
			_tblAIPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			_tblAIPlayers.setBounds(this.getWidth() - 600, 300, 200, 300);
			this.add(_tblAIPlayers);
		}
	}

	public boolean isMultiplayerMode()
	{
		return _isMultiplayer;
	}
}
