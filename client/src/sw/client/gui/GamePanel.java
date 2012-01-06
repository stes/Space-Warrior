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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import sw.client.ClientMessageListener;
import sw.client.IClient;
import sw.client.gcontrol.GameStateChangedEvent;
import sw.client.gcontrol.GameStateChangedListener;
import sw.client.gcontrol.IGameStateManager;
import sw.client.gui.ConnectionEvent.ActionType;
import sw.shared.Packettype;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

public class GamePanel extends JPanel implements ClientMessageListener, ActionListener,
		GameStateChangedListener
{
	private class PlayerTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 3882143612301180149L;

		private String[] _headers = { "Player", "Score" };

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
			return _stateManager.getPlayerList().length;
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			switch (col)
			{
				case 0:
					return _stateManager.getPlayerList()[row].getName();
				case 1:
					return _stateManager.getPlayerList()[row].getScore();
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

	private static final long serialVersionUID = -8751902318746091633L;
	private PlayingFieldPanel _playingField;
	private JTextField _txtChatmessage;
	private JTextArea _lstChathistory;
	private JScrollPane _scrollScoreBoard;
	private JScrollPane _scrollChathistory;
	private JTable _tblScoreBoard;
	private JButton _btnDisconnect;

	private PlayerTableModel _model;

	private GamePanel _self;

	// other references
	private IGameStateManager _stateManager;
	private IClient _client;
	private ArrayList<ConnectionListener> _connectionListener;

	public GamePanel(int width, int height, IGameStateManager stateManager, IClient client)
	{
		super();
		_self = this;
		_connectionListener = new ArrayList<ConnectionListener>();
		_stateManager = stateManager;
		_client = client;
		_model = new PlayerTableModel();
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
				_self.resizeComponents();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.sendChat(_txtChatmessage.getText());
		_txtChatmessage.setText("");
	}

	public void addConnectionListener(ConnectionListener l)
	{
		_connectionListener.add(l);
	}

	@Override
	public void chatMessage(String name, String text)
	{
		this.appendMessage("[ " + name + " ] " + text + "\n");
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
		// TODO best practise?
		_playingField.playerInit();
	}

	public void removeConnecionListener(ConnectionListener l)
	{
		_connectionListener.remove(l);
	}

	public void render(Graphics2D g)
	{
		_playingField.render(g.create());
		super.paintComponents(g.create());
	}

	@Override
	public void snapshot(Unpacker packet)
	{
		_model.fireTableDataChanged();
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

	private void appendMessage(String message)
	{
		_lstChathistory.append(message);
		_lstChathistory.setCaretPosition(_lstChathistory.getText().length());
	}

	private void initComponents()
	{
		_btnDisconnect = new JButton("Disconnect");
		this.add(_btnDisconnect);
		_btnDisconnect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ConnectionEvent e = new ConnectionEvent(this, ActionType.LOGOUT);
				_self.invokeDisconnect(e);
			}
		});

		_txtChatmessage = new JTextField()
		{
			private static final long serialVersionUID = 2109656328663846511L;

			@Override
			protected void paintComponent(Graphics g)
			{
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
				g2d.setColor(this.getBackground());
				Rectangle bounds = g2d.getClip().getBounds();
				g2d.fillRect((int) bounds.getX(),
						(int) bounds.getY(),
						(int) bounds.getWidth(),
						(int) bounds.getHeight());
				super.paintComponent(g2d);
			}
		};
		_txtChatmessage.setOpaque(false);
		_txtChatmessage.addActionListener(this);
		this.add(_txtChatmessage);

		_tblScoreBoard = new JTable(_model);

		_scrollScoreBoard = new JScrollPane(_tblScoreBoard)
		{
			private static final long serialVersionUID = -2017756045550747936L;

			@Override
			protected void paintComponent(Graphics g)
			{
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
				g2d.setColor(this.getBackground());
				Rectangle bounds = g2d.getClip().getBounds();
				g2d.fillRect((int) bounds.getX(),
						(int) bounds.getY(),
						(int) bounds.getWidth(),
						(int) bounds.getHeight());
				super.paintComponent(g2d);
			}
		};
		_scrollScoreBoard.setOpaque(false);
		this.add(_scrollScoreBoard);

		_lstChathistory = new JTextArea();
		_lstChathistory.setEditable(false);

		_scrollChathistory = new JScrollPane(_lstChathistory)
		{
			private static final long serialVersionUID = 8703321293181453130L;

			@Override
			protected void paintComponent(Graphics g)
			{
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
				g2d.setColor(this.getBackground());
				Rectangle bounds = g2d.getClip().getBounds();
				g2d.fillRect((int) bounds.getX(),
						(int) bounds.getY(),
						(int) bounds.getWidth(),
						(int) bounds.getHeight());
				super.paintComponent(g2d);
			}
		};
		_scrollChathistory.setOpaque(false);
		this.add(_scrollChathistory);

		_playingField = new PlayingFieldPanel(this.getWidth(), this.getHeight(), _stateManager);
		this.add(_playingField);
	}

	private void resizeComponents()
	{
		_scrollScoreBoard.setBounds(this.getWidth() * 5 / 6 - 50, 50, this.getWidth() / 6, 150);
		_scrollChathistory.setBounds(50, this.getHeight() - 150, this.getWidth() / 3, 90);
		_txtChatmessage.setBounds(50, this.getHeight() - 50, this.getWidth() / 3, 25);
		_btnDisconnect.setBounds(this.getWidth() - 150, 20, 100, 20);
		_playingField.setSize(this.getSize());
	}

	private void sendChat(String text)
	{
		Packer p = new Packer(Packettype.CL_CHAT_MESSAGE);
		p.writeUTF(text);
		_client.sendPacket(p);
	}

}
