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

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import sw.client.ClientListener;
import sw.client.IClient;
import sw.client.gcontrol.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packer;
import sw.shared.data.Unpacker;

public class GamePanel extends JPanel implements ClientListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8751902318746091633L;
	
	private PlayingFieldPanel _playingField;
    @SuppressWarnings("unused")
	private AbstractButton _btnChat; //TODO remove?
    private TransparentTextField _txtChatmessage;
    private TransparentTextArea _lstChathistory;    
    private JTable _tblPoints;
	
    // other references
    private IGameStateManager _stateManager;
    private IClient _client;
    //private BfInterpreter _bfInterpreter; TODO integrate later
    
	public GamePanel(int width, int height, IGameStateManager stateManager, IClient client)
	{
		super();
		_stateManager = stateManager;
		_client = client;
		this.initComponents();
		this.setLayout(null);
		this.setSize(width, height);
		this.setBackground(Color.RED);
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
		
		
//        if ( !nachricht.isEmpty())
//        {
////            if (!FortyTwo.answer(nachricht))
////            {
////                if (nachricht.startsWith("/"))
////                {
////                    if (nachricht.startsWith("/bf"))
////                    {
////                        System.out.println("[BF]" + nachricht);
////                        _bfInterpreter.readCode(nachricht);
////                    }
////                    if (nachricht.startsWith("/exe"))
////                    {
////                        System.out.println("[BF] Execute");
////                        _bfInterpreter.execute();
////                    }
////                }
////                else
//                {
//                	// TODO send message
//                    //sendChatmessage(nachricht);
//                }
////            }
//        }
		_txtChatmessage.setText("");
    }

    private void initComponents()
    {
        int chat = 700;
        
//        _btnChat = new JButton("Chat");
//        _btnChat.setBounds(640, chat+100, 100, 25);
//        this.add(_btnChat);
//        _btnChat.addActionListener(this);
    	
        _txtChatmessage = new TransparentTextField();
        _txtChatmessage.setBounds(100, chat+100, 520, 25);
        this.add(_txtChatmessage);
        _txtChatmessage.addActionListener(this);
    	
        _lstChathistory = new TransparentTextArea();
        _lstChathistory.setBounds(100, chat, 645, 90);
        this.add(_lstChathistory);
        
        _tblPoints = new JTable(GameConstants.MAX_PLAYERS, 2);
        _tblPoints.setBounds(1100, 100, 200, 150);
        _tblPoints.getColumnModel().getColumn(0).setHeaderValue("Player");
        _tblPoints.getColumnModel().getColumn(1).setHeaderValue("Points");
        this.add(_tblPoints);
        
        _playingField = new PlayingFieldPanel(_stateManager);
        _playingField.setBounds(
        		GameConstants.REFERENCE_X,
        		GameConstants.REFERENCE_Y,
        		GameConstants.PLAYING_FIELD_WIDTH,
        		GameConstants.PLAYING_FIELD_HEIGHT);
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
		_lstChathistory.append("[ " + name + " ] " + text + "\n");
	}

	@Override
	public void shot(Unpacker packet)	{}

	@Override
	public void snapshot(Unpacker packet) {}
	
	@Override
	public void serverInfo(Unpacker packet) {}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.processInput();
	}
}
