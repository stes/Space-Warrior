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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import sw.client.control.IGameController;
import sw.client.events.comm.IClientMessageListener;
import sw.client.gui.LoginPanelEvent.ActionType;
import sw.client.net.IClient;
import sw.shared.GameConstants.Images;
import sw.shared.Packettype;
import sw.shared.data.entities.shots.IWeapon.WeaponType;
import sw.shared.net.Packer;
import sw.shared.net.Unpacker;

public class StateBarPanel extends JPanel implements IClientMessageListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8021124343672288132L;

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

	private JTextField _txtChatmessage;
	private JTextArea _lstChathistory;
	private JScrollPane _scrollScoreBoard;
	private JScrollPane _scrollChathistory;
	private JButton _btnDisconnect;
	private PlayerTableModel _model;
	private JButton _btnWeapon;
	
	private BufferedImage _background;

	// other references
	private IGameController _stateManager;
	private IClient _client;
	private WeaponType _currentWeapon;
	private ArrayList<ILoginPanelListener> _connectionListener;
	private ArrayList<IWeaponChangedListener> _weaponChangedListener;

	public StateBarPanel(int width, int height, IGameController stateManager, IClient client)
	{
		super(null);
		_connectionListener = new ArrayList<ILoginPanelListener>();
		_weaponChangedListener = new ArrayList<IWeaponChangedListener>();
		_stateManager = stateManager;
		_client = client;
		_model = new PlayerTableModel();
		_currentWeapon = WeaponType.MASTER_LASER;
		this.setSize(width, height);
		this.initComponents();
		this.resizeComponents();
		_background = ImageContainer.getLocalInstance().getImage(Images.COCKPIT);
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
	
	private void resizeComponents()
	{
		_txtChatmessage.setBounds(getWidth()/4, getWidth()/100, getWidth()/4, getWidth()/70);
		_scrollChathistory.setBounds(getWidth()/4, getWidth()/100+getWidth()/70, getWidth()/4, getHeight()*1/2);
		_scrollScoreBoard.setBounds(getWidth()*3/5, 10, getWidth()/8, 25+getHeight()*2/5);
		_btnDisconnect.setBounds(getWidth()-100, getHeight()-50, 100, 50);
		_btnWeapon.setBounds(getWidth()/10, getHeight()/10, getWidth()/20, getWidth()/20);
	}

	public void addConnectionListener(ILoginPanelListener l)
	{
		_connectionListener.add(l);
	}

	public void removeConnecionListener(ILoginPanelListener l)
	{
		_connectionListener.remove(l);
	}
	
	public void addWeaponChangedListener(IWeaponChangedListener l)
	{
		_weaponChangedListener.add(l);
	}

	public void removeWeaponChangedListener(IWeaponChangedListener l)
	{
		_weaponChangedListener.remove(l);
	}
	
	@Override
	public void chatMessage(String name, String text)
	{
		this.appendMessage("[ " + name + " ] " + text + "\n");
	}

	public void paintComponent(Graphics g)
	{
		render(g);
	}
	
	public void render(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(_background, 0, 0, getWidth(), getHeight(), null);
		super.paintComponents(g);
	}

	@Override
	public void snapshot(Unpacker packet)
	{
		_model.fireTableDataChanged();
	}

	protected void invokeDisconnect(LoginPanelEvent e)
	{
		if (_connectionListener.size() == 0)
		{
			return;
		}
		for (ILoginPanelListener l : _connectionListener)
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
				LoginPanelEvent e = new LoginPanelEvent(this, ActionType.LOGOUT);
				invokeDisconnect(e);
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
		_txtChatmessage.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				sendChat(_txtChatmessage.getText());
				_txtChatmessage.setText("");
			}
		});
		// TODO remove comment after bug is fixed
		//this.add(_txtChatmessage);

		_scrollScoreBoard = new JScrollPane(new JTable(_model))
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
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
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
		
		_btnWeapon = new JButton(_currentWeapon.toString());
		_btnWeapon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				changeWeapon();
			}
		});
		this.add(_btnWeapon);
	}

	private void changeWeapon()
	{
		boolean next = false;
		for (WeaponType w : WeaponType.values())
		{
			if (w.equals(WeaponType.LASER))
				continue;
			if (next)
			{
				_currentWeapon = w;
				next = false;
				break;
			}
			if (w.equals(_currentWeapon))
				next = true;
		}
		if (next)
		{
			int i = 0;
			do
				_currentWeapon = WeaponType.values()[i++];
			while(_currentWeapon.equals(WeaponType.LASER));
				
		}
		this.invokeWeaponChanged(_currentWeapon);
	}
	
	private void invokeWeaponChanged(WeaponType currentWeapon)
	{
		_btnWeapon.setText(currentWeapon.toString());
		if (_weaponChangedListener == null)
			return;
		for (IWeaponChangedListener l : _weaponChangedListener)
			l.weaponChanged(currentWeapon);
	}

	public WeaponType getCurrentWeapon()
	{
		return _currentWeapon;
	}
	
	private void sendChat(String text)
	{
		Packer p = null;
		String content = text;
		if (text.startsWith("/"))
		{
			p = new Packer(Packettype.CL_COMMAND);
			content = content.substring(1);
		}
		else
		{
			p = new Packer(Packettype.CL_CHAT_MESSAGE);
		}
		p.writeUTF(content);
		_client.sendPacket(p);
	}
}
