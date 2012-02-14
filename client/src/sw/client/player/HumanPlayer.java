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
package sw.client.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import sw.shared.data.entities.shots.IWeapon.WeaponType;

/**
 * A player controlled by a human using the keyboard
 * <p>
 * <p>
 * Controls:
 * <p>
 * W - Move forwards
 * <p>
 * S - Move backwards
 * <p>
 * A - Turn left
 * <p>
 * D - Turn right
 * <p>
 * <p>
 * N - Perform regular shot
 * <p>
 * M - Perform master shot
 * <p>
 * 
 * @author Redix, stes, Abbadonn
 * @version 27.11.2011
 */
public class HumanPlayer extends Player implements KeyListener
{
	private boolean _forward;
	private boolean _backward;
	private boolean _left;
	private boolean _right;

	/**
	 * Creates a new instance
	 * 
	 * @param stateManager
	 */
	public HumanPlayer(String name)
	{
		super(name);
	}
	
	public HumanPlayer(String name, int imageID)
	{
		super(name, imageID);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyChar())
		{
			case 'w':
				_forward = true;
				break;
			case 's':
				_backward = true;
				break;
			case 'a':
				_left = true;
				break;
			case 'd':
				_right = true;
				break;
			case 'n':
				this.getCurrentInput().setShot(WeaponType.LASER.getID());
				break;
			case 'm':
				this.getCurrentInput().setShot(WeaponType.MASTER_LASER.getID());
				break;
			case 'b':
				this.getCurrentInput().setShot(WeaponType.ROCKET.getID());
				break;
			case 'v':
				this.getCurrentInput().setShot(WeaponType.MINE.getID());
				break;
		}

		this.updateInput();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyChar())
		{
			case 'w':
				_forward = false;
				break;
			case 's':
				_backward = false;
				break;
			case 'a':
				_left = false;
				break;
			case 'd':
				_right = false;
				break;
			case 'n':
			case 'm':
			case 'b':
			case 'v':
				this.getCurrentInput().setShot(0);
				break;
		}

		this.updateInput();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{}

	private void updateInput()
	{
		int direction = 0;
		if (_forward && !_backward)
		{
			direction = 1;
		}
		if (!_forward && _backward)
		{
			direction = -1;
		}

		int rotation = 0;
		if (_left && !_right)
		{
			rotation = 1;
		}
		if (!_left && _right)
		{
			rotation = -1;
		}

		this.getCurrentInput().setDirection(direction);
		this.getCurrentInput().setRotation(rotation);

		this.update();
	}
}
