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
package sw.client.player.ai;

import java.util.Random;

import sw.client.gcontrol.GameStateChangedEvent;
import sw.shared.GameConstants;
import sw.shared.data.entities.shots.IWeapon.WeaponType;

public class SampleAI extends AIPlayer
{
	private static Random _random = new Random(System.currentTimeMillis());

	private int _hold;
	private int _currentWeapon;

	public SampleAI(String name)
	{
		super( name);
		System.out.println("init ai player");
		this.getCurrentInput().setDirection(1);
		WeaponType[] types = WeaponType.values();
		_currentWeapon = types[_random.nextInt(types.length)].getID();
	}

	@Override
	public void gameStateChanged(GameStateChangedEvent e)
	{
		boolean turn = false;
		if (getDataSet().getPosition().x < 150)
		{
			turn = true;
			this.getCurrentInput().setRotation((int) Math.signum(90 - getDataSet().getDirection()));
		}
		if (getDataSet().getPosition().y < 150)
		{
			turn = true;
			this.getCurrentInput().setRotation(-(int) Math.signum(180 - getDataSet().getDirection()));
		}
		if (getDataSet().getPosition().x > GameConstants.PLAYING_FIELD_WIDTH - 150)
		{
			turn = true;
			this.getCurrentInput().setRotation((int) Math.signum(270 - getDataSet().getDirection()));
		}
		if (getDataSet().getPosition().y > GameConstants.PLAYING_FIELD_HEIGHT - 150)
		{
			turn = true;
			this.getCurrentInput().setRotation((int) Math.signum(180 - getDataSet().getDirection()));
		}
		if (!turn && _hold == 0)
		{
			if (_random.nextDouble() > 0.9)
			{
				_hold = 1 + _random.nextInt(9);
				this.getCurrentInput().setRotation(_random.nextInt(2) * 2 - 1);
			}
			else
				this.getCurrentInput().setRotation(0);
			this.getCurrentInput().setDirection(1);
		}
		else if (_hold > 0)
		{
			_hold--;
		}
		if (_random.nextDouble() < 0.01)
		{
			this.getCurrentInput().setShot(_currentWeapon);
			System.out.println("shot"+getDataSet().getAmmo());
		}
		if (_random.nextDouble() < 0.001)
		{
			WeaponType[] types = WeaponType.values();
			_currentWeapon = types[_random.nextInt(types.length)].getID();
		}
//		LaserBeam laserBeam = new LaserBeam(this.getDataSet().getX(),
//				this.getDataSet().getY(),
//				this.getDataSet().getDirection(),
//				this.getDataSet());
//		LaserBeam mshot = new LaserBeam(this.getDataSet().getX(),
//				this.getDataSet().getY(),
//				this.getDataSet().getDirection(),
//				this.getDataSet(),
//				true);
//		for (SpaceShip d : this.getStateManager().getPlayerList())
//		{
//			if (d.equals(this.getDataSet()))
//				continue;
//			if (laserBeam.distanceTo(d.getPosition()) < GameConstants.PLAYER_SIZE / 2)
//			{
//				this.getCurrentInput().setShot(WeaponType.LASER.getID());
//			}
//			else if (mshot.distanceTo(d.getPosition()) < GameConstants.PLAYER_SIZE / 2)
//			{
//				this.getCurrentInput().setShot(WeaponType.MASTER_LASER.getID());
//			}
//		}
		this.update();
		//this.getCurrentInput().setShot(0);
	}

	@Override
	public void newRound(GameStateChangedEvent arg0)
	{
		this.getCurrentInput().setDirection(1);
	}
}
