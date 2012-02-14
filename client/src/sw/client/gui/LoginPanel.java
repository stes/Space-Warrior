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
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import sw.client.GameController;
import sw.client.SWFrame;
import sw.client.gui.ConnectionEvent.ActionType;
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
	private JButton _btnChooseAI;
	private JLabel _lblIPAdress;
	private JLabel _lblName;
	private JScrollPane _scroll;
	private JTable _tblServers;
	private JTable _tblAIPlayers;
	private JButton _btnImage;
	private JButton _btnExit;
	private JLabel _lblPort;
	private Vector<ServerInfo> _servers;
	private ServerTableModel _tableModel;
	private JTextField _txtChooseAI;
	
	private ArrayList<ConnectionListener> _connectionListener;
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

		_connectionListener = new ArrayList<ConnectionListener>();

		_servers = new Vector<ServerInfo>();
		_tableModel = new ServerTableModel();

		_imageID = Images.min().getID();
		_background = ImageContainer.getLocalInstance().getImage(Images.TITLE);
		_pluginLoader = new PluginLoader();
		//_pluginLoader.addDirectory("C:\\Users\\Steffen\\Projekte\\Projekte\\SpaceWarrior\\current_build\\ai_players", "sample");
		this.initComponents();

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				_btnUpdate.setBounds(_self.getWidth() - 250, _self.getHeight() / 2 + 200, 100, 25);
				_scroll.setBounds(_self.getWidth() - 250, _self.getHeight() / 2 - 150, 200, 300);
				// TODO rework
				if (_tblAIPlayers != null)
				{
					_tblAIPlayers.setBounds(_self.getWidth() - 600, 300, 200, 300);
				}
				_self.repaint();
			}
		});
	}

	public void addConnectionListener(ConnectionListener l)
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

	public void removeConnecionListener(ConnectionListener l)
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
		_tableModel.fireTableDataChanged();
	}

	protected void invokeLogin(ConnectionEvent e)
	{
		if (_connectionListener.size() == 0)
		{
			return;
		}
		for (ConnectionListener l : _connectionListener)
		{
			l.login(e);
		}

	}

	/**
	 * Initializes the GUI components
	 */
	private void initComponents()
	{
		_btnExit = new JButton("Exit");
		_btnExit.setBounds(getWidth() - 200, 260, 100, 25);
		_btnExit.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		this.add(_btnExit);
		
		_btnImage = new JButton();
		_btnImage.setIcon(new ImageIcon(ImageContainer.getLocalInstance().getImage(_imageID)));
		_btnImage.setBounds(200, 450, 64, 64);
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
		this.add(_btnImage);

		_txtIPAddress = new JTextField();
		_txtIPAddress.setBounds(200, 210, 100, 25);
		_txtIPAddress.setText("localhost");
		this.add(_txtIPAddress);

		_txtPort = new JTextField();
		_txtPort.setBounds(400, 210, 50, 25);
		_txtPort.setText(GameConstants.STANDARD_PORT + "");
		this.add(_txtPort);

		_txtName = new JTextField();
		_txtName.setBounds(200, 250, 400, 25);
		_txtName.setText("test");
		this.add(_txtName);

		_lblIPAdress = new JLabel("IP-Address");
		_lblIPAdress.setBounds(100, 210, 100, 25);
		this.add(_lblIPAdress);

		_lblPort = new JLabel("Port");
		_lblPort.setBounds(350, 210, 100, 25);
		this.add(_lblPort);

		_lblName = new JLabel("Name");
		_lblName.setBounds(100, 250, 100, 25);
		this.add(_lblName);

		_btnConnect = new JButton("Connect");
		_btnConnect.setBounds(640, 210, 100, 25);
		_btnConnect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (!_txtName.getText().isEmpty())
				{
					SWFrame.out.println("connect");
					ConnectionEvent e = new ConnectionEvent(this, ActionType.LOGIN);
					e.setIPAdress(new InetSocketAddress(_txtIPAddress.getText(),
							Integer.parseInt(_txtPort.getText())));
					e.setLoginName(_txtName.getText());
					e.setImageID(_imageID);
					_self.invokeLogin(e);
				}
			}
		});
		this.add(_btnConnect);

		_btnUpdate = new JButton("Update");
		_btnUpdate.setBounds(this.getWidth() - 300, 620, 100, 25);
		_btnUpdate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				_servers.clear();
				for (ConnectionListener l : _connectionListener)
				{
					l.scan();
				}
			}
		});
		this.add(_btnUpdate);

		_btnHost = new JButton("Host");
		_btnHost.setBounds(100, 300, 100, 25);
		_btnHost.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				_server = new SWServer(GameConstants.STANDARD_PORT);
			}
		});
		this.add(_btnHost);

		_tblServers = new JTable(_tableModel);
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
		
		_scroll = new JScrollPane(_tblServers);
		_scroll.setBounds(this.getWidth() - 300, 400, 200, 300);
		this.add(_scroll);

		File[] pluginFiles = _pluginLoader.getAIs("sample");
		File[][] f = new File[][] {pluginFiles};
		// TODO improve
		if (pluginFiles.length > 0)
		{
			_tblAIPlayers = new JTable(f, new String[] {"Path"});
			_tblAIPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			_tblAIPlayers.setBounds(this.getWidth() - 600, 300, 200, 300);
			this.add(_tblAIPlayers);
		}
		
		_btnChooseAI = new JButton("Choose AI");
		_btnChooseAI.setBounds(100, 500, 100, 25);
		_btnChooseAI.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GameController.setAIPlugin(new File(_txtChooseAI.getText()));
			}
		});

		this.add(_btnChooseAI);
		_txtChooseAI = new JTextField();
		_txtChooseAI.setBounds(100, 550, 300, 25);
		// TODO change this after testing
		_txtChooseAI.setText("C:/Users/Steffen/Projekte/Projekte/SpaceWarrior/current_build/ai_players/sample_ai.jar");
		this.add(_txtChooseAI);
	}
}
