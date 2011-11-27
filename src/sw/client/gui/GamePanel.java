package sw.client.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sw.client.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.shared.GameConstants;

public class GamePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8751902318746091633L;
	
	// component section
	private GamePanel _self;
    private PlayingFieldGraphics _playingField;
    private AbstractButton _btnChat;
    private JTextField _txtChatmessage;
    private JTextArea _lstChathistory;    
    private JTable _tblPoints;
	
    // other references
    private IGameStateManager _stateManager;
    //private BfInterpreter _bfInterpreter; TODO integrate later
    
	public GamePanel(int width, int height, IGameStateManager stateManager)
	{
		super();
		_self = this;
		_stateManager = stateManager;
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
        String nachricht = _txtChatmessage.getText();
        if ( !nachricht.isEmpty())
        {
//            if (!FortyTwo.answer(nachricht))
//            {
//                if (nachricht.startsWith("/"))
//                {
//                    if (nachricht.startsWith("/bf"))
//                    {
//                        System.out.println("[BF]" + nachricht);
//                        _bfInterpreter.readCode(nachricht);
//                    }
//                    if (nachricht.startsWith("/exe"))
//                    {
//                        System.out.println("[BF] Execute");
//                        _bfInterpreter.execute();
//                    }
//                }
//                else
                {
                	// TODO send message
                    //sendChatmessage(nachricht);
                }
//            }
        }
        _txtChatmessage.setText("");
    }

    private void initComponents()
    {
        int chat = 700;
        
        _btnChat = new JButton("Chat");
        _btnChat.setBounds(640, chat+100, 100, 25);
        _btnChat.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				_lstChathistory.append(_txtChatmessage.getText());
			}
		});
        this.add(_btnChat);
    	
        _txtChatmessage = new JTextField("");
        _txtChatmessage.setBounds(100, chat+100, 520, 25);
        _txtChatmessage.addKeyListener(new KeyListener()
        {
			@Override
			public void keyTyped(KeyEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				// TODO test
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					_self.processInput();
				}
			}
		});
        this.add(_txtChatmessage);
    	
    	
        _lstChathistory = new JTextArea();
        _lstChathistory.setBounds(100, chat, 645, 90);
        this.add(_lstChathistory);
        
        _tblPoints = new JTable(GameConstants.MAX_PLAYERS, 2);
        _tblPoints.setBounds(1100, 100, 200, 150);
        _tblPoints.getColumnModel().getColumn(0).setHeaderValue("Player");
        _tblPoints.getColumnModel().getColumn(1).setHeaderValue("Points");
        this.add(_tblPoints);
        

        _playingField = new PlayingFieldGraphics(_stateManager.getPlayerList());
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
}
