package sw.client.player;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

import sw.client.IGameStateManager;
import sw.shared.data.PlayerInput;

/**
 * A player controlled by a human using the keyboard<p>
 * <p>
 * Controls:<p>
 * W - Move forwards<p>
 * S - Move backwards<p>
 * A - Turn left<p>
 * D - Turn right<p>
 * <p>
 * N - Perform regular shot<p>
 * M - Perform master shot<p>
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 */
public class HumanPlayer extends Player implements AWTEventListener
{
	public HumanPlayer(IGameStateManager stateManager)
	{
		super(stateManager);
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
	}
	
    @Override
    public void eventDispatched(AWTEvent e)
    {
        if (e.getID() == KeyEvent.KEY_TYPED)
        {
            KeyEvent keyEvent = (KeyEvent)e;
            char eingabe = keyEvent.getKeyChar();
            switch (eingabe)
            {
                case 'w':
                {
                    // forwards
                    this.getCurrentState().setzeBewegung(1);
                    break;
                }
                case 's':
                {
                    // backwards
                    this.getCurrentState().setzeBewegung(-1);
                    break;
                }
                case 'a':
                {
                    // Left
                    this.getCurrentState().setzeDrehung(1);
                    break;
                }
                case 'd':
                {
                    // right
                    this.getCurrentState().setzeDrehung(-1);
                    break;
                }
                case 'n':
                {
                    // normal shot
                    this.getCurrentState().setzeSchuss(1);
                    break;
                }
                case 'm':
                {
                    // master shot
                    this.getCurrentState().setzeSchuss(2);
                    break;
                }
            }
        }
        else if (e.getID() == KeyEvent.KEY_RELEASED)
        {
            KeyEvent keyEvent = (KeyEvent)e;
            char eingabe = keyEvent.getKeyChar();
            switch (eingabe)
            {
                case 'w':
                {
                    // forwards
                    this.getCurrentState().setzeBewegung(-1);
                    break;
                }
                case 's':
                {
                    // backwards
                    this.getCurrentState().setzeBewegung(0);
                    break;
                }
                case 'a':
                {
                    // left
                    this.getCurrentState().setzeDrehung(0);
                    break;
                }
                case 'd':
                {
                    // right
                    this.getCurrentState().setzeDrehung(0);
                    break;
                }
                case 'm': case 'n':
                {
                    this.getCurrentState().setzeSchuss(0);
                    break;
                }
            }
        }
        if (!this.getOldState().equals(this.getCurrentState()))
        {
            this.setOldState(new PlayerInput(this.getCurrentState()));
            //Packet p = this.getCurrentState().pack();
            //_client.sendPacket(p);
            // TODO invoke sending the new input
        }
    }
}
