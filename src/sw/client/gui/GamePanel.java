package sw.client.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import sw.client.ClientListener;
import sw.client.IClient;
import sw.client.IGameStateManager;
import sw.client.player.HumanPlayer;
import sw.client.player.Player;
import sw.shared.GameConstants;
import sw.shared.Packettype;
import sw.shared.data.Packet;

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
		Packet p = new Packet(Packettype.CL_CHAT_MSG);
		p.addString(_txtChatmessage.getText());
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
        
        _playingField = new PlayingFieldPanel(_stateManager.getPlayerList());
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
	public void shot(Packet packet)	{}

	@Override
	public void snapshot(Packet packet) {}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.processInput();
	}
}