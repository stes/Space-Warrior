/*******************************************************************************
 * Copyright (c) 2012 stes
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package sw.neuralagent;

import java.awt.Graphics;

public class Player
{
	private int _x;
	private int _y;

	private double _reward;
	private boolean _terminated;
	private double _terminalReward;

	private GridWorld _gridWorld;

	public Player(GridWorld world)
	{
		_gridWorld = world;
		this.respawn();
	}

	public void draw(Graphics g)
	{
		g.drawRect(_x * 60 + 1, _y * 60 + 1, 58, 58);
	}

	public double getReward()
	{
		return _reward;
	}

	public int getX()
	{
		return _x;
	}

	public int getY()
	{
		return _y;
	}

	public boolean isTerminated()
	{
		return _terminated;
	}
	
	public double getTerminalReward()
	{
		return _terminalReward;
	}

	public void move(int dx, int dy)
	{
		if (this.isTerminated())
		{
			return;
		}
		_x = Math.max(Math.min(_x + dx, _gridWorld.getWidth() - 1), 0);
		_y = Math.max(Math.min(_y + dy, _gridWorld.getHeight() - 1), 0);

		_reward += GridWorld.STEPCOST;

		_terminated = _gridWorld.isTerminal(_x, _y);
		if (_terminated)
		{
			_terminalReward = _gridWorld.getValue(_x, _y);
			_reward += _gridWorld.getValue(_x, _y);
		}
	}

	public void respawn()
	{
		_terminated = false;
		_reward = 0;
//		_x = Player._random.nextInt(_gridWorld.getWidth());
//		_y = Player._random.nextInt(_gridWorld.getHeight());
		_x = 1;
		_y = 1;
		if (_gridWorld.isTerminal(_x, _y))
		{
			this.respawn();
		}
	}

	public GridWorld getWorld()
	{
		return _gridWorld;
	}
}
