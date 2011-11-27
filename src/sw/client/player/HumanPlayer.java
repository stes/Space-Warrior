package sw.client.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
public class HumanPlayer extends Player implements KeyListener
{
	public HumanPlayer(IGameStateManager stateManager)
	{
		super(stateManager);
        //Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
	}

    private void update()
    {
        if (!this.getOldState().equals(this.getCurrentState()))
        {
            this.setOldState(new PlayerInput(this.getCurrentState()));
            this.getStateManager().stateUpdated(this.getCurrentState());
        }
    }
    
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
        char eingabe = e.getKeyChar();
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
        this.update();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO rin
		System.out.println(e.getKeyChar());
        char eingabe = e.getKeyChar();
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
        this.update();
	}
}
