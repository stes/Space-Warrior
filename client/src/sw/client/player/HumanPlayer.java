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
package sw.client.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import sw.client.gcontrol.IGameStateManager;

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
    
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		//System.out.println(e.getKeyChar());
        char eingabe = e.getKeyChar();
        switch (eingabe)
        {
            case 'w':
            {
                // forwards
                this.getCurrentState().setDirection(-1);
                break;
            }
            case 's':
            {
                // backwards
                this.getCurrentState().setDirection(0);
                break;
            }
            case 'a':
            {
                // left
                this.getCurrentState().setRotation(0);
                break;
            }
            case 'd':
            {
                // right
                this.getCurrentState().setRotation(0);
                break;
            }
            case 'm': case 'n':
            {
                this.getCurrentState().setShot(0);
                break;
            }
        }
        this.update();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
        char eingabe = e.getKeyChar();
        switch (eingabe)
        {
            case 'w':
            {
                // forwards
                this.getCurrentState().setDirection(1);
                break;
            }
            case 's':
            {
                // backwards
                this.getCurrentState().setDirection(-1);
                break;
            }
            case 'a':
            {
                // Left
                this.getCurrentState().setRotation(1);
                break;
            }
            case 'd':
            {
                // right
                this.getCurrentState().setRotation(-1);
                break;
            }
            case 'n':
            {
                // normal shot
                this.getCurrentState().setShot(1);
                break;
            }
            case 'm':
            {
                // master shot
                this.getCurrentState().setShot(2);
                break;
            }
        }
        this.update();
	}
}
