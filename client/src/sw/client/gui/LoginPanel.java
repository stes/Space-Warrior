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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import sw.client.ClientListener;
import sw.client.GameController;
import sw.shared.GameConstants;
import sw.shared.data.ServerInfo;
import sw.shared.data.Unpacker;

public class LoginPanel extends JPanel implements ClientListener
{
	private static final long serialVersionUID = -2058618925690808825L;

	private LoginPanel _self;

	private JTextField _txtIPAddress;
	private JTextField _txtPort;
	private JTextField _txtName;
	private AbstractButton _btnConnect;
	private AbstractButton _btnUpdate;
	private JFileChooser _fileChooser;
	private AbstractButton _btnChooseAI;
	private JLabel _lblIPAdress;
	private JLabel _lblName;
	private JScrollPane _scroll;
	private JTable _tblServers;

	private Vector<ServerInfo> _servers;
	private ServerTableModel _tableModel;

	private ArrayList<LoginListener> _loginListener;

	private JLabel _lblPort;

	public LoginPanel(int width, int height)
	{
		super();
		_self = this;
		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.WHITE);

		_loginListener = new ArrayList<LoginListener>();

		_servers = new Vector<ServerInfo>();
		_tableModel = new ServerTableModel();

		this.initComponents();
		
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				_btnUpdate.setBounds(_self.getWidth()-250, _self.getHeight()/2+200, 100, 25);
				_scroll.setBounds(_self.getWidth()-250, _self.getHeight()/2-150, 200, 300);
				_self.repaint();
			}
		});
	}

	public void addLoginListener(LoginListener l)
	{
		_loginListener.add(l);
	}

	public void removeLoginListener(LoginListener l)
	{
		_loginListener.remove(l);
	}

	protected void invokeLogin(LoginEvent e)
	{
		if (_loginListener.size() == 0)
			return;
		for (LoginListener l : _loginListener)
			l.login(e);

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

	/**
	 * Initializes the GUI components
	 */
	private void initComponents()
	{
		_txtIPAddress = new JTextField();
		_txtIPAddress.setBounds(200, 10, 100, 25);
		this.add(_txtIPAddress);

		_txtPort = new JTextField();
		_txtPort.setBounds(400, 10, 50, 25);
		this.add(_txtPort);

		_txtName = new JTextField();
		_txtName.setBounds(200, 50, 400, 25);
		this.add(_txtName);

		_lblIPAdress = new JLabel("IP-Address");
		_lblIPAdress.setBounds(100, 10, 100, 25);
		this.add(_lblIPAdress);

		_lblPort = new JLabel("Port");
		_lblPort.setBounds(350, 10, 100, 25);
		this.add(_lblPort);

		_lblName = new JLabel("Name");
		_lblName.setBounds(100, 50, 100, 25);
		this.add(_lblName);

		_btnConnect = new JButton("Connect");
		_btnConnect.setBounds(640, 10, 100, 25);
		_btnConnect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (!_txtName.getText().isEmpty())
				{
					System.out.println("connect");
					_self.invokeLogin(new LoginEvent(this,
							new InetSocketAddress(
									_txtIPAddress.getText(),
									Integer.parseInt(_txtPort.getText())),
									_txtName.getText()));
				}
			}
		});
		this.add(_btnConnect);

		_btnUpdate = new JButton("Update");
		_btnUpdate.setBounds(this.getWidth()-300, 620, 100, 25);
		_btnUpdate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				_servers.clear();
				for (LoginListener l : _loginListener)
					l.scan();
			}
		});
		this.add(_btnUpdate);

		_tblServers = new JTable(_tableModel);
		_tblServers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_tblServers.getSelectionModel().addListSelectionListener(
				new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						int index = _tblServers.getSelectedRow();
						_txtIPAddress.setText(_servers.get(index).getAddress().getAddress().getHostAddress());
						_txtPort.setText(_servers.get(index).getAddress().getPort()+"");
					}
				});
		_scroll = new JScrollPane(_tblServers);
		_scroll.setBounds(this.getWidth()-300, 300, 200, 300);
		this.add(_scroll);

		_fileChooser = new JFileChooser(System.getProperty("user.dir"));

		_btnChooseAI = new JButton("Choose AI");
		_btnChooseAI.setBounds(100, 500, 100, 25);
		_btnChooseAI.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Handle open button action.
				int returnVal = _fileChooser.showOpenDialog(_self);

				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					GameController.setAIPlugin(_fileChooser.getSelectedFile());
				}
			}
		});
		this.add(_btnChooseAI);
		
		// TODO move
		_txtName.setText("test");
		_txtIPAddress.setText("localhost");
		_txtPort.setText(GameConstants.STANDARD_PORT+"");
	}

	@Override
	public void serverInfo(ServerInfo info)
	{
		_servers.add(info);
		_tableModel.fireTableDataChanged();
	}

	@Override
	public void connected()
	{
	}

	@Override
	public void disconnected(String reason)
	{
	}

	@Override
	public void chatMessage(String name, String text)
	{
	}

	@Override
	public void shot(Unpacker packet)
	{
	}

	@Override
	public void snapshot(Unpacker packet)
	{
	}

	private class ServerTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 6220705373064394932L;

		private String[] _headers = { "Server", "Players/Max" };

		@Override
		public String getColumnName(int col)
		{
			return _headers[col];
		}

		@Override
		public int getColumnCount()
		{
			return _headers.length;
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
}
