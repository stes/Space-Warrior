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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import sw.client.ClientListener;
import sw.client.IClient;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.shared.Packettype;
import sw.shared.data.Packer;
import sw.shared.data.ServerInfo;
import sw.shared.data.Unpacker;

public class GamePanel extends JPanel implements ClientListener, ActionListener
{
	private static final long serialVersionUID = -8751902318746091633L;
	
	private PlayingFieldPanel _playingField;
    private TransparentTextField _txtChatmessage;
    private TransparentTextArea _lstChathistory;
    private JScrollPane _scrollScoreBoard;
    private JTable _tblScoreBoard;
    
    private PlayerTableModel _model;
    
    private GamePanel _self;
	
    // other references
    private IGameStateManager _stateManager;
    private IClient _client;
    
	public GamePanel(int width, int height, IGameStateManager stateManager, IClient client)
	{
		super();
		_self = this;
		_stateManager = stateManager;
		_client = client;
		_model = new PlayerTableModel();
		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.BLACK);
		this.initComponents();
		
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
		        _txtChatmessage.setBounds(50, _self.getHeight()-50, _self.getWidth()/3, 25);
		        _lstChathistory.setBounds(50, _self.getHeight()-150, _self.getWidth()/3, 90);
				_scrollScoreBoard.setBounds(_self.getWidth()*5/6-50, 50, _self.getWidth()/6, 150);
		        _playingField.setSize(_self.getSize());
				_self.repaint();
			}
		});
	}

	/**
    * invoked after chat button is pressed
    */
	public void btnChat_Action(ActionEvent e)
	{
		if (e.getID() == ActionEvent.ACTION_PERFORMED)
		{
			this.processInput();
		}
	}
	    
    public void updatePlayingField()
    {
        if (_playingField != null)
        {
            _playingField.repaint();
        }
    }
    
    private void processInput()
    {
		Packer p = new Packer(Packettype.CL_CHAT_MESSAGE);
		p.writeUTF(_txtChatmessage.getText());
		_client.sendPacket(p);
		_txtChatmessage.setText("");
    }

    private void initComponents()
    {
        int chat = this.getHeight()-200;
        System.out.println(chat);
        _txtChatmessage = new TransparentTextField();
        _txtChatmessage.setBounds(100, chat+100, 520, 25);
        this.add(_txtChatmessage);
        _txtChatmessage.addActionListener(this);
    	
        _lstChathistory = new TransparentTextArea();
        _lstChathistory.setBounds(100, chat, 645, 90);
        _lstChathistory.setEditable(false);
        this.add(_lstChathistory);
        
        _tblScoreBoard = new JTable(_model);
        
        _scrollScoreBoard = new JScrollPane(_tblScoreBoard);
		_scrollScoreBoard.setBounds(this.getWidth()-300, 100, 200, 150);
        
        this.add(_scrollScoreBoard);
        
        _playingField = new PlayingFieldPanel(this.getWidth(), this.getHeight(), _stateManager);
		Player localPlayer = _stateManager.getLocalPlayer();
        if (localPlayer instanceof HumanPlayer)
        {
        	_playingField.addKeyListener((HumanPlayer)localPlayer);
        }
        this.add(_playingField);
    }

	@Override
	public void connected() {}

	@Override
	public void disconnected(String reason) {} // TODO: show reason

	@Override
	public void chatMessage(String name, String text)
	{
		this.appendMessage("[ " + name + " ] " + text + "\n");
	}

	@Override
	public void shot(Unpacker packet) {}

	@Override
	public void snapshot(Unpacker packet)
	{
		_model.fireTableDataChanged();
	}
	
	@Override
	public void serverInfo(ServerInfo info) {}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.processInput();
	}
	
	private void appendMessage(String message)
	{
		_lstChathistory.append(message);
		_lstChathistory.setCaretPosition(_lstChathistory.getText().length());
	}
	
	private class PlayerTableModel extends AbstractTableModel
	{
		
		private static final long serialVersionUID = 3882143612301180149L;
		
		private String[] _headers = {"Player", "Score"};
		
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
			return _stateManager.getPlayerList().count(false);
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			switch (col)
			{
				case 0:
					return _stateManager.getPlayerList().dataAt(row).getName();
				case 1:
					return _stateManager.getPlayerList().dataAt(row).getScore();
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
